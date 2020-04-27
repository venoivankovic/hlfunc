package hl9;

public class Stop {

	String stop_id;
	String stop_code;
	String stop_name;
	String stop_lat;
	String stop_lon;
	String stop_metadata;
	double distance;
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public double getDistance() {
		return distance;
	}

	public String getStop_metadata() {
		return stop_metadata;
	}

	public void setStop_metadata(String stop_metadata) {
		this.stop_metadata = stop_metadata;
	}
	
	public void appendStop_metadata(String append) {
		this.stop_metadata = stop_metadata+=append;
	}

	public String getStop_id() {
		return stop_id;
	}

	public void setStop_id(String stop_id) {
		this.stop_id = stop_id;
	}

	public String getStop_code() {
		return stop_code;
	}

	public void setStop_code(String stop_code) {
		this.stop_code = stop_code;
	}

	public String getStop_name() {
		return stop_name;
	}

	public void setStop_name(String stop_name) {
		this.stop_name = stop_name;
	}

	public String getStop_lat() {
		return stop_lat;
	}

	public void setStop_lat(String stop_lat) {
		this.stop_lat = stop_lat;
	}

	public String getStop_lon() {
		return stop_lon;
	}
	
	public double get_Longitude() {
		return Double.parseDouble(stop_lon);
	}
	
	public double get_Latitude() {
		return Double.parseDouble(stop_lat);
	}

	public void setStop_lon(String stop_lon) {
		this.stop_lon = stop_lon;
	}
}