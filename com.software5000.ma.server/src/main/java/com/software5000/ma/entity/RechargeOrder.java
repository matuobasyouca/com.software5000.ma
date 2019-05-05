package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/** 充值订单实体 */
public class RechargeOrder extends BaseEntity {
    public enum Daos {
        selectRechargeOrderById("RechargeOrder.selectRechargeOrderById"),
        selectPayRechargeOrderByState("RechargeOrder.selectPayRechargeOrderByState"),
        selectMaxReChargeMoneyById("RechargeOrder.selectMaxReChargeMoneyById"),
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
    * 状态
    */
    private Integer state;

    /**
     * 充值金额
     */
    private Double reChargeMoney;

    /**
     * 赠送金额
     */
    private Double grantMoney;

    /**
     * 支付方式
     */
    private Integer payType;

    /**
     *车牌号
     */
    @NotDatabaseField
    private String carNumbers;

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
    
    public Integer getState(){
      return this.state;
    }
    
    public void setState(Integer state){
      this.state = state;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Double getReChargeMoney() {
        return reChargeMoney;
    }

    public void setReChargeMoney(Double reChargeMoney) {
        this.reChargeMoney = reChargeMoney;
    }

    public Double getGrantMoney() {
        return grantMoney;
    }

    public void setGrantMoney(Double grantMoney) {
        this.grantMoney = grantMoney;
    }

    public String getCarNumbers() {
        List<String> cars=new ArrayList<String>();
        user.getCars().forEach(
                car -> cars.add(car.getCarNumber())
        );
        return String.join(",",cars);
    }

    public void setCarNumbers(String carNumbers) {
        this.carNumbers = carNumbers;
    }
}