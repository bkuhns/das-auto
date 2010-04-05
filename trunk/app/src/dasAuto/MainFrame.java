package dasAuto;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import dasAuto.panels.CourseMapPanel;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2269971701250845501L;
	
	
	public MainFrame() {
		initializeFrame();
		
		addCourseMapPanel();
		/*addDummyPanel();
		addDummyPanel();
		addDummyPanel();
		addDummyPanel();*/
		
		pack();
	}
	
	
	private void addDummyPanel() {
		JPanel panel = new JPanel();
		panel.setLayout(new BorderLayout());
		JButton button = new JButton("Test");
		panel.add(button);
		add(panel);
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
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH); // TODO: Why the hell doesn't this work in Linux? Does it work in Windows? 
		getContentPane().setLayout(new BorderLayout()); // TODO: We need to use GridBagLayout to set up all the panels, but I'm too tired for that tonight.
	}
	
	
	private void addCourseMapPanel() {
		CourseMapPanel courseMapPanel = new CourseMapPanel();
		courseMapPanel.setPreferredSize(new Dimension((int)getSize().getWidth(), (int)getSize().getHeight()));
	    
		/*GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.gridx = 0;
		c.gridy = 0;*/
	    getContentPane().add(courseMapPanel);
	}
	
	
}
