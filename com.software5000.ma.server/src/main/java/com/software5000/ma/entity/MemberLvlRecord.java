package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/**
 * 会员
 */
public class MemberLvlRecord extends BaseEntity {
    public enum Daos {
        /**
         * 根据用户id跟商家id查询 该用户在该商家的会员情况
         */
        selectMemberLvlRecordAndMemberLvlByUserId("MemberLvlRecord.selectMemberLvlRecordAndMemberLvlByUserId"),
        selectPageMemberLvlRecordByParam("MemberLvlRecord.selectPageMemberLvlRecordByParam"),
        selectPageMemberLvlRecordIdByParam("MemberLvlRecord.selectPageMemberLvlRecordIdByParam"),
        selectCarsListByUserIds("MemberLvlRecord.selectCarsListByUserIds"),
        selectMemberByParamForExcle("MemberLvlRecord.selectMemberByParamForExcle"),
        selectPackageWithMemberByParamForExcle("MemberLvlRecord.selectPackageWithMemberByParamForExcle"),
        selectMemberLvlRecordByParam("MemberLvlRecord.selectMemberLvlRecordByParam"),
        selectUserCount("MemberLvlRecord.selectUserCount"),
        deleteMemberLvlRecords("MemberLvlRecord.deleteMemberLvlRecords"),
        updateMemberLvlRecordChangeUserId("MemberLvlRecord.updateMemberLvlRecordChangeUserId"),
        selectMemberLvlRecord("MemberLvlRecord.selectMemberLvlRecord"),
        selectMemberLvlRecordBusiness("MemberLvlRecord.selectMemberLvlRecordBusiness"),;

        public String sqlMapname;

        private Daos(String name) {
            this.sqlMapname = name;
        }

        public String toString() {
            return this.sqlMapname;
        }
    }


    /**
     * 用户
     */
    private Integer userId;

    /**
     * 用户
     */

    @NotDatabaseField
    private User user;

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
     * 会员等级
     */
    private Integer memberLvlId;

    /**
     * 会员等级
     */

    @NotDatabaseField
    private MemberLvl memberLvl;

    /**
     * 累积消费金额
     */
    private Double totalConsume;

    /**
     * 累计消费次数
     */
    private Integer totalTimes;

    /**
     * 会员备注
     */
    private String remarks;
    /**
     * 会员余额
     */
    private Double memberBalance;

    public Double getMemberBalance() {
        return memberBalance;
    }

    public void setMemberBalance(Double memberBalance) {
        this.memberBalance = memberBalance;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getBusinessId() {
        return this.businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }


    public Business getBusiness() {
        return this.business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Integer getMemberLvlId() {
        return this.memberLvlId;
    }

    public void setMemberLvlId(Integer memberLvlId) {
        this.memberLvlId = memberLvlId;
    }


    public MemberLvl getMemberLvl() {
        return this.memberLvl;
    }

    public void setMemberLvl(MemberLvl memberLvl) {
        this.memberLvl = memberLvl;
    }

    public Double getTotalConsume() {
        return this.totalConsume;
    }

    public void setTotalConsume(Double totalConsume) {
        this.totalConsume = totalConsume;
    }

}