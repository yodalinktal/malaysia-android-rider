package com.bsmart.pos.rider.base.api.bean;

import androidx.annotation.NonNull;

/**
 * Author: yoda
 * DateTime: 2020/3/1 22:01
 */
public class AddressBean {

    public static final String FROM = "from";
    public static final String TO = "to";

    private String zone; //GPS定位得到的数据
    private Double lat;
    private Double lon;
    private GEO loc;

    private String detail;      //这个作为地址的详细信息 address
    private String postcode;
    private String state;       // 州
    private String city;        //city

    private String telephone;
    private String name;

    private String type; // form to

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public GEO getLoc() {
        return loc;
    }

    public void setLoc(GEO loc) {
        this.loc = loc;
    }

    @NonNull
    @Override
    public String toString() {
        return "type:"+type+"zone:"+zone+",lat:"+lat+",lon:"+lon+",detail:"+detail+",telephone:"+telephone+",name:"+name+",postcode:"+postcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }
}
