package com.software5000.ma.dto;

import com.software5000.ma.entity.WorkOrderImg;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by wujin on 2017/1/18.
 */
public class UserWorkOrderDto {
    private Integer workOrderId;
    private String orderNo;
    private String carNumber;
    private Integer state;
    private Timestamp createTime;
    private Double totalPrice;
    private Double businessDeduct;
    private String businessName;
    private Integer businessId;
    private Double discountNumber;
    private Double materialCost;
    private Double couponDeduct;
    private Double discountDeduct;
    private Double discountNum;
    private String updateTime;
    private Integer payType;
    private List<ServiceItemDto> serviceItems;
    private List<WorkOrderImg> workOrderImgs;

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getBusinessDeduct() {
        return businessDeduct;
    }

    public void setBusinessDeduct(Double businessDeduct) {
        this.businessDeduct = businessDeduct;
    }

    public Double getDiscountNumber() {
        return discountNumber;
    }

    public void setDiscountNumber(Double discountNumber) {
        this.discountNumber = discountNumber;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public List<ServiceItemDto> getServiceItems() {
        return serviceItems;
    }

    public void setServiceItems(List<ServiceItemDto> serviceItems) {
        this.serviceItems = serviceItems;
    }

    public List<WorkOrderImg> getWorkOrderImgs() {
        return workOrderImgs;
    }

    public void setWorkOrderImgs(List<WorkOrderImg> workOrderImgs) {
        this.workOrderImgs = workOrderImgs;
    }

    public Double getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(Double materialCost) {
        this.materialCost = materialCost;
    }

    public Double getCouponDeduct() {
        return couponDeduct;
    }

    public void setCouponDeduct(Double couponDeduct) {
        this.couponDeduct = couponDeduct;
    }

    public Double getDiscountDeduct() {
        return discountDeduct;
    }

    public void setDiscountDeduct(Double discountDeduct) {
        this.discountDeduct = discountDeduct;
    }

    public Double getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(Double discountNum) {
        this.discountNum = discountNum;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }
}
