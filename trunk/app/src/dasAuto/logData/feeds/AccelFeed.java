package dasAuto.logData.feeds;

import java.util.ListIterator;

import org.jfree.data.xy.*;

import dasAuto.logData.samples.AccelSample;



public class AccelFeed extends DataFeed<AccelSample> {
	private static final long serialVersionUID = -3210947898740405829L;
	
	public static final int X_AXIS = 0;
	public static final int Y_AXIS = 1;
	public static final int Z_AXIS = 2;
	
	public static final int DEFAULT_FILTER_RESOLUTION = 75;
	
	private AccelFeed filteredFeed;
	private int lastFilterResolution = -1;
	
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
	
	
	/*public AccelFeed getFilteredFeed(int filterResolution) {
		if(filterResolution != lastFilterResolution) {
			AccelFeed tmpFilteredFeed = new AccelFeed();
			
			ListIterator<AccelSample> it = listIterator();
			while(it.hasNext()) {
				int N = Integer.parseInt(args[0]);
		        double[] a = new double[N];
		        double sum = 0.0;
		        for (int i = 1; !StdIn.isEmpty(); i++) {
		            sum -= a[i % N];
		            a[i % N] = StdIn.readDouble();
		            sum += a[i % N];
		            if (i >= N) StdOut.print(sum/N + " ");
		        }
			}
		}
	}*/
	
	
	public AccelFeed getFilteredFeed(int filterResolution) {
		if(filterResolution != lastFilterResolution) {
			AccelFeed tmpFilteredFeed = new AccelFeed();
			
			ListIterator<AccelSample> it = listIterator();
			while(it.hasNext()) {
				int xSum = 0;
				int ySum = 0;
				int zSum = 0;
				int i = 0;
				long timestamp = 0;
				
				for(i = 0; i < filterResolution; i++) {
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
				averagedSample.setXValue(xSum / i);
				averagedSample.setYValue(ySum / i);
				averagedSample.setZValue(zSum / i);
				
				tmpFilteredFeed.add(averagedSample);
			}
			
			filteredFeed = tmpFilteredFeed;
		}
		
		return filteredFeed;
	}
	
	
	public AccelFeed getFilteredFeed() {
		return getFilteredFeed(DEFAULT_FILTER_RESOLUTION);
	}
	
	
	/**
	 * @param	axis	Determines if the returned XYSeries represents the x,y, or z axis.
	 */
	public XYSeries getXySeries(int axis) throws IllegalArgumentException {
		XYSeries xySeries = new XYSeries("accel series");
		long initialTimestamp = 0;
		
		for(int i = 0; i < size(); i++) {
			AccelSample currentSample = get(i);
			double currentValue;
			
			switch(axis) {
				case X_AXIS:
					currentValue = currentSample.getXValueInG();
					break;
				case Y_AXIS:
					currentValue = currentSample.getYValueInG();
					break;
				case Z_AXIS:
					currentValue = currentSample.getZValueInG();
					break;
				default:
					throw new IllegalArgumentException("AccelPanel must be of type X,Y, or Z");
			}
			
			// Pull the initial Timestamp from the first sample. Normalizes times to '0' at start.
			if(i == 0) {
				initialTimestamp = currentSample.getTimestamp();
			}
			xySeries.add((currentSample.getTimestamp() - initialTimestamp) / 1000.0, currentValue);
		}
		
		return xySeries;
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
	 * Compute the min/max acceleration values.
	 */
	private void computeMinMaxValues() {
		resetMinMaxValues();
		
		ListIterator<AccelSample> it = listIterator();
		while(it.hasNext()) {
			checkMinMaxValues(it.next());
		}
	}
	
	
	/**
	 * Check if the provided sample contains a min/max value of the feed.
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
	
	
	/* min/max X values */
	public int getMinXValue() {
		return minXValue;
	}

	public int getMaxXValue() {
		return maxXValue;
	}
	
	public double getMinXValueInG() {
		return AccelSample.convertCountToG(minXValue, AccelSample.X_OFFSET);
	}
	
	public double getMaxXValueInG() {
		return AccelSample.convertCountToG(maxXValue, AccelSample.X_OFFSET);
	}
	

	/* min/max Y values */
	public int getMinYValue() {
		return minYValue;
	}

	public int getMaxYValue() {
		return maxYValue;
	}
	
	public double getMinYValueInG() {
		return AccelSample.convertCountToG(minYValue, AccelSample.Y_OFFSET);
	}
	
	public double getMaxYValueInG() {
		return AccelSample.convertCountToG(maxYValue, AccelSample.Y_OFFSET);
	}
	

	/* min/max Z values */
	public int getMinZValue() {
		return minZValue;
	}

	public int getMaxZValue() {
		return maxZValue;
	}
	
	public double getMinZValueInG() {
		return AccelSample.convertCountToG(minZValue, AccelSample.Z_OFFSET);
	}
	
	public double getMaxZValueInG() {
		return AccelSample.convertCountToG(maxZValue, AccelSample.Z_OFFSET);
	}
	

}
