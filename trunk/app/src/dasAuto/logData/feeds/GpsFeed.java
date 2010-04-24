package dasAuto.logData.feeds;

import java.util.ArrayList;
import java.util.ListIterator;

import dasAuto.logData.samples.GpsSample;


public class GpsFeed extends ArrayList<GpsSample> {
	private static final long serialVersionUID = 5579977194924931720L;
	
	double minLatitude;
	double maxLatitude;
	
	double minLongitude;
	double maxLongitude;
	
	
	public GpsFeed() {
		super();
		
		resetMinMaxValues();
	}
	
	
	private void resetMinMaxValues() {
		minLatitude = Double.MAX_VALUE;
		maxLatitude = Double.MIN_VALUE;
		
		minLongitude = Double.MAX_VALUE;
		maxLongitude = Double.MIN_VALUE;
	}
	
	
	/**
	 * Compute the min/max values for all GPS coordinates.
	 */
	//TODO: Ask Bret about this function, I'm not sure it written the way it should be.
	private void computeMinMaxCoordinates() {
		ListIterator<GpsSample> it = listIterator();
		while(it.hasNext()) {
			computeMinMaxCoordinates();
		}
	}
	
	
	/**
	 * Check if the provided sample contains a min/max coordinate of the feed.
	 * 
	 * @param gpsSample
	 */
	private void checkMinMaxCoordinates(GpsSample gpsSample) {
		if(gpsSample.getLatitude() < minLatitude) {
			minLatitude = gpsSample.getLatitude();
		}
		if(gpsSample.getLatitude() > maxLatitude) {
			maxLatitude = gpsSample.getLatitude();
		}
		
		if(gpsSample.getLongitude() < minLongitude) {
			minLongitude = gpsSample.getLongitude();
		}
		if(gpsSample.getLongitude() > maxLongitude) {
			maxLongitude = gpsSample.getLongitude();
		}
	}
	
	
	@Override
	public boolean add(GpsSample gpsSample) {
		checkMinMaxCoordinates(gpsSample);
		
		return super.add(gpsSample);
	}
	
	
	@Override
	public void add(int index, GpsSample gpsSample) {
		checkMinMaxCoordinates(gpsSample);
		
		super.add(index, gpsSample);
	}
	
	
	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		
		resetMinMaxValues();
		computeMinMaxCoordinates();
		
		return result;
	}


	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		
		resetMinMaxValues();
		computeMinMaxCoordinates();
	}


	public double getMinLatitude() {
		return minLatitude;
	}

	
	public double getMaxLatitude() {
		return maxLatitude;
	}
	
	
	public double getMinLongitude() {
		return minLongitude;
	}
	
	
	public double getMaxLongitude() {
		return maxLongitude;
	}
	
	
}
