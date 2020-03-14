package com.bsmart.pos.rider.base.api.bean;

import android.graphics.Bitmap;

/**
 * Author: yoda
 * DateTime: 2020/3/5 15:23
 */
public class OrderBean {

    private String _id;
    private String orderNo;

    private String customerId;
    private String riderId;

    private AddressBean from;
    private Double[] coordinates;
    private AddressBean to;
    private Integer amount;
    private Integer sizeWeight;
    private String pickupTime;
    private Integer payType;
    private Integer postType;
    private Bitmap qrCode;
    private String status;
    private String createDate;
    private Long createdTimestamp;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        this.riderId = riderId;
    }

    public Double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Double[] coordinates) {
        this.coordinates = coordinates;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public Long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(Long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Bitmap getQrCode() {
        return qrCode;
    }

    public void setQrCode(Bitmap qrCode) {
        this.qrCode = qrCode;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getPostType() {
        return postType;
    }

    public void setPostType(Integer postType) {
        this.postType = postType;
    }

    public AddressBean getFrom() {
        return from;
    }

    public void setFrom(AddressBean from) {
        this.from = from;
    }

    public AddressBean getTo() {
        return to;
    }

    public void setTo(AddressBean to) {
        this.to = to;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getSizeWeight() {
        return sizeWeight;
    }

    public void setSizeWeight(Integer sizeWeight) {
        this.sizeWeight = sizeWeight;
    }

    public String getPickupTime() {
        return pickupTime;
    }

    public void setPickupTime(String pickupTime) {
        this.pickupTime = pickupTime;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
