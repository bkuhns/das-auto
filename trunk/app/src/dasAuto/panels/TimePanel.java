package dasAuto.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSlider;

public class TimePanel extends DataPanel { 
	private static final long serialVersionUID = 5479583871728381610L;
	JSlider timeSlider;

	
	public TimePanel() {
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		
		addPlayButton();
		addSlider();
	}
	
	
	private void addPlayButton() {
		add(new JButton("Play"), BorderLayout.WEST);
	}
	
	
	private void addSlider() {
		timeSlider = new JSlider(JSlider.HORIZONTAL, 0, 1000, 0);
		timeSlider.setPaintTrack(false);
		timeSlider.setBackground(Color.WHITE);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(false);
		timeSlider.setMinorTickSpacing(25);
		timeSlider.setMajorTickSpacing(250);
		
		add(timeSlider, BorderLayout.CENTER);
	}
	
	
}
