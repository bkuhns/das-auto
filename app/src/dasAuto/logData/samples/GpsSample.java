package dasAuto.logData.samples;



public class GpsSample extends DataSample {
	private double latitude;	// dddmm.mmmm
	private double longitude;	// dddmm.mmmm
	private double speed;	// 000.0-999.9 (knots)
	private double heading;		// 000.0-359.9 (degrees)
	
	private char nsIndicator;	// N/S
	private char ewIndicator;	// E/W
	
	
	public boolean equals(GpsSample sample) {
		
		return (sample.getLatitude() != latitude || 
		   sample.getLongitude() != longitude ||
		   sample.getSpeed() != speed ||
		   sample.getHeading() != heading ||
		   sample.getNsIndicator() != nsIndicator ||
		   sample.getEwIndicator() != ewIndicator);
	}
	
	/* Latitude */
	public double getLatitude() {
		return latitude;
	}
	
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	
	/* Longitude */
	public double getLongitude() {
		return longitude;
	}
	
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	
	/* Velocity */
	public double getSpeed() {
		return speed;
	}
	
	public void setSpeed(double velocity) {
		this.speed = velocity;
	}
	
	
	/* Heading */
	public double getHeading() {
		return heading;
	}
	
	public void setHeading(double course) {
		this.heading = course;
	}
	
	
	/* nsIndicator */
	public char getNsIndicator() {
		return nsIndicator;
	}
	
	public void setNsIndicator(char directionNS) {
		this.nsIndicator = directionNS;
	}
	
	
	/* ewIndicator */
	public char getEwIndicator() {
		return ewIndicator;
	}
	
	public void setEwIndicator(char direstionEW) {
		this.ewIndicator = direstionEW;
	}
	
		
}
