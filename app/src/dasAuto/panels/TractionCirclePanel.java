package dasAuto.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.BorderFactory;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.logData.samples.AccelSample;
import dasAuto.logData.samples.GpsSample;


public class TractionCirclePanel extends DataPanel {
	private static final long serialVersionUID = -600064128610900053L;

	private final double MAX_CIRCLE_VAL_X;
	private final double MAX_CIRCLE_VAL_Y; 
	
	private int panelWidth;
	private int panelHeight;
	
	
	public TractionCirclePanel() {
		super();
		
		MAX_CIRCLE_VAL_X = accelFeed.getMaxXValueInG() + 0.25;
		MAX_CIRCLE_VAL_Y = accelFeed.getMaxYValueInG() + 0.25;
		
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		panelWidth = getWidth();
		panelHeight = getHeight();
		
		BufferedImage tracCircleImage = (BufferedImage)createImage(panelWidth, panelHeight);

		Graphics2D tracCircleG = tracCircleImage.createGraphics();
		
		tracCircleG.setColor(Color.WHITE);
		tracCircleG.fill(getVisibleRect());
		
		drawAxis(tracCircleG);
		drawMaxCircle(tracCircleG);
		drawAccelPoints(tracCircleG);
		
		g2d.drawImage(tracCircleImage, 0, 0, this);
	}
	
	
	private void drawAxis(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		
		g.drawLine(0, panelHeight / 2 - 1, panelWidth, panelHeight / 2 -1); // Draw x-axis.
		g.drawLine(panelWidth / 2 - 1, 0, panelWidth / 2 - 1, panelHeight); // Draw y-axis.
	}
	
	
	private void drawMaxCircle(Graphics2D g)
	{
		Point origin = this.getOrigin();
		final AccelFeed accelFeed = this.accelFeed.getFilteredFeed();
		
		//Determine largest outer points, reflect
		double YMin = accelFeed.getMinYValueInG();
		double YMax = accelFeed.getMaxYValueInG();
		double XMin = accelFeed.getMinXValueInG();
		double XMax = accelFeed.getMaxXValueInG();
		double mirroredYMax = -YMax;
		double mirroredXMax = -XMax;
		
		if( Math.abs(YMin) > YMax) 
			mirroredYMax = -YMin;
		if( Math.abs(XMin) > XMax)
			mirroredXMax = -XMin;
		
		
		//-- Draw Theoretical Traction circle
		g.setColor(Color.GRAY);
		g.draw(new Ellipse2D.Double( origin.x - scaleX(Math.abs(mirroredYMax)), origin.y - scaleY(Math.abs(mirroredXMax)), scaleX(2.0*Math.abs(mirroredYMax)), scaleY(2.0 * Math.abs(mirroredXMax)) ) );

		//-- Draw Points
		g.setColor(Color.BLACK);
		this.drawPoint(origin.x + scaleX(accelFeed.getMinYValueInG()), origin.y, g); // Left
		this.drawPoint(origin.x + scaleX(accelFeed.getMaxYValueInG()), origin.y, g); // Right
		this.drawPoint(origin.x, origin.y + scaleY(accelFeed.getMaxXValueInG()), g); // Top 		
		this.drawPoint(origin.x, origin.y + scaleY(accelFeed.getMinXValueInG()), g); // Bottom
		
		//-- Draw Theoretical Max Points
		g.setColor(Color.GRAY);
		this.drawPoint(origin.x + scaleX(mirroredYMax), origin.y, g);
		this.drawPoint(origin.x, origin.y + scaleY(mirroredXMax), g);
		
		
		//-- Then Arcs
		g.setColor(Color.BLACK);
		/g.drawArc(origin.x - scaleX(accelFeed.getMaxYValueInG()),  origin.y - scaleY(accelFeed.getMaxXValueInG()) , scaleX( 2.0 * accelFeed.getMaxYValueInG()), scaleY( 2.0 *accelFeed.getMaxXValueInG()), 270, 90);
	}
	
	private void drawAccelPoints(Graphics2D g)
	{
		Point origin = this.getOrigin();
		AccelSample currentSample = null;
		
		g.setPaint(Color.RED);
		
		for(int k=0;k<gpsFeed.size();k++)
		{	
			if(currentTimestamp - gpsFeed.get(k).getTimestamp() > -100 && 
					currentTimestamp - gpsFeed.get(k).getTimestamp() < 100)
			{
				
				//Find the closest value to timestamp for gps, and previous 4 gps values closest to the found gps
				ArrayList<AccelSample> nearestGpsPoints = new ArrayList<AccelSample>();
				
				//Draw the points in
				currentSample = accelFeed.getFilteredFeed().getNearestSample(currentTimestamp);
				this.drawPoint(origin.x + scaleX(currentSample.getYValueInG()),
							   origin.y + scaleY(currentSample.getXValueInG()), g);
			}
		}
	}

	private void drawPoint(int loc_x, int loc_y, Graphics2D g)
	{
		final int pointSize = 6;
		g.drawOval(loc_x - (pointSize >> 1), loc_y - (pointSize >> 1), pointSize, pointSize);
		g.fillOval(loc_x - (pointSize >> 1), loc_y - (pointSize >> 1), pointSize, pointSize);;
	}
	
	
	public void update(long timestamp) {
		super.update(timestamp);
		
		paint(getGraphics());
	}
	
	
	public void update(Graphics g) {
		paint(g);
		
		panelWidth = getWidth();
		panelHeight = getHeight();
	}

	/*
	 * Scale the accel coordinates to fit on the axis
	 */
	private int scaleX(double d)
	{
		return (int)Math.round(d * panelWidth / 2 / MAX_CIRCLE_VAL_X);
	}

	/*
	 * Scale the accel coordinates to fit on the axis
	 */
	private int scaleY(double d)
	{
		return (int)Math.round(d * panelHeight / 2 / MAX_CIRCLE_VAL_Y);
	}

	private Point getOrigin()
	{
		return new Point(panelWidth / 2 - 1, panelHeight / 2 - 1);
	}
}
