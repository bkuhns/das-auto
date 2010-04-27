package dasAuto.panels;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import javax.swing.BorderFactory;


public class TractionCirclePanel extends DataPanel {
	private static final long serialVersionUID = -600064128610900053L;
	
	private int panelWidth;
	private int panelHeight;
	
	
	public TractionCirclePanel() {
		super();
		
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createLineBorder(Color.black, 1));
	}
	
	
	public void paint(Graphics g) {
		super.paint(g);
		
		Graphics2D g2d = (Graphics2D)g;
		
		panelWidth = getSize().width;
		panelHeight = getSize().height;
		
		BufferedImage tracCircleImage = (BufferedImage)createImage(panelWidth, panelHeight);

		Graphics2D tracCircleG = tracCircleImage.createGraphics();
		
		tracCircleG.setColor(Color.WHITE);
		tracCircleG.fill(getVisibleRect());
		
		drawAxis(tracCircleG);
		
		g2d.drawImage(tracCircleImage, 0, 0, this);
	}
	
	
	private void drawAxis(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.setStroke(new BasicStroke(1));
		
		g.drawLine(0, panelHeight / 2 - 1, panelWidth, panelHeight / 2 -1); // Draw x-axis.
		g.drawLine(panelWidth / 2 - 1, 0, panelWidth / 2 - 1, panelHeight); // Draw y-axis.
	}
	
	
	private void drawMaxCircle(Graphics2D g) {
		//g.drawOval(accelFeed.getMinXValue(), y, width, height)
	}
	
	
	public void update(Graphics g) {
		paint(g);
	}
	
	
}
