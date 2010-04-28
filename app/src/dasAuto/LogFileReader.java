package dasAuto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.logData.feeds.GpsFeed;
import dasAuto.logData.samples.AccelSample;
import dasAuto.logData.samples.GpsSample;

public class LogFileReader {
	private File inputLogFile;
	
	private AccelFeed accelFeed = new AccelFeed();
	private GpsFeed gpsFeed = new GpsFeed();
	
	private long previousTimestamp = 0;
	private long firstTimestamp = 0;
	
	public final String SENSOR_GPS_ID = Messages.getString("LogFileReader.sensorGpsId");
	public final String SENSOR_ACCEL_ID = Messages.getString("LogFileReader.sensorAccelId");
	
	
	public LogFileReader(File inFilename) {
		inputLogFile = inFilename;
		loadLogData();
	}
	
	
	private void loadLogData() {
		try {
			FileInputStream fstream = new FileInputStream(inputLogFile);
			BufferedReader inputLog = new BufferedReader(new InputStreamReader(fstream));
			
			while(inputLog.ready()) {
				String currentLine = inputLog.readLine();
				
				// TODO: We should be checking for a GPS line because it's messing
				// up the accel feed. Basically we need to pretend that the GPS
				// sample was there and increment the "previousTimestamp" value so
				// the accelerometer deltas are computed properly.
				if(isValidLine(currentLine)) {
					String[] currentLineParts = currentLine.split(Messages.getString("LogFileReader.sensorSeparator"));
					String[] sensorData = currentLineParts[1].split(Messages.getString("LogFileReader.dataSeparator"));
					
					if(currentLineParts[0].equals(SENSOR_GPS_ID)) {
						addGpsLine(sensorData);
					} else if(currentLineParts[0].equals(SENSOR_ACCEL_ID)) {
 						addAccelLine(sensorData);
					}
				}
			}
			
			inputLog.close();
		} catch(Exception e) {
			System.out.println(Messages.getString("LogFileReader.errorLoadData"));
			System.out.println(e.toString());
		}
	}
	
	
	private void addGpsLine(String[] sensorData) {
		String utcTime = sensorData[0];
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss.SSS");
		try {
			Date currentDateTime = sdf.parse(utcTime);
			
			if(firstTimestamp == 0) {
				firstTimestamp = currentDateTime.getTime();
				previousTimestamp = 0;
			} else {
				previousTimestamp = currentDateTime.getTime() - firstTimestamp;
			}
		} catch(Exception ex) {
			return;
		}
		
		GpsSample gpsSample = new GpsSample();
		gpsSample.setTimestamp(previousTimestamp);
		gpsSample.setLatitude(Double.valueOf(sensorData[1]));
		gpsSample.setLongitude(Double.valueOf(sensorData[3]));
		gpsSample.setSpeed(Double.valueOf(sensorData[5]) * 1.15077945); // Convert knots to mph.
		gpsSample.setHeading(Double.valueOf(sensorData[6]));
		
		gpsFeed.add(gpsSample);
	}
	
	
	private void addAccelLine(String[] sensorData) {
		AccelSample loadingAccel = new AccelSample();
		loadingAccel.setTimestamp(previousTimestamp + Long.parseLong(sensorData[0]));
		loadingAccel.setXValue(1024 - Integer.parseInt(sensorData[1]));
		loadingAccel.setYValue(Integer.parseInt(sensorData[2]));
		loadingAccel.setZValue(Integer.parseInt(sensorData[3]));
		
		accelFeed.add(loadingAccel);
	}
	
	
	private boolean isValidLine(String currentLine) {
		Pattern accelPattern = Pattern.compile("^\\d{2}?:\\d{1,},\\d{1,4},\\d{1,4},\\d{1,4}$");
		Pattern gpsPattern = Pattern.compile("^\\d{2}?:\\d{6}?\\.\\d{3}?,\\d{4}?\\.\\d{4}?,[NS],\\d{5}?\\.\\d{4}?,[EW],\\d{3}?\\.\\d,\\d{3}?\\.\\d,\\d{6}?$");
		Matcher gpsMatcher = gpsPattern.matcher(currentLine);
		Matcher accelMatcher = accelPattern.matcher(currentLine);

		try {
			if(accelMatcher.find()) {
				return true;
			} else if(gpsMatcher.find()) {
				return true;
			} else {
				return false;
			}
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	
	public AccelFeed getAccelFeed() {
		return accelFeed;
	}
	
	public GpsFeed getGpsFeed() {
		return gpsFeed;
	}
	
	
}