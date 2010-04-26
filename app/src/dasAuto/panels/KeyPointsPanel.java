package dasAuto.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

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
		super.setToolTipText(TOOL_TIP_TEXT);
		super.setLayout(new GridLayout(0,3));
		super.setBackground(Color.WHITE);
		super.setBorder(BorderFactory.createLineBorder(Color.black, 1));

		/* Headers */
		super.add(headerLabelFactory("Data Item"));
		super.add(headerLabelFactory("Max"));
		super.add(headerLabelFactory("Min"));

		/* Data*/
		super.add(bodyLabelFactory("Speed"));
		super.add(bodyLabelFactory(String.valueOf(super.gpsFeed.getMaxSpeed())));
		super.add(bodyLabelFactory(String.valueOf(super.gpsFeed.getMinSpeed())));

		super.add(bodyLabelFactory("Latitude"));
		super.add(bodyLabelFactory(String.valueOf(super.gpsFeed.getMaxLatitude())));
		super.add(bodyLabelFactory(String.valueOf(super.gpsFeed.getMinLatitude())));

		super.add(bodyLabelFactory("Longitude"));
		super.add(bodyLabelFactory(String.valueOf(super.gpsFeed.getMaxLongitude())));
		super.add(bodyLabelFactory(String.valueOf(super.gpsFeed.getMinLongitude())));

		super.add(bodyLabelFactory("X Acceleration"));
		super.add(bodyLabelFactory(String.valueOf(super.accelFeed.getMaxXValue())));
		super.add(bodyLabelFactory(String.valueOf(super.accelFeed.getMinXValue())));

		super.add(bodyLabelFactory("Y Acceleration"));
		super.add(bodyLabelFactory(String.valueOf(super.accelFeed.getMaxYValue())));
		super.add(bodyLabelFactory(String.valueOf(super.accelFeed.getMinYValue())));

		super.add(bodyLabelFactory("Z Acceleration"));
		super.add(bodyLabelFactory(String.valueOf(super.accelFeed.getMaxZValue())));
		super.add(bodyLabelFactory(String.valueOf(super.accelFeed.getMinZValue())));

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