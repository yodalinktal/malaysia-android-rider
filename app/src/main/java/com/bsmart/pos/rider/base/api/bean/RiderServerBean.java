package com.bsmart.pos.rider.base.api.bean;

/**
 * Author: yoda
 * DateTime: 2020/3/10 15:50
 */
public class RiderServerBean extends BaseServerBean {

    private RiderBean data;

    public RiderBean getData() {
        return data;
    }

    public void setData(RiderBean data) {
        this.data = data;
    }
}
