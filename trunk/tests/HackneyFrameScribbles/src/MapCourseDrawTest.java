import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class MapCourseDrawTest extends JPanel implements MouseListener, MouseMotionListener 
{
	private static final long serialVersionUID = 1L;
	
	BufferedImage bufferedimageCourseMap = new BufferedImage(5,5, BufferedImage.TYPE_INT_RGB);
	Graphics2D bufferedCourseGraphic;
	Rectangle area;
	
	boolean firstTime = true;
	
	public static Vector<String> accelData = new Vector<String>(), gpsData = new Vector<String>();
	public static double maxLat = 0, maxLon = 0, minLat = 0, minLon = 0;

	public MapCourseDrawTest(Dimension dim){
		setBackground(Color.WHITE);
		addMouseListener(this);
		addMouseMotionListener(this);
		this.setSize(dim);
	}
	
	public void mouseClicked(MouseEvent arg0) {
	}

	public void mouseEntered(MouseEvent arg0) {
	}

	public void mouseExited(MouseEvent arg0) {
	}

	public void mousePressed(MouseEvent arg0) {
	}

	public void mouseReleased(MouseEvent arg0) {
	}

	public void mouseDragged(MouseEvent arg0) {
	}

	public void mouseMoved(MouseEvent arg0) {
		repaint();
	}
	
	public void paint(Graphics g){
		update(g);
	}
	
	public void update(Graphics g)
	{
		Graphics2D courseGraphic = (Graphics2D) g;
		firstTimeCourseMapDraw();
		
		courseGraphic.drawImage(bufferedimageCourseMap, 0, 0, this);
	}

	public void firstTimeCourseMapDraw()
	{
		Dimension dim = getSize();
		double currentLat = 0, currentLon = 0; 
		Point previousPoint = null;
		int currentX = 0, currentY = 0;
		
		int w = dim.width, h = dim.height;
		bufferedimageCourseMap = (BufferedImage) createImage(w, h);
		
		bufferedCourseGraphic = bufferedimageCourseMap.createGraphics();
		bufferedCourseGraphic.setColor(Color.WHITE);
		bufferedCourseGraphic.fill(getVisibleRect());
		
		bufferedCourseGraphic.setColor(Color.BLACK);
		
		
		for(int gpsNum = 0; gpsNum < gpsData.size(); gpsNum++)
		{
			currentLat = Double.parseDouble(gpsData.elementAt(gpsNum).substring(11, gpsData.elementAt(gpsNum).indexOf(',', 11)));
			currentLon = Double.parseDouble(gpsData.elementAt(gpsNum).substring(23, gpsData.elementAt(gpsNum).indexOf(',', 23)));
			
			currentX = (int)Math.round(((maxLon - currentLon)  * (double)w)/  (maxLon - minLon));
			currentY = (int)Math.round(((maxLat - currentLat)  * (double)h)/  (maxLat - minLat));
			
			System.out.println("currentX: " + currentX + " , currentY: " + currentY);
			
			bufferedCourseGraphic.setStroke(new BasicStroke(2.0f));
			bufferedCourseGraphic.drawOval(currentX, currentY, 1, 1);
			
			if(previousPoint == null) {
				previousPoint = new Point();
			}
			else{
				bufferedCourseGraphic.setStroke(new BasicStroke(1.0f));
				bufferedCourseGraphic.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
				bufferedCourseGraphic.drawLine(previousPoint.x, previousPoint.y, currentX, currentY);
			}
			previousPoint.setLocation(currentX, currentY);
		}
	}
	
	public static void drawFrame(){
	    JFrame f = new JFrame("Map Course Draw Test");
	    f.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	    
	    f.getContentPane().setLayout(new BorderLayout());
	    f.getContentPane().add(new MapCourseDrawTest(f.getSize()), "Center");
	    
	    f.setVisible(true);
	}
	
	public static void raceDataFill(){
		DasFileReader raceData = new DasFileReader("gps_lccc2_log_snippet.txt");
		accelData = raceData.getAccelData();
		gpsData = raceData.getGPSData();
	}
	
	//Latitude(N/S): 1nd index; Longitude(E/W): 3th index
	public static void parseMinsAndMaxs(){
		String currentGPSLine[];
		for(int gpsVectorPosition = 0; gpsVectorPosition < gpsData.size(); gpsVectorPosition++){
			currentGPSLine = gpsData.elementAt(gpsVectorPosition).split(",");
			if(gpsVectorPosition == 0){
				minLat = Double.valueOf(currentGPSLine[1]);
				maxLat = Double.valueOf(currentGPSLine[1]);
				minLon = Double.valueOf(currentGPSLine[3]);
				maxLon = Double.valueOf(currentGPSLine[3]);
			}
			else{
				if(Double.valueOf(currentGPSLine[1]) < minLat)
					minLat = Double.valueOf(currentGPSLine[1]);
				if(Double.valueOf(currentGPSLine[1]) > maxLat)
					maxLat = Double.valueOf(currentGPSLine[1]);
				if(Double.valueOf(currentGPSLine[3]) < minLon)
					minLon = Double.valueOf(currentGPSLine[3]);
				if(Double.valueOf(currentGPSLine[1]) > maxLon)
					maxLon = Double.valueOf(currentGPSLine[3]);
			}
		}
	}
	
	//Main for testing MapCourseDrawTest JPanel type
	public static void main(String[] args) {
		raceDataFill();
		parseMinsAndMaxs();
		drawFrame();
	}
}
