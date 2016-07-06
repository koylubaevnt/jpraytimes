package com.koylubaevnt.jpraytimes.services;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import com.koylubaevnt.jpraytimes.services.add.GeocoderHelper;
import com.koylubaevnt.jpraytimes.services.add.IpGeoBaseLocation;


public class NetworkGeocoder implements Geocoder{

	private GeocoderHelper[] geocoderHelper;
	private int size = 1;
	
	private String getIpAddress() {
		String ipAddress = "";
		try {
			ipAddress = getIpAddressViaCheckipOrg();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (ipAddress.isEmpty()) {
			try {
				ipAddress = getIpAddressViaWhatismyipOrg();
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (ipAddress.isEmpty()) {
				try {
					ipAddress = getIpAddresViaAmazonawsCom();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return ipAddress;
	}
	
	
	private String getIpAddressViaCheckipOrg() throws Exception {
		String ipAddress = "";
		Pattern pattern = Pattern.compile(".*>(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})<.*");
		Matcher matcher;
        
		URL url = new URL("http://checkip.org/");
		BufferedReader bufferReader = null;
        try {
        	String lineBufferReader;
            bufferReader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((lineBufferReader = bufferReader.readLine()) != null) {
    			matcher = pattern.matcher(lineBufferReader);
    			if (matcher.matches()) {
    				ipAddress = matcher.group(1);
    				break;
    			}
    		}
            return ipAddress;
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
		
	private String getIpAddressViaWhatismyipOrg() throws Exception {
		String ipAddress = "";
		Pattern pattern = Pattern.compile(".*>(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3})<.*");
		Matcher matcher;
        
		URL url = new URL("http://www.whatismyip.org/");
		BufferedReader bufferReader = null;
        try {
        	String lineBufferReader;
            bufferReader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((lineBufferReader = bufferReader.readLine()) != null) {
    			matcher = pattern.matcher(lineBufferReader);
    			if (matcher.matches()) {
    				ipAddress = matcher.group(1);
    				break;
    			}
    		}
            return ipAddress;
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
	}
	
	private String getIpAddresViaAmazonawsCom() throws Exception {
		String ipAddress;
		URL url = new URL("http://checkip.amazonaws.com");
        BufferedReader bufferReader = null;
        try {
            bufferReader = new BufferedReader(new InputStreamReader(url.openStream()));
            ipAddress = bufferReader.readLine();
            return ipAddress;
        } finally {
            if (bufferReader != null) {
                try {
                    bufferReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
	
	
	private static String makeGetRequest(String URL) throws IOException {
	    URL url = new URL(URL);
	    URLConnection connection = url.openConnection();
	    connection.setDoOutput(true);
	    StringBuffer response = new StringBuffer();
	    BufferedReader reader = new BufferedReader(new InputStreamReader(
	            connection.getInputStream(), "windows-1251"));
	    String line;
	    while ((line = reader.readLine()) != null) {
	        response.append(line);
	    }
	    reader.close();
	    return response.toString();
	}

	private GeocoderHelper getGeocoderHelper(String ip) {
		StringBuilder sb = new StringBuilder();
		GeocoderHelper gh = new GeocoderHelper();
		try {
	        JAXBContext jaxbContext = JAXBContext.newInstance(IpGeoBaseLocation.class);
	        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
	        IpGeoBaseLocation ipGeoBaseLocation = (IpGeoBaseLocation) jaxbUnmarshaller.unmarshal(
	        		new ByteArrayInputStream(makeGetRequest(
	                        "http://ipgeobase.ru:7020/geo?ip=" + ip).getBytes()));
	        
	        if (ipGeoBaseLocation.getIp().getCity() != null) {
	            sb.append(ipGeoBaseLocation.getIp().getCity());
	        }
	        if (ipGeoBaseLocation.getIp().getDistrict() != null) {
	        	sb.append(ipGeoBaseLocation.getIp().getCountry());
	        }
	        if (ipGeoBaseLocation.getIp().getRegion() != null) {
	        	sb.append(ipGeoBaseLocation.getIp().getRegion());
	        }
	        gh.setAddress(sb.toString());
	        if (ipGeoBaseLocation.getIp().getLat() != 0) {
	        	gh.setLatitude(ipGeoBaseLocation.getIp().getLat());
	        }
	        if (ipGeoBaseLocation.getIp().getLng() != 0) {
	        	gh.setLongitude(ipGeoBaseLocation.getIp().getLng());
	        }
	        return gh;
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return gh;
	}
	
	public void searchGeocoding(String address) {
		throw new UnsupportedOperationException();
	}

	public void searchGeocoding() {
		size = 1;
		geocoderHelper = new GeocoderHelper[size];
		geocoderHelper[0] = getGeocoderHelper(getIpAddress());
		
	}

	public int getSize() {
		return size;
	}

	public GeocoderHelper[] getGeocoderHelper() {
		return geocoderHelper;
	}
}
