package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/** 会员服务使用记录 */
public class MemberItemUseRecord extends BaseEntity {
    public enum Daos {
        //TODO
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    
    /**
    * 剩余次数 
    */
    private Integer remainTimes;

    /**
    * 一共可以使用的次数
    */
    private Integer useTimes;

    /**
     * 服务项
     */
    @NotDatabaseField
    private ServiceItem serviceItem;

    /**
     * 服务项
     */
    private Integer serviceItemId;

    /**
    * 用户套餐
    */
    private Integer memberPackageRecordId;

    /**
    * 用户套餐
    */
    @NotDatabaseField
    private com.software5000.ma.entity.MemberPackageRecord MemberPackageRecord;


    public Integer getRemainTimes() {
        return remainTimes;
    }

    public void setRemainTimes(Integer remainTimes) {
        this.remainTimes = remainTimes;
    }

    public Integer getUseTimes(){
      return this.useTimes;
    }

    public void setUseTimes(Integer useTimes){
      this.useTimes = useTimes;
    }

    public Integer getMemberPackageRecordId() {
        return memberPackageRecordId;
    }

    public void setMemberPackageRecordId(Integer memberPackageRecordId) {
        this.memberPackageRecordId = memberPackageRecordId;
    }

    public com.software5000.ma.entity.MemberPackageRecord getMemberPackageRecord(){
      return this.MemberPackageRecord;
    }

    public void setMemberPackageRecord(com.software5000.ma.entity.MemberPackageRecord MemberPackageRecord){
      this.MemberPackageRecord = MemberPackageRecord;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

    public Integer getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Integer serviceItemId) {
        this.serviceItemId = serviceItemId;
    }
}