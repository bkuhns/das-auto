package dasAuto.graphics;

import java.awt.Polygon;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import dasAuto.logData.samples.GpsSample;

public class CourseMapPolygon extends Polygon {

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
	private double oldSpeed;
	private double newSpeed;
	
	
	//Normal Constructor for construction with GpsSample
	public CourseMapPolygon(GpsSample currentSample, GpsSample previousSample, 
						    Point2D previousLocationLow, Point2D previousLocationHigh, 
						    int currentAccel, double previousSpeed, double currentSpeed){
		
		newSample = currentSample;
		oldSample = previousSample;
		newAccel = currentAccel;
		oldSpeed = previousSpeed;
		newSpeed = currentSpeed;
		
		oldLocationLow = previousLocationLow;
		oldLocationHigh = previousLocationHigh;
	}
	
	
	//Specialized constructor for first Polygon with 2 GpsSamples
	public CourseMapPolygon(GpsSample currentSample, GpsSample previousSample, 
							int currentAccel,  int previousAccel, 
							double previousSpeed, double currentSpeed) {
		
		newSample = currentSample;
		oldSample = previousSample;
		oldAccel = previousAccel;
		newAccel = currentAccel;
		oldSpeed = previousSpeed;
		newSpeed = currentSpeed;
	}
	
	
	public void instantiatePolygon() {
		newLocationHigh = findPoint2D(newSample, oldSample, newAccel, 1);
		newLocationLow = findPoint2D(newSample, oldSample, newAccel, -1);
	}
	
	
	public void instantiateFirstPolygon() {
		oldLocationHigh = findPoint2D(oldSample, newSample, oldAccel, 1);
		oldLocationLow = findPoint2D(oldSample, newSample, oldAccel, -1);
		newLocationHigh = findPoint2D(newSample, oldSample, newAccel, 1);
		newLocationLow = findPoint2D(newSample, oldSample, newAccel, -1);
	}
	
	
	public Polygon getCourseMapPolygon(int panelWidth, int panelHeight)
	{
		Polygon roundedPolygon = new Polygon();
		
		int point1Y = (int)Math.round(  (((maxLat - newLocationHigh.getY()) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
		int point1X = (int)Math.round( (((maxLon - newLocationHigh.getX()) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
		int point2Y = (int)Math.round(  (((maxLat - newLocationLow.getY()) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
		int point2X = (int)Math.round( (((maxLon - newLocationLow.getX()) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
		int point3Y = (int)Math.round(  (((maxLat - oldLocationHigh.getY()) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
		int point3X = (int)Math.round( (((maxLon - oldLocationHigh.getX()) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
		int point4Y = (int)Math.round(  (((maxLat - oldLocationLow.getY()) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
		int point4X = (int)Math.round( (((maxLon - oldLocationLow.getX()) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
		
		//Need special conditions here before adding points, make sure to add them in the correct order.
		//Check within the points themselves
		if(Line2D.linesIntersect(oldLocationLow.getX(), oldLocationLow.getY(), 
					             newLocationHigh.getX(), newLocationHigh.getY(), 
					             newLocationLow.getX(), newLocationLow.getY(), 
					             oldLocationHigh.getX(), oldLocationHigh.getY())) {
			
			roundedPolygon.addPoint(point4X, point4Y);
			roundedPolygon.addPoint(point2X, point2Y);
			roundedPolygon.addPoint(point1X, point1Y);
			roundedPolygon.addPoint(point3X, point3Y);
		}
		else {
			
			roundedPolygon.addPoint(point4X, point4Y);
			roundedPolygon.addPoint(point1X, point1Y);
			roundedPolygon.addPoint(point2X, point2Y);
			roundedPolygon.addPoint(point3X, point3Y);
		}
		
		
		
		return roundedPolygon;
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

	
	public void addPoint(Point p) {
		addPoint(p.x, p.y);
	}
	
	
	public Point2D getOldLocationHigh() {
		return oldLocationHigh;
	}


	public Point2D getOldLocationLow() {
		return oldLocationLow;
	}


	public Point2D getNewLocationHigh() {
		return newLocationHigh;
	}


	public Point2D getNewLocationLow() {
		return newLocationLow;
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
	
	
	
	
	private Point2D findPoint2D(GpsSample toSample, GpsSample fromSample, int accelValue, int directionMultipler)
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
		double currentWidthProportion = ( (double)(accel - minAccel) ) / ( (double)(maxAccel - minAccel) );
		
		double latDelta = maxLat - minLat;
		double lonDelta = maxLon - minLon;
		
		if(latDelta > lonDelta) {
			minAccelGpsWidth = 0.01 * lonDelta;
			maxAccelGpsWidth = 0.02 * lonDelta;
		} 
		else {
			minAccelGpsWidth = 0.01 * latDelta;
			maxAccelGpsWidth = 0.02 * latDelta;
		}
		
		if(currentWidthProportion == 0.0)
			currentAccelWidth = minAccelGpsWidth;
		else
			currentAccelWidth = minAccelGpsWidth + (currentWidthProportion * (maxAccelGpsWidth - minAccelGpsWidth));
		
/*		System.out.println();
		System.out.println("Accel: " + accel);
		System.out.println("Max Accel: " + maxAccel);
		System.out.println("Min Accel: " + minAccel);
		System.out.println("Lat Delta: " + latDelta);
		System.out.println("Lon Delta: " + lonDelta);
		System.out.println("Min Accel Width: " + minAccelGpsWidth);
		System.out.println("Max Accel Width: " + maxAccelGpsWidth);
		System.out.println("currentAccelWidth: " + currentAccelWidth);
		System.out.println("currentWidth proportion: " + currentWidthProportion);
		System.out.println();*/
		
		return currentAccelWidth;
	}
	
	private Point2D determineGpsLocation(GpsSample toSample, GpsSample fromSample, double distanceToNewPoint) {
		Point2D gpsPoint = new Point2D.Double();
		
		double changeInLatitude = toSample.getLatitude() - fromSample.getLatitude();
		double changeInLongitude = toSample.getLongitude() - fromSample.getLongitude();
		
		double angleBetweenGpsCoord = 0.0;
		double perpendicularAngle = 0.0;
		
		double distanceToGpsPointLon = 0.0;
		double distanceToGpsPointLat = 0.0;
		
		double gpsPointLat = 0.0;
		double gpsPointLon = 0.0;
		
		if(changeInLongitude == 0.0) {
			perpendicularAngle = 0.0;
		}
		else {
			angleBetweenGpsCoord = Math.atan( changeInLatitude / changeInLongitude);
			perpendicularAngle = angleBetweenGpsCoord + (Math.PI/2);
		}
		
		distanceToGpsPointLon = distanceToNewPoint * Math.cos(perpendicularAngle);
		distanceToGpsPointLat = distanceToNewPoint * Math.sin(perpendicularAngle);
		
		gpsPointLat = fromSample.getLatitude() + distanceToGpsPointLat;
		gpsPointLon = fromSample.getLongitude() + distanceToGpsPointLon;
		
		gpsPoint.setLocation(gpsPointLon, gpsPointLat);
		
/*		System.out.println();
		System.out.println("Change in Lat: " + changeInLatitude);
		System.out.println("Change in Long: "+ changeInLongitude);
		System.out.println("Angle Bwtn points: " + angleBetweenGpsCoord );
		System.out.println("Perp Angle: " + perpendicularAngle);
		System.out.println("distance to point: " + distanceToNewPoint);
		System.out.println("gpsPointLon: " + fromSample.getLongitude());
		System.out.println("gpsPointLat: " + fromSample.getLatitude());
		System.out.println("new gpsPointLon: " + gpsPointLon);
		System.out.println("new gpsPointLat: " + gpsPointLat);
		System.out.println();*/
		
		return gpsPoint;
	}


}
