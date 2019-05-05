package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.sql.Timestamp;

/**
 * 微信支付订单实体
 * Created by lsj on 2017/1/10.
 */
public class WeChatPayOrder extends BaseEntity {
    public enum Daos {
        selectWeChatPayOrderByIds("WeChatPayOrder.selectWeChatPayOrderByIds"),
        updateStateToSettlement("WeChatPayOrder.updateStateToSettlement"),
        selectNoCheckMoneyWechatOrderDto("WeChatPayOrder.selectNoCheckMoneyWechatOrderDto"),
        selectBusinessCheckMoney("WeChatPayOrder.selectBusinessCheckMoney"),
        selectPageWechatPayOrder("WeChatPayOrder.selectPageWechatPayOrder"),
        selectPaymentPage("WeChatPayOrder.selectPaymentPage"),
        selecPaymentBusiness("WeChatPayOrder.selecPaymentBusiness"),
        selectTotalSum("WeChatPayOrder.selectTotalSum"),
        ;

        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 1代表未付款，2代表以付款
     */
    private String status;
    /**
     * 时间戳
     */
    @NotDatabaseField
    private Long timeStamp;
    /**
     * 下定金额
     */
    private Integer tradeFee;

    /**
     * 核销金额
     */
    private Integer checkMoneyFee;

    /**
     * 实时金额（记录当前微信付款记录时，该商家账号可以提现的金额）
     */
    private Integer realTimeMoneyFee;

    /**
     * 订单号
     */
    private String tradeNo;
    /**
     * 用户微信openid
     */
    private String openId;
    /**
     * wechat appid
     */
    @NotDatabaseField
    private String appId;

    @NotDatabaseField
    private String nonceStr;

    private String prepayId;

    @NotDatabaseField
    private String paySign;

    @NotDatabaseField
    private String packagevar;
    /**
     * 退款单号
     */
    private String refundNo;
    /**
     * 退款金额
     */
    private String refundFee;
    /**
     * 套餐订单id
     */
    private Integer packageOrderId;
    /**
     * 工单订单
     */
    private Integer workOrderId;

    /**
     * 商家id
     */
    private Integer businessId;

    /**
     * 确认状态(0表示未纳入对账单，1表示订单已经纳入对账单) 默认状态是0
     */
    private Integer confirmState;

    /**
     * 0表示工单，1表示套餐，2表示核销结算的订单,3表示优惠券对账单,4核销优惠券对账单
     */
    private Integer orderType;

    /**
     * 所属对账单id
     */
    private Integer checkMoneyOrderId;

    /**
     * 展示详情名称
     */
    private String showName;

    /**
     * 交易时间
     */
    private Timestamp tradingTime;

    /**
     * 套餐订单
     */
    @NotDatabaseField
    private BusinessPackageOrder businessPackageOrder;

    @NotDatabaseField
    private String errorcode;

    @NotDatabaseField
    private String errormsg;


    public Integer getRealTimeMoneyFee() {
        return realTimeMoneyFee;
    }

    public void setRealTimeMoneyFee(Integer realTimeMoneyFee) {
        this.realTimeMoneyFee = realTimeMoneyFee;
    }

    public String getShowName() {
        return showName;
    }

    public Timestamp getTradingTime() {
        return tradingTime;
    }

    public void setTradingTime(Timestamp tradingTime) {
        this.tradingTime = tradingTime;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public Integer getCheckMoneyFee() {
        return checkMoneyFee;
    }

    public void setCheckMoneyFee(Integer checkMoneyFee) {
        this.checkMoneyFee = checkMoneyFee;
    }

    public Integer getConfirmState() {
        return confirmState;
    }

    public void setConfirmState(Integer confirmState) {
        this.confirmState = confirmState;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Integer getTradeFee() {
        return tradeFee;
    }

    public void setTradeFee(Integer tradeFee) {
        this.tradeFee = tradeFee;
    }

    public String getTradeNo() {
        return tradeNo;
    }

    public void setTradeNo(String tradeNo) {
        this.tradeNo = tradeNo;
    }

    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPrepayId() {
        return prepayId;
    }

    public void setPrepayId(String prepayId) {
        this.prepayId = prepayId;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }

    public String getPackagevar() {
        return packagevar;
    }

    public void setPackagevar(String packagevar) {
        this.packagevar = packagevar;
    }

    public String getRefundNo() {
        return refundNo;
    }

    public void setRefundNo(String refundNo) {
        this.refundNo = refundNo;
    }

    public String getRefundFee() {
        return refundFee;
    }

    public void setRefundFee(String refundFee) {
        this.refundFee = refundFee;
    }

    public String getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(String errorcode) {
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
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

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getCheckMoneyOrderId() {
        return checkMoneyOrderId;
    }

    public void setCheckMoneyOrderId(Integer checkMoneyOrderId) {
        this.checkMoneyOrderId = checkMoneyOrderId;
    }
}
