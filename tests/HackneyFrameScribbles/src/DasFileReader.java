import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class DasFileReader {

	protected Vector<String> accelRaceData = new Vector<String>();
	protected Vector<String> gpsRaceData = new Vector<String>();
	
	public DasFileReader(String fileName)
	{
		String nextLineFromScanner = "";
		
		try{
			Scanner raceFileScanner = new Scanner(new File(fileName));
			
			while(raceFileScanner.hasNextLine())
			{
				nextLineFromScanner = raceFileScanner.nextLine();
				
				//run testing in lineCheck for errors in data
				if(lineCheck(nextLineFromScanner))
				{
					//checks to see if device is gps: device 01
					if(nextLineFromScanner.substring(0, 2).equals("01"))
					{
						gpsRaceData.add(nextLineFromScanner.substring(3));
					}
					
					//checks to see if device is accelerometere: device 02
					else if(nextLineFromScanner.substring(0, 2).equals("02"))
					{
						accelRaceData.add(nextLineFromScanner.substring(3));
					}
				}
			}
			System.out.println("Accel Data Size: " + accelRaceData.size());
			System.out.println("Gps Data Size: " + gpsRaceData.size());
			raceFileScanner.close();
		}catch(Exception e){
			System.out.println("Oh noes! That file caused us to crash and burn!");
		}
	}
	
	public Vector<String> getAccelData()
	{
		return accelRaceData;
	}
	
	public Vector<String> getGPSData()
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
