import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;
import org.jfree.data.io.*;
import org.jfree.layout.*;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class DAS_GUI_Testing {
	
	public static void main(String[] args){
		XYSeries xAxisData = new XYSeries("X direction"), yAxisData = new XYSeries("Y direction"), zAxisData = new XYSeries("Z direction");
		
		try{
			File csvFile = new File("log_deadmans-curve.csv");
			Scanner csvScanner = new Scanner(csvFile);
			csvScanner.nextLine();
			Double timeInput = 0.0, lateralInput = 0.0, accelInput = 0.0;
			
			while(csvScanner.hasNextLine())
			{
				StringTokenizer csvLine = new StringTokenizer(csvScanner.nextLine());
				timeInput = Double.valueOf(csvLine.nextToken(",").toString());
				lateralInput = Double.valueOf(csvLine.nextToken(",").toString());
				accelInput = Double.valueOf(csvLine.nextToken().toString());
				xAxisData.add(timeInput, lateralInput);
				yAxisData.add(timeInput, accelInput);
			}
			csvScanner.close();
		}
		catch(FileNotFoundException e){
			System.out.println(e.toString());
		}
		
		XYSeriesCollection accelSeries = new XYSeriesCollection();
		XYSeriesCollection lateralSeries = new XYSeriesCollection();
		accelSeries.addSeries(yAxisData);
		lateralSeries.addSeries(xAxisData);
		XYDataset accelData = (XYDataset)accelSeries;
		XYDataset lateralData = (XYDataset)lateralSeries;
		JFreeChart accelChart = ChartFactory.createXYLineChart("DAS Auto Chart: Accel/Time", "Time(ms)", "AccelerationG(unk-nown)", 
															accelData, PlotOrientation.VERTICAL, false, false, false);
		JFreeChart lateralChart = ChartFactory.createXYLineChart("DAS Auto Chart: Accel/Time", "Time(ms)", "LateralG(unk-nown)", 
				   lateralData, PlotOrientation.VERTICAL, false, false, false);
		ChartPanel accelPanel = new ChartPanel(accelChart);
		ChartPanel lateralPanel = new ChartPanel(lateralChart);
		
		JPanel jframesPanel = new JPanel(new BorderLayout());
		jframesPanel.add(accelPanel, BorderLayout.NORTH);
		jframesPanel.add(lateralPanel, BorderLayout.SOUTH);
		
		JFrame frame = new JFrame();
		frame.add(jframesPanel);
		frame.pack();
		frame.setVisible(true);
	}
}