package dasAuto.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import dasAuto.Messages;
import dasAuto.logData.feeds.AccelFeed;
import dasAuto.logData.samples.AccelSample;
import dasAuto.logData.samples.GpsSample;

/**
 * Panel designed to pull the Key points of the load log file. This includes local minimums for
 * vert and lat acceleration as well as speed.
 * 
 * @author Tony Greer
 * @since 4/25/2010 7:44P
 */
public class KeyPointsPanel extends DataPanel
{
	private static final long serialVersionUID = -3258092186335482905L;

	private static final String TOOL_TIP_TEXT = "This panel provides the global minimum and "
		+ "maximum values for elements such "
		+ "as speed, acceleration, and position.";

	private static final int ACCEL_FILTER_RESOLUTION = 75;

	/* Display formats */
	private static final DecimalFormat GPS_FMT = new DecimalFormat("0.0000");
	private static final DecimalFormat SPEED_FMT = new DecimalFormat("0.00 mph");
	private static final DecimalFormat ACCEL_FMT = new DecimalFormat("0.00 g");

	/* 
	 * References to all of the value holding JLabels
     * in the panel so updates can be made to the contained text.
	 */
	protected final JLabel speedMax, speedMin, speedCurr,
	latMax, latMin, latCurr,
	longMax, longMin, longCurr,
	xAccelMax, xAccelMin, xAccelCurr,
	yAccelMax, yAccelMin, yAccelCurr,
	zAccelMax, zAccelMin, zAccelCurr;

	public KeyPointsPanel()
	{
		super();
		final AccelFeed filteredAccelFeed = accelFeed.getFilteredFeed(ACCEL_FILTER_RESOLUTION);

		setToolTipText(TOOL_TIP_TEXT);
		setLayout(new GridLayout(0,4));
		setBackground(Color.WHITE);
		setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

		/* Headers */
		add(headerLabelFactory(Messages.getString("keyPointsPanel.dataItem")));
		add(headerLabelFactory(Messages.getString("keyPointsPanel.currItem")));
		add(headerLabelFactory(Messages.getString("keyPointsPanel.minItem")));
		add(headerLabelFactory(Messages.getString("keyPointsPanel.maxItem")));

		/* Data*/
		add(bodyLabelFactory(Messages.getString("keyPointsPanel.speed")));
		add(this.speedCurr = bodyLabelFactory(SPEED_FMT.format(gpsFeed.get(0).getSpeed())));
		add(this.speedMin = bodyLabelFactory(SPEED_FMT.format(gpsFeed.getMinSpeed())));
		add(this.speedMax = bodyLabelFactory(SPEED_FMT.format(gpsFeed.getMaxSpeed())));

		add(bodyLabelFactory(Messages.getString("keyPointsPanel.latGps")));
		add(this.latCurr = bodyLabelFactory(GPS_FMT.format(gpsFeed.get(0).getLatitude())));
		add(this.latMin = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMinLatitude())));
		add(this.latMax = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMaxLatitude())));

		add(bodyLabelFactory(Messages.getString("keyPointsPanel.lonGps")));
		add(this.longCurr = bodyLabelFactory(GPS_FMT.format(gpsFeed.get(0).getLongitude())));
		add(this.longMin = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMinLongitude())));
		add(this.longMax = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMaxLongitude())));

		add(bodyLabelFactory(Messages.getString("keyPointsPanel.lonAccel")));
		add(this.xAccelCurr = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.get(0).getXValueInG())));
		add(this.xAccelMin = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMinXValueInG())));
		add(this.xAccelMax = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMaxXValueInG())));

		add(bodyLabelFactory(Messages.getString("keyPointsPanel.latAccel")));
		add(this.yAccelCurr = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.get(0).getYValueInG())));
		add(this.yAccelMin = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMinYValueInG())));
		add(this.yAccelMax = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMaxYValueInG())));

		add(bodyLabelFactory(Messages.getString("keyPointsPanel.vertAccel")));
		add(this.zAccelCurr = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.get(0).getZValueInG())));
		add(this.zAccelMin = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMinZValueInG())));
		add(this.zAccelMax = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMaxZValueInG())));
	}

	@Override
	public void update(long timestamp)
	{
		try
		{
			/*
			 * This will redraw the the "Current" values whilst leaving
			 * the max values alone.
			 */
			if(timestamp > 0)
			{
				final AccelSample accelsample =  accelFeed.getFilteredFeed(ACCEL_FILTER_RESOLUTION).getNearestSample(timestamp);
				final GpsSample gpsSample = gpsFeed.getNearestSample(timestamp);
				
				this.speedCurr.setText(SPEED_FMT.format(gpsSample.getSpeed()));

				this.latCurr.setText(GPS_FMT.format(gpsSample.getLatitude()));
				this.longCurr.setText(GPS_FMT.format(gpsSample.getLongitude()));

				this.xAccelCurr.setText(ACCEL_FMT.format(accelsample.getXValueInG()));
				this.yAccelCurr.setText(ACCEL_FMT.format(accelsample.getYValueInG()));
				this.zAccelCurr.setText(ACCEL_FMT.format(accelsample.getZValueInG()));
			}else
			{

			}

			//-- Don't forget to force a repaint since the UI is lazy.
			super.repaint();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
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

	/*
	 * Creates a JLabel suitable for body presentation.
	 */
	private static JLabel bodyLabelFactory(String text)
	{
		final JLabel body = new JLabel(text, JLabel.RIGHT);		
		return body;
	}
}