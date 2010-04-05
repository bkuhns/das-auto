package dasAuto;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Calendar;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.logData.feeds.GpsFeed;
import dasAuto.logData.samples.AccelSample;
import dasAuto.logData.samples.GpsSample;




public class LogFileReader {
	private final String filename = "log_100401_lccc.txt"; //TODO: make this a parameter of some sort.
	
	private AccelFeed accelFeed = new AccelFeed();
	private GpsFeed gpsFeed = new GpsFeed();
	
	private long lastTimestamp = 0;
	
	public final String SENSOR_GPS_ID = "01";
	public final String SENSOR_ACCEL_ID = "02";
	
	
	public LogFileReader() {
		loadLogData();
	}
	
	
	private void loadLogData() {
		try {
			FileInputStream fstream = new FileInputStream(filename);
			BufferedReader inputLog = new BufferedReader(new InputStreamReader(fstream));
			
			while(inputLog.ready()) {
				String currentLine = inputLog.readLine();
				
				if(isValidLine(currentLine)) {
					String[] currentLineParts = currentLine.split(":");
					String[] sensorData = currentLineParts[1].split(",");
					
					if(currentLineParts[0].equals(SENSOR_GPS_ID)) {
						addGpsLine(sensorData);
					} else if(currentLineParts[0].equals(SENSOR_ACCEL_ID)) {
 						addAccelLine(sensorData);
					}
				}
			}
			
			inputLog.close();
		} catch(Exception e) {
			System.out.println("Oh noes!");
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
		boolean isGoodLine = true;
		String lineSplit[] = currentLine.split(",");
		
		try {
			if(lineSplit.length == 4) { // Accel data will have 4 parts
				String currentAccel = "";
				for(int parseAccel = 0; parseAccel < 4; parseAccel++) {
					if (parseAccel == 0) {
						if(!lineSplit[parseAccel].contains("02:")) {
							isGoodLine = false;
						} else {
							currentAccel = lineSplit[parseAccel].substring(3, lineSplit[parseAccel].length());
							Integer.parseInt(currentAccel);
						}
					} else {
						currentAccel = lineSplit[parseAccel];
						Integer.parseInt(currentAccel);
					}
				}
			} else if(lineSplit.length == 8) { // GPS data will have 8 parts
				String currentGps = "";
				for(int parseGps = 0; parseGps < 8; parseGps++) {
					if(parseGps == 0) {
						currentGps = lineSplit[parseGps].substring(3, lineSplit[parseGps].length());
						if(!lineSplit[parseGps].contains("01:")) {
							return false;
						} else {
							Double.parseDouble(currentGps);							
						}
					} else if(parseGps == 1 || parseGps == 3 || parseGps == 5 || parseGps == 6 || parseGps == 7) {
						currentGps = lineSplit[parseGps];
						Double.parseDouble(currentGps);
					}
				}
			} else {
				isGoodLine = false;
			}
		} catch(NumberFormatException e) {
			isGoodLine = false;
		}
		
		return isGoodLine;
	}
	
	
	public AccelFeed getAccelFeed() {
		return accelFeed;
	}
	
	
	public GpsFeed getGpsFeed() {
		return gpsFeed;
	}
	
	
}