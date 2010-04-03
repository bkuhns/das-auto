
public class GpsSample extends LogSample {
	
	private double  latitude, longitude, velocity, course;
	private char directionNS, directionEW;
	
	public double getLatitude() {
		return latitude;
	}
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	public double getLongitude() {
		return longitude;
	}
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	public double getVelocity() {
		return velocity;
	}
	public void setVelocity(double velocity) {
		this.velocity = velocity;
	}
	public double getCourse() {
		return course;
	}
	public void setCourse(double course) {
		this.course = course;
	}
	public char getDirectionNS() {
		return directionNS;
	}
	public void setDirectionNS(char directionNS) {
		this.directionNS = directionNS;
	}
	public char getDirestionEW() {
		return directionEW;
	}
	public void setDirestionEW(char direstionEW) {
		this.directionEW = direstionEW;
	}
	
		
}
