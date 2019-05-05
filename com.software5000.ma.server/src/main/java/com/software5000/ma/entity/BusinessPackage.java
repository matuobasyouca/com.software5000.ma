package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

public class BusinessPackage extends BaseEntity {
    public enum Daos {
        deletePackageAndItem("BusinessPackage.deletePackageAndItem"),
        selectBusinessPackageByPage("BusinessPackage.selectBusinessPackageByPage"),
        selectBusinessPackageList("BusinessPackage.selectBusinessPackageList"),
        selectBusinessPackageById("BusinessPackage.selectBusinessPackageById"),
        selectBusinessPackageIdsByPage("BusinessPackage.selectBusinessPackageIdsByPage"),
        selectBusinessPackageCount("BusinessPackage.selectBusinessPackageCount"),
        //根据套餐id及商家查询套餐内容及商家的名称
        selectBusinessPackageForOpenEmkt("BusinessPackage.selectBusinessPackageForOpenEmkt"),
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    
    /**
    * 套餐名
    */
    private String packageName;

    /**
    * 有效期
    */
    private Integer validTerm;

    /**
     * 无效字段
     */
    @NotDatabaseField
    private String validTime;

    /**
    * 原价
    */
    private Double originalPrice;

    /**
    * 销售价
    */
    private Double salePrice;

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
     * 套餐类型
     */
    private Integer packageType;

    /**
     * 套餐状态
     */
    private Integer packageState;

    /**
    * 套餐服务集
    */
    @NotDatabaseField
    private java.util.List<PackageAndItem> packageAndItems;

    /**
     * 使用说明
     */
    private String instructions;

    public String getInstructions() {
        return instructions;
    }

    public void setInstructions(String instructions) {
        this.instructions = instructions;
    }

    public Integer getPackageState() {
        return packageState;
    }

    public void setPackageState(Integer packageState) {
        this.packageState = packageState;
    }

    public String getPackageName(){
      return this.packageName;
    }

    public void setPackageName(String packageName){
      this.packageName = packageName;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public Double getOriginalPrice(){
      return this.originalPrice;
    }

    public void setOriginalPrice(Double originalPrice){
      this.originalPrice = originalPrice;
    }

    public Double getSalePrice(){
      return this.salePrice;
    }

    public void setSalePrice(Double salePrice){
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

    public Integer getPackageType() {
        return packageType;
    }

    public void setPackageType(Integer packageType) {
        this.packageType = packageType;
    }

    public java.util.List<PackageAndItem> getPackageAndItems(){
      return this.packageAndItems;
    }

    public void setPackageAndItems(java.util.List<PackageAndItem> packageAndItems){
      this.packageAndItems = packageAndItems;
    }

    public String getValidTime() {
        return validTime;
    }

    public void setValidTime(String validTime) {
        this.validTime = validTime;
    }

    @Override
    public String toString() {
        return "BusinessPackage{" +
                "packageName='" + packageName + '\'' +
                ", validTerm='" + validTerm + '\'' +
                ", originalPrice=" + originalPrice +
                ", salePrice=" + salePrice +
                ", businessId=" + businessId +
                ", business=" + business +
                ", packageType='" + packageType + '\'' +
                ", packageState='" + packageState + '\'' +
                '}';
    }
}