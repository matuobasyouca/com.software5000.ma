package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/** 订单详情 */
public class WorkOrderDetail extends BaseEntity {

    public enum Daos {
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }


    /**
    * 商家服务项
    */
    private Integer serviceItemId;

    /**
     * 商家服务项名称
     */
    private String serviceItemName;

    /**
     * 服务次数
     */
    private Integer itemsTimes;

    /**
     * 所有服务次数中，其中用套餐卡支付的次数
     */
    private Integer usePackageTimes;

    /**
     * 销售价
     */
    private Double salePrice;

    /**
    * 会员折扣扣减
    */
    private Double discountPrice;

    /**
     * 折扣类型
     */
    private Integer discountType;

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

    /**
     * 所属工单
     */
    private Integer workOrderId;

    /**
     * 派工人员
     */
    private Integer workerId;

    /**
     * 派工人员姓名
     */
    @NotDatabaseField
    private String workerName;

    /**
     * 销售人员
     */
    private Integer salerId;

    /**
     * 销售人员姓名
     */
    @NotDatabaseField
    private String salerName;
    /**
     * 项目类型
     */
    @NotDatabaseField
    private String itemType;

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * 商家服务项
     */
    @NotDatabaseField
    private ServiceItem serviceItem;

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    public Integer getItemsTimes() {
        return itemsTimes;
    }

    public Integer getUsePackageTimes() {
        return usePackageTimes;
    }

    public void setUsePackageTimes(Integer usePackageTimes) {
        this.usePackageTimes = usePackageTimes;
    }

    public void setItemsTimes(Integer itemsTimes) {
        this.itemsTimes = itemsTimes;
    }

    public Integer getServiceItemId(){
      return this.serviceItemId;
    }

    public void setServiceItemId(Integer serviceItemId){
      this.serviceItemId = serviceItemId;
    }

    public Double getDiscountPrice(){
      return this.discountPrice;
    }

    public void setDiscountPrice(Double discountPrice){
      this.discountPrice = discountPrice;
    }

    public Double getDiscountNumber() {
        return discountNumber;
    }

    public void setDiscountNumber(Double discountNumber) {
        this.discountNumber = discountNumber;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public String getServiceItemName() {
        return serviceItemName;
    }

    public void setServiceItemName(String serviceItemName) {
        this.serviceItemName = serviceItemName;
    }

    public Integer getWorkerId() {
        return workerId;
    }

    public void setWorkerId(Integer workerId) {
        this.workerId = workerId;
    }

    public Integer getSalerId() {
        return salerId;
    }

    public void setSalerId(Integer salerId) {
        this.salerId = salerId;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getSalerName() {
        return salerName;
    }

    public void setSalerName(String salerName) {
        this.salerName = salerName;
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

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WorkOrderDetail that = (WorkOrderDetail)o;
        if(that.getId()==null || getId()==null) return false;
        return (that.getId().intValue() == getId().intValue());
    }
}