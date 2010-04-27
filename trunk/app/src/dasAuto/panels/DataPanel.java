package dasAuto.panels;

import javax.swing.JPanel;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.logData.feeds.LogFeed;
import dasAuto.logData.feeds.GpsFeed;


public abstract class DataPanel extends JPanel {
	private static final long serialVersionUID = 4385467239459710925L;
	
	public AccelFeed accelFeed;
	public GpsFeed gpsFeed;
	
	
	public DataPanel() {
		LogFeed dataFeed = LogFeed.getInstance();
		
		accelFeed = dataFeed.getAccelFeed();
		gpsFeed = dataFeed.getGpsFeed();
	}
	
	
}
