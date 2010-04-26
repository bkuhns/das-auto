package dasAuto.panels;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import dasAuto.graphics.CourseMapPolygon;
import dasAuto.graphics.PolygonList;
import dasAuto.logData.samples.GpsSample;



public class PolygonCourseMapPanel extends DataPanel {

	private static final long serialVersionUID = 3413521493798935318L;

	BufferedImage courseMapImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	PolygonList coursePolygonList = new PolygonList();
	Graphics2D courseMapGraphic;
	Rectangle area;

	
	public PolygonCourseMapPanel() {
		super();
		instantiatePolygonList();
		setBackground(Color.WHITE);
	}


	public void update(Graphics g) {
		paint(g);
	}
	
	
	public void paint(Graphics g) {
		paintCourseGraphic((Graphics2D)g);
	}
	
	
	public void paintCourseGraphic(Graphics2D g) {
		super.paint(g);
		
		boolean firstPolygonDrawn = false;

		//Determines the width and height of the panel in pixels
		int panelWidth = getSize().width;
		int panelHeight = getSize().height;
		
		double minLat = gpsFeed.getMinLatitude();
		double maxLat = gpsFeed.getMaxLatitude();
		double minLon = gpsFeed.getMinLongitude();
		double maxLon = gpsFeed.getMaxLongitude();
		
		//Sets up a buffered image to draw the graphics into
		courseMapImage = (BufferedImage)createImage(panelWidth, panelHeight);
		courseMapGraphic = courseMapImage.createGraphics();
		
		//Colors over old buffered image with white.
		courseMapGraphic.setColor(Color.WHITE);
		courseMapGraphic.fill(getVisibleRect());
		
		courseMapGraphic.setColor(Color.BLACK);
		courseMapGraphic.setStroke(new BasicStroke(1.0f));
		courseMapGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		for(int k=0; k < coursePolygonList.size(); k++) {
			
			CourseMapPolygon currentPolygon = coursePolygonList.get(k);
			
			double point1Lat = currentPolygon.getNewLocationHigh().getY();
			double point1Lon = currentPolygon.getNewLocationHigh().getX();
			double point2Lat = currentPolygon.getNewLocationLow().getY();
			double point2Lon = currentPolygon.getNewLocationLow().getX();
			double point3Lat = currentPolygon.getOldLocationHigh().getY();
			double point3Lon = currentPolygon.getOldLocationHigh().getX();
			double point4Lat = currentPolygon.getOldLocationLow().getY();
			double point4Lon = currentPolygon.getOldLocationLow().getX();
			
			
			int point1Y = (int)Math.round(  (((maxLat - point1Lat) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
			int point1X = (int)Math.round( (((maxLon - point1Lon) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
			int point2Y = (int)Math.round(  (((maxLat - point2Lat) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
			int point2X = (int)Math.round( (((maxLon - point2Lon) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
			int point3Y = (int)Math.round(  (((maxLat - point3Lat) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
			int point3X = (int)Math.round( (((maxLon - point3Lon) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
			int point4Y = (int)Math.round(  (((maxLat - point4Lat) *(double)(panelHeight) * 0.9) / (maxLat - minLat)) + (double)(panelHeight) * 0.05  );
			int point4X = (int)Math.round( (((maxLon - point4Lon) *(double)(panelWidth) * 0.9) / (maxLon - minLon)) + (double)(panelWidth) * 0.05  );
			
          /*System.out.println();
			System.out.println("Point1(x,y): (" + point1X + "," + point1Y + ")");
			System.out.println("Point2(x,y): (" + point2X + "," + point2Y + ")");
			System.out.println("Point3(x,y): (" + point3X + "," + point3Y + ")");
			System.out.println("Point4(x,y): (" + point4X + "," + point4Y + ")");
			System.out.println();
			*/
			
			Polygon testPolygon = new Polygon();
			
			testPolygon.addPoint(point4X, point4Y);
			testPolygon.addPoint(point1X, point1Y);
			testPolygon.addPoint(point2X, point2Y);
			testPolygon.addPoint(point3X, point3Y);
			
			courseMapGraphic.drawPolygon(testPolygon);
			//courseMapGraphic.drawOval( point1X, point1Y, 1, 1);
			//courseMapGraphic.drawOval( point2X, point2Y, 1, 1);
			//courseMapGraphic.drawOval( point3X, point3Y, 1, 1);
			//courseMapGraphic.drawOval( point4X, point4Y, 1, 1);
           
			
		}
		
		g.drawImage(courseMapImage, 0, 0, this);
	}

	private void instantiatePolygonList() {
		
		GpsSample previousGpsSample = null;
		GpsSample currentGpsSample = null;
		Point2D oldPointHigh = new Point2D.Double();
		Point2D oldPointLow = new Point2D.Double();
		
		//create local values for maximums
		double minLat = gpsFeed.getMinLatitude();
		double maxLat = gpsFeed.getMaxLatitude();
		double minLon = gpsFeed.getMinLongitude();
		double maxLon = gpsFeed.getMaxLongitude();
		
		int maxAccel = accelFeed.getMaxYValue();
		int minAccel = accelFeed.getMinYValue();
		
		int previousAccel = 0;
		int currentAccel = 250;
		boolean firstPolygon = false;
		
		//iterate through our GPS data to draw polygons.
		//Need to include some method for finding the acceleration data previous to the GPS point.
		for(int i = 0; i < gpsFeed.size(); i++) {
			currentGpsSample = gpsFeed.get(i);
			
			//TODO: Create method for finding closest acceleration value
			
			//Need to find the acceleration immediately after the current gpsFeed value.
			//For now, use a fixed length for the side width
			
			//For the first GPS sample of currentGpsSample, fill in to the previousGpsSample, and skip to the next loop
			
			if(previousGpsSample == null) {
				previousGpsSample = currentGpsSample;
				previousAccel = currentAccel;
			} else {
				
				CourseMapPolygon racePolygon;
				
				if(!firstPolygon) {
					racePolygon = new CourseMapPolygon(currentGpsSample, previousGpsSample, previousAccel, currentAccel);
					racePolygon.setMaxAndMins(minLat, maxLat, minLon, maxLon, minAccel, maxAccel);
					racePolygon.instantiateFirstPolygon();
					firstPolygon = true;
				} else {
					racePolygon = new CourseMapPolygon(currentGpsSample, previousGpsSample, oldPointHigh, oldPointLow, currentAccel);
					racePolygon.setMaxAndMins(minLat, maxLat, minLon, maxLon, minAccel, maxAccel);
					racePolygon.instantiatePolygon();
				}
				
				coursePolygonList.add(racePolygon);
				oldPointHigh = racePolygon.getNewLocationHigh();
				oldPointLow = racePolygon.getNewLocationLow();
				previousAccel = currentAccel;
				previousGpsSample = currentGpsSample;
			}
		}
	}
	
	
}
