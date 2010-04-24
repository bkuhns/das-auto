package dasAuto.graphics;

import java.awt.Polygon;
import java.awt.Point;

import dasAuto.logData.samples.GpsSample;

public class CourseMapPolygon extends Polygon {
	
	private Point oldPolyPointHigh;
	private Point oldPolyPointLow;
	private Point newPolyPointHigh;
	private Point newPolyPointLow;
	private GpsSample oldSample;
	private GpsSample newSample;
	private double minLat;
	private double maxLat;
	private double minLon;
	private double maxLon;
	private double minAccel;
	private double maxAccel;
	private int oldAccel;
	private int newAccel;
	
	//Normal Constructor for construction with GpsSample
	public CourseMapPolygon(GpsSample currentSample, Point previousPointHigh, Point previousPointLow, int currentAccel){
		
		newSample = currentSample;
		oldPolyPointHigh = previousPointHigh;
		oldPolyPointLow = previousPointLow;
		newAccel = currentAccel;
		
	}
	
	//Specialized constructor for first Polygon with 2 GpsSamples
	public CourseMapPolygon(GpsSample currentSample, GpsSample previousSample, int previousAccel, int currentAccel)
	{
		newSample = currentSample;
		oldSample = previousSample;
		oldAccel = previousAccel;
		newAccel = currentAccel;
	}
	
	
	public void addPoint(Point p) {
		addPoint(p.x, p.y);
	}
	
	
	public Point getNewPolyPointHigh() {
		return newPolyPointHigh;
	}

	public Point getNewPolyPointLow() {
		return newPolyPointLow;
	}
	

	public void setMaxAndMins(double minimumLat, double maximumLat, double minimumLon,
			                  double maximumLon, int minimumAccel, int maximumAccel) 
	{
		setMinLat(minimumLat);
		setMinLon(minimumLon);
		setMinAccel(minimumAccel);
		
		setMaxLat(maximumLat);
		setMaxLon(maximumLon);
		setMaxAccel(maximumAccel);
	}
	
	
	public void setMinLat(double minLat) {
		this.minLat = minLat;
	}

	
	public void setMaxLat(double maxLat) {
		this.maxLat = maxLat;
	}

	
	public void setMinLon(double minLon) {
		this.minLon = minLon;
	}

	
	public void setMaxLon(double maxLon) {
		this.maxLon = maxLon;
	}

	
	public void setMinAccel(double minAccel) {
		this.minAccel = minAccel;
	}

	
	public void setMaxAccel(double maxAccel) {
		this.maxAccel = maxAccel;
	}


}
