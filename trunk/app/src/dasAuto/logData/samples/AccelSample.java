package dasAuto.logData.samples;



public class AccelSample extends LogSample {
	private int xValue;	// 0-1024 (+-3Gs)
	private int yValue;	// 0-1024 (+-3Gs)
	private int zValue;	// 0-1024 (+-3Gs)

	
	/* xValue */
	public int getXValue() {
		return xValue;
	}

	public void setXValue(int xValue) {
		this.xValue = xValue;
	}
	

	/* yValue */
	public int getYValue() {
		return yValue;
	}

	public void setYValue(int yValue) {
		this.yValue = yValue;
	}
	

	/* zValue */
	public int getZValue() {
		return zValue;
	}

	public void setZValue(int zValue) {
		this.zValue = zValue;
	}

	
}
