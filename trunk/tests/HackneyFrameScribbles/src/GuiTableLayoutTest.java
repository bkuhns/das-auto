
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import java.util.Scanner;

import info.clearthought.layout.TableLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;


public class GuiTableLayoutTest  extends JFrame {
	private static final long serialVersionUID = 1L;
	XYSeries latAndLongData = new XYSeries("Latitude and Longitude"), 
	 timeAndVelocityData = new XYSeries("Velocity over Time"),
	 laterAccelData = new XYSeries("Lateral Acceleration over Time"),
	 straightLineAccelData = new XYSeries("Straight Line Acceleration over Time");
	
	//private Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
	//private int dimWidth = dim.width;
	//private int dimHeight = dim.height;
	

	public GuiTableLayoutTest() {
		readInputFile();
		structuringTableLayout();
		addingPanels();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void structuringTableLayout()
	{
		this.setBounds(100, 0, 1200, 750);
		double size[][] = {{0.333, 0.333, 0.083, TableLayout.FILL}, {TableLayout.FILL, 0.05, 0.25}};
		this.setLayout(new TableLayout(size));
	}
	
	private void addingPanels()
	{
		//testing done with Buttons: easy to make & see
		String buttonLabels[] = {"Main Panel","Hard Data Panel","Scroll Bar Panel","Accel Panel 1"," Accel Panel 2","Trac Panel"};
		Button button[] = new Button[buttonLabels.length];
		for(int i=0;i < buttonLabels.length;i++)
			button[i] = new Button(buttonLabels[i]);
		ChartPanel testGPSChart = getGPSChartPanel();
		ChartPanel testAccelChart = getLatAccelChartPanel();
		
		this.add(testGPSChart, "0,0,2,0");
		//this.add(button[0], "0,0,2,0");
		this.add(button[1], "3,0,3,1");
		this.add(button[2], "0,1,2,1");
		//this.add(button[3], "0,2,0,2");
		this.add(testAccelChart, "0,2,0,2");
		this.add(button[4], "1,2,1,2");
		this.add(button[5], "2,2,3,2");
	}
	
	private void readInputFile() {
		try {
			Scanner dataFileScanner = new Scanner(new File("gps_midview_log.txt"));
			Double	latitudeInput = 0.0, 
					longitudeInput = 0.0,
					gpsTimeInput = 0.0,
					velocityInput = 0.0,
					accelTimeInput = 0.0,
					lateralAccelInput = 0.0,
					straightLineAccelInput = 0.0;
			
			while(dataFileScanner.hasNextLine()) {
				String dataLine[] = dataFileScanner.nextLine().split(",");
				if(dataLine[0].equals("$GPRMC")) {
					
						gpsTimeInput = Double.valueOf(dataLine[1]);
						latitudeInput = Double.valueOf(dataLine[3]);
						longitudeInput = Double.valueOf(dataLine[5]);
						velocityInput = Double.valueOf(dataLine[7]);
						latAndLongData.add(longitudeInput, latitudeInput);
						timeAndVelocityData.add(gpsTimeInput, velocityInput);
				}
				else if(dataLine[0].contains("###")){
					accelTimeInput = Double.valueOf(dataLine[0].substring(3, dataLine[0].length()));
					lateralAccelInput = Double.valueOf(dataLine[2]);
					straightLineAccelInput = Double.valueOf(dataLine[3].substring(0, dataLine[3].length() - 3));
					laterAccelData.add(accelTimeInput, lateralAccelInput);
					straightLineAccelData.add(accelTimeInput, straightLineAccelInput); 
				}
			}
			dataFileScanner.close();
		} catch(Exception ex) {
			System.out.println("Oh noes, an error has occured: " + ex.getMessage());
		}
	}
	
	
	private ChartPanel getGPSChartPanel() {
		XYSeriesCollection latAndLongCollection = new XYSeriesCollection(latAndLongData);
		XYDataset latLongDataset = (XYDataset)latAndLongCollection;
		JFreeChart gpsChart = ChartFactory.createScatterPlot(null,  null, null, latLongDataset, PlotOrientation.VERTICAL, false, false, false);
		
		XYPlot gpsXYPlot = gpsChart.getXYPlot();
		gpsXYPlot.getRangeAxis().setUpperBound(latAndLongData.getMaxY() + 0.0025);
		gpsXYPlot.getRangeAxis().setLowerBound(latAndLongData.getMinY() - 0.0025);
		gpsXYPlot.getDomainAxis().setInverted(true);
		
		return new ChartPanel(gpsChart);
	}
	
	private ChartPanel getLatAccelChartPanel()
	{
		XYSeriesCollection laterAccelCollection = new XYSeriesCollection(laterAccelData);
		XYDataset laterAccelDataset = (XYDataset)laterAccelCollection;
		JFreeChart laterAccelChart = ChartFactory.createXYLineChart(null, "Time(ms)", "LateralG(unk-nown)", 
				   laterAccelDataset, PlotOrientation.VERTICAL, false, false, false);
		
		XYPlot laterAccelXYPlot = laterAccelChart.getXYPlot();
		laterAccelXYPlot.getRangeAxis().setUpperBound(laterAccelData.getMaxY());
		laterAccelXYPlot.getRangeAxis().setLowerBound(laterAccelData.getMinY());
		
		return new ChartPanel(laterAccelChart);
	}
	
	public static void main(String[] args) {
		new GuiTableLayoutTest();
	}

}