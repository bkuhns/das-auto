package dasAuto.panels;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import dasAuto.graphics.CourseMapPolygon;
import dasAuto.logData.samples.GpsSample;



public class PolygonCourseMapPanel extends DataPanel {

	private static final long serialVersionUID = 3413521493798935318L;

	BufferedImage courseMapImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	Graphics2D courseMapGraphic;
	Rectangle area;

	
	public PolygonCourseMapPanel() {
		super();
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
		
		GpsSample previousGpsSample = null;
		GpsSample currentGpsSample = null;
		Point oldPointHigh = new Point();
		Point oldPointLow = new Point();
		
		boolean firstPolygonDrawn = false;

		//Determines the width and height of the panel in pixels
		int panelWidth = getSize().width;
		int panelHeight = getSize().height;
		
		//Sets up a buffered image to draw the graphics into
		courseMapImage = (BufferedImage)createImage(panelWidth, panelHeight);
		courseMapGraphic = courseMapImage.createGraphics();
		
		//Colors over old buffered image with white.
		courseMapGraphic.setColor(Color.WHITE);
		courseMapGraphic.fill(getVisibleRect());
		
		//create local values for maximums
		double minLat = gpsFeed.getMinLatitude();
		double maxLat = gpsFeed.getMaxLatitude();
		double minLon = gpsFeed.getMinLongitude();
		double maxLon = gpsFeed.getMaxLongitude();
		int maxAccel = accelFeed.getMaxYValue();
		int minAccel = accelFeed.getMinYValue();
		int previousAccel = 0;
		int currentAccel = 0;
		
		//iterate through our GPS data to draw polygons.
		//Need to include some method for finding the acceleration data previous to the GPS point.
		for(int i = 0; i < gpsFeed.size(); i++) {
			currentGpsSample = gpsFeed.get(i);
			
			//TODO: Create method for finding closest acceleration value
			currentAccel = 324;
			//Need to find the acceleration immediately after the current gpsFeed value.
			//For now, use a fixed length for the side width
			
			//For the first GPS sample of currentGpsSample, fill in to the previousGpsSample, and skip to the next loop
			
			if(previousGpsSample.equals(null)) {
				previousGpsSample = currentGpsSample;
				previousAccel = currentAccel;
			} else {
				
				CourseMapPolygon racePolygon;
				//TODO: Find the distance from the previous GPS point to the new GPS point,
				//		or instantiate & pass values to CourseMapPolygon
				
				/*  If we're in this else, we're at least on the second GPS feed, with a current & previous GpsSample.
				 *  However, no points have been created. The very first time we go through, need to make a specialized
				 *  'first polygon' which creates 4 points for a polygon. Thereafter, only need to generate 2 new points
				 *  for a polygon.
				 */
				if(!firstPolygonDrawn) {
					racePolygon = new CourseMapPolygon(currentGpsSample, previousGpsSample, previousAccel, currentAccel);
					racePolygon.setMaxAndMins(minLat, maxLat, minLon, maxLon, maxAccel, minAccel);
					
					
					
					firstPolygonDrawn = true;
				} else {
					racePolygon = new CourseMapPolygon(currentGpsSample, oldPointHigh, oldPointLow, currentAccel);
					racePolygon.setMaxAndMins(minLat, maxLat, minLon, maxLon, maxAccel, minAccel);
					
					
				}
				
				oldPointHigh = racePolygon.getNewPolyPointHigh();
				oldPointLow = racePolygon.getNewPolyPointLow();
				previousAccel = currentAccel;
			}
			
			
			
			
			
			
			
			//Old method of determining an Oval to draw a point at, to be changed to polygon objects.
			
			//double currentLat = gpsFeed.get(i).getLatitude();
			//double currentLon = gpsFeed.get(i).getLongitude();
			
			//TODO: Instead of 10% fixed padding, adjust padding to maintain a square set of GPS coordinates.
			/*int currentX = (int)Math.round((((maxLon - currentLon) * (double) (panelWidth * 0.9)) / (maxLon - minLon)) + panelWidth * 0.05);
			int currentY = (int)Math.round((((maxLat - currentLat) * (double) (panelHeight * 0.9)) / (maxLat - minLat)) + panelHeight * 0.05);

			courseMapG.setStroke(new BasicStroke(2.0f));
			courseMapG.drawOval(currentX, currentY, 1, 1);

			if (previousPoint == null) {
				previousPoint = new Point();
			} else {
				courseMapG.setStroke(new BasicStroke(1.0f));
				courseMapG.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				courseMapG.drawLine(previousPoint.x, previousPoint.y, currentX, currentY);
			}
			previousPoint.setLocation(currentX, currentY);*/
		}
		g.drawImage(courseMapImage, 0, 0, this);
	}

	
}
