package dasAuto.logData.feeds;

import java.util.ArrayList;
import java.util.ListIterator;

import org.jfree.data.xy.*;

import dasAuto.logData.samples.AccelSample;


public class AccelFeed extends ArrayList<AccelSample> {
	private static final long serialVersionUID = -3210947898740405829L;
	
	private AccelFeed filteredFeed;
	private int lastFilterResolution = -1;
	
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;
	
	private int minXValue;
	private int maxXValue;
	
	private int minYValue;
	private int maxYValue;
	
	private int minZValue;
	private int maxZValue;
	
	
	public AccelFeed() {
		super();
		
		resetMinMaxValues();
	}
	
	
	public AccelFeed getFilteredFeed(int filterResolution) {
		if(filterResolution != lastFilterResolution) {
			AccelFeed tmpFilteredFeed = new AccelFeed();
			
			ListIterator<AccelSample> it = listIterator();
			while(it.hasNext()) {
				int xSum = 0;
				int ySum = 0;
				int zSum = 0;
				long timestamp = 0;
				
				for(int i = 0; i < filterResolution; i++) {
					if(!it.hasNext()) {
						break;
					}
					
					AccelSample currentSample = it.next();
					if(i == 0) {
						timestamp = currentSample.getTimestamp();
					}
					
					xSum += currentSample.getXValue();
					ySum += currentSample.getYValue();
					zSum += currentSample.getZValue();
				}
				
				AccelSample averagedSample = new AccelSample();
				averagedSample.setTimestamp(timestamp);
				averagedSample.setXValue(xSum / filterResolution);
				averagedSample.setYValue(ySum / filterResolution);
				averagedSample.setZValue(zSum / filterResolution);
				tmpFilteredFeed.add(averagedSample);
			}
			
			filteredFeed = tmpFilteredFeed;
		}
		
		return filteredFeed;
	}
	
	
	private void resetMinMaxValues() {
		minXValue = Integer.MAX_VALUE;
		maxXValue = Integer.MIN_VALUE;
		
		minYValue = Integer.MAX_VALUE;
		maxYValue = Integer.MIN_VALUE;
		
		minZValue = Integer.MAX_VALUE;
		maxZValue = Integer.MIN_VALUE;
	}
	
	
	/**
	 * Compute the min/max values for all GPS coordinates.
	 */
	private void computeMinMaxValues() {
		resetMinMaxValues();
		
		ListIterator<AccelSample> it = listIterator();
		while(it.hasNext()) {
			checkMinMaxValues(it.next());
		}
	}
	
	
	/**
	 * Check if the provided sample contains a min/max coordinate of the feed.
	 * 
	 * @param sample
	 */
	private void checkMinMaxValues(AccelSample sample) {
		if(sample.getXValue() < minXValue) {
			minXValue = sample.getXValue();
		}
		if(sample.getXValue() > maxXValue) {
			maxXValue = sample.getXValue();
		}
		
		if(sample.getYValue() < minYValue) {
			minYValue = sample.getYValue();
		}
		if(sample.getYValue() > maxYValue) {
			maxYValue = sample.getYValue();
		}
		
		if(sample.getZValue() < minZValue) {
			minZValue = sample.getZValue();
		}
		if(sample.getZValue() > maxZValue) {
			maxZValue = sample.getZValue();
		}
	}
	
	
	@Override
	public boolean add(AccelSample sample) {
		checkMinMaxValues(sample);
		
		return super.add(sample);
	}
	
	
	@Override
	public void add(int index, AccelSample sample) {
		checkMinMaxValues(sample);
		
		super.add(index, sample);
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
	
	
	/**
	 * @param	axis	Determines if the returned XYSeries represents the x,y, or z axis.
	 */
	public XYSeries getXySeries(int axis) throws IllegalArgumentException {
		XYSeries xySeries = new XYSeries("accel series");
		long initialTimestamp = 0;
		
		for(int i = 0; i < size(); i++) {
			AccelSample currentSample = get(i);
			int currentValue;
			
			switch(axis) {
				case X_AXIS:
					currentValue = currentSample.getXValue();
					break;
				case Y_AXIS:
					currentValue = currentSample.getYValue();
					break;
				case Z_AXIS:
					currentValue = currentSample.getZValue();
					break;
				default:
					throw new IllegalArgumentException("AccelPanel must be of type X,Y, or Z");
			}
			
			// Pull the initial Timestamp from the first sample. Normalizes times to '0' at start.
			if(i == 0) {
				initialTimestamp = currentSample.getTimestamp();
			}
			xySeries.add(currentSample.getTimestamp() - initialTimestamp, currentValue);
		}
		
		return xySeries;
	}
	
	
	public int getMinXValue() {
		return minXValue;
	}

	public int getMaxXValue() {
		return maxXValue;
	}

	public int getMinYValue() {
		return minYValue;
	}

	public int getMaxYValue() {
		return maxYValue;
	}

	public int getMinZValue() {
		return minZValue;
	}

	public int getMaxZValue() {
		return maxZValue;
	}
	

}
