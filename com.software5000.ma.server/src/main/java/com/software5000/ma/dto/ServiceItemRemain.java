package com.software5000.ma.dto;

import com.software5000.ma.entity.ServiceItem;

/**
 * 套餐剩余服务项
 * Created by wujin on 2016/12/28.
 */
public class ServiceItemRemain{
    private ServiceItem serviceItem;
    private Integer serviceItemId;
    private Integer totalTimes;
    private Integer remainTimes;
    private String itemName;

    public ServiceItemRemain() {
    }

    public ServiceItemRemain(Integer serviceItemId, Integer totalTimes, Integer remainTimes) {
        this.serviceItemId = serviceItemId;
        this.totalTimes = totalTimes;
        this.remainTimes = remainTimes;
    }

    public ServiceItemRemain(Integer serviceItemId, Integer remainTimes) {
        this.serviceItemId = serviceItemId;
        this.remainTimes = remainTimes;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(Integer remainTimes) {
        this.remainTimes = remainTimes;
    }

    public Integer getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Integer serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
}
