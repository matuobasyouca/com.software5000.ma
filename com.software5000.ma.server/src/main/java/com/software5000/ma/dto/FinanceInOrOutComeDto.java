package com.software5000.ma.dto;

/**
 * 统计商家总收入及总支出
 * Created by jiye on 2017/7/24.
 */
public class FinanceInOrOutComeDto {
    //总收入
    private Integer inComeMoney=0;
    //总支出
    private Integer outComeMoney=0;

    public Integer getInComeMoney() {
        return inComeMoney;
    }

    public void setInComeMoney(Integer inComeMoney) {
        this.inComeMoney = inComeMoney;
    }

    public Integer getOutComeMoney() {
        return outComeMoney;
    }

    public void setOutComeMoney(Integer outComeMoney) {
        this.outComeMoney = outComeMoney;
    }
}
