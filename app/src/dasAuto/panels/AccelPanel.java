package dasAuto.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.*;

import dasAuto.logData.feeds.AccelFeed;


public class AccelPanel extends DataPanel {
	private static final long serialVersionUID = 7994490881238648787L;
	

	/**
	 * @param axis	Determines if the plot is of the x,y, or z axis.
	 */
	public AccelPanel(int axis) throws IllegalArgumentException {
		XYSeriesCollection accelSeriesCollection = new XYSeriesCollection();
		Color accelChartColor = Color.BLACK;
		
		String yLabel = "";
		switch(axis) {
			case AccelFeed.X_AXIS:
				yLabel = "Longitudinal Acceleration";
				accelChartColor = new Color(0,155,0);
				break;
			case AccelFeed.Y_AXIS:
				yLabel = "Lateral Acceleration";
				accelChartColor = Color.BLUE;
				break;
			case AccelFeed.Z_AXIS:
				yLabel = "Vertical Acceleration";
				break;
		}
		
		AccelFeed filteredAccelFeed = accelFeed.getFilteredFeed(75);
		XYSeries accelSeries = filteredAccelFeed.getXySeries(axis);
		//XYSeries accelSeries = accelFeed.getXySeries(axis);
		
		accelSeriesCollection.addSeries(accelSeries);
		JFreeChart accelChart = ChartFactory.createXYLineChart(null, null, yLabel, (XYDataset)accelSeriesCollection, PlotOrientation.VERTICAL, false, false, false);
		
		// Chart cropping: Fits the data into the chart with fewer empty chart values and minor padding.
		accelChart.getXYPlot().getRangeAxis().setLowerBound(accelSeries.getMinY() / 1.005);
		accelChart.getXYPlot().getRangeAxis().setUpperBound(accelSeries.getMaxY() * 1.005);
		accelChart.getXYPlot().getDomainAxis().setUpperBound(accelSeries.getMaxX() * 1.005);
		
		XYItemRenderer renderer = accelChart.getXYPlot().getRenderer();
		renderer.setSeriesPaint(0, accelChartColor);
		
		ChartPanel accelChartPanel = new ChartPanel(accelChart);
		this.setLayout(new BorderLayout());
		this.add(accelChartPanel, BorderLayout.CENTER);
		accelChartPanel.setHorizontalAxisTrace(true);
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0 ,Color.black));
	}
	
	
}
