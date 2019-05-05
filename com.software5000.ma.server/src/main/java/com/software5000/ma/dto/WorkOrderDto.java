package com.software5000.ma.dto;


import com.software5000.ma.entity.WorkOrderDetail;

import java.util.List;

/**
 * Created by wujin on 2017/1/16.
 */
public class WorkOrderDto {
    private Integer id;
    private Integer userId;
    private String carNumber;
    private String updateTime;
    private String mobile;//用户表手机号
    private String workOrderMobile;//工单手机号
    private String lvlName;
    private String businessName;
    private String codeName;
    private String state;
    private String totalPrice;
    private String payType;
    private List<WorkOrderDetail> workOrderDetails;
    private List<WorkOrderDetailDto> workOrderDetailDtos;

    public String getWorkOrderMobile() {
        return workOrderMobile;
    }

    public void setWorkOrderMobile(String workOrderMobile) {
        this.workOrderMobile = workOrderMobile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }

    public List<WorkOrderDetail> getWorkOrderDetails() {
        return workOrderDetails;
    }

    public void setWorkOrderDetails(List<WorkOrderDetail> workOrderDetails) {
        this.workOrderDetails = workOrderDetails;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public List<WorkOrderDetailDto> getWorkOrderDetailDtos() {
        return workOrderDetailDtos;
    }

    public void setWorkOrderDetailDtos(List<WorkOrderDetailDto> workOrderDetailDtos) {
        this.workOrderDetailDtos = workOrderDetailDtos;
    }
}

class WorkOrderDetailDto{
    int workOrderDetailId;
    int serviceItemId;
    String itemName;
    double salePrice;

    public int getWorkOrderDetailId() {
        return workOrderDetailId;
    }

    public void setWorkOrderDetailId(int workOrderDetailId) {
        this.workOrderDetailId = workOrderDetailId;
    }

    public int getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(int serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice = salePrice;
    }
}
