package com.software5000.ma.dto;

import com.software5000.base.Constant;
import com.software5000.base.entity.BaseEntity;

/**
 * Created by luo on 2017/08/24.
 */
public class PackClusterOpenRecordDto extends BaseEntity {
    private String packClusterNo;
    private String clusterName;
    private String mobile;
    private String carNumber;
    private String payMoney;
    private String payTime;
    private String state;
    private String stateDesc;

    public String getPackClusterNo() {
        return packClusterNo;
    }

    public void setPackClusterNo(String packClusterNo) {
        this.packClusterNo = packClusterNo;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStateDesc() {
        return Constant.enumValues.get(Constant.PackClusterOpenRecordState.class.getSimpleName()+","+state);
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }
}
