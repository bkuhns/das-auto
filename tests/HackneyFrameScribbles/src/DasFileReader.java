import java.io.File;
import java.util.Scanner;
import java.util.Vector;

public class DasFileReader {

	protected Vector<String> accelRaceData = new Vector<String>();
	protected Vector<String> gpsRaceData = new Vector<String>();
	
	public DasFileReader(String fileName){
		String nextLineFromScanner = "";
		
		try{
			Scanner raceFileScanner = new Scanner(new File(fileName));
			
			while(raceFileScanner.hasNextLine()){
				nextLineFromScanner = raceFileScanner.nextLine();
				//checks to see if device is gps: device 01
				if(nextLineFromScanner.substring(0, 2).equals("01")){
					gpsRaceData.add(nextLineFromScanner.substring(3));
				}
				
				//checks to see if device is accelerometere: device 02
				else if(nextLineFromScanner.substring(0, 2).equals("02")){
					accelRaceData.add(nextLineFromScanner.substring(3));
				}
			}
			raceFileScanner.close();
		}
		catch(Exception e){
			System.out.println("Oh noes! That file caused us to crash and burn!");
		}
	}
	
	public Vector<String> getAccelData(){
		return accelRaceData;
	}
	
	public Vector<String> getGPSData(){
		return gpsRaceData;
	}
}
