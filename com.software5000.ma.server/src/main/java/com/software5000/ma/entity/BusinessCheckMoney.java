package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;

/**
 * Created by jiye on 2017/7/25.
 */
public class BusinessCheckMoney extends BaseEntity {
    private Integer businessId;//商家id
    private Integer canCheckMoney;//可提现金额
    private Integer haveCheckMoney;//已提现金额

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Integer getCanCheckMoney() {
        return canCheckMoney;
    }

    public void setCanCheckMoney(Integer canCheckMoney) {
        this.canCheckMoney = canCheckMoney;
    }

    public Integer getHaveCheckMoney() {
        return haveCheckMoney;
    }

    public void setHaveCheckMoney(Integer haveCheckMoney) {
        this.haveCheckMoney = haveCheckMoney;
    }
}
