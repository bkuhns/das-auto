/***********************************************************************************************************
 * 
 *  GPRMC: RMC - Recommended Minimum Specific GNSS Data
 *  Gives time, date, position, course and speed data provided by a GNSS navigation receiver.
 *  Structure:
 *  $GPRMC,hhmmss.sss,A,dddmm.mmmm,a,dddmm.mmmm,a,x.x,x.x,ddmmyy,,,a*hh<CR><LF>
 *  12 part sequence, we want to use parts: 2, 4, 6, 8 for now.
 *  
 *  hhmmss.sss : time, in hours, minutes, seconds, and fractions of a second of the given date
 *  A: status (V or A, want A)
 *  dddmm.mmmm : latitude, in format of: ddd - degrees up to 180 , 
 *  									 mm - minutes up to 60 , 
 *  									 mmmm - parts of minutes up to .9999 (can convert to seconds 0-60)
 *  a: N or S latitude
 *  dddmm.mmmm : longitude, in format of: ddd - degrees up to 180 , 
 *  									 mm - minutes up to 60 , 
 *  									 mmmm - parts of minutes up to .9999 (can convert to seconds 0-60)
 *  a: E or W longitude
 *  x.x (1): speed in knots
 *  x.x (2): course over ground in degrees
 *  ddmmyy: date
 *  ...mode(several options, want A) and checksum (61)
 *  
 *  
 *  GPS_XY_PLOT_TEST: Instead of attempting to sort out Accelerometer data, this test will do simple sorting
 *  	through the GPS only log file.
 *  	(1) Be able to read in the .txt file, and sort out lines of the $GPRMC data
 *  	(2) Be able to trim out data corresponding to the lat, long, time, and velocity
 *  
 ***********************************************************************************************************/


import org.jfree.chart.*;
import org.jfree.chart.plot.*;
import org.jfree.data.xy.*;

import javax.swing.*;
import java.io.*;
import java.util.*;


public class GpsXyPlotTest extends JFrame {
	private static final long serialVersionUID = 1L;
	
	XYSeries latAndLongData = new XYSeries("Latitude and Longitude"), 
	 		 timeAndVelocityData = new XYSeries("Time and Speed");
	
	
	public GpsXyPlotTest() {
		// Prepare data from input file.
		readInputFile();
		
		// Construct the GUI.
		setContentPane(getChartPanel());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
	
	private void readInputFile() {
		try {
			Scanner gpsScanner = new Scanner(new File("gps_lccc2_without_accel_trim.txt"));
			Double	latitudeInput = 0.0, 
					longitudeInput = 0.0,
					timeInput = 0.0,
					velocityInput = 0.0;
			
			while(gpsScanner.hasNextLine()) {
				String gpsLine[] = gpsScanner.nextLine().split(",");
				if(gpsLine[0].equals("$GPRMC")) {
					timeInput = Double.valueOf(gpsLine[1]);
					latitudeInput = Double.valueOf(gpsLine[3]);
					longitudeInput = Double.valueOf(gpsLine[5]);
					velocityInput = Double.valueOf(gpsLine[7]);
					latAndLongData.add(longitudeInput, latitudeInput);
					timeAndVelocityData.add(latitudeInput, longitudeInput);
				}
			}
			gpsScanner.close();
		} catch(Exception ex) {
			System.out.println("Oh noes, an error has occured: " + ex.getMessage());
		}
	}
	
	
	private ChartPanel getChartPanel() {
		XYSeriesCollection latAndLongCollection = new XYSeriesCollection(latAndLongData);
		XYDataset latLongDataset = (XYDataset)latAndLongCollection;
		JFreeChart gpsChart = ChartFactory.createScatterPlot(null,  null, null, latLongDataset, PlotOrientation.VERTICAL, false, false, false);
		XYPlot xyPlot = gpsChart.getXYPlot();
		
		xyPlot.getRangeAxis().setUpperBound(latAndLongData.getMaxY());
		xyPlot.getRangeAxis().setLowerBound(latAndLongData.getMinY());
		xyPlot.getDomainAxis().setInverted(true);
		
		// Not sure what to do with this yet, but I'd like to be able to change the render shape and color somehow.
		/*XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
		renderer.setSeriesShapesVisible(0, true);
		renderer.setSeriesLinesVisible(0, false);
		renderer.setBaseShape(new Ellipse2D.Float());
		renderer.setBasePaint(Color.BLUE);
		gpsChart.getXYPlot().setRenderer(renderer);*/
		
		return new ChartPanel(gpsChart);
	}

	
	public static void main(String[] args){	
		new GpsXyPlotTest();
	}

	
}
