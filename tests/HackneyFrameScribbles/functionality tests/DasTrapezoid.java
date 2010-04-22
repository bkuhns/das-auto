import java.awt.Point;
import java.awt.Polygon;
import java.lang.Double;

public class DasTrapezoid extends Polygon {
	
	private Point	oldPoint1, oldPoint2, newPoint1, newPoint2,
		 	oldPointYint, newPointYint;
	private double  oldSlope, newSlope;
	

	public DasTrapezoid(Point oldGPSPoint,   Point newGPSPoint, 
						Point oldPointTrap1, Point oldPointTrap2,
						int currentWidth, 	 int previousWidth,
						int previousAccel, 	 int currentAccel, 
						double oldTrapSlope)
	{
		
	}
	
	
	// Specialized constructor for very first Trapezoid, requires more calculation
	// Need to calculate: oldSlope, newSlope, oldPoint1, oldPoint2, newPoint1, newPoint2
	public DasTrapezoid(Point oldGPSPoint,  Point newGPSPoint, 
						int currentWidth, 	int previousWidth,
						int previousAccel,	int currentAccel)
	{
		
	}
	
	
	//Get the slope of line between gps points
	public double getTrapeSlope()
	{
		return newSlope;
	}
	
	
	private Double calculateSlope(Point gpsPoint1, Point gpsPoint2)
	{
		double slope = 0.0;
		
		//Need to include special conditions here for slope cases
		//Special cases: Infinite slope (no change in x)
		
		//Thought: set slope to null, acting as a undefined?
		if(gpsPoint2.x == gpsPoint1.x && gpsPoint2.y > gpsPoint1.y) {
			return Double.POSITIVE_INFINITY;
		}
		else if(gpsPoint2.x == gpsPoint1.x && gpsPoint2.y < gpsPoint1.y) {
			return Double.NEGATIVE_INFINITY;
		}
		else {
			slope = (gpsPoint2.y - gpsPoint1.y) / (gpsPoint2.x - gpsPoint1.x);
		}
		return slope;
	}
	
	
	private Double calculatePerpSlopeLine(Double slope)
	{
		double perpSlope = 0.0;
		
		//Special Conditions: pos/neg inf., zero's.
		if(slope.equals(Double.NEGATIVE_INFINITY) || slope.equals(Double.POSITIVE_INFINITY)) {
			perpSlope = 0.0;
		}
		else if (slope.equals(0.0)) {
			perpSlope = Double.POSITIVE_INFINITY;
		}
		//Common Formula for perpendicular slope.
		else {
			perpSlope = -1.0 * (1.0 / slope);
		}
		return perpSlope;
	}
	
	
	private Point findYInterceptPoint(Point gpsCoord, Double trapeSlope)
	{
		Point yIntercept = new Point();
		double yValue = 0.0;
		
		//Common formula for finding y-intercept point
		yValue = gpsCoord.y / (trapeSlope * gpsCoord.x);
		yIntercept.setLocation(0.0, yValue);

		return yIntercept;
	}
	
	
	private Point calculateTrapePoint()
	{
		Point trapePoint = new Point();
		
		return trapePoint;
	}
}
