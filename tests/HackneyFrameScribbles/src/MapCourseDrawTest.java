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
	
	public static Vector<AccelSample> accelData = new Vector<AccelSample>();
	public static Vector<GpsSample> gpsData = new Vector<GpsSample>();
	public static double maxLat = 0, maxLon = 0, minLat = 0, minLon = 0;

	public MapCourseDrawTest(Dimension dim, DasFileReader raceData){
		raceDataFill(raceData);
		parseMinsAndMaxs();
		
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
			currentLat = gpsData.elementAt(gpsNum).getLatitude();
			currentLon = gpsData.elementAt(gpsNum).getLongitude();
			
			currentX = (int)Math.round((((maxLon - currentLon)  * (double)(w * 0.9))/  (maxLon - minLon)) + w * 0.05) ;
			currentY = (int)Math.round((((maxLat - currentLat)  * (double)(h * 0.9))/  (maxLat - minLat)) + h * 0.05);
			
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
	
	public static void drawFrame(DasFileReader raceData){
	    JFrame f = new JFrame("Map Course Draw Test");
	    f.addWindowListener(new WindowAdapter() {
	      public void windowClosing(WindowEvent e) {
	        System.exit(0);
	      }
	    });
	    
	    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    f.setExtendedState(f.getExtendedState() | JFrame.MAXIMIZED_BOTH);
	    
	    f.getContentPane().setLayout(new BorderLayout());
	    f.getContentPane().add(new MapCourseDrawTest(f.getSize(), raceData ), "Center");
	    
	    f.setVisible(true);
	}
	
	public static void raceDataFill( DasFileReader raceData){
		accelData = raceData.getAccelData();
		gpsData = raceData.getGPSData();
	}
	
	//Latitude(N/S): 1nd index; Longitude(E/W): 3th index
	public static void parseMinsAndMaxs(){
		for(int gpsVectorPosition = 0; gpsVectorPosition < gpsData.size(); gpsVectorPosition++){
			
			if(gpsVectorPosition == 0){
				minLat = gpsData.elementAt(gpsVectorPosition).getLatitude();
				maxLat = gpsData.elementAt(gpsVectorPosition).getLatitude();
				minLon = gpsData.elementAt(gpsVectorPosition).getLongitude();
				maxLon = gpsData.elementAt(gpsVectorPosition).getLongitude();
			}
			else{
				if(gpsData.elementAt(gpsVectorPosition).getLatitude() < minLat)
					minLat = gpsData.elementAt(gpsVectorPosition).getLatitude();
				if(gpsData.elementAt(gpsVectorPosition).getLatitude() > maxLat)
					maxLat = gpsData.elementAt(gpsVectorPosition).getLatitude();
				if(gpsData.elementAt(gpsVectorPosition).getLongitude() < minLon)
					minLon =  gpsData.elementAt(gpsVectorPosition).getLongitude();
				if( gpsData.elementAt(gpsVectorPosition).getLongitude() > maxLon)
					maxLon =  gpsData.elementAt(gpsVectorPosition).getLongitude();
			}
		}
	}
	
	//Main for testing MapCourseDrawTest JPanel type
	public static void main(String[] args) {
		DasFileReader raceData = new DasFileReader("log_100401_lccc.txt");
		raceDataFill(raceData);
		parseMinsAndMaxs();
		drawFrame(raceData);
	}
}
