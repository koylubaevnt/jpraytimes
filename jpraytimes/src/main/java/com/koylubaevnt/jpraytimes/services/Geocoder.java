package com.koylubaevnt.jpraytimes.services;

import com.koylubaevnt.jpraytimes.services.add.GeocoderHelper;

public interface Geocoder {

	public void searchGeocoding(String address);
	
	public void searchGeocoding();
	
	public int getSize();
	
	public GeocoderHelper[] getGeocoderHelper();
	
}
