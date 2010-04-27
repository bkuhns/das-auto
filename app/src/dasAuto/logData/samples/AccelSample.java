package dasAuto.logData.samples;



public class AccelSample extends DataSample {
	private int xValue;	// 0-1024 (+-3Gs)
	private int yValue;	// 0-1024 (+-3Gs)
	private int zValue;	// 0-1024 (+-3Gs)
	
	
	/**
	 * Convert a 10-bit count integer (0-1024) to a decimal G-force value.
	 * 
	 * @param count
	 */
	public static double convertCountToG(int count) {
		return count * 3.0/512.0 - 3.0;
	}

	
	/* xValue */
	public int getXValue() {
		return xValue;
	}
	
	public double getXValueInG() {
		return convertCountToG(xValue);
	}

	public void setXValue(int xValue) {
		this.xValue = xValue;
	}
	

	/* yValue */
	public int getYValue() {
		return yValue;
	}
	
	public double getYValueInG() {
		return convertCountToG(yValue);
	}

	public void setYValue(int yValue) {
		this.yValue = yValue;
	}
	

	/* zValue */
	public int getZValue() {
		return zValue;
	}
	
	public double getZValueInG() {
		return convertCountToG(zValue);
	}

	public void setZValue(int zValue) {
		this.zValue = zValue;
	}

	
}
