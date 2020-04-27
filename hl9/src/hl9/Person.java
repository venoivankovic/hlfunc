package hl9;

import java.time.LocalDateTime;

public class Person {

	double latitude;
	double longitude;
	LocalDateTime currentTime;

	public Person() {

	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longditude) {
		this.longitude = longditude;
	}

}
