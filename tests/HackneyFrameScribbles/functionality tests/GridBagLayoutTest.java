import java.awt.Button;
import java.awt.Container;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollBar;
import javax.swing.JSlider;


public class GridBagLayoutTest {

	public static void addPanes(Container contentPane)
	{
		contentPane.setLayout(new GridBagLayout());
		GridBagConstraints griddyCon = new GridBagConstraints();
		
		String buttonLabels[] = {"Main Panel","Hard Data Panel","Scroll Bar Panel","Accel Panel 1"," Accel Panel 2","Trac Panel"};
		JButton button[] = new JButton[buttonLabels.length];
		for(int i=0;i < buttonLabels.length;i++)
			button[i] = new JButton(buttonLabels[i]);
		
		//JScrolBar parameters:	Horizontal or Vertical orientation, starting value, 
		//					 	'extent' or scroll unit size (I imagine this relates to the amount scrolled, also), minimum, maximum)
		//JScrollBar testScrollBar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, 0, 1000);
		
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
		contentPane.add(button[0], griddyCon);
		
		griddyCon.fill = GridBagConstraints.BOTH;
		griddyCon.weightx = 0.25;
		griddyCon.gridwidth = 1;
		griddyCon.gridheight = 2;
		griddyCon.gridx = 2;
		griddyCon.gridy = 0;
		contentPane.add(button[1], griddyCon);
		
		griddyCon.fill = GridBagConstraints.HORIZONTAL;
		griddyCon.ipady = 0;
		griddyCon.weighty = 0.1;
		griddyCon.gridheight = 1;
		griddyCon.gridwidth = 2;
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
		
		griddyCon.weightx = 1.0/3.0;
		griddyCon.gridx = 1;
		griddyCon.gridy = 2;
		contentPane.add(button[4], griddyCon);
		
		griddyCon.weightx = 1.0/3.0;
		griddyCon.gridx = 2;
		griddyCon.gridy = 2;
		contentPane.add(button[5], griddyCon);
		
	}
	
	public static void main(String[] args) {
	    JFrame frame = new JFrame();
	    frame.setTitle("Grid Bag Test");
	    frame.setSize(1000, 810);
	    frame.addWindowListener( new WindowAdapter(){ 
	    							public void windowClosing(WindowEvent e) {
	    								System.exit(0); 
	    							} 
	    						}
	    						);
	    addPanes(frame.getContentPane());
	    frame.setVisible(true);
	}
}
