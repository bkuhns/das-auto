package dasAuto.panels;

import java.awt.BorderLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;


public class SpeedPanel extends DataPanel {
	private static final long serialVersionUID = 7994490881238648787L;
	

	/**
	 * @param axis	Determines if the plot is of the x,y, or z axis.
	 */
	public SpeedPanel() {
		XYSeriesCollection speedSeriesCollection = new XYSeriesCollection();
		
		XYSeries speedSeries = gpsFeed.getXySpeedSeries();
		
		speedSeriesCollection.addSeries(speedSeries);
		JFreeChart accelChart = ChartFactory.createXYLineChart(null, null, null, (XYDataset)speedSeriesCollection, PlotOrientation.VERTICAL, false, false, false);
		
		// Chart cropping: Fits the data into the chart with fewer empty chart values and minor padding.
		accelChart.getXYPlot().getRangeAxis().setLowerBound(0);
		accelChart.getXYPlot().getRangeAxis().setUpperBound(speedSeries.getMaxY() * 1.005);
		accelChart.getXYPlot().getDomainAxis().setUpperBound(speedSeries.getMaxX() * 1.005);
				
		ChartPanel accelChartPanel = new ChartPanel(accelChart);
		this.setLayout(new BorderLayout());
		this.add(accelChartPanel, BorderLayout.CENTER);
	}
	
	
}