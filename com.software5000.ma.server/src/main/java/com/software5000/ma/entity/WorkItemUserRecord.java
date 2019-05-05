package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;

/** 会员服务使用记录 */
public class WorkItemUserRecord extends BaseEntity {
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
    private Integer workOrderId;

    /**
    * 一共可以使用的次数
    */
    private Integer memberItemUseRecordId;

    /**
     * 服务项
     */
    private Integer times;

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }

    public Integer getMemberItemUseRecordId() {
        return memberItemUseRecordId;
    }

    public void setMemberItemUseRecordId(Integer memberItemUseRecordId) {
        this.memberItemUseRecordId = memberItemUseRecordId;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }
}