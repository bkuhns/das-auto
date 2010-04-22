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
	private double newGPSSlope;
	private int minAccel = 0;
	private int maxAccel = 1024;
	private int oldAccel = 0;
	private int newAccel = 0;
	private double minPolyWidth = 2.0;
	private double maxPolyWidth = 100.0;
	private double currentPolyWidth = 2.0;
	

	//Normal constructor 
	public DasTrapezoid(Point currentGPSPoint,	Point pastTrapePointHigh, 
						Point pastTrapePointLow, int currentAccel)
	{
		//TODO: Build out generic constructor
		newGPSPoint = currentGPSPoint;
		oldTrapePointHigh = pastTrapePointHigh;
		oldTrapePointHigh = pastTrapePointLow;
		newAccel = currentAccel;
	}
	
	
	// Specialized constructor for very first Trapezoid, requires more calculation
	// Need to calculate: oldSlope, newSlope, oldPoint1, oldPoint2, newPoint1, newPoint2
	public DasTrapezoid(Point pastGPSPoint,  Point currentGPSPoint, 
						int previousAccel,  int currentAccel)
	{
		//TODO: Build out initial trapezoid constructor
		oldGPSPoint = pastGPSPoint;
		newGPSPoint = currentGPSPoint;
		oldAccel = previousAccel;
		newAccel = currentAccel;
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
	
	
	public double getCurrentWidth(int accel ) {
		 double curWidth = 0.0;
		 double currentWidthProportion = 
				( (double)(accel - minAccel) / (double)(maxAccel - minAccel) );
		 
		 if(currentWidthProportion == 0.0)
			 curWidth = minPolyWidth;
		 else if (currentWidthProportion == 1.0)
			 curWidth = maxPolyWidth;
		 else
		 {
			 curWidth = minPolyWidth + (currentWidthProportion * (maxPolyWidth - minPolyWidth));
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
	
	
	private Double calculatePerpSlope(Double slope)
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
	
	
	private Point calculateTrapePoint(Point gpsCoord, Point yInt, double lengthToPoint)
	{
		Point trapePoint = new Point();
		double distanceYIntToGpsCoord = Math.sqrt( Math.pow((gpsCoord.x - yInt.x), 2.0) + Math.pow((gpsCoord.y - yInt.y),2) );
		double distanceProportion = lengthToPoint / distanceYIntToGpsCoord;
		
		trapePoint.x = gpsCoord.x - (int)((yInt.x - gpsCoord.x)* distanceProportion);
		trapePoint.y = gpsCoord.y - (int)((yInt.y - gpsCoord.y)* distanceProportion);
		
		return trapePoint;
	}
	
	
	//Method for calculating the initial Trapezoid, need to find all 4 points
	public void assembleInitialTrape() {
		double oldWidth = getCurrentWidth(oldAccel);
		double newWidth = getCurrentWidth(newAccel);
		
		double gpsSlope = calculateSlope(oldGPSPoint, newGPSPoint);
		
		double perpGPSSlope = calculatePerpSlope(gpsSlope);
		
		Point oldYIntercept = findYInterceptPoint(oldGPSPoint, perpGPSSlope);
		Point newYIntercept = findYInterceptPoint(newGPSPoint, perpGPSSlope);
		
		oldTrapePointHigh = calculateTrapePoint(oldGPSPoint, oldYIntercept, 1.0 * (oldWidth / 2.0));
		oldTrapePointLow = calculateTrapePoint(oldGPSPoint, oldYIntercept, -1.0 * (oldWidth / 2.0));
		newTrapePointHigh = calculateTrapePoint(newGPSPoint, newYIntercept, 1.0 * (oldWidth / 2.0));
		newTrapePointLow = calculateTrapePoint(newGPSPoint, newYIntercept, -1.0 * (oldWidth / 2.0));
		
		//this.addPoint(oldTrapePointHigh);
		//this.addPoint(oldTrapePointLow);
		//this.addPoint(newTrapePointHigh);
		//this.addPoint(newTrapePointLow);
		
		this.addPoint(oldTrapePointHigh.x, oldTrapePointHigh.y);
		this.addPoint(oldTrapePointLow.x, oldTrapePointLow.y);
		this.addPoint(newTrapePointHigh.x, newTrapePointHigh.y);
		this.addPoint(newTrapePointLow.x, newTrapePointLow.y);
		
		System.out.println("old Trape High(x,y): " + oldTrapePointHigh.x + ", " + oldTrapePointHigh.y );
		System.out.println("old Trape Low(x,y): " + oldTrapePointLow.x + ", " + oldTrapePointLow.y );
		System.out.println("new Trape High(x,y): " + newTrapePointHigh.x + ", " + newTrapePointHigh.y );
		System.out.println("new Trape High(x,y): " + newTrapePointLow.x + ", " + newTrapePointLow.y );
	}
	
	
	//Method for calculating all other Trapezoids, only need to find 2 points
	public void assembleTrape() {
		
	}
	
	public void addPoint(Point p)
	{
		addPoint(p.x, p.y);
	}
	
	
}
