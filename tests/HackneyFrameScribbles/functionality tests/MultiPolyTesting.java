import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MultiPolyTesting extends JPanel {
	
	 Point point1 = new Point(100,100);
	 Point point2 = new Point(200,200);
	 Point point3 = new Point(250,250);
	 Point point4 = new Point(300,250);
	 double accel[] = {350, 500, 750, 1000};
	 int minPolyWidth, maxPolyWidth;
	
	 public void paintComponent(Graphics g) {
		 
		 
		 
	 }
	 
	 
	 public void calculateMinMaxPolyWidth() {
		 
	 }
	
	
	public static void main(String[] args) {
	    JFrame frame = new JFrame();
	    frame.setTitle("MultiPoly");
	    frame.setExtendedState(frame.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	    frame.addWindowListener( new WindowAdapter(){ 
	    							public void windowClosing(WindowEvent e) {
	    								System.exit(0); 
	    							} 
	    						}
	    						);
	    Container contentPane = frame.getContentPane();
	    
	    
	    contentPane.add(new MultiPolyTesting());
	    frame.setVisible(true);
	}

}
