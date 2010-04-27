package dasAuto.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import dasAuto.logData.feeds.AccelFeed;

/**
 * Panel designed to pull the Key points of the load log file. This includes local minimums for
 * vert and lat acceleration as well as speed.
 * 
 * @author Tony Greer
 * @since 4/25/2010 7:44P
 */
public class KeyPointsPanel extends DataPanel implements Observer
{
	private static final long serialVersionUID = -3258092186335482905L;

	private static final String TOOL_TIP_TEXT = "This panel provides the global minimum and "
		+ "maximum values for elements such "
		+ "as speed, acceleration, and position.";

	/*
	 * Creates a JLabel suitable for body presentation.
	 */
	private static JLabel bodyLabelFactory(String text)
	{
		final JLabel body = new JLabel(text, JLabel.CENTER);		
		return body;
	}

	/*
	 * Creates a JLabel in the format of a header... including a 
	 * border, font, etc...
	 */
	private static JLabel headerLabelFactory(String text)
	{
		final JLabel header = KeyPointsPanel.bodyLabelFactory(text);
		header.setFont(new Font("Arial", Font.BOLD, 18));
		return header;
	}
	
	public KeyPointsPanel()
	{
		super();
		
		setToolTipText(TOOL_TIP_TEXT);
		setLayout(new GridLayout(0,3));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

		/* Headers */
		add(headerLabelFactory("Data Item"));
		add(headerLabelFactory("Min"));
		add(headerLabelFactory("Max"));

		/* Data*/
		add(bodyLabelFactory("Speed"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * gpsFeed.getMinSpeed()) / 100.0) + "mph"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * gpsFeed.getMaxSpeed()) / 100.0) + "mph"));

		add(bodyLabelFactory("Latitude"));
		add(bodyLabelFactory(String.valueOf(gpsFeed.getMinLatitude())));
		add(bodyLabelFactory(String.valueOf(gpsFeed.getMaxLatitude())));

		add(bodyLabelFactory("Longitude"));
		add(bodyLabelFactory(String.valueOf(gpsFeed.getMinLongitude())));
		add(bodyLabelFactory(String.valueOf(gpsFeed.getMaxLongitude())));
		
		AccelFeed filteredAccelFeed = accelFeed.getFilteredFeed(75);

		add(bodyLabelFactory("X Acceleration"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * filteredAccelFeed.getMinXValueInG()) / 100.0) + "g"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * filteredAccelFeed.getMaxXValueInG()) / 100.0) + "g"));

		add(bodyLabelFactory("Y Acceleration"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * filteredAccelFeed.getMinYValueInG()) / 100.0) + "g"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * filteredAccelFeed.getMaxYValueInG()) / 100.0) + "g"));

		add(bodyLabelFactory("Z Acceleration"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * filteredAccelFeed.getMinZValueInG()) / 100.0) + "g"));
		add(bodyLabelFactory(String.valueOf(Math.round(100.0 * filteredAccelFeed.getMaxZValueInG()) / 100.0) + "g"));

		/*
		 * TODO Need a scope local to the point chosen on the slider... this will put some
		 * local data in the panel as opposed to just global maxes and mins.
		 */
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		// TODO Auto-generated method stub
		
	}
}