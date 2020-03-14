package com.bsmart.pos.rider.base.api.bean;

/**
 * Author: yoda
 * DateTime: 2020/3/9 21:57
 */
public class RiderBean extends DBBean{

    private String username;
    private String token;
    private Long expired_in;
    private Long createdTimestamp;
    private Integer status; // from RiderStatusEnum
    private GEO loc; // latest position
    private Double[] coordinates; //坐标的数组

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getExpired_in() {
        return expired_in;
    }

    public void setExpired_in(Long expired_in) {
        this.expired_in = expired_in;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public GEO getLoc() {
        return loc;
    }

    public void setLoc(GEO loc) {
        this.loc = loc;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }
}
