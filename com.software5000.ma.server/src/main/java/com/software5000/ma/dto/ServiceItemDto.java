package com.software5000.ma.dto;

/**
 * Created by wujin on 2017/1/18.
 */
public class ServiceItemDto {
    private String itemName;
    private Integer serviceItemId;
    private Double discountPrice;
    private Double salePrice;
    private Integer useTimes;
    private Double originalPrice;
    private Integer usePackageTimes;
    private Integer itemsTimes;
    /**
     * 实际付款
     */
    private Double payPrice;

    /**
     * 会员折扣数
     */
    private Double discountNumber;

    /**
     * 优惠券uuid
     */
    private String couponUuid;

    /**
     * 卡券名称
     */
    private String couponName;

    /**
     * 优惠券扣减
     */
    private Double couponDeduct;

    /**
     * 项目总价（扣减后）
     */
    private Double totalPrice;


    public Double getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(Double payPrice) {
        this.payPrice = payPrice;
    }

    public Double getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(Double discountPrice) {
        this.discountPrice = discountPrice;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Integer serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public Integer getUseTimes() {
        return useTimes;
    }

    public void setUseTimes(Integer useTimes) {
        this.useTimes = useTimes;
    }

    public Double getOriginalPrice() {
        return originalPrice;
    }

    public void setOriginalPrice(Double originalPrice) {
        this.originalPrice = originalPrice;
    }

    public Integer getUsePackageTimes() {
        return usePackageTimes;
    }

    public void setUsePackageTimes(Integer usePackageTimes) {
        this.usePackageTimes = usePackageTimes;
    }

    public Integer getItemsTimes() {
        return itemsTimes;
    }

    public void setItemsTimes(Integer itemsTimes) {
        this.itemsTimes = itemsTimes;
    }

    public Double getDiscountNumber() {
        return discountNumber;
    }

    public void setDiscountNumber(Double discountNumber) {
        this.discountNumber = discountNumber;
    }

    public String getCouponUuid() {
        return couponUuid;
    }

    public void setCouponUuid(String couponUuid) {
        this.couponUuid = couponUuid;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public Double getCouponDeduct() {
        return couponDeduct;
    }

    public void setCouponDeduct(Double couponDeduct) {
        this.couponDeduct = couponDeduct;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
