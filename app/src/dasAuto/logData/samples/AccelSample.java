package dasAuto.logData.samples;



public class AccelSample extends LogSample {
	private int xValue;	// 0-1024 (+-3Gs)
	private int yValue;	// 0-1024 (+-3Gs)
	private int zValue;	// 0-1024 (+-3Gs)

	
	/* xValue */
	public int getxValue() {
		return xValue;
	}

	public void setxValue(int xValue) {
		this.xValue = xValue;
	}
	

	/* yValue */
	public int getyValue() {
		return yValue;
	}

	public void setyValue(int yValue) {
		this.yValue = yValue;
	}
	

	/* zValue */
	public int getzValue() {
		return zValue;
	}

	public void setzValue(int zValue) {
		this.zValue = zValue;
	}

	
}
