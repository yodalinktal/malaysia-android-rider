package com.bsmart.pos.rider.base.api.bean;

/**
 * Author: yoda
 * DateTime: 2020/3/10 17:05
 */
public class GEO {

    private Double lon; //longitude(维度)
    private Double lat; //latitude(经度)

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

}
