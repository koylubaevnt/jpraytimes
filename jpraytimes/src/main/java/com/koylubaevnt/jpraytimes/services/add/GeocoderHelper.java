package com.koylubaevnt.jpraytimes.services.add;

public class GeocoderHelper {

	private String address;
	private double latitude;
	private double longitude;
	private double elevation;
	
	public GeocoderHelper() {
		this("Unknown", 0, 0, 0);
	}
	
	public GeocoderHelper(double latitude, double longitude) {
		this("Unknown", latitude, longitude, 0);
	}
	
	public GeocoderHelper(double latitude, double longitude, double elevation) {
		this("Unknown", latitude, longitude, elevation);
	}
	
	public GeocoderHelper(String address, double latitude, double longitude) {
		this(address, latitude, longitude, 0);
	}
	
	public GeocoderHelper(String address, double latitude, double longitude, double elevation) {
		this.address = address;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getElevation() {
		return elevation;
	}

	public void setElevation(double elevation) {
		this.elevation = elevation;
	}

	@Override
	public String toString() {
		return getClass().getName() + "(" +
				"address = " + address + 
				"location[latitude, longitude,elevation] = [" + latitude + ", " + longitude + ", " + elevation + "])";
	}
	
	
	
}
