package dasAuto;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.panels.AccelPanel;
import dasAuto.panels.KeyPointsPanel;
import dasAuto.panels.PolygonCourseMapPanel;
import dasAuto.panels.SpeedPanel;
import dasAuto.panels.TractionCirclePanel;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2269971701250845501L;
	
	
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
	}
	
	
	private void addPanels() {
		addCenterPanel();
		addSidePanel();
		addBottomPanel();
	}
	
	
	private void addCenterPanel() {
		JPanel centerPanel = new JPanel(new BorderLayout());
		centerPanel.add(new PolygonCourseMapPanel(), BorderLayout.CENTER);
		
		JSlider timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		timeSlider.setPaintTrack(false);
		timeSlider.setBackground(Color.WHITE);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(false);
		timeSlider.setMinorTickSpacing(25);
		timeSlider.setMajorTickSpacing(250);
		centerPanel.add(timeSlider, BorderLayout.SOUTH);
		
		getContentPane().add(centerPanel);
	}
	
	
	private void addSidePanel() {
		JPanel sidePanel = new JPanel(new GridLayout(2, 1));
		sidePanel.setPreferredSize(new Dimension((int)(getWidth() * 0.35), getHeight()));

		//-- AJG Key Points Panel
		sidePanel.add(new KeyPointsPanel());
		sidePanel.add(new TractionCirclePanel());
		
		getContentPane().add(sidePanel, BorderLayout.EAST);
	}
	
	
	private void addBottomPanel() {
		JPanel bottomPanel = new JPanel(new GridLayout(1, 3));
		bottomPanel.setPreferredSize(new Dimension(getWidth(), (int)(getHeight() * 0.35)));
		
		bottomPanel.add(new AccelPanel(AccelFeed.X_AXIS));
		bottomPanel.add(new AccelPanel(AccelFeed.Y_AXIS));
		bottomPanel.add(new SpeedPanel());
		
		getContentPane().add(bottomPanel, BorderLayout.SOUTH);
	}
}