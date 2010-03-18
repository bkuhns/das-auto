
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
	 timeAndVelocityData = new XYSeries("Time and Speed");
	
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
		ChartPanel testChart = getChartPanel();
		
		this.add(testChart, "0,0,2,0");
		//this.add(button[0], "0,0,2,0");
		this.add(button[1], "3,0,3,1");
		this.add(button[2], "0,1,2,1");
		this.add(button[3], "0,2,0,2");
		this.add(button[4], "1,2,1,2");
		this.add(button[5], "2,2,3,2");
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
	
	public static void main(String[] args) {
		new GuiTableLayoutTest();
	}

}
