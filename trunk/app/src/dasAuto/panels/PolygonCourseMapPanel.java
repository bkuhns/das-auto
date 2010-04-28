package dasAuto.panels;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
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

		//Determines the width and height of the panel in pixels
		int panelWidth = getSize().width;
		int panelHeight = getSize().height;
		
		//Speeds for determining colors
		double maxSpeed = gpsFeed.getMaxSpeed();
		
		//Sets up a buffered image to draw the graphics into
		courseMapImage = (BufferedImage)createImage(panelWidth, panelHeight);
		courseMapGraphic = courseMapImage.createGraphics();
		
		//Colors over old buffered image with white.
		courseMapGraphic.setColor(Color.WHITE);
		courseMapGraphic.fill(getVisibleRect());
		
		courseMapGraphic.setColor(Color.WHITE);
		courseMapGraphic.setStroke(new BasicStroke(1.0f));
		courseMapGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		for(int k=0; k < coursePolygonList.size(); k++) {
			CourseMapPolygon currentPolygon = coursePolygonList.get(k);
			
			//Draw in the Polygon
			Polygon drawingPolygon = currentPolygon.getCourseMapPolygon(panelWidth, panelHeight);
			courseMapGraphic.drawPolygon(drawingPolygon);
			
			if(currentPolygon.getCurrentGpsSample().equals(gpsFeed.getNearestSample(currentPolygon.getCurrentGpsSample().getTimestamp()))) {
				courseMapGraphic.setPaint(new Color(255,0,0));
				courseMapGraphic.fillPolygon(drawingPolygon);
			}
			
			else {
				//Color in the Polygon 
				int oldBluePortion =  (int)Math.round( (1 - currentPolygon.getOldSpeed() / maxSpeed) * 255 );
				int oldRedPortion = (int)Math.round( ((currentPolygon.getOldSpeed() / maxSpeed) ) * 255 );
				int oldOrangePortion = (int)Math.round( ((currentPolygon.getOldSpeed() / maxSpeed) ) * 140 );
				
				Color oldColor = new Color(oldRedPortion, oldOrangePortion, oldBluePortion);
				
				int newBluePortion =  (int)Math.round( (1 - currentPolygon.getNewSpeed() / maxSpeed) * 255 );
				int newRedPortion = (int)Math.round( ((currentPolygon.getNewSpeed() / maxSpeed) ) * 255 );
				int newOrangePortion = (int)Math.round( ((currentPolygon.getOldSpeed() / maxSpeed) ) * 140 );
				
				Color newColor = new Color(newRedPortion, newOrangePortion, newBluePortion);
				
				Point oldPoint = currentPolygon.getOldSamplePoint(panelWidth, panelHeight);
				Point newPoint = currentPolygon.getNewSamplePoint(panelWidth, panelHeight);
				
				GradientPaint gradient = new GradientPaint(oldPoint.x, oldPoint.y, oldColor, newPoint.x, newPoint.y, newColor);
				
				courseMapGraphic.setPaint(gradient);
				courseMapGraphic.fillPolygon(drawingPolygon);
				
				
				courseMapGraphic.setPaint(gradient);
			}
			
		}
		
		courseMapGraphic.setPaint(Color.WHITE);
		
		for(int k=0; k < coursePolygonList.size(); k++) {
			
			CourseMapPolygon currentPolygon = coursePolygonList.get(k);
			
			if(currentPolygon.getCurrentGpsSample().equals(gpsFeed.getNearestSample(currentPolygon.getCurrentGpsSample().getTimestamp()))) {
				//Don't draw the line
			}
			else {
				if(k == coursePolygonList.size() - 1)
					break;
				
				//Drawing line in the middle of the course.
				Point oldPoint = currentPolygon.getOldSamplePoint(panelWidth, panelHeight);
				Point newPoint = currentPolygon.getNewSamplePoint(panelWidth, panelHeight);
				
				courseMapGraphic.drawLine(oldPoint.x, oldPoint.y, newPoint.x, newPoint.y);
			}
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
		int maxAccel = accelFeed.getFilteredFeed(15).getMaxYValue();
		int minAccel = accelFeed.getFilteredFeed(15).getMinYValue();
		
		int previousAccel = 0;
		int currentAccel = minAccel;
		boolean firstPolygonCreated = false;
		
		//iterate through our GPS data to draw polygons.
		//Need to include some method for finding the acceleration data previous to the GPS point.
		for(int i = 0; i < gpsFeed.size(); i++) {
			currentGpsSample = gpsFeed.get(i);
			//TODO: Create method for finding closest acceleration value
			int currentAccelValue = accelFeed.getFilteredFeed(15).getNearestSampleIndex(currentGpsSample.getTimestamp());
			currentAccel = accelFeed.getFilteredFeed(15).get(currentAccelValue).getYValue();
			
			//Need to find the acceleration immediately after the current gpsFeed value.
			//For now, use a fixed length for the side width
			
			//For the first GPS sample of currentGpsSample, fill in to the previousGpsSample, and skip to the next loop
			
			if(previousGpsSample == null) {
				previousGpsSample = currentGpsSample;
				previousAccel = currentAccel;
			} else {
				
				CourseMapPolygon racePolygon;
				
				if(!firstPolygonCreated) {
					racePolygon = new CourseMapPolygon(currentGpsSample, previousGpsSample, previousAccel, currentAccel, 
													   previousGpsSample.getSpeed(), currentGpsSample.getSpeed());
					
					racePolygon.setMaxAndMins(minLat, maxLat, minLon, maxLon, minAccel, maxAccel);
					racePolygon.instantiateFirstPolygon();
					firstPolygonCreated = true;
				} else {
					racePolygon = new CourseMapPolygon(currentGpsSample, previousGpsSample, oldPointHigh, oldPointLow, currentAccel, 
													   previousGpsSample.getSpeed(), currentGpsSample.getSpeed());
					
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
