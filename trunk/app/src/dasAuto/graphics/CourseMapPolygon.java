package dasAuto.graphics;

import java.awt.Polygon;
import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

import dasAuto.logData.samples.GpsSample;

public class CourseMapPolygon extends Polygon {

	private static final long serialVersionUID = -3787779528072837425L;
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
	
	private int minAccel;
	private int maxAccel;
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


	public void buildPolygon() {
		/*if(newAccel >= 512) {
			newLocationHigh = findPoint2D(newSample, oldSample, newAccel, 1);
			newLocationLow = findPoint2D(newSample, oldSample, minAccel, -1);
		} else {
			newLocationHigh = findPoint2D(newSample, oldSample, minAccel, 1);
			newLocationLow = findPoint2D(newSample, oldSample, newAccel, -1);
		}*/
		
		newLocationHigh = findPoint2D(newSample, oldSample, newAccel, 1);
		newLocationLow = findPoint2D(newSample, oldSample, newAccel, -1);
	}
	
	
	public void buildFirstPolygon() {
		/*if(oldAccel >= 512) {
			oldLocationHigh = findPoint2D(oldSample, newSample, oldAccel, 1);
			oldLocationLow = findPoint2D(oldSample, newSample, minAccel, -1);
		} else {
			oldLocationHigh = findPoint2D(oldSample, newSample, minAccel, 1);
			oldLocationLow = findPoint2D(oldSample, newSample, oldAccel, -1);
		}
		
		if(newAccel >= 512) {
			newLocationHigh = findPoint2D(newSample, oldSample, newAccel, 1);
			newLocationLow = findPoint2D(newSample, oldSample, minAccel, -1);
		} else {
			newLocationHigh = findPoint2D(newSample, oldSample, minAccel, 1);
			newLocationLow = findPoint2D(newSample, oldSample, newAccel, -1);
		}*/
		
		oldLocationHigh = findPoint2D(oldSample, newSample, minAccel, 1);
		oldLocationLow = findPoint2D(oldSample, newSample, minAccel, -1);
		
		newLocationHigh = findPoint2D(newSample, oldSample, newAccel, 1);
		newLocationLow = findPoint2D(newSample, oldSample, newAccel, -1);
	}
	
	
	public Polygon getCourseMapPolygon(int panelWidth, int panelHeight) {
		Polygon roundedPolygon = new Polygon();
		
		Point point1 = convertGpsPointToScreenPoint(newLocationHigh.getX(), newLocationHigh.getY(), panelWidth, panelHeight);
		Point point2 = convertGpsPointToScreenPoint(newLocationLow.getX(), newLocationLow.getY(), panelWidth, panelHeight);
		Point point3 = convertGpsPointToScreenPoint(oldLocationHigh.getX(), oldLocationHigh.getY(), panelWidth, panelHeight);
		Point point4 = convertGpsPointToScreenPoint(oldLocationLow.getX(), oldLocationLow.getY(), panelWidth, panelHeight);
		
		//Need special conditions here before adding points, make sure to add them in the correct order.
		//Check within the points themselves
		if(Line2D.linesIntersect(oldLocationLow.getX(), oldLocationLow.getY(), 
					             newLocationHigh.getX(), newLocationHigh.getY(), 
					             newLocationLow.getX(), newLocationLow.getY(), 
					             oldLocationHigh.getX(), oldLocationHigh.getY())) {
			roundedPolygon.addPoint(point4.x, point4.y);
			roundedPolygon.addPoint(point2.x, point2.y);
			roundedPolygon.addPoint(point1.x, point1.y);
			roundedPolygon.addPoint(point3.x, point3.y);
		} else {
			roundedPolygon.addPoint(point4.x, point4.y);
			roundedPolygon.addPoint(point1.x, point1.y);
			roundedPolygon.addPoint(point2.x, point2.y);
			roundedPolygon.addPoint(point3.x, point3.y);
		}
		
		return roundedPolygon;
	}
	
	
	public Point getOldSamplePoint(int panelWidth, int panelHeight) {
		return convertGpsPointToScreenPoint(oldSample.getLongitude(), oldSample.getLatitude(), panelWidth, panelHeight);
	}
	
 	
	public Point getNewSamplePoint(int panelWidth, int panelHeight) {
		return convertGpsPointToScreenPoint(newSample.getLongitude(), newSample.getLatitude(), panelWidth, panelHeight);
	}
	
	
	private Point convertGpsPointToScreenPoint(double gpsLon, double gpsLat, int panelWidth, int panelHeight) {
		return new Point(
				(int)Math.round((((maxLon - gpsLon) * (double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05 ),
				(int)Math.round((((maxLat - gpsLat) * (double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05)
			);
	}
	
	
	public void setMaxAndMins(double minimumLat, double maximumLat, double minimumLon, double maximumLon, int minimumAccel, int maximumAccel) {
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
	
	
	private Point2D findPoint2D(GpsSample toSample, GpsSample fromSample, int accelValue, int directionMultipler) {
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
			maxAccelGpsWidth = 0.04 * lonDelta;
		} else {
			minAccelGpsWidth = 0.01 * latDelta;
			maxAccelGpsWidth = 0.04 * latDelta;
		}
		
		if(currentWidthProportion == 0.0) {
			currentAccelWidth = minAccelGpsWidth;
		} else {
			currentAccelWidth = minAccelGpsWidth + (currentWidthProportion * (maxAccelGpsWidth - minAccelGpsWidth));
		}
		
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
		} else {
			angleBetweenGpsCoord = Math.atan( changeInLatitude / changeInLongitude);
			perpendicularAngle = angleBetweenGpsCoord + (Math.PI/2);
		}
		
		distanceToGpsPointLon = distanceToNewPoint * Math.cos(perpendicularAngle);
		distanceToGpsPointLat = distanceToNewPoint * Math.sin(perpendicularAngle);
		
		gpsPointLat = fromSample.getLatitude() + distanceToGpsPointLat;
		gpsPointLon = fromSample.getLongitude() + distanceToGpsPointLon;
		
		gpsPoint.setLocation(gpsPointLon, gpsPointLat);
		
		return gpsPoint;
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

	
	public void setMinAccel(int minAccel) {
		this.minAccel = minAccel;
	}

	
	public void setMaxAccel(int maxAccel) {
		this.maxAccel = maxAccel;
	}
	
	
	public double getOldSpeed() {
		return oldSpeed;
	}


	public double getNewSpeed() {
		return newSpeed;
	}
	
	
	public GpsSample getCurrentGpsSample() {
		return newSample;
	}


}