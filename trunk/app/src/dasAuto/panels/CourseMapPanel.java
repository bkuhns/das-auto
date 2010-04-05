package dasAuto.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.image.BufferedImage;


public class CourseMapPanel extends DataPanel {
	private static final long serialVersionUID = 3413521493798935318L;

	BufferedImage courseMapImage = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	Rectangle area;

	
	public CourseMapPanel() {
		super();
		
		setBackground(Color.WHITE);
	}
	
	
	public void paint(Graphics g) {
		paint((Graphics2D)g);
	}

	
	public void paint(Graphics2D g) {
		super.paint(g);
		
		Point previousPoint = null;

		int panelWidth = getSize().width;
		int panelHeight = getSize().height;
		courseMapImage = (BufferedImage)createImage(panelWidth, panelHeight);

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
		
		g.drawImage(courseMapImage, 0, 0, this);
	}
	
	
	public void update(Graphics g) {
		paint(g);
	}
	
	
}
