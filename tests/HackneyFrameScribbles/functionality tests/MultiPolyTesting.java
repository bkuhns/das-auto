import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MultiPolyTesting extends JPanel {
	
	public static Point[] arrayPoint = new Point[4];
	private static final long serialVersionUID = 5369072467124325227L;
	int minPolyWidth, maxPolyWidth, minAccel = 0, maxAccel = 0;
	int accel[] = {375, 350, 995, 1000};
	boolean firstTime = true;
	
	 
	 public void paintComponent(Graphics g) {
		 Graphics2D graphic = (Graphics2D)g;
		 Point previousPoint = null, currentPoint = null;
		 int currentWidth = 0, previousWidth = 0;
		 
		 if(firstTime) {
			 calculateMinMaxPolyWidthAndMinMaxAccelValues();
			 firstTime = false;		 
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
			 currentWidth = getCurrentWidth(p);
			 
			 /*  Check to see if we're on the first point to create a line for. If so...find the
			  *  slope based upon the lateral acceleration.
			  */
			 if(previousPoint == null) {
				 previousWidth = currentWidth;
				 previousPoint = currentPoint;
			 } else {
				 
				 
				 previousPoint = currentPoint;
			 }
		 }
	 }
	 
	 
	 public void calculateMinMaxPolyWidthAndMinMaxAccelValues() {
		 //For testing purposes, create a minimum width of 1% of the screen height (from pixels)
		 //and a maximum width of 3% of the same.
		 minPolyWidth = (int) Math.round(this.getHeight() * 0.01);
		 maxPolyWidth = (int) Math.round(this.getHeight() * 0.03);
		 
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
	 
	 
	 public int getCurrentWidth(int currentNum) {
		 int curWidth = 0;
		 double currentWidthProportion = 
				( (double)(accel[currentNum] - minAccel) / (double)(maxAccel - minAccel) );
		 
		 if(currentWidthProportion == 0.0)
			 curWidth = minPolyWidth;
		 else if (currentWidthProportion == 1.0)
			 curWidth = maxPolyWidth;
		 else
		 {
			 curWidth = minPolyWidth + 
			 			    (int)Math.round(currentWidthProportion * (maxPolyWidth - minPolyWidth));
		 }
		 
		 System.out.println("Current width Proportion: " + currentWidthProportion);
		 System.out.println("Current width: " + curWidth);
		 
		 return curWidth;
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
