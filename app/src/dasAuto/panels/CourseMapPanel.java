package dasAuto.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;


public class CourseMapPanel extends DataPanel {
	private static final long serialVersionUID = 3413521493798935318L;
	
	
	public CourseMapPanel() {
		super();
		setBackground(Color.WHITE);
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		Point previousPoint = null;

		int panelWidth = getSize().width;
		int panelHeight = getSize().height;
		//TODO: Need to compute course polygon min/max widths as a percentage of the panel height and width.
		BufferedImage courseMapImage = (BufferedImage)createImage(panelWidth, panelHeight);

		Graphics2D courseMapG = courseMapImage.createGraphics();
		
		courseMapG.setColor(Color.WHITE);
		courseMapG.fill(getVisibleRect());
		
		courseMapG.setColor(Color.BLACK);
		
		double minLat = gpsFeed.getMinLatitude();
		double maxLat = gpsFeed.getMaxLatitude();
		double minLon = gpsFeed.getMinLongitude();
		double maxLon = gpsFeed.getMaxLongitude();
		
		for(int i = 0; i < gpsFeed.size(); i++) {
			double currentLat = gpsFeed.get(i).getLatitude();
			double currentLon = gpsFeed.get(i).getLongitude();

			//TODO: Instead of 10% fixed padding, adjust padding to maintain a square set of GPS coordinates. 
			int currentX = (int)Math.round((((maxLon - currentLon) * (double) (panelWidth * 0.9)) / (maxLon - minLon)) + panelWidth * 0.05);
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
			previousPoint.setLocation(currentX, currentY);
		}
		
		g2d.drawImage(courseMapImage, 0, 0, this);
	}
	
	
	public void update(Graphics g) {
		paint(g);
	}
	
	
}
