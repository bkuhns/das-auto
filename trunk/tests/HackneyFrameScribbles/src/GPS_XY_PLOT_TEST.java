/*	GPRMC: RMC - Recommended Minimum Specific GNSS Data
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
 */

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.*;
import org.jfree.data.io.*;
import org.jfree.layout.*;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.lang.StringBuffer;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class GPS_XY_PLOT_TEST {

	public static void main(String[] args){
		XYSeries latAndLongData = new XYSeries("Latitude and Longitude"), 
				 timeAndVelocityData = new XYSeries("Time and Speed");
		
		try{
			File gpsFile = new File("gps_lccc_without_accel_test.txt");
			Scanner gpsScanner = new Scanner(gpsFile);
			Double latitudeInput = 0.0, longitudeInput = 0.0, timeInput = 0.0, velocityInput = 0.0;
			
			while(gpsScanner.hasNextLine()){
				String gpsLine[] = gpsScanner.nextLine().split(",");
				if(gpsLine[0].equals("$GPRMC"))
				{
					timeInput = Double.valueOf(gpsLine[1]);
					latitudeInput = Double.valueOf(gpsLine[3]);
					longitudeInput = Double.valueOf(gpsLine[5]);
					velocityInput = Double.valueOf(gpsLine[7]);
					//System.out.println("RMC Data string");
					//System.out.println("Testing Data Entry, timeInput value: " + timeInput + 
					//				   " , latitudeInput: " + latitudeInput + " , longitudeInput: " +
					//				   longitudeInput + " , veloctityInput: " + velocityInput);	
					latAndLongData.add( longitudeInput, latitudeInput);
					timeAndVelocityData.add(latitudeInput, longitudeInput);
				}
			}
			gpsScanner.close();
		}
		catch(FileNotFoundException e){
			System.out.println(e.toString());
		}
		
		XYSeriesCollection latAndLongCollection = new XYSeriesCollection(latAndLongData);
		XYDataset latLongDataset = (XYDataset)latAndLongCollection;
		
		//JFreeChart gpsChart = ChartFactory.createXYLineChart("DAS Auto Chart: Lat/Long", "Longitude", "Latitude", 
		//		latLongDataset, PlotOrientation.VERTICAL, false, false, false);
		JFreeChart gpsChart = ChartFactory.createScatterPlot("GPS Coord's",  "Longitude", "Latitude", 
				latLongDataset, PlotOrientation.VERTICAL, false, false, false);
		gpsChart.getXYPlot().getRangeAxis().setUpperBound(latAndLongData.getMaxY());
		gpsChart.getXYPlot().getRangeAxis().setLowerBound(latAndLongData.getMinY());
		gpsChart.getXYPlot().getDomainAxis().setInverted(true);
		//gpsChart.getXYPlot().getRenderer().
		ChartPanel gpsPanel = new ChartPanel(gpsChart);
		JPanel jframesPanel = new JPanel();
		jframesPanel.add(gpsPanel);
		JFrame frame = new JFrame();
		frame.add(jframesPanel);
		frame.pack();
		frame.setVisible(true);
	}

}
