package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/** 套餐服务 */
public class PackageAndItem extends BaseEntity {
    public enum Daos {
        /**
         * 根据套餐id查询对应的套餐项
         */
        selectByPackageId("PackageAndItem.selectByPackageId")
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    
    /**
    * 使用次数
    */
    private Integer useTimes;

    /**
    * 服务项目
    */
    private Integer serviceItemId;

    /**
     * 服务项
     */
    @NotDatabaseField
    private ServiceItem serviceItem;

    /**
    * 套餐
    */
    private Integer businessPackageId;

    /**
     * 套餐
     */
    @NotDatabaseField
    private BusinessPackage businessPackage;


    public Integer getUseTimes(){
      return this.useTimes;
    }

    public void setUseTimes(Integer useTimes){
      this.useTimes = useTimes;
    }
    
    public Integer getServiceItemId(){
      return this.serviceItemId;
    }
    
    public void setServiceItemId(Integer serviceItemId){
      this.serviceItemId = serviceItemId;
    }

    public Integer getBusinessPackageId() {
        return businessPackageId;
    }

    public void setBusinessPackageId(Integer businessPackageId) {
        this.businessPackageId = businessPackageId;
    }

    public BusinessPackage getBusinessPackage() {
        return businessPackage;
    }

    public void setBusinessPackage(BusinessPackage businessPackage) {
        this.businessPackage = businessPackage;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    @Override
    public String toString() {
        return "PackageAndItem{" +
                "useTimes=" + useTimes +
                ", serviceItemId=" + serviceItemId +
                ", BusinessPackageId=" + businessPackageId +
                '}';
    }
}