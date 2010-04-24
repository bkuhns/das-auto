package dasAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.panels.AccelPanel;
import dasAuto.panels.CourseMapPanel;
import dasAuto.panels.PolygonCourseMapPanel;
import dasAuto.panels.TractionCirclePanel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JSlider;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2269971701250845501L;
	GridBagConstraints gridCon;
	
	
	public MainFrame() {
		initializeFrame();
		addPanels();
		pack();
	}	
	
	
	private void initializeFrame() {
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		
		setTitle("DAS Auto");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(800, 600));
		setMinimumSize(new Dimension(800, 600));
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH); // TODO: Why the hell doesn't this work in Linux? Does it work in Windows? 
		getContentPane().setLayout(new BorderLayout());
		
		getContentPane().setLayout(new GridBagLayout());
		gridCon = new GridBagConstraints();
	}
	
	
	private void addPanels() {
		addCourseMapPanel();
		addDataTablePanel();
		addSliderPanel();
		addLatAccelPanel();
		addLonAccelPanel();
		addTractionCirclePanel();
	}
	
	
	private void addCourseMapPanel() {
		gridCon.fill = GridBagConstraints.BOTH;
		gridCon.ipady = 500;
		gridCon.weightx = 0.75;
		gridCon.weighty = 0.70;
		gridCon.gridwidth = 2;
		gridCon.gridx = 0;
		gridCon.gridy = 0;
		getContentPane().add(new PolygonCourseMapPanel(), gridCon);
	}
	
	
	private void addDataTablePanel() {
		gridCon.weightx = 0.25;
		gridCon.gridwidth = 1;
		gridCon.gridheight = 2;
		gridCon.gridx = 2;
		gridCon.gridy = 0;
		getContentPane().add(new JButton("Data Table"), gridCon);
	}
	
	
	private void addSliderPanel() {
		gridCon.ipady = 25;
		gridCon.weighty = 0.1;
		gridCon.gridwidth = 2;
		gridCon.gridheight = 1;
		gridCon.gridx = 0;
		gridCon.gridy = 1;
		
		JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		timeSlider.setBackground(Color.WHITE);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(true);
		timeSlider.setMinorTickSpacing(25);
		timeSlider.setMajorTickSpacing(250);
		getContentPane().add(timeSlider, gridCon);
	}
	
	
	private void addLatAccelPanel() {
		gridCon.ipady = 200;
		gridCon.weighty = 0.20;
		gridCon.weightx = 1.0/3.0;
		gridCon.gridwidth = 1;
		gridCon.gridx = 0;
		gridCon.gridy = 2;
		getContentPane().add(new AccelPanel(AccelFeed.X_AXIS), gridCon);
	}
	
	
	private void addLonAccelPanel() {
		gridCon.gridx = 1;
		gridCon.gridy = 2;
		getContentPane().add(new AccelPanel(AccelFeed.Y_AXIS), gridCon);
	}
	
	
	private void addTractionCirclePanel() {
		gridCon.gridx = 2;
		gridCon.gridy = 2;
		getContentPane().add(new TractionCirclePanel(), gridCon);
	}
	
	
}
