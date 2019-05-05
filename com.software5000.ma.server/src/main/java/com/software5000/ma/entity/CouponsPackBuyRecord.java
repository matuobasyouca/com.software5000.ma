package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * Created by Jiang on 2017/07/01.
 */
public class CouponsPackBuyRecord extends BaseEntity {

    public enum Daos {
        selectBuyRecordForCheck("CouponsPackBuyRecord.selectBuyRecordForCheck"),
        updateBuyRecordForInsert("CouponsPackBuyRecord.updateBuyRecordForInsert"),
        selectCooperBusiness("CouponsPackBuyRecord.selectCooperBusiness"),
        selectPageBuyRecord("CouponsPackBuyRecord.selectPageBuyRecord");
        ;

        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 商家ID
     */
    private Integer businessId;

    /**
     * 微信openId
     */
    private String wxOpenId;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 卡券礼包ID
     */
    private Integer packId;

    /**
     * 购买数量
     */
    private Integer buyCount;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 预支付ID
     */
    private String prepayId;

    /**
     * 支付时间
     */
    private Timestamp payTime;

    /**
     * 合计金额(单位是分)
     */
    private Integer tradeFee;

    public Integer getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(Integer tradeFee) {
        this.tradeFee = tradeFee;
    }

    @NotDatabaseField
    private String businessName;

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
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

    public Integer getPackId() {
        return packId;
    }

    public void setPackId(Integer packId) {
        this.packId = packId;
    }

    public Integer getBuyCount() {
        return buyCount;
    }

    public void setBuyCount(Integer buyCount) {
        this.buyCount = buyCount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }
}
