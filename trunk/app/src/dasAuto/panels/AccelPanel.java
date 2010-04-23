package dasAuto.panels;

import java.awt.BorderLayout;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;
import dasAuto.logData.samples.AccelSample;

public class AccelPanel extends DataPanel {
	
	//int accelType determining x,y, or z axis with value of 0(x), 1(y), or 2(z)
	public AccelPanel(int accelType)
	{
		XYSeries accelSeries = new XYSeries("accel series");
		XYSeriesCollection accelSeriesCollection = new XYSeriesCollection();
		XYDataset accelDataset;
		
		AccelSample currentSample;
		int currentAccel;
		long initialTimestamp = 0;
		
		for(int accelParser=0; accelParser<accelFeed.size(); accelParser++)
		{
			currentSample = accelFeed.get(accelParser);
			
			if(accelType == 0)
				currentAccel = currentSample.getxValue(); 
			else if(accelType == 1)
				currentAccel = currentSample.getyValue();
			else if(accelType == 2)
				currentAccel = currentSample.getzValue();
			else {
				//trouble: bad int for accelType, error handling here?
				currentAccel = 0;
			}
			
			//pulls the initial Timestamp from the first sample, normalizes time to '0' at start
			if(accelParser == 0) {
				initialTimestamp = currentSample.getTimestamp();
			}
			accelSeries.add(currentSample.getTimestamp() - initialTimestamp, currentAccel);
		}

		accelSeriesCollection.addSeries(accelSeries);
		accelDataset = (XYDataset) accelSeriesCollection;
		
		JFreeChart accelFreeChart = ChartFactory.createXYLineChart(null, "Time(ms)", "Accel", 
				accelDataset, PlotOrientation.VERTICAL, false, false, false);
		
		//Chart cropping: Will try to fit the data into the chart with less empty chart values, and minor padding.
		accelFreeChart.getXYPlot().getRangeAxis().setLowerBound(accelSeries.getMinY() / 1.005);
		accelFreeChart.getXYPlot().getRangeAxis().setUpperBound(accelSeries.getMaxY() * 1.005);
		accelFreeChart.getXYPlot().getDomainAxis().setUpperBound(accelSeries.getMaxX() * 1.005);
		
		ChartPanel accelChartPanel = new ChartPanel(accelFreeChart);
		this.setLayout(new BorderLayout());
		this.add(accelChartPanel, BorderLayout.CENTER);
	}
}
