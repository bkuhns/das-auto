import java.awt.Point;
import java.awt.Polygon;
import java.lang.Double;

public class DasTrapezoid extends Polygon {
	
	private Point oldGPSPoint;
	private Point newGPSPoint;
	private Point oldTrapePointHigh;
	private Point oldTrapePointLow;
	private Point newTrapePointHigh;
	private Point newTrapePointLow;
	private Point oldPointYint;
	private Point newPointYint;
	private double oldSlope;
	private double newSlope;
	private int minAccel = 0;
	private int maxAccel = 1024;
	private int minPolyWidth = 2;
	private int maxPolyWidth = 100;
	

	//Normal constructor 
	public DasTrapezoid(Point currentGPSPoint,	Point pastTrapePointHigh, 
						Point pastTrapePointLow, int currentAccel)
	{
		//TODO: Build out generic constructor
	}
	
	
	// Specialized constructor for very first Trapezoid, requires more calculation
	// Need to calculate: oldSlope, newSlope, oldPoint1, oldPoint2, newPoint1, newPoint2
	public DasTrapezoid(Point pastGPSPoint,  Point currentGPSPoint, 
						int previousAccel,  int currentAccel)
	{
		//TODO: Build out initial trapezoid constructor
	}
	
	
	public Point getTrapPointHigh() {
		return this.newTrapePointHigh;
	}
	
	
	public Point getTrapPointLow() {
		return this.newTrapePointLow;
	}
	
	
	public void setMaxAccel(int dataMaxAccel) {
		this.maxAccel = dataMaxAccel;
		
	}


	public void setMinAccel(int dataMinAccel) {
		this.minAccel = dataMinAccel;
		
	}
	
	
	public void setMinPolyWidth(int dataMinPolyWidth) {
		this.minPolyWidth = dataMinPolyWidth;
	}


	public void setMaxPolyWidth(int dataMaxPolyWidth) {
		this.maxPolyWidth = dataMaxPolyWidth;
	}
	
	
	public int getCurrentWidth(int accel ) {
		 int curWidth = 0;
		 double currentWidthProportion = 
				( (double)(accel - minAccel) / (double)(maxAccel - minAccel) );
		 
		 if(currentWidthProportion == 0.0)
			 curWidth = minPolyWidth;
		 else if (currentWidthProportion == 1.0)
			 curWidth = maxPolyWidth;
		 else
		 {
			 curWidth = minPolyWidth + 
			 			    (int)Math.round(currentWidthProportion * (maxPolyWidth - minPolyWidth));
		 }
		 
		 System.out.println("Current width Proportion: " + currentWidthProportion);
		 System.out.println("Current width: " + curWidth);
		 
		 return curWidth;
	 }
	
	private Double calculateSlope(Point gpsPoint1, Point gpsPoint2) {
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
