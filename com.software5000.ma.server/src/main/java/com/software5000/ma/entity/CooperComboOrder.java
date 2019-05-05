package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/**
 * Created by jinbo on 2017/5/4.
 */
public class CooperComboOrder extends BaseEntity{
    public enum Daos {
        selectCooperComboDetailById("CooperComboOrder.selectCooperComboDetailById"),
        selectCooperComboOrderPageByParam("CooperComboOrder.selectCooperComboOrderPageByParam");
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
     * 用户
     */
    private Integer userId;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 电话
     */
    private String mobile;

    /**
     * 合作套餐卡
     */
    private Integer cooperComboId;

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
     * 核销金额
     */
    private Double backAmount;

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCooperComboId() {
        return cooperComboId;
    }

    public void setCooperComboId(Integer cooperComboId) {
        this.cooperComboId = cooperComboId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
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

    public Double getBackAmount() {
        return backAmount;
    }

    public void setBackAmount(Double backAmount) {
        this.backAmount = backAmount;
    }

    /**------------------非业务数据 -----------------------------**/
    /*** 用户*/
    @NotDatabaseField
    private User user;

    /*** 商家*/
    @NotDatabaseField
    private Business business;

    /**合作套餐**/
    @NotDatabaseField
    private CooperCombo cooperCombo;

    /*** 核销状态*/
    @NotDatabaseField
    private Integer confirmState;

    /**诚品合作名称**/
    @NotDatabaseField
    private String comboName;

    public String getComboName() {
        return comboName;
    }

    public void setComboName(String comboName) {
        this.comboName = comboName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public CooperCombo getCooperCombo() {
        return cooperCombo;
    }

    public void setCooperCombo(CooperCombo cooperCombo) {
        this.cooperCombo = cooperCombo;
    }

    public Integer getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(Integer confirmState) {
        this.confirmState = confirmState;
    }
}
