package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * Created by jinbo on 2017/4/28.
 */

/** 诚品合作套餐 */
public class CooperCombo extends BaseEntity {
    public enum Daos {
        selectCooperComboPageByParam("CooperCombo.selectCooperComboPageByParam"),
        updateCooperComboSaleNumById("CooperCombo.updateCooperComboSaleNumById");
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 套餐名
     */
    private String comboName;
    /**
     * 套餐价格
     */
    private Double comboSalePrice;
    /**
     * 套餐佣金
     */
    private Double comboBackPrice;

    /**
     * 上架状态
     */
    private  Integer comboState;

    /**
     * 使用说明
     */
    private String instructions;

    /**
     * 商家ID
     */
    private Integer businessId;

    /**
     * 卡券模板
     */
    private String couponUUID;

    /**
     * 销售数量
     */
    private  Integer saleComboNum;

    /**
     * 有效期
     */
    private Integer validTerm;


    private  Integer creatorId;


    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public Double getComboSalePrice() {
        return comboSalePrice;
    }

    public void setComboSalePrice(Double comboSalePrice) {
        this.comboSalePrice = comboSalePrice;
    }

    public Double getComboBackPrice() {
        return comboBackPrice;
    }

    public void setComboBackPrice(Double comboBackPrice) {
        this.comboBackPrice = comboBackPrice;
    }

    public Integer getComboState() {
        return comboState;
    }

    public void setComboState(Integer comboState) {
        this.comboState = comboState;
    }

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getCouponUUID() {
        return couponUUID;
    }

    public void setCouponUUID(String couponUUID) {
        this.couponUUID = couponUUID;
    }

    public Integer getSaleComboNum() {
        return saleComboNum;
    }

    public void setSaleComboNum(Integer saleComboNum) {
        this.saleComboNum = saleComboNum;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public Integer getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Integer creatorId) {
        this.creatorId = creatorId;
    }

    /************** 其他业务数据 ***************/

    /*** 商家名称 */
    @NotDatabaseField
    private  String businessName;

    /**优惠券已销售数量*/
    @NotDatabaseField
    private Integer maShop;

    /**优惠券已销售数量*/
    @NotDatabaseField
    private Integer remaShop;

    /**优惠券每次领取*/
    @NotDatabaseField
    private Integer cpsTimes;

    /**优惠券总数量 */
    @NotDatabaseField
    private  Integer cpNums;

    /**优惠券销售状态 */
    @NotDatabaseField
    private  Integer cpSellState;

    /*** 优惠券-名字 */
    @NotDatabaseField
    private  String cpName;

    /***优惠券-套餐原价 */
    @NotDatabaseField
    private  Double cpPrice;

    /***优惠券-使用有效期*/
    @NotDatabaseField
    private   Timestamp cpTakeBeginDay;

    /***优惠券-使用有效期*/
    @NotDatabaseField
    private   Timestamp cpTakeEndDay;

    /***优惠券-创建有效期*/
    @NotDatabaseField
    private   Timestamp cpCreateTime;


    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getCpNums() {
        return cpNums;
    }

    public void setCpNums(Integer cpNums) {
        this.cpNums = cpNums;
    }

    public String getCpName() {
        return cpName;
    }

    public void setCpName(String cpName) {
        this.cpName = cpName;
    }

    public Double getCpPrice() {
        return cpPrice;
    }

    public void setCpPrice(Double cpPrice) {
        this.cpPrice = cpPrice;
    }

    public Timestamp getCpCreateTime() {
        return cpCreateTime;
    }

    public void setCpCreateTime(Timestamp cpCreateTime) {
        this.cpCreateTime = cpCreateTime;
    }

    public Timestamp getCpTakeBeginDay() {
        return cpTakeBeginDay;
    }

    public void setCpTakeBeginDay(Timestamp cpTakeBeginDay) {
        this.cpTakeBeginDay = cpTakeBeginDay;
    }

    public Timestamp getCpTakeEndDay() {
        return cpTakeEndDay;
    }

    public void setCpTakeEndDay(Timestamp cpTakeEndDay) {
        this.cpTakeEndDay = cpTakeEndDay;
    }

    public Integer getMaShop() {
        return maShop;
    }

    public void setMaShop(Integer maShop) {
        this.maShop = maShop;
    }

    public Integer getCpsTimes() {
        return cpsTimes;
    }

    public void setCpsTimes(Integer cpsTimes) {
        this.cpsTimes = cpsTimes;
    }

    public Integer getCpSellState() {
        return cpSellState;
    }

    public void setCpSellState(Integer cpSellState) {
        this.cpSellState = cpSellState;
    }

    public Integer getRemaShop() {
        return remaShop;
    }

    public void setRemaShop(Integer remaShop) {
        this.remaShop = remaShop;
    }
}
