package dasAuto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
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
	
	private long lastTimestamp = 0;
	
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
		Calendar calendar = Calendar.getInstance();
		String utcTime = sensorData[0];
		String utcDate = sensorData[7];
		
		Calendar year = Calendar.getInstance();
		calendar.set(Integer.parseInt(utcDate.substring(4,6)) + year.get(Calendar.YEAR) / 1000 * 1000,
				Integer.parseInt(utcDate.substring(2, 4)),
				Integer.parseInt(utcDate.substring(0, 2)),
				Integer.parseInt(utcTime.substring(0, 2)), 
				Integer.parseInt(utcTime.substring(2, 4)),
				Integer.parseInt(utcTime.substring(4, 6)));
		lastTimestamp = calendar.getTimeInMillis() +  Long.parseLong(utcTime.substring(7, 10));
		
		GpsSample gpsSample = new GpsSample();
		gpsSample.setTimestamp(lastTimestamp);
		gpsSample.setLatitude(Double.valueOf(sensorData[1]));
		gpsSample.setLongitude(Double.valueOf(sensorData[3]));
		gpsSample.setSpeed(Double.valueOf(sensorData[5]));
		gpsSample.setHeading(Double.valueOf(sensorData[6]));
		
		gpsFeed.add(gpsSample);
	}
	
	private void addAccelLine(String[] sensorData) {
		AccelSample loadingAccel = new AccelSample();
		loadingAccel.setTimestamp(lastTimestamp + Long.parseLong(sensorData[0]));
		loadingAccel.setxValue(Integer.parseInt(sensorData[1]));
		loadingAccel.setyValue(Integer.parseInt(sensorData[2]));
		loadingAccel.setzValue(Integer.parseInt(sensorData[3]));
		
		accelFeed.add(loadingAccel);
	}
	
	private boolean isValidLine(String currentLine) {
		Pattern accelPattern = Pattern.compile("^\\d{2,2}?:\\d{1,},\\d{1,},\\d{1,},\\d{1,}$");
		Pattern gpsPattern = Pattern.compile
		("^\\d{2,2}?:\\d{6,6}?\\.\\d{3,3}?,\\d{4,4}?\\.\\d{4,4}?,[NS],\\d{5,5}?\\.\\d{4,4}?,[EW],\\d{3,3}?\\.\\d,\\d{3,3}?\\.\\d,\\d{6,6}?$");
		Matcher gpsMatcher = gpsPattern.matcher(currentLine);
		Matcher accelMatcher = accelPattern.matcher(currentLine);
		
		//System.out.println(currentLine);
		
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