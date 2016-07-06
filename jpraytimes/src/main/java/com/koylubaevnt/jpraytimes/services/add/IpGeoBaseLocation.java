package com.koylubaevnt.jpraytimes.services.add;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ip-answer")
public class IpGeoBaseLocation {
	
	private IpGeoBaseIP ip;
	 
    public IpGeoBaseIP getIp() {
        return ip;
    }
 
    @XmlElement
    public void setIp(IpGeoBaseIP ip) {
        this.ip = ip;
    }
 
    public static class IpGeoBaseIP {
        private String inetnum;
        private String country;
        private String city;
        private String region;
        private String district;
        private double lat;
        private double lng;
 
        public String getInetnum() {
            return inetnum;
        }
 
        @XmlElement
        public void setInetnum(String inetnum) {
            this.inetnum = inetnum;
        }
 
        public String getCountry() {
            return country;
        }
 
        @XmlElement
        public void setCountry(String country) {
            this.country = country;
        }
 
        public String getCity() {
            return city;
        }
 
        @XmlElement
        public void setCity(String city) {
            this.city = city;
        }
 
        public String getRegion() {
            return region;
        }
 
        @XmlElement
        public void setRegion(String region) {
            this.region = region;
        }
 
        public String getDistrict() {
            return district;
        }
 
        @XmlElement
        public void setDistrict(String district) {
            this.district = district;
        }
 
        public double getLat() {
            return lat;
        }
 
        @XmlElement
        public void setLat(double lat) {
            this.lat = lat;
        }
 
        public double getLng() {
            return lng;
        }
 
        @XmlElement
        public void setLng(double lng) {
            this.lng = lng;
        } 
 
    }
}