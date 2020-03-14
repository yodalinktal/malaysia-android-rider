package com.bsmart.pos.rider.base.api.bean;

/**
 * Author: yoda
 * DateTime: 2020/3/9 22:51
 */
public class BaseServerBean {

    private int errno;
    private String errmsg;

    public int getErrno() {
        return errno;
    }

    public void setErrno(int errno) {
        this.errno = errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }
}
