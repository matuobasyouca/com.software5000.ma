package com.software5000.ma.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.sql.Timestamp;

/** 用户套餐 */
public class MemberPackageRecord extends BaseEntity {
    public enum Daos {
        /**
         * 统计套餐被购买的数量
         */
        countPackageHaveBuyNum("MemberPackageRecord.countPackageHaveBuyNum"),
        /**
         * 根据用户id及商家id查询套餐情况
         */
        selectByParam("MemberPackageRecord.selectByParam"),
        selectMemberPackageRecordByRecordInfo("MemberPackageRecord.selectMemberPackageRecordByRecordInfo"),
        selectMemberPackageRecordByOpenId("MemberPackageRecord.selectMemberPackageRecordByOpenId"),
        selectMemberPackageRecordIds("MemberPackageRecord.selectMemberPackageRecordIds"),
        selectMemberPackageRecordCount("MemberPackageRecord.selectMemberPackageRecordCount"),

        /**
         * 获取工单使用套餐卡微信消息信息
         */
        selectWorkOrderPackageMsg("MemberPackageRecord.selectWorkOrderPackageMsg"),
        /**
         * 获取即将到期的套餐为微信消息信息
         */
        selectExpirePackageMsg("MemberPackageRecord.selectExpirePackageMsg"),
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    
    /**
    * 联盟商家 
    */
    private Integer businessId;
    
    /**
    * 联盟商家 
    */
    
    @NotDatabaseField
    private Business business;
    
    /**
    * 用户 
    */
    private Integer userId;
    
    /**
    * 用户 
    */
    
    @NotDatabaseField
    private User user;

    /**
    * 套餐
    */
    private Integer businessPackageId;

    /**
    * 套餐
    */
    @NotDatabaseField
    private BusinessPackage businessPackage;

    /**
     * 订单编号
     */
    private Integer packageOrderId;

    /**
     * 订单
     */
    @NotDatabaseField
    private BusinessPackageOrder businessPackageOrder;

    /**
     * 过期时间
     */
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Timestamp validTime;

    /**
    * 套餐使用集
    */
    @NotDatabaseField
    private java.util.List<MemberItemUseRecord> memberItemUseRecords;

    /**
     * 支付方式
     */
    @NotDatabaseField
    private Integer payType;

    /**
     * 总金额
     */
    @NotDatabaseField
    private Double totalAmount;

    public Integer getBusinessId(){
      return this.businessId;
    }

    public void setBusinessId(Integer businessId){
      this.businessId = businessId;
    }


    public Business getBusiness(){
      return this.business;
    }

    public void setBusiness(Business business){
      this.business = business;
    }

    public Integer getUserId(){
      return this.userId;
    }

    public void setUserId(Integer userId){
      this.userId = userId;
    }


    public User getUser(){
      return this.user;
    }

    public void setUser(User user){
      this.user = user;
    }
    
    public Integer getBusinessPackageId(){
      return this.businessPackageId;
    }
    
    public void setBusinessPackageId(Integer businessPackageId){
      this.businessPackageId = businessPackageId;
    }
    
    
    public BusinessPackage getBusinessPackage(){
      return this.businessPackage;
    }
    
    public void setBusinessPackage(BusinessPackage businessPackage){
      this.businessPackage = businessPackage;
    }
    
    public java.util.List<MemberItemUseRecord> getMemberItemUseRecords(){
      return this.memberItemUseRecords;
    }
    
    public void setMemberItemUseRecords(java.util.List<MemberItemUseRecord> memberItemUseRecords){
      this.memberItemUseRecords = memberItemUseRecords;
    }

    public Timestamp getValidTime() {
        return validTime;
    }

    public void setValidTime(Timestamp validTime) {
        this.validTime = validTime;
    }

    public Integer getPackageOrderId() {
        return packageOrderId;
    }

    public void setPackageOrderId(Integer packageOrderId) {
        this.packageOrderId = packageOrderId;
    }

    public BusinessPackageOrder getBusinessPackageOrder() {
        return businessPackageOrder;
    }

    public void setBusinessPackageOrder(BusinessPackageOrder businessPackageOrder) {
        this.businessPackageOrder = businessPackageOrder;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }
}