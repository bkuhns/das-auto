package dasAuto.logData.feeds;

import java.io.File;

import javax.swing.JFileChooser;

import dasAuto.LogFileReader;


public class LogFeed {
	private static LogFeed instance = null;
	
	private AccelFeed accelFeed;
	private GpsFeed gpsFeed;
	
	private String logFilename;

	
	private LogFeed() {
		loadData();
	}
	
	
	public void loadData() {
		LogFileReader log = new LogFileReader(getFileFromUser());
		accelFeed = log.getAccelFeed();
		gpsFeed = log.getGpsFeed();
	}
	
	
	private File getFileFromUser() {
		JFileChooser dataFileChooser = new JFileChooser();
	    dataFileChooser.setName("jFileChooser1");
	    //dataFileChooser.setBounds(0, 0, 582, 397);
	    File logFile = null;
	
	   // Attempt to Close the frame if the User Closes the File Browser
	   // TODO: Add functionality to either close program or show blank frame if user does not select a file.
	   // If no file is selected, dataFile will be null at upon return.
	   int fileBrowserResult = dataFileChooser.showOpenDialog(null);
	   if(fileBrowserResult == JFileChooser.APPROVE_OPTION){
	        logFile = dataFileChooser.getSelectedFile();
	        logFilename = logFile.getName();
	   }
	   else if(fileBrowserResult == JFileChooser.CANCEL_OPTION){
	       System.out.println("Dialog was Canceled.");
	   }
	   return logFile;
	}
	
	
	public GpsFeed getGpsFeed() {
		return gpsFeed;
	}
	
	
	public AccelFeed getAccelFeed() {
		return accelFeed;
	}
	
	
	public String getLogFilename() {
		return logFilename;
	}
	
	
	public static LogFeed getInstance() {
		if(instance == null) {
			synchronized(LogFeed.class) {
				instance = new LogFeed();
			}
		}
		
		return instance;
	}
	
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	
}
