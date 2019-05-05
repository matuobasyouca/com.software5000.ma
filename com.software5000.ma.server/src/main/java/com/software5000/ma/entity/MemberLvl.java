package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/** 会员等级 */
public class MemberLvl extends BaseEntity {
    public enum Daos {
        /**
         * 根据累计消费查询出订单
         */
        selectByConsumeTopOne("MemberLvl.selectByConsumeTopOne"),
        /**
         * 获取等级列表分页
         */
        selectMemberLvlPage("MemberLvl.selectMemberLvlPage"),
        /**
         * 更新会员等级
         */
        updateMemberLvlForUpdateState("MemberLvl.updateMemberLvlForUpdateState"),
        selectByRechargeTopOne("MemberLvl.selectByRechargeTopOne"),
        ;

        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 等级名称
     */
    private String lvlName;

    /**
     * 累积消费
     */
    private Double totalConsume;

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
     * 折扣
     */
    private Double discount;

    /**
     * 状态
     */
    private Integer state;

    /**
     * 充值金额
     */
    private Double rechargeMoney;

    public Double getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(Double rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }

    public Double getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(Double totalConsume) {
        this.totalConsume = totalConsume;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}