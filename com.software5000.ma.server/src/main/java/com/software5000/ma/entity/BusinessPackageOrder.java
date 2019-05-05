package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/** 购买套餐卡订单实体 */
public class BusinessPackageOrder extends BaseEntity {
    public enum Daos {
        selectBusinessPackageOrderById("BusinessPackageOrder.selectBusinessPackageOrderById"),
        selectUserPackageOrderDtoByOrderInfo("BusinessPackageOrder.selectUserPackageOrderDtoByOrderInfo"),
        selectPayPackageOrderByState("BusinessPackageOrder.selectPayPackageOrderByState"),
        updatePackageOrderByDate("BusinessPackageOrder.updatePackageOrderByDate"),
        selectNoPaidPackageOrderCount("BusinessPackageOrder.selectNoPaidPackageOrderCount"),
        selectCountByState("BusinessPackageOrder.selectCountByState"),
        selectInitialByOpenId("BusinessPackageOrder.selectInitialByOpenId"),
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    
    /**
    * 订单编号 
    */
    private String orderNumber;
    
    /**
    * 商家 
    */
    private Integer businessId;
    
    /**
    * 商家 
    */
    
    @NotDatabaseField
    private Business business;
    
    /**
    * 用户 
    */
    private Integer userId;

    /**
     * 用户电话
     */
    @NotDatabaseField
    private String mobile;
    
    /**
    * 用户 
    */
    
    @NotDatabaseField
    private User user;

    /**
    * 商家套餐卡
    */
    private Integer businessPackageId;

    /**
    * 商家套餐卡
    */

    @NotDatabaseField
    private BusinessPackage businessPackage;

    /**
    * 状态
    */
    private Integer state;

    /**
     * 数量
     */
    private Integer quantity;

    /**
     * 总金额
     */
    private Double totalAmount;

    /**
     * 销售人员
     */
    private Integer businessUserId;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     * 套餐名称+手机号
     */
    @NotDatabaseField
    private String businessPackageName;

    /**
     * 用户recordId
     */
    @NotDatabaseField
    private Integer recordId;

    public String getBusinessPackageName() {
        return businessPackageName;
    }

    public void setBusinessPackageName(String businessPackageName) {
        this.businessPackageName = businessPackageName;
    }

    public String getOrderNumber(){
      return this.orderNumber;
    }

    public void setOrderNumber(String orderNumber){
      this.orderNumber = orderNumber;
    }

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
    
    public Integer getState(){
      return this.state;
    }
    
    public void setState(Integer state){
      this.state = state;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getBusinessUserId() {
        return businessUserId;
    }

    public void setBusinessUserId(Integer businessUserId) {
        this.businessUserId = businessUserId;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
}