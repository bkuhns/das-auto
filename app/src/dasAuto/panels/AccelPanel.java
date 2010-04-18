package dasAuto.panels;

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
			
			if(currentAccel > 1000)
				System.out.println("Time: " + currentSample.getTimestamp() + " , currentAccel: " + currentAccel);
			
			
			accelSeries.add(currentSample.getTimestamp(),currentAccel);
		}
		
		accelSeriesCollection.addSeries(accelSeries);
		accelDataset = (XYDataset) accelSeriesCollection;
		
		JFreeChart accelFreeChart = ChartFactory.createXYLineChart("DAS Auto Chart: Accel/Time", "Time(ms)", "AccelerationG(unk-nown)", 
				accelDataset, PlotOrientation.VERTICAL, false, false, false);
		
		ChartPanel accelChartPanel = new ChartPanel(accelFreeChart);
		this.add(accelChartPanel);
	}
}
