package dasAuto.logData.feeds;

import dasAuto.LogFileReader;


public class DataFeed {
	private static DataFeed instance = null;
	
	private AccelFeed accelFeed;
	private GpsFeed gpsFeed;

	
	private DataFeed() {
		loadData();
	}
	
	
	public void loadData() {
		LogFileReader log = new LogFileReader();
		accelFeed = log.getAccelFeed();
		gpsFeed = log.getGpsFeed();
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
