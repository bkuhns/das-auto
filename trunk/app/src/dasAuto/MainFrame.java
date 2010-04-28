package dasAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.panels.AccelPanel;
import dasAuto.panels.KeyPointsPanel;
import dasAuto.panels.PolygonCourseMapPanel;
import dasAuto.panels.SpeedPanel;
import dasAuto.panels.TimePanel;
import dasAuto.panels.TractionCirclePanel;



public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2269971701250845501L;
	
	private JPanel centerPanel;
	private JPanel sidePanel;
	private JPanel bottomPanel;
	
	private AccelPanel latAccelPanel;
	private AccelPanel lonAccelPanel;
	private SpeedPanel speedPanel;
	private PolygonCourseMapPanel courseMapPanel;
	private TimePanel timePanel;
	private KeyPointsPanel keyPointsPanel;
	private TractionCirclePanel tractionCirclePanel;
	
	
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
		
		setTitle("Data Acquisition System for Automobiles");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setPreferredSize(new Dimension(1000, 700));
		setMinimumSize(new Dimension(800, 600));
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH); // TODO: Why the hell doesn't this work in Linux? Does it work in Windows? 
		getContentPane().setLayout(new BorderLayout());
	}
	
	
	private void addPanels() {
		addCenterPanel();
		addSidePanel();
		addBottomPanel();
	}
	
	
	private void addCenterPanel() {
		centerPanel = new JPanel(new BorderLayout());
		
		centerPanel.add(courseMapPanel = new PolygonCourseMapPanel(), BorderLayout.CENTER);
		centerPanel.add(timePanel = new TimePanel(this), BorderLayout.SOUTH);
		
		getContentPane().add(centerPanel);
	}
	
	
	private void addSidePanel() {
		sidePanel = new JPanel(new GridLayout(2, 1));
		sidePanel.setBorder(BorderFactory.createMatteBorder(0, 1, 0, 0 ,Color.black));
		sidePanel.setPreferredSize(new Dimension((int)(getHeight() * 0.55), (int)(getHeight() * 0.65)));

		//-- AJG Key Points Panel
		sidePanel.add(keyPointsPanel = new KeyPointsPanel());
		sidePanel.add(tractionCirclePanel = new TractionCirclePanel());
		
		getContentPane().add(sidePanel, BorderLayout.EAST);
	}
	
	
	private void addBottomPanel() {
		bottomPanel = new JPanel(new GridLayout(1, 3));
		bottomPanel.setPreferredSize(new Dimension(getWidth(), (int)(getHeight() * 0.35)));
		
		bottomPanel.add(lonAccelPanel = new AccelPanel(AccelFeed.X_AXIS));
		bottomPanel.add(latAccelPanel = new AccelPanel(AccelFeed.Y_AXIS));
		bottomPanel.add(speedPanel = new SpeedPanel());
		
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
	
	
	public void updatePanels(long timestamp) {
		latAccelPanel.update(timestamp);
		lonAccelPanel.update(timestamp);
		speedPanel.update(timestamp);
		courseMapPanel.update(timestamp);
		timePanel.update(timestamp);
		keyPointsPanel.update(timestamp);
		tractionCirclePanel.update(timestamp);
	}
	
	
}