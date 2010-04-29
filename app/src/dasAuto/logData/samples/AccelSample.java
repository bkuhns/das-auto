package dasAuto.logData.samples;



public class AccelSample extends DataSample {
	private int xValue;	// 0-1024 (+-3Gs)
	private int yValue;	// 0-1024 (+-3Gs)
	private int zValue;	// 0-1024 (+-3Gs)
	
	public static final double X_OFFSET = -0.01;
	public static final double Y_OFFSET = 0.16;
	public static final double Z_OFFSET = 0;
	
	
	/**
	 * Convert a 10-bit count integer (0-1024) to a decimal G-force value.
	 * 
	 * @param count
	 */
	public static double convertCountToG(int count, double offset) {
		return count * 5/512.0 - 5 + offset;
	}

	
	/* xValue */
	public int getXValue() {
		return xValue;
	}
	
	public double getXValueInG() {
		return convertCountToG(xValue, X_OFFSET);
	}

	public void setXValue(int xValue) {
		this.xValue = xValue;
	}
	

	/* yValue */
	public int getYValue() {
		return yValue;
	}
	
	public double getYValueInG() {
		return convertCountToG(yValue, Y_OFFSET);
	}

	public void setYValue(int yValue) {
		this.yValue = yValue;
	}
	

	/* zValue */
	public int getZValue() {
		return zValue;
	}
	
	public double getZValueInG() {
		return convertCountToG(zValue, Z_OFFSET);
	}

	public void setZValue(int zValue) {
		this.zValue = zValue;
	}

	
}
