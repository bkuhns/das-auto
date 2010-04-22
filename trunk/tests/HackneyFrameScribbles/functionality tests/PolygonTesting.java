import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class PolygonTesting extends JPanel {
	
	 public void paintComponent(Graphics g) {
	    Graphics2D test2D = (Graphics2D)g;

	    Polygon trape = new Polygon();
	    trape.addPoint(221, 221);
	    trape.addPoint(121, 121);
	    trape.addPoint(170, 170);
	    trape.addPoint(79, 79);
	    
	    Line2D testLine;
	    
	    //Color(int r, int g, int b) range: 0-255
	    Color testRed = new Color(0,0,255);
	    Color testGreen = new Color(250,250,0);
	    test2D.drawPolygon(trape);
	    GradientPaint gradient = new GradientPaint(100,150,testRed,300,150,testGreen,true);
	    test2D.setPaint(gradient);
	    //test2D.fillPolygon(trape);
	}
	 
	public static void main(String[] args) {
	    JFrame frame = new JFrame();
	    frame.setTitle("PolygonTesting");
	    frame.setSize(500, 500);
	    frame.addWindowListener( new WindowAdapter(){ 
	    							public void windowClosing(WindowEvent e) {
	    								System.exit(0); 
	    							} 
	    						}
	    						);
	    Container contentPane = frame.getContentPane();
	    contentPane.add(new PolygonTesting());

	    frame.setVisible(true);
	}
}
