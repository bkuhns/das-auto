package dasAuto.panels;

import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

import dasAuto.logData.feeds.AccelFeed;
import dasAuto.logData.samples.AccelSample;

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

		super.setToolTipText(TOOL_TIP_TEXT);
		super.setLayout(new GridLayout(0,4));
		super.setBackground(Color.WHITE);
		super.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY));

		/* Headers */
		super.add(headerLabelFactory("Data Item"));
		super.add(headerLabelFactory("Curr"));
		super.add(headerLabelFactory("Min"));
		super.add(headerLabelFactory("Max"));

		/* Data*/
		super.add(bodyLabelFactory("Speed"));
		super.add(this.speedCurr = bodyLabelFactory(SPEED_FMT.format(gpsFeed.get(0).getSpeed())));
		super.add(this.speedMin = bodyLabelFactory(SPEED_FMT.format(gpsFeed.getMinSpeed())));
		super.add(this.speedMax = bodyLabelFactory(SPEED_FMT.format(gpsFeed.getMaxSpeed())));

		super.add(bodyLabelFactory("Latitude"));
		super.add(this.latCurr = bodyLabelFactory(GPS_FMT.format(gpsFeed.get(0).getLatitude())));
		super.add(this.latMin = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMinLatitude())));
		super.add(this.latMax = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMaxLatitude())));

		super.add(bodyLabelFactory("Longitude"));
		super.add(this.longCurr = bodyLabelFactory(GPS_FMT.format(gpsFeed.get(0).getLongitude())));
		super.add(this.longMin = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMinLongitude())));
		super.add(this.longMax = bodyLabelFactory(GPS_FMT.format(gpsFeed.getMaxLongitude())));

		super.add(bodyLabelFactory("X Acceleration"));
		super.add(this.xAccelCurr = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.get(0).getXValueInG())));
		super.add(this.xAccelMin = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMinXValueInG())));
		super.add(this.xAccelMax = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMaxXValueInG())));

		super.add(bodyLabelFactory("Y Acceleration"));
		super.add(this.yAccelCurr = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.get(0).getYValueInG())));
		super.add(this.yAccelMin = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMinYValueInG())));
		super.add(this.yAccelMax = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMaxYValueInG())));

		super.add(bodyLabelFactory("Z Acceleration"));
		super.add(this.zAccelCurr = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.get(0).getZValueInG())));
		super.add(this.zAccelMin = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMinZValueInG())));
		super.add(this.zAccelMax = bodyLabelFactory(ACCEL_FMT.format(filteredAccelFeed.getMaxZValueInG())));
	}

	@Override
	public void update(Observable arg0, Object arg1)
	{
		try
		{
			/*
			 * Expecting type Long for updates from observable.
			 * This will redraw the the "Current" values whilest leaving
			 * the max values alone.
			 */
			if(arg1 instanceof Long)
			{
				final long t_millis = ((Long)arg1).longValue();

				if(t_millis > 0)
				{
					final AccelSample accelsample =  accelFeed.getFilteredFeed(ACCEL_FILTER_RESOLUTION).getNearestSample(t_millis);
					// TODO Where the F is this code! Cannot find GPS nearest()
					//final GpsSample gpsSample = gpsFeed.g
					//			this.speedCurr;
					//			this.latCurr;
					//			this.longCurr;

					this.xAccelCurr.setText(ACCEL_FMT.format(accelsample.getXValueInG()));
					this.yAccelCurr.setText(ACCEL_FMT.format(accelsample.getYValueInG()));
					this.zAccelCurr.setText(ACCEL_FMT.format(accelsample.getZValueInG()));
				}else
				{

				}

				//-- Don't forget to force a repaint since the UI is lazy.
				super.repaint();
			}else
			{
				throw new Exception("What are you sending me? I only take type Long.");
			}
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
		final JLabel body = new JLabel(text, JLabel.CENTER);		
		return body;
	}
}