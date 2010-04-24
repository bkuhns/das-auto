package dasAuto.graphics;

import java.awt.Polygon;
import java.awt.Point;
import java.awt.geom.Point2D;

import dasAuto.logData.samples.GpsSample;

public class CourseMapPolygon extends Polygon {
	
	private Point oldPolyPointHigh;
	private Point oldPolyPointLow;
	private Point newPolyPointHigh;
	private Point newPolyPointLow;
	private Point2D oldLocationHigh;
	private Point2D oldLocationLow;
	private Point2D newLocationHigh;
	private Point2D newLocationLow;
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
	public CourseMapPolygon(GpsSample currentSample, GpsSample previousSample, Point2D previousLocationLow, Point2D previousLocationHigh, int currentAccel){
		
		newSample = currentSample;
		oldSample = previousSample;
		newAccel = currentAccel;
		
		oldLocationLow = previousLocationLow;
		oldLocationHigh = previousLocationHigh;
		newLocationHigh = findPoint2D(newSample, oldSample, currentAccel, 1);
		newLocationLow = findPoint2D(newSample, oldSample, currentAccel, -1);
	}
	
	
	//Specialized constructor for first Polygon with 2 GpsSamples
	public CourseMapPolygon(GpsSample currentSample, GpsSample previousSample, int currentAccel,  int previousAccel)
	{
		newSample = currentSample;
		oldSample = previousSample;
		oldAccel = previousAccel;
		newAccel = currentAccel;
		
		oldLocationHigh = findPoint2D(oldSample, newSample, previousAccel, 1);
		oldLocationLow = findPoint2D(oldSample, newSample, previousAccel, -1);
		newLocationHigh = findPoint2D(newSample, oldSample, currentAccel, 1);
		newLocationLow = findPoint2D(newSample, oldSample, currentAccel, -1);
	}
	
	
	public Point2D findPoint2D(GpsSample toSample, GpsSample fromSample, int accelValue, int directionMultipler)
	{
		Point2D returnPoint = new Point2D.Double();
		double accelWidth = 0;
		
		accelWidth = determineCurrentAccelWidth(accelValue);
		returnPoint = determineGpsLocation(toSample, fromSample, directionMultipler * accelWidth);
		
		return returnPoint;
	}
	
	private double determineCurrentAccelWidth(int accel) {
		
		double currentAccelWidth = 0;
		double minAccelGpsWidth = 0;
		double maxAccelGpsWidth = 0;
		double currentWidthProportion = (double)(accel - minAccel) / (double)(maxAccel - minAccel);
		
		double latDelta = maxLat - minLat;
		double lonDelta = maxLon - minLon;
		
		if(latDelta > lonDelta) {
			minAccelGpsWidth = 0.05 * lonDelta;
			maxAccelGpsWidth = 0.10 * lonDelta;
		} 
		else {
			minAccelGpsWidth = 0.05 * latDelta;
			maxAccelGpsWidth = 0.10 * latDelta;
		}
		
		if(currentWidthProportion == 0.0)
			currentAccelWidth = minAccelGpsWidth;
		else
			currentAccelWidth = minAccelGpsWidth + (currentWidthProportion * (maxAccelGpsWidth - minAccelGpsWidth));
		
		
		return currentAccelWidth;
	}
	
	private Point2D determineGpsLocation(GpsSample toSample, GpsSample fromSample, double distanceToNewPoint) {
		Point2D gpsPoint = new Point2D.Double();
		
		double changeInLatitude = toSample.getLatitude() - fromSample.getLatitude();
		double changeInLongitude = toSample.getLongitude() - fromSample.getLongitude();
		
		double angleBetweenGpsCoord = 0.0;
		double perpendicularAngle = 0.0;
		
		double gpsPointLat = 0.0;
		double gpsPointLon = 0.0;
		
		if(changeInLongitude == 0.0) {
			angleBetweenGpsCoord = 0.0;
		}
		else {
			angleBetweenGpsCoord = Math.atan( changeInLatitude / changeInLongitude);
		}
		
		perpendicularAngle = angleBetweenGpsCoord + 90.0;
		
		gpsPointLon = distanceToNewPoint * Math.cos(perpendicularAngle);
		gpsPointLat = distanceToNewPoint * Math.sin(perpendicularAngle);
		
		gpsPoint.setLocation(gpsPointLon, gpsPointLat);
		
		return gpsPoint;
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
