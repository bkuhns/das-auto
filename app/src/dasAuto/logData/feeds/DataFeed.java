package dasAuto.logData.feeds;

import java.util.ArrayList;
import java.util.ListIterator;

import dasAuto.logData.samples.DataSample;



public class DataFeed<Sample extends DataSample> extends ArrayList<Sample> {
	private static final long serialVersionUID = 7624210033955378312L;
	
	private long minTimestamp;
	private long maxTimestamp;
	
	
	public DataFeed() {
		super();
		resetMinMaxTimestamps();
	}
	
	
	/**
	 * Finds the nearest sample in the list to the given timestamp. We look at
	 * the provided timestamp and calculate a ratio to determine that timestamp's
	 * general location in the entire timeline (from min to max timestamp). Take
	 * ratio location in our data feed to get a guessed starting point for
	 * searching. From there, just linear search in either forward of backward
	 * directions until we find the right sample and return it's index.
	 * 
	 * TODO We may want to consider a binary search.
	 * 
	 * @param timestamp
	 * @return
	 */
	public int getNearestSampleIndex(long timestamp) {
		double indexRatio = (timestamp - minTimestamp) / (maxTimestamp - minTimestamp);	// Locational ratio of timestamp in the range of min->max timestamps.
		int startingIndex = (int)Math.round(indexRatio * size()) - 1;
		int returnIndex = -1;
		
		ListIterator<Sample> it = listIterator(startingIndex);
		if(it.hasNext() && it.next().getTimestamp() < timestamp) {
			// The next adjacent item is smaller, so we need to find larger values => iterate forward.
			while(it.hasNext()) {
				returnIndex = it.nextIndex();
				if(it.next().getTimestamp() > timestamp) {
					// Found it!
					break;
				}
			}
		} else {
			// Looks like we need to iterator backwards. First check to make sure, 
			// otherwise we've already found the right index.
			if(it.hasPrevious() && it.previous().getTimestamp() > timestamp) {
				while(it.hasPrevious()) {
					returnIndex = it.previousIndex();
					if(it.previous().getTimestamp() < timestamp) {
						// Found it!
						break;
					}
				}
			} else {
				// Holy crap, we found it on the first try!
				returnIndex = startingIndex;
			}
		}
		
		return returnIndex;
	}
	
	
	public Sample getNearestSample(long timestamp) {
		return get(getNearestSampleIndex(timestamp));
	}
	
	
	private void resetMinMaxTimestamps() {
		minTimestamp = Long.MAX_VALUE;
		maxTimestamp = Long.MIN_VALUE;
	}
	
	
	/**
	 * Compute the min/max acceleration values.
	 */
	private void computeMinMaxTimestamps() {
		resetMinMaxTimestamps();
		
		ListIterator<Sample> it = listIterator();
		while(it.hasNext()) {
			checkMinMaxTimestamps(it.next());
		}
	}
	
	
	/**
	 * Check if the provided sample contains a min/max timestamp of the feed.
	 * 
	 * @param sample
	 */
	private void checkMinMaxTimestamps(Sample sample) {
		if(sample.getTimestamp() < minTimestamp) {
			minTimestamp = sample.getTimestamp();
		}
		if(sample.getTimestamp() > maxTimestamp) {
			maxTimestamp = sample.getTimestamp();
		}
	}
	
	
	@Override
	public boolean add(Sample sample) {
		checkMinMaxTimestamps(sample);
		
		return super.add(sample);
	}
	
	
	@Override
	public void add(int index, Sample sample) {
		checkMinMaxTimestamps(sample);
		
		super.add(index, sample);
	}
	
	
	@Override
	public boolean remove(Object o) {
		boolean result = super.remove(o);
		
		resetMinMaxTimestamps();
		computeMinMaxTimestamps();
		
		return result;
	}


	@Override
	protected void removeRange(int fromIndex, int toIndex) {
		super.removeRange(fromIndex, toIndex);
		
		resetMinMaxTimestamps();
		computeMinMaxTimestamps();
	}
	
	
	/* minTimestamp */
	public long getMinTimestamp() {
		return minTimestamp;
	}
	
	/* maxTimestamp */
	public long getMaxTimestamp() {
		return maxTimestamp;
	}
	
	
}
