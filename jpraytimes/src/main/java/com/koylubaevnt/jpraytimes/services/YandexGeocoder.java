package com.koylubaevnt.jpraytimes.services;

import com.koylubaevnt.jpraytimes.services.add.GeocoderHelper;

public class YandexGeocoder implements Geocoder {
	
	//private static String KEY_YANDEX_GEOCODER = "";

	private GeocoderHelper[] geocoderHelper;
	private int size = 0;
	
	public void searchGeocoding(String address) {
		size = 0;
		geocoderHelper = null;
		
		
	}

	public void searchGeocoding() {
		throw new UnsupportedOperationException();		
	}

	public int getSize() {
		return size;
	}

	public GeocoderHelper[] getGeocoderHelper() {
		return geocoderHelper;
	}

}
