package dasAuto.panels;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dasAuto.MainFrame;



public class TimePanel extends DataPanel {
	private static final long serialVersionUID = 5479583871728381610L;
	
	private JSlider timeSlider;
	private MainFrame mainFrame;

	
	public TimePanel(MainFrame mainFrame) {
		super();
		
		this.mainFrame = mainFrame;
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		
		addPlayButton();
		addSlider();
	}
	
	
	public long getTimestamp() {
		return timeSlider.getValue();
	}
	
	
	private void addPlayButton() {
		add(new JButton("Play"), BorderLayout.WEST);
	}
	
	
	private void addSlider() {
		int maxTimestamp = (int)accelFeed.getMaxTimestamp();
		
		timeSlider = new JSlider(JSlider.HORIZONTAL, 0, maxTimestamp, 0);
		timeSlider.setPaintTrack(false);
		timeSlider.setBackground(Color.WHITE);
		timeSlider.setPaintTicks(true);
		timeSlider.setPaintLabels(false);
		timeSlider.setMinorTickSpacing((int)(maxTimestamp / 40.0));
		timeSlider.setMajorTickSpacing((int)(maxTimestamp / 4.0));
		
		timeSlider.addChangeListener(new TimeChangeListener());
		
		add(timeSlider, BorderLayout.CENTER);
	}
	
	
	private class TimeChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			mainFrame.updatePanels(timeSlider.getValue());
		}
	}
	
	
}
