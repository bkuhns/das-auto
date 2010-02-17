import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.*;
import org.jfree.data.io.*;
import org.jfree.layout.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.StringTokenizer;

import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;


public class DAS_GUI_Testing {
	
	public static void main(String[] args){
		XYSeries xAxisData = new XYSeries("X direction"), yAxisData = new XYSeries("Y direction"), zAxisData = new XYSeries("Z direction");
		
		try{
			File csvFile = new File("output.csv");
			Scanner csvScanner = new Scanner(csvFile);
			//csvScanner.nextLine();
			Double timeInput = 0.0, lateralInput = 0.0, accelInput = 0.0;
			
			while(csvScanner.hasNextLine())
			{
				StringTokenizer csvLine = new StringTokenizer(csvScanner.nextLine());
				timeInput = Double.valueOf(csvLine.nextToken(",").toString());
				lateralInput = Double.valueOf(csvLine.nextToken(",").toString());
				accelInput = Double.valueOf(csvLine.nextToken().toString());
				//System.out.println("Line o' Data; Time: " + timeInput + ", Lat: " 
				//		           + lateralInput + ", Accel: " + accelInput);
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
		
		JPanel jPanel1 = new JPanel(new BorderLayout());
		JPanel jPanel2 = new JPanel(new BorderLayout());
		jPanel1.add(accelPanel);
		jPanel2.add(lateralPanel);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu1 = new JMenu("File");
		JMenu menu2 = new JMenu("Edit");
		JMenu menu3 = new JMenu("Help!");
		JMenuItem menuHelp = new JMenuItem("No help yet....sorry!");
		menu3.add(menuHelp);
		menuBar.add(menu1);
		menuBar.add(menu2);
		menuBar.add(menu3);
		
		
//		JPopupMenu popup = new JPopupMenu();
//	    menuHelp.addActionListener(this);
//	    popup.add(menuItem);
//	    menuItem = new JMenuItem("Another popup menu item");
//	    menuItem.addActionListener(this);
//	    popup.add(menuItem);
//
//	    //Add listener to components that can bring up popup menus.
//	    MouseListener popupListener = new PopupListener();
//	    output.addMouseListener(popupListener);
//	    menuBar.addMouseListener(popupListener);

		Dimension interFrameSize = new Dimension(750,500);
		Dimension outerFrameSize = new Dimension(1000,750);
		
		JFrame frameOuter;
		JInternalFrame frameInnerTop, frameInnerBottom;
		JPanel mainFramePanel = new JPanel(new BorderLayout());
		frameOuter = new JFrame();
		frameOuter.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frameInnerTop = new JInternalFrame("AccelChart",true,true,true,true);
		frameInnerBottom = new JInternalFrame("LateralChart",true,true,true,true);
		
		frameInnerTop.setIconifiable(true);
		frameInnerBottom.setIconifiable(true);
		
		frameInnerTop.add(jPanel1);
		frameInnerBottom.add(jPanel2);
		
		frameInnerTop.setPreferredSize(interFrameSize);
		frameInnerBottom.setPreferredSize(interFrameSize);
		frameOuter.setSize(outerFrameSize);
		
		mainFramePanel.add(frameInnerTop);
		mainFramePanel.add(frameInnerBottom);
		frameOuter.setJMenuBar(menuBar);
		frameOuter.add(mainFramePanel);
		
		frameInnerTop.pack();
		frameInnerBottom.pack();
		frameInnerTop.setVisible(true);
		frameInnerBottom.setVisible(true);
		
		frameOuter.pack();
		frameOuter.setVisible(true);
	}
}

/*class PopupListener extends MouseAdapter
{
	public void mousePressed(MouseEvent e) {
        maybeShowPopup(e);
    }

    public void mouseReleased(MouseEvent e) {
        maybeShowPopup(e);
    }

    private void maybeShowPopup(MouseEvent e) {
        if (e.isPopupTrigger()) {
            popup.show(e.getComponent(),
                       e.getX(), e.getY());
        }
    }

}*/