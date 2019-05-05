package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;

/**
 * Created by jiye on 2017/7/20.
 */
public class ServiceItemExcelData extends BaseEntity {
    private String serviceItemName;
    private Double salePrice;
    private String itemType;
    private Integer sort;
    private String remark;
    private Integer businessId;
    private Integer successFlag;//是否可以导入（1为成功数据,2为失败数据）

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Integer successFlag) {
        this.successFlag = successFlag;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }
}
