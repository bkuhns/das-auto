import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MultiPolyTesting extends JPanel {
	
	BufferedImage bi = new BufferedImage(5, 5, BufferedImage.TYPE_INT_RGB);
	Graphics2D biCourse;
	
	public static Point[] arrayPoint = new Point[4];
	private static final long serialVersionUID = 5369072467124325227L;
	int minPolyWidth;
	int maxPolyWidth;
	int minAccel = 0;
	int maxAccel = 0;
	int accel[] = {800, 350, 995, 1000};
	boolean calcMinMaxes = false; 
	boolean firstTrapeDrawn =  false;
	Point oldTrapePointHigh = null;
	Point oldTrapePointLow = null;
	Color maxxedYellow;
	Color minnedBlue;
	
	
	public void paint(Graphics g) {
		update(g);
	}
	
	
	public void update(Graphics g) {
		 Graphics2D graphic = (Graphics2D)g;
		 
		 Dimension dim = getSize();
		 int w = dim.width;
	     int h = dim.height;
	     bi = (BufferedImage) createImage(w, h);
	     biCourse = bi.createGraphics();
	     biCourse.setStroke(new BasicStroke(2.0f));
		 
		 Point previousPoint = null, currentPoint = null;
		 
		 if(!calcMinMaxes) {
			 calculateMinMaxPolyWidthAndMinMaxAccelValues();
			 calcMinMaxes = true;		 
		 }
		 
		//For the very first point(x,y), determine your line size.
		 //This should be the current value ...as some proportion of min or max accel...Fuck.
		 //So, Hokay. There should be a range of accel values from your log data.
		 //maxAccel - minAccel: will give you this range. If you do a currentAccel - minAccel...
		 //divided by this value; this will be the proportion of Width.
		 //NOTE: Maybe we only need to calculate the maxPolyWidth we want, and just do portions
		 //		 of that.
		 
		 for(int p=0; p<4; p++)
		 { 
			 currentPoint = arrayPoint[p];
			 
			 /*  Check to see if we're on the first point to create a line for. If so...find the
			  *  slope based upon the lateral acceleration.
			  *  if: handles first point
			  *  else: handles all other points, including final point
			  */
			 if(previousPoint == null) {
				 
				 previousPoint = currentPoint;
				 
			 } else {
				 //Create a new trapezoid
				 DasTrapezoid drawingTrap;
				 
				 //After the first trapezoid is drawn, keep the oldSlope, and oldTrapPoints, which
				 //will be passed from the current drawn trapezoids newTrapPoints, and newSlope.
				 if(!firstTrapeDrawn) {
					 drawingTrap = new DasTrapezoid(previousPoint, currentPoint, 
								 					accel[p-1], accel[p]); 
					drawingTrap.setMaxAccel(maxAccel);
					drawingTrap.setMinAccel(minAccel);
					drawingTrap.setMaxPolyWidth(maxPolyWidth);
					drawingTrap.setMinPolyWidth(minPolyWidth);
					
					drawingTrap.assembleInitialTrape();
					
					biCourse.drawPolygon(drawingTrap);
					biCourse.fillPolygon(drawingTrap);
					
					firstTrapeDrawn = true;
				 }
				 else {
					 drawingTrap = new DasTrapezoid(currentPoint, oldTrapePointHigh,  
							 						oldTrapePointLow, accel[p]);
					 drawingTrap.setMaxAccel(maxAccel);
					 drawingTrap.setMinAccel(minAccel);
					 drawingTrap.setMaxPolyWidth(maxPolyWidth);
					 drawingTrap.setMinPolyWidth(minPolyWidth);
					 
					 /*TODO: Add lines here (and methods in DasTrapezoid) for
					  * 	 determining the 4 points, once the mins/maxs are set.
					  *
					  */
				 }
				 
				 //TODO:Draw the trapezoid in.
				 
				 /*TODO: Color in the trapezoid, using gradient, based upon the 2 points it is along.
				  *      At some point here, based upon the maximum & minimum speeds, need to find a
				  *      way of setting a currentColor for the color gradient.
				  */
				 
				 oldTrapePointHigh = drawingTrap.getTrapPointHigh();
				 oldTrapePointLow = drawingTrap.getTrapPointLow();
			 }
		 }
	firstTrapeDrawn = false;	 
	graphic.drawImage(bi, 0, 0, this);
	}
	 
	 
	 public void calculateMinMaxPolyWidthAndMinMaxAccelValues() {
		 //For testing purposes, create a minimum width of 1% of the screen height (from pixels)
		 //and a maximum width of 3% of the same.
		 minPolyWidth = (int) Math.round(this.getHeight() * 0.01);
		 maxPolyWidth = (int) Math.round(this.getHeight() * 0.10);
		 
		 for(int a=0; a<4; a++)
		 {
			 if(a == 0) {
				 minAccel = accel[a];
				 maxAccel = accel[a];
			 } else {
				 if(accel[a] < minAccel)
					 minAccel = accel[a];
				 if(accel[a] > maxAccel)
					 maxAccel = accel[a];
			 }
				 
		 }
		 System.out.println("Min width: " + minPolyWidth + " , Max width: " + maxPolyWidth);
		 System.out.println("Min accel: " + minAccel + " , Max accel: " + maxAccel);
	 }
	 
	 
	 public static void fillPoints() {
		 for(int i = 0; i < 4; i++) {
			 switch(i) {
			 	case 0: arrayPoint[0] = new Point(100,100);
			 	case 1: arrayPoint[1] = new Point(200,200);
			 	case 2: arrayPoint[2] = new Point(250,250);
			 	case 3: arrayPoint[3] = new Point(300,250);
			 }
		 }
	 }
	
	 
	 public static void main(String[] args) {
	    JFrame frame = new JFrame();
	    fillPoints();
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