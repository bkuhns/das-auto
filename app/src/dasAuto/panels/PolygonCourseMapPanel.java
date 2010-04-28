package dasAuto.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import dasAuto.graphics.CourseMapPolygon;
import dasAuto.graphics.PolygonList;
import dasAuto.logData.samples.GpsSample;



public class PolygonCourseMapPanel extends DataPanel {
	private static final long serialVersionUID = 3413521493798935318L;
	
	PolygonList coursePolygonList = new PolygonList();
	
	private double maxSpeed; //Speed for determining colors.

	
	public PolygonCourseMapPanel() {
		super();
		buildPolygonList();
		setBackground(Color.WHITE);
		maxSpeed = gpsFeed.getMaxSpeed();
	}
	
	
	public void paintCourseGraphic(Graphics g) {
		int panelWidth = getWidth();
		int panelHeight = getHeight();
		
		//Sets up a buffered image to draw the graphics into
		BufferedImage courseMapImage = (BufferedImage)createImage(panelWidth, panelHeight);
		Graphics2D courseMapGraphic = courseMapImage.createGraphics();
		
		//Colors over old buffered image with white.
		courseMapGraphic.setColor(Color.WHITE);
		courseMapGraphic.fill(getVisibleRect());
		
		courseMapGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		
		renderPolygonList(courseMapGraphic);
		renderGpsLine(courseMapGraphic);
		
		((Graphics2D)g).drawImage(courseMapImage, 0, 0, this);
	}
	
	
	private void renderPolygonList(Graphics2D g2) {
		for(int k=0; k < coursePolygonList.size(); k++) {
			CourseMapPolygon currentPolygon = coursePolygonList.get(k);
			
			//Draw in the Polygon
			Polygon drawingPolygon = currentPolygon.getCourseMapPolygon(getWidth(), getHeight());
			g2.drawPolygon(drawingPolygon);
			
			if(currentTimestamp - currentPolygon.getCurrentGpsSample().getTimestamp() > -100 && 
					currentTimestamp - currentPolygon.getCurrentGpsSample().getTimestamp() < 100) {
				g2.setPaint(new Color(255,0,0));
				g2.fillPolygon(drawingPolygon);
			} else {
				//Color in the Polygon 
				Point oldPoint = currentPolygon.getOldSamplePoint(getWidth(), getHeight());
				Point newPoint = currentPolygon.getNewSamplePoint(getWidth(), getHeight());
				
				g2.setPaint(getPolygonGradient(oldPoint, newPoint, currentPolygon.getOldSpeed(), currentPolygon.getNewSpeed()));
				g2.fillPolygon(drawingPolygon);
			}
		}
	}
	
	
	private void renderGpsLine(Graphics2D g2) {
		g2.setPaint(Color.WHITE);
		for(int k=0; k < coursePolygonList.size(); k++) {
			CourseMapPolygon currentPolygon = coursePolygonList.get(k);
			
			if(!currentPolygon.getCurrentGpsSample().equals(gpsFeed.getNearestSample(currentPolygon.getCurrentGpsSample().getTimestamp()))) {
				if(k == coursePolygonList.size() - 1)
					break;
				
				//Drawing line in the middle of the course.
				Point oldPoint = currentPolygon.getOldSamplePoint(getWidth(), getHeight());
				Point newPoint = currentPolygon.getNewSamplePoint(getWidth(), getHeight());
				
				g2.drawLine(oldPoint.x, oldPoint.y, newPoint.x, newPoint.y);
			}
		}
	}
	
	
	private GradientPaint getPolygonGradient(Point oldPoint, Point newPoint, double oldSpeed, double newSpeed) {
		int oldBluePortion =  (int)Math.round( (1 - oldSpeed / maxSpeed) * 255 );
		int oldRedPortion = (int)Math.round( ((oldSpeed / maxSpeed) ) * 255 );
		int oldOrangePortion = (int)Math.round( ((oldSpeed / maxSpeed) ) * 140 );
		
		Color oldColor = new Color(oldRedPortion, oldOrangePortion, oldBluePortion);
		
		int newBluePortion =  (int)Math.round( (1 - newSpeed / maxSpeed) * 255 );
		int newRedPortion = (int)Math.round( ((newSpeed / maxSpeed) ) * 255 );
		int newOrangePortion = (int)Math.round( ((newSpeed / maxSpeed) ) * 140 );
		
		Color newColor = new Color(newRedPortion, newOrangePortion, newBluePortion);
		
		return new GradientPaint(oldPoint.x, oldPoint.y, oldColor, newPoint.x, newPoint.y, newColor);
	}
	
	
	private void buildPolygonList() {
		GpsSample previousGpsSample = null;
		GpsSample currentGpsSample = null;
		Point2D oldPointHigh = new Point2D.Double();
		Point2D oldPointLow = new Point2D.Double();
		
		// Create local values for min/max.
		double minLat = gpsFeed.getMinLatitude();
		double maxLat = gpsFeed.getMaxLatitude();
		double minLon = gpsFeed.getMinLongitude();
		double maxLon = gpsFeed.getMaxLongitude();
		int maxAccel = accelFeed.getFilteredFeed(15).getMaxYValue();
		int minAccel = accelFeed.getFilteredFeed(15).getMinYValue();
		
		int previousAccel = 0;
		int currentAccel = minAccel;
		boolean firstPolygonCreated = false;
		
		// Iterate through our GPS data to draw polygons.
		for(int i = 0; i < gpsFeed.size(); i++) {
			currentGpsSample = gpsFeed.get(i);
			int currentAccelValue = accelFeed.getFilteredFeed(30).getNearestSampleIndex(currentGpsSample.getTimestamp());
			currentAccel = accelFeed.getFilteredFeed(30).get(currentAccelValue).getYValue();
			
			//For the first GPS sample of currentGpsSample, fill in to the previousGpsSample, and skip to the next loop.
			if(previousGpsSample == null) {
				previousGpsSample = currentGpsSample;
				previousAccel = currentAccel;
			} else {
				CourseMapPolygon racePolygon;
				
				if(!firstPolygonCreated) {
					racePolygon = new CourseMapPolygon(currentGpsSample, previousGpsSample, previousAccel, currentAccel, 
													   previousGpsSample.getSpeed(), currentGpsSample.getSpeed());
					
					racePolygon.setMaxAndMins(minLat, maxLat, minLon, maxLon, minAccel, maxAccel);
					racePolygon.buildFirstPolygon();
					firstPolygonCreated = true;
				} else {
					racePolygon = new CourseMapPolygon(currentGpsSample, previousGpsSample, oldPointHigh, oldPointLow, currentAccel, 
													   previousGpsSample.getSpeed(), currentGpsSample.getSpeed());
					
					racePolygon.setMaxAndMins(minLat, maxLat, minLon, maxLon, minAccel, maxAccel);
					racePolygon.buildPolygon();
				}
				
				coursePolygonList.add(racePolygon);
				oldPointHigh = racePolygon.getNewLocationHigh();
				oldPointLow = racePolygon.getNewLocationLow();
				previousAccel = currentAccel;
				previousGpsSample = currentGpsSample;
			}
		}
	}
	
	
	@Override
	public void update(long timestamp) {
		super.update(timestamp);
		
		paintCourseGraphic(getGraphics());
	}
	
	
	public void update(Graphics g) {
		paint(g);
	}
	
	
	public void paint(Graphics g) {
		paintCourseGraphic((Graphics2D)g);
	}
	
	
}
