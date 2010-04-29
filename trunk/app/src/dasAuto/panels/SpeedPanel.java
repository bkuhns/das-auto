package dasAuto.panels;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Marker;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.*;
import org.jfree.ui.RectangleAnchor;
import org.jfree.ui.TextAnchor;


public class SpeedPanel extends DataPanel {
	private static final long serialVersionUID = 7994490881238648787L;
	
	private Marker timeMarker;
	private JFreeChart speedChart;
	
	
	public SpeedPanel() {
		XYSeriesCollection speedSeriesCollection = new XYSeriesCollection();
		XYSeries speedSeries = gpsFeed.getXySpeedSeries();
		
		speedSeriesCollection.addSeries(speedSeries);
		speedChart = ChartFactory.createXYLineChart(null, null, "Vehicle Speed", (XYDataset)speedSeriesCollection, PlotOrientation.VERTICAL, false, false, false);
		
		// Chart cropping: Fits the data into the chart with fewer empty chart values and minor padding.
		speedChart.getXYPlot().getRangeAxis().setLowerBound(0);
		speedChart.getXYPlot().getRangeAxis().setUpperBound(speedSeries.getMaxY() * 1.005);
		speedChart.getXYPlot().getDomainAxis().setUpperBound(speedSeries.getMaxX() * 1.005);
		
		XYItemRenderer renderer = speedChart.getXYPlot().getRenderer();
		renderer.setSeriesPaint(0, new Color(200,0,0));
				
		setLayout(new BorderLayout());
		add(new ChartPanel(speedChart), BorderLayout.CENTER);
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0 ,Color.black));
	}
	
	
	@Override
	public void update(long timestamp) {
		super.update(timestamp);
		
		speedChart.getXYPlot().removeDomainMarker(timeMarker);
		timeMarker = new ValueMarker(timestamp / 1000.0);
        timeMarker.setPaint(Color.BLACK);
        timeMarker.setStroke(new BasicStroke(1.0f));
        timeMarker.setLabelAnchor(RectangleAnchor.TOP_LEFT);
        timeMarker.setLabelTextAnchor(TextAnchor.TOP_RIGHT);
		speedChart.getXYPlot().addDomainMarker(timeMarker);
	}
	
	
}