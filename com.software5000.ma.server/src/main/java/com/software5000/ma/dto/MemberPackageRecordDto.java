package com.software5000.ma.dto;

import com.software5000.ma.entity.MemberPackageRecord;

import java.util.List;

/**
 *
 * Created by lsj on 2017/1/19.
 */
public class MemberPackageRecordDto extends MemberPackageRecord {
    private String businessName;
    private String packageName;
    private String isEnable;
    private List<ServiceItemRemain> serviceItemRemainList;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public List<ServiceItemRemain> getServiceItemRemainList() {
        return serviceItemRemainList;
    }

    public void setServiceItemRemainList(List<ServiceItemRemain> serviceItemRemainList) {
        this.serviceItemRemainList = serviceItemRemainList;
    }

    public String getIsEnable() {
        return isEnable;
    }

    public void setIsEnable(String isEnable) {
        this.isEnable = isEnable;
    }
}
