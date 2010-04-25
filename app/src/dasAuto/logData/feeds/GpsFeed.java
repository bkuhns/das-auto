package dasAuto.logData.feeds;

import java.util.ArrayList;
import java.util.ListIterator;

import org.jfree.data.xy.XYSeries;

import dasAuto.logData.samples.GpsSample;


public class GpsFeed extends ArrayList<GpsSample> {
	private static final long serialVersionUID = 5579977194924931720L;
	
	private double minLatitude;
	private double maxLatitude;
	
	private double minLongitude;
	private double maxLongitude;
	
	private double minSpeed;
	private double maxSpeed;
	
	
	public GpsFeed() {
		super();
		
		resetMinMaxValues();
	}
	
	
	public XYSeries getXySpeedSeries() {
		XYSeries xySeries = new XYSeries("GPS speed series");
		long initialTimestamp = 0;
		
		for(int i = 0; i < size(); i++) {
			GpsSample currentSample = get(i);
			double currentValue = currentSample.getSpeed();
			
			// Pull the initial Timestamp from the first sample. Normalizes times to '0' at start.
			if(i == 0) {
				initialTimestamp = currentSample.getTimestamp();
			}
			xySeries.add(currentSample.getTimestamp() - initialTimestamp, currentValue);
		}
		
		return xySeries;
	}
	
	
	private void resetMinMaxValues() {
		minLatitude = Double.MAX_VALUE;
		maxLatitude = Double.MIN_VALUE;
		
		minLongitude = Double.MAX_VALUE;
		maxLongitude = Double.MIN_VALUE;
		
		minSpeed = Double.MAX_VALUE;
		maxSpeed = Double.MIN_VALUE;
	}
	
	
	/**
	 * Compute the min/max values for all GPS coordinates.
	 */
	private void computeMinMaxValues() {
		resetMinMaxValues();
		
		ListIterator<GpsSample> it = listIterator();
		while(it.hasNext()) {
			checkMinMaxValues(it.next());
		}
	}
	
	
	/**
	 * Check if the provided sample contains a min/max coordinate of the feed.
	 * 
	 * @param gpsSample
	 */
	private void checkMinMaxValues(GpsSample gpsSample) {
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
		
		if(gpsSample.getSpeed() < minSpeed) {
			minSpeed = gpsSample.getSpeed();
		}
		if(gpsSample.getSpeed() > maxSpeed) {
			maxSpeed = gpsSample.getSpeed();
		}
	}
	
	
	@Override
	public boolean add(GpsSample gpsSample) {
		checkMinMaxValues(gpsSample);
		
		return super.add(gpsSample);
	}
	
	
	@Override
	public void add(int index, GpsSample gpsSample) {
		checkMinMaxValues(gpsSample);
		
		super.add(index, gpsSample);
	}
	
	
	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		
		resetMinMaxValues();
		computeMinMaxValues();
		
		return result;
	}


	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		
		resetMinMaxValues();
		computeMinMaxValues();
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
	
	
	public double getMinSpeed() {
		return minSpeed;
	}
	
	public double getMaxSpeed() {
		return maxSpeed;
	}
	
	
}
