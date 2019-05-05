package com.software5000.ma.dto;

import com.zscp.master.util.MathUtil;

/**
 * Created by Jiang on 2017/07/22.
 */
public class WorkOrderExcelDto {

    /**
     * 工单编号
     */
    private String orderNo;

    /**
     * 创建日期
     */
    private String createTime;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 客户账号
     */
    private String mobile;

    /**
     * 服务项目（服务名称 + 次数 + (扣卡N次)， XXX）（eg:洗车1次（扣卡1次））
     */
    private String serviceItemNames;

    /**
     * 施工价格（项目价格 - 套餐扣减 + 材料费用）
     */
    private Double totalPrice;

    /**
     * 附加费用
     */
    private Double materialCost;

    /**
     * 会员折扣
     */
    private Double discountDeduct;

    /**
     * 商家扣减
     */
    private Double businessDeduct;

    /**
     * 卡券扣减
     */
    private Double couponDeduct;

    /**
     * 余额支付
     */
    private Double balanceDeduct;

    /**
     * 实际付款金额
     */
    private Double payPrice;

    /**
     * 付款方式
     */
    private String payType;

    /**
     * 付款时间
     */
    private String payTime;

    /**
     * 备注
     */
    private String comment;

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
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

    public String getServiceItemNames() {
        return serviceItemNames;
    }

    public void setServiceItemNames(String serviceItemNames) {
        this.serviceItemNames = serviceItemNames;
    }

    /**
     * 总价格
     * @return
     */
    public Double getTotalPrice() {
        return MathUtil.sub(MathUtil.add(MathUtil.add(MathUtil.add(MathUtil.add(transToZero(this.payPrice),transToZero(this.businessDeduct))
                            ,transToZero(this.discountDeduct)) ,transToZero(this.couponDeduct)),transToZero(this.balanceDeduct)), transToZero(this.materialCost));
    }

    /**
     * 转换
     * @param data
     * @return
     */
    private Double transToZero(Double data) {
        if(data == null) {
            return 0D;
        }
        return data;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Double getDiscountDeduct() {
        return discountDeduct;
    }

    public void setDiscountDeduct(Double discountDeduct) {
        this.discountDeduct = discountDeduct;
    }

    public Double getBusinessDeduct() {
        return businessDeduct;
    }

    public void setBusinessDeduct(Double businessDeduct) {
        this.businessDeduct = businessDeduct;
    }

    public Double getCouponDeduct() {
        return couponDeduct;
    }

    public void setCouponDeduct(Double couponDeduct) {
        this.couponDeduct = couponDeduct;
    }

    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Double getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(Double materialCost) {
        this.materialCost = materialCost;
    }

    public Double getBalanceDeduct() {
        return balanceDeduct;
    }

    public void setBalanceDeduct(Double balanceDeduct) {
        this.balanceDeduct = balanceDeduct;
    }
}
