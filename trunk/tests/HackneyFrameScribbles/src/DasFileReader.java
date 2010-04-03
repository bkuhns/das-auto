import java.io.File;
import java.util.Calendar;
import java.util.Scanner;
import java.util.Vector;

public class DasFileReader {

	protected Vector<AccelSample> accelRaceData = new Vector<AccelSample>();
	protected Vector<GpsSample> gpsRaceData = new Vector<GpsSample>();
	
	public DasFileReader(String fileName)
	{
		String nextLineFromScanner = "";
		
		try{
			Scanner raceFileScanner = new Scanner(new File(fileName));
			
			while(raceFileScanner.hasNextLine())
			{
				nextLineFromScanner = raceFileScanner.nextLine();
				long currentTimestamp = 0;
				
				//run testing in lineCheck for errors in data
				if(lineCheck(nextLineFromScanner))
				{
					//checks to see if device is gps: device 01
					if(nextLineFromScanner.substring(0, 2).equals("01"))
					{
						GpsSample loadingGps = new GpsSample();
						Calendar timestampDate = Calendar.getInstance();
						timestampDate.set(	2000 + Integer.parseInt(nextLineFromScanner.substring(55)),
											Integer.parseInt(nextLineFromScanner.substring(53, 55)),
											Integer.parseInt(nextLineFromScanner.substring(51, 53)),
											Integer.parseInt(nextLineFromScanner.substring(3, 5)), 
											Integer.parseInt(nextLineFromScanner.substring(5, 7)),
											Integer.parseInt(nextLineFromScanner.substring(7, 9)));
						currentTimestamp = timestampDate.getTimeInMillis() +  
												Long.parseLong(nextLineFromScanner.substring(10, 13));
						
						loadingGps.setTimestamp(currentTimestamp);
						loadingGps.setLatitude(Double.valueOf(nextLineFromScanner.substring(14, 23)));
						loadingGps.setLongitude(Double.valueOf(nextLineFromScanner.substring(26, 36)));
						loadingGps.setVelocity(Double.valueOf(nextLineFromScanner.substring(39, 44)));
						loadingGps.setCourse(Double.valueOf(nextLineFromScanner.substring(45, 50)));
						
						gpsRaceData.add(loadingGps);
					}
					
					//checks to see if device is accelerometere: device 02
					else if(nextLineFromScanner.substring(0, 2).equals("02"))
					{
						String splitAccelString[] = nextLineFromScanner.split(",");
 						AccelSample loadingAccel = new AccelSample();
						loadingAccel.setTimestamp(currentTimestamp + 
							Long.parseLong(splitAccelString[0].substring(3, splitAccelString[0].indexOf(',', 3))) );
						loadingAccel.setxValue(Integer.parseInt(splitAccelString[1]));
						loadingAccel.setyValue(Integer.parseInt(splitAccelString[2]));
						loadingAccel.setzValue(Integer.parseInt(splitAccelString[3]));
						
						accelRaceData.add(loadingAccel);
					}
				}
			}
			raceFileScanner.close();
		}catch(Exception e){
			System.out.println("Oh noes! That file caused us to crash and burn!");
		}
	}
	
	public Vector<AccelSample> getAccelData()
	{
		return accelRaceData;
	}
	
	public Vector<GpsSample> getGPSData()
	{
		return gpsRaceData;
	}
	
	private boolean lineCheck(String currentLine)
	{
		boolean isGoodLine = true;
		String lineSplit[] = currentLine.split(",");
		
		try{
			//Accel data will have 4 parts
			if(lineSplit.length == 4)
			{
				String currentAccel = "";
				for(int parseAccel = 0; parseAccel < 4; parseAccel++)
				{
					if (parseAccel == 0){
						if(!lineSplit[parseAccel].contains("02:"))
						{
							return false;
						}
						currentAccel = lineSplit[parseAccel].substring(3, lineSplit[parseAccel].length());
						Integer.parseInt(currentAccel);
					}else{
						currentAccel = lineSplit[parseAccel];
						Integer.parseInt(currentAccel);
					}
				}
			}
			//GPS data will have 8 parts
			else if(lineSplit.length == 8)
			{
				String currentGps = "";
				for(int parseGps = 0; parseGps < 8; parseGps++){
					if (parseGps == 0){
						currentGps = lineSplit[parseGps].substring(3, lineSplit[parseGps].length());
						if(!lineSplit[parseGps].contains("01:"))
						{
							return false;
						}
						Double.parseDouble(currentGps);
					}else if (parseGps == 1 || parseGps == 3 || parseGps == 5 || parseGps == 6 || parseGps == 7){
						currentGps = lineSplit[parseGps];
						Double.parseDouble(currentGps);
					}
				}
			}
			else
			{
				return false;
			}
		}
		catch(NumberFormatException e){
			return false;
		}
		return isGoodLine;
	}
}