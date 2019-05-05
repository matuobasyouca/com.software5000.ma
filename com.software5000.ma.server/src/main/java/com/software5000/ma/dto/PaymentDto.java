package com.software5000.ma.dto;

import com.software5000.base.entity.BaseEntity;

/**
 * Created by jiye on 2017/7/13.
 */
public class PaymentDto extends BaseEntity {

    private String businessName;//商家名称
    private Integer costSum;//总支出
    private Integer incomeSum;//总收入

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getCostSum() {
        return costSum;
    }

    public void setCostSum(Integer costSum) {
        this.costSum = costSum;
    }

    public Integer getIncomeSum() {
        return incomeSum;
    }

    public void setIncomeSum(Integer incomeSum) {
        this.incomeSum = incomeSum;
    }
}
