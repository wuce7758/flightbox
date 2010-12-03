package org.lttng.flightbox;

import java.util.ArrayList;

import org.lttng.flightbox.GlobalState.KernelMode;

public class TimeStatsBucket {

	ArrayList<TimeStats> buckets;
	
	double t1;
	double t2;
	double bucketDuration;
	
	public TimeStatsBucket() {
		this(0, 0, 1);
	}
	
	public TimeStatsBucket(double t1, double t2, int nbBucket) {
		init(t1, t2, nbBucket);
	}

	
	public double getStartTime() {
		return t1;
	}

	public double getEndTime() {
		return t2;
	}

	public double getBinDuration() {
		return bucketDuration;
	}

	
	public TimeStats getSum() {
		if (buckets.size() == 0) {
			return new TimeStats();
		}
		TimeStats first = buckets.get(0);
		TimeStats sum = new TimeStats(first.getStartTime(), first.getEndTime());
		for (TimeStats t: buckets) {
			sum.add(t);
		}
		return sum;
	}
	
	public void init(double t1, double t2, int nbBucket) {
		buckets = new ArrayList<TimeStats>();
		this.t1 = t1;
		this.t2 = t2;
		if (nbBucket <= 0) {
			return;
		} 
		bucketDuration = (t2 - t1) / nbBucket;
		for(int i=0; i<nbBucket; i++) {
			buckets.add(new TimeStats(t1 + (bucketDuration * i), t1 + (bucketDuration * (i + 1))));
		}
	}
	
	public int size() {
		return buckets.size();
	}
	
	public TimeStats getIntervalByTime(double t) {
		int index = getIntervalIndex(t);
		return buckets.get(index);
	}
	
	public int getIntervalIndex(double t) {
		return (int)Math.floor((t - t1) / bucketDuration);
	}
	
	public void addInterval(double t1, double t2, KernelMode mode) {
		int index_start = getIntervalIndex(t1);
		int index_end = getIntervalIndex(t2);
		for(int i=index_start; i<=index_end; i++) {
			buckets.get(i).addInterval(t1, t2, mode);
		}
	}
	
}