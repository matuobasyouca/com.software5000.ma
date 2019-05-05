package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;

/**
 * 抽奖奖品
 * Created by jiye on 2017/8/7.
 */
public class LotteryAward extends BaseEntity {
    /**
     * 奖品名称
     */
    private String rewordName;

    /**
     * 支付订单的id
     */
    private Integer payOrderId;

    /**
     * 商家id
     */
    private Integer businessId;

    /**
     * 微信id
     */
    private String wxOpenId;

    private String carNumber;

    private String mobile;

    /**
     * 是否已经领奖
     */
    private Integer state;

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getRewordName() {
        return rewordName;
    }

    public void setRewordName(String rewordName) {
        this.rewordName = rewordName;
    }

    public Integer getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(Integer payOrderId) {
        this.payOrderId = payOrderId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

}
