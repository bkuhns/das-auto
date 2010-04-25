package dasAuto.logData.feeds;

import java.io.File;

import javax.swing.JFileChooser;

import dasAuto.LogFileReader;


public class DataFeed {
	private static DataFeed instance = null;
	
	private AccelFeed accelFeed;
	private GpsFeed gpsFeed;

	
	private DataFeed() {
		loadData();
	}
	
	
	public void loadData() {
		LogFileReader log = new LogFileReader(getFileFromUser());
		accelFeed = log.getAccelFeed();
		gpsFeed = log.getGpsFeed();
	}
	
	
	private File getFileFromUser() {
		JFileChooser dataFileChooser = new JFileChooser();
	    dataFileChooser.setName("jFileChooser1"); // NOI18N
	    //dataFileChooser.setBounds(0, 0, 582, 397);
	    File dataFile = null;
	
	   //Attempt to Close the frame if the User Closes the File Browser
	   //TODO:  Adding functionality to either close program or show blank frame if user does not select a file.
	   //   	If no file is selected, dataFile will be null at upon return.
	   int fileBrowserResult = dataFileChooser.showOpenDialog(null);
	   if(fileBrowserResult == JFileChooser.APPROVE_OPTION){
	        dataFile = dataFileChooser.getSelectedFile();
	   }
	   else if(fileBrowserResult == JFileChooser.CANCEL_OPTION){
	       System.out.println("Dialog was Canceled.");
	   }
	   return dataFile;
	}
	
	
	public GpsFeed getGpsFeed() {
		return gpsFeed;
	}
	
	
	public AccelFeed getAccelFeed() {
		return accelFeed;
	}
	
	
	public static DataFeed getInstance() {
		if(instance == null) {
			synchronized(DataFeed.class) {
				instance = new DataFeed();
			}
		}
		
		return instance;
	}
	
	
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}
	
	
}
