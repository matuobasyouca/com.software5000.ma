package com.software5000.ma.entity;

import com.software5000.base.Constant;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/** 服务项 */
public class ServiceItem extends BaseEntity {
    public enum Daos {
        countServiceItemUseInPackage("ServiceItem.countServiceItemUseInPackage"),
        selectPageServiceItem("ServiceItem.selectPageServiceItem"),
        selectItemCount("ServiceItem.selectItemCount"),
        selectServiceItemDiscountNumber("ServiceItem.selectServiceItemDiscountNumber"),
        selectPageServiceItemForWorkOrder("ServiceItem.selectPageServiceItemForWorkOrder"),
        ;

        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }


    /**
     * 服务名
     */
    private java.lang.String itemName;

    /**
     * 原价
     */
    private java.lang.Double originalPrice;

    /**
     * 销售价
     */
    private java.lang.Double salePrice;

    /**
     * 联盟商家
     */
    private Integer businessId;
    /**
     * 施工提成
     */
    private Double workBonus;
    /**
     * 销售提成
     */
    private Double saleBonus;
    /**
     * 服务项类别
     */
    private Integer itemType;

    /**
     * 联盟商家
     */

    @NotDatabaseField
    private Business business;

    /**
     * 是否置顶(1表示置顶，0或者null表示不置顶)
     */
    private Integer  topState;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 类别描述
     */
    @NotDatabaseField
    private String itemTypeDes;

    /**
     * 1.会员折扣，2.会员价
     */
    @NotDatabaseField
    private Integer discountType;

    /**
     * 优惠
     */
    @NotDatabaseField
    private Double discountNumber;

    @NotDatabaseField
    private Integer itemAndMemberLvlId;

    public Integer getItemAndMemberLvlId() {
        return itemAndMemberLvlId;
    }

    public void setItemAndMemberLvlId(Integer itemAndMemberLvlId) {
        this.itemAndMemberLvlId = itemAndMemberLvlId;
    }

    public String getItemTypeDes() {
        return Constant.enumValues.get(Constant.ServiceItemType.class.getSimpleName()+","+itemType);
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Double getWorkBonus() {
        return workBonus;
    }

    public void setWorkBonus(Double workBonus) {
        this.workBonus = workBonus;
    }

    public Double getSaleBonus() {
        return saleBonus;
    }

    public void setSaleBonus(Double saleBonus) {
        this.saleBonus = saleBonus;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getTopState() {
        return topState;
    }

    public void setTopState(Integer topState) {
        this.topState = topState;
    }

    public java.lang.String getItemName(){
        return this.itemName;
    }

    public void setItemName(java.lang.String itemName){
        this.itemName = itemName;
    }

    public java.lang.Double getOriginalPrice(){
        return this.originalPrice;
    }

    public void setOriginalPrice(java.lang.Double originalPrice){
        this.originalPrice = originalPrice;
    }

    public java.lang.Double getSalePrice(){
        return this.salePrice;
    }

    public void setSalePrice(java.lang.Double salePrice){
        this.salePrice = salePrice;
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

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountNumber() {
        return discountNumber;
    }

    public void setDiscountNumber(Double discountNumber) {
        this.discountNumber = discountNumber;
    }

    @Override
    public String toString() {
        return "ServiceItem{" +
                "itemName='" + itemName + '\'' +
                ", originalPrice=" + originalPrice +
                ", salePrice=" + salePrice +
                ", businessId=" + businessId +
                '}';
    }
}