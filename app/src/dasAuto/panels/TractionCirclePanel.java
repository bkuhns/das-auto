package dasAuto.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;

import dasAuto.logData.feeds.AccelFeed;


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

		//-- Draw Points first
		this.drawPoint(origin.x + scaleX(accelFeed.getMinYValueInG()), origin.y, g); // Left
		this.drawPoint(origin.x + scaleX(accelFeed.getMaxYValueInG()), origin.y, g); // Right
		this.drawPoint(origin.x, origin.y + scaleY(accelFeed.getMaxXValueInG()), g); // Top 		
		this.drawPoint(origin.x, origin.y + scaleY(accelFeed.getMinXValueInG()), g); // Bottom

		//-- Then Arcs
		//g.drawArc(origin.x,  origin.y + scaleY(accelFeed.getMaxXValueInG()), scaleX(accelFeed.getMaxYValueInG()), -scaleY(accelFeed.getMaxXValueInG()), 0, 90); // Top -> Right
		//g.drawArc(0, (int)scaleY(accelFeed.getMaxXValueInG()), (int)scaleX(accelFeed.getMaxYValueInG()), (int)scaleY(accelFeed.getMaxXValueInG()), 90, 90);
	}

	private void drawPoint(int loc_x, int loc_y, Graphics2D g)
	{
		final int pointSize = 6;
		g.drawOval(loc_x - (pointSize>> 1), loc_y - (pointSize >> 1), pointSize, pointSize);
		g.fillOval(loc_x - (pointSize >> 1), loc_y - (pointSize >> 1), pointSize, pointSize);;
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
