package com.software5000.bank.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/**
 * 支付订单
 * Created by jiye on 2017/7/1.
 */
public class PayOrder extends BaseEntity{
    /**
     * 订单标题
     */
    private String orderTitle;
    /**
     * 总金额(单位为分)
     */
    private Integer totalPrice;

    /**
     * 服务信息
     */
    private String orderService;
    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 明细json串
     */
    private String orderDetail;

    /**
     * 说明
     */
    private String orderContent;

    /**
     * 付款成功后跳转页面
     */
    private String redirectUrl;

    /**
     * 回调地址,支付完成后，回调各个业务处理订单数据
     */
    private String notifyUrl;

    /**
     * 付款之前验证下是否可以支付，如果值为空则不验证
     */
    private String payCheckUrl;

    /**
     * 付款状态
     */
    private Integer payState;

    /**
     * 预支付订单号
     */
    private String prepayId;

    /**
     * 各平台订单号(目前是微信订单号)
     */
    private String transactionId;

    /**
     * 微信openId
     */
    private String wxOpenId;

    /**
     * 商家id
     */
    private Integer businessId;

    /**
     * 微信code
     */
    @NotDatabaseField
    private String code;


    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getPayCheckUrl() {
        return payCheckUrl;
    }

    public void setPayCheckUrl(String payCheckUrl) {
        this.payCheckUrl = payCheckUrl;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getOrderDetail() {
        return orderDetail;
    }

    public void setOrderDetail(String orderDetail) {
        this.orderDetail = orderDetail;
    }

    public String getOrderTitle() {
        return orderTitle;
    }

    public void setOrderTitle(String orderTitle) {
        this.orderTitle = orderTitle;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public String getNotifyUrl() {
        return notifyUrl;
    }

    public void setNotifyUrl(String notifyUrl) {
        this.notifyUrl = notifyUrl;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getOrderService() {
        return orderService;
    }

    public void setOrderService(String orderService) {
        this.orderService = orderService;
    }

    public String getOrderContent() {
        return orderContent;
    }

    public void setOrderContent(String orderContent) {
        this.orderContent = orderContent;
    }
}
