package dasAuto.logData.feeds;

import java.util.ArrayList;
import java.util.ListIterator;

import dasAuto.logData.samples.AccelSample;
import dasAuto.logData.samples.GpsSample;



public class AccelFeed extends ArrayList<AccelSample> {
	private static final long serialVersionUID = -3210947898740405829L;
	
	int minXAccel;
	int maxXAccel;
	
	int minYAccel;
	int maxYAccel;
	
	int minZAccel;
	int maxZAccel;
	
	
	public AccelFeed() {
		super();
		
		resetMinMaxValues();
	}


	private void resetMinMaxValues() {
		minXAccel = Integer.MIN_VALUE;
		maxXAccel = Integer.MAX_VALUE;
		
		minYAccel = Integer.MIN_VALUE;
		maxYAccel = Integer.MAX_VALUE;
		
		minZAccel = Integer.MIN_VALUE;
		maxZAccel = Integer.MAX_VALUE;
	}
	
	/**
	 * Compute the min/max values for all GPS coordinates.
	 */
	//TODO: Ask Bret about this function, I'm not sure it written the way it should be.
	private void computeMinMaxCoordinates() {
		ListIterator<AccelSample> it = listIterator();
		while(it.hasNext()) {
			computeMinMaxCoordinates();
		}
	}
	
	
	/**
	 * Check if the provided sample contains a min/max value of the feed.
	 * 
	 * @param accelSample
	 */
	private void checkMinMaxCoordinates(AccelSample accelSample) {
		if(accelSample.getxValue() < minXAccel) {
			minXAccel = accelSample.getxValue();
		}
		if(accelSample.getxValue() > maxXAccel) {
			maxXAccel = accelSample.getxValue();
		}
		
		if(accelSample.getyValue() < minYAccel) {
			minYAccel = accelSample.getyValue();
		}
		if(accelSample.getyValue() > maxYAccel) {
			maxYAccel = accelSample.getyValue();
		}
		
		if(accelSample.getzValue() < minZAccel) {
			minZAccel = accelSample.getzValue();
		}
		if(accelSample.getzValue() > maxZAccel) {
			maxZAccel = accelSample.getzValue();
		}
	}
	
	
	@Override
	public boolean add(AccelSample accelSample) {
		checkMinMaxCoordinates(accelSample);
		
		return super.add(accelSample);
	}
	
	
	@Override
	public void add(int index, AccelSample accelSample) {
		checkMinMaxCoordinates(accelSample);
		
		super.add(index, accelSample);
	}
	
	
	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		
		resetMinMaxValues();
		computeMinMaxCoordinates();
		
		return result;
	}
	
	
	public int getMinXAccel() {
		return minXAccel;
	}


	public int getMaxXAccel() {
		return maxXAccel;
	}


	public int getMinYAccel() {
		return minYAccel;
	}


	public int getMaxYAccel() {
		return maxYAccel;
	}


	public int getMinZAccel() {
		return minZAccel;
	}


	public int getMaxZAccel() {
		return maxZAccel;
	}
	
	
}
