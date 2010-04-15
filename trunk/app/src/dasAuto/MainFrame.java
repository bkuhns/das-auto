package dasAuto;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import dasAuto.panels.CourseMapPanel;


import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;


public class MainFrame extends JFrame {
	private static final long serialVersionUID = 2269971701250845501L;
	
	
	public MainFrame() {
		initializeFrame();
		addPanels(this.getContentPane());
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
		setExtendedState(getExtendedState() | JFrame.MAXIMIZED_BOTH); // TODO: Why the hell doesn't this work in Linux? Does it work in Windows? 
		getContentPane().setLayout(new BorderLayout()); // TODO: We need to use GridBagLayout to set up all the panels, but I'm too tired for that tonight.
	}
	
	
	private void addPanels(Container contentPane) {
		CourseMapPanel courseMapPanel = new CourseMapPanel();
		//courseMapPanel.setPreferredSize(new Dimension((int)getSize().getWidth(), (int)getSize().getHeight()));
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints griddyCon = new GridBagConstraints();
		
		String buttonLabels[] = {"Main Panel","Hard Data Panel","Scroll Bar Panel","Accel Panel 1"," Accel Panel 2","Trac Panel"};
		JButton button[] = new JButton[buttonLabels.length];
		for(int i=0;i < buttonLabels.length;i++)
			button[i] = new JButton(buttonLabels[i]);
		
		JSlider testSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		testSlider.setPaintTicks(true);
		testSlider.setPaintLabels(true);
		testSlider.setMinorTickSpacing(10);
		testSlider.setMajorTickSpacing(100);
		
		griddyCon.fill = GridBagConstraints.BOTH;
		griddyCon.ipady = 500;
		griddyCon.weightx = 0.75;
		griddyCon.weighty = 0.70;
		griddyCon.gridwidth = 2;
		griddyCon.gridx = 0;
		griddyCon.gridy = 0;
		contentPane.add(courseMapPanel, griddyCon);
		
		griddyCon.weightx = 0.25;
		griddyCon.gridwidth = 1;
		griddyCon.gridheight = 2;
		griddyCon.gridx = 2;
		griddyCon.gridy = 0;
		contentPane.add(button[1], griddyCon);
		
		griddyCon.ipady = 25;
		griddyCon.weighty = 0.1;
		griddyCon.gridwidth = 2;
		griddyCon.gridheight = 1;
		griddyCon.gridx = 0;
		griddyCon.gridy = 1;
		contentPane.add(testSlider, griddyCon);
	    
		griddyCon.ipady = 200;
		griddyCon.weighty = 0.20;
		griddyCon.weightx = 1.0/3.0;
		griddyCon.gridwidth = 1;
		griddyCon.gridx = 0;
		griddyCon.gridy = 2;
		contentPane.add(button[3], griddyCon);
		
		griddyCon.gridx = 1;
		griddyCon.gridy = 2;
		contentPane.add(button[4], griddyCon);
		
		griddyCon.gridx = 2;
		griddyCon.gridy = 2;
		contentPane.add(button[5], griddyCon);
	}
	
	
}
