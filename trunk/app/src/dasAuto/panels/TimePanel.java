package dasAuto.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JSlider;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import dasAuto.MainFrame;
import dasAuto.Messages;



public class TimePanel extends DataPanel implements ActionListener {
	private static final long serialVersionUID = 5479583871728381610L;
	
	private final int DELAY_MS = 70;
	
	private JSlider timeSlider;
	private JButton playButton;
	
	private Timer timer;
	private MainFrame mainFrame;

	
	public TimePanel(MainFrame mainFrame) {
		super();
		
		this.mainFrame = mainFrame;
		
		timer = new Timer(DELAY_MS, this);
		
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.GRAY));
		
		addPlayButton();
		addSlider();
	}
	
	
	public long getTimestamp() {
		return timeSlider.getValue();
	}
	
	
	private void addPlayButton() {
		playButton = new JButton(Messages.getString("timePanel.playButton.play")); 
		playButton.setPreferredSize(new Dimension(65, getHeight()));
		playButton.addActionListener(new PlayButtonListener());
		
		add(playButton, BorderLayout.WEST);
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
	
	
	@Override
	public void actionPerformed(ActionEvent e) {
		int newValue = timeSlider.getValue() + DELAY_MS;
		
		if(newValue < timeSlider.getMaximum()) {
			timeSlider.setValue(newValue);
		} else {
			timer.stop();
			timeSlider.setValue(0);
			playButton.setText(Messages.getString("timePanel.playButton.play"));
		}
	}
	
	
	// Subclass action listeners.
	
	
	private class TimeChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			mainFrame.updatePanels(timeSlider.getValue());
		}
	}
	
	
	private class PlayButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(timer.isRunning()) {
				timer.stop();
				playButton.setText(Messages.getString("timePanel.playButton.play"));
			} else {
				timer.start();
				playButton.setText(Messages.getString("timePanel.playButton.stop"));
			}
		}
	}
	
	
}
