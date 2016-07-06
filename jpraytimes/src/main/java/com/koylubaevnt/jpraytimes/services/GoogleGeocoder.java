package com.koylubaevnt.jpraytimes.services;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.koylubaevnt.jpraytimes.services.add.GeocoderHelper;

public class GoogleGeocoder implements Geocoder{

	private static String KEY_GOOGLE_GEOCODER = "AIzaSyCKScvDi5K-3Qt3x9Y2-SqBJsHJZTlkkdA";

	private GeocoderHelper[] geocoderHelper;
	private int size = 0;
	
	public void searchGeocoding() {
		throw new UnsupportedOperationException();
	}
	
	public void searchGeocoding(String address) {
		GeoApiContext context = new GeoApiContext().setApiKey(KEY_GOOGLE_GEOCODER);
		GeocodingResult[] results;
		geocoderHelper = null;
		size = 0;
		try {
			results = GeocodingApi.geocode(context, address).await();
			size = results.length;
			geocoderHelper = new GeocoderHelper[size];
			for (int i = 0; i < results.length; i++) {
				geocoderHelper[i] = new GeocoderHelper(results[i].formattedAddress, results[i].geometry.location.lat, results[i].geometry.location.lng);
			}
		} catch (Exception e) {
			e.printStackTrace();
			geocoderHelper = new GeocoderHelper[1];
			geocoderHelper[0] = new GeocoderHelper();
		}
	}

	public GeocoderHelper[] getGeocoderHelper() {
		return geocoderHelper;
	}

	public int getSize() {
		return size;
	}
	
	
	/*
	public static void main(String[] args) {
		for (GeocoderHelper geocoderHelper : getGeocoding("Пермь")) {
			System.out.println(geocoderHelper.toString());
		}
		
		
	}*/
}
