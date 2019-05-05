package com.software5000.ma.dto;

/**
 * 微信提现信息dto
 * Created by jiye on 2017/7/25.
 */
public class CheckMoneyDto {

    /**
     * 可提现金额最小金额
     */
    private Integer minCheckMoney;

    /**
     * 到账微信昵称
     */
    private String nickName;
    /**
     * 可提现最大金额
     */
    private Integer maxCheckMoney;

    /**
     * 是否已经提现过了
     */
    private boolean haveCheck;

    /**
     * 今日开单数据
     */
    private Integer countTodayWorkOrder;

    /**
     * 今日营业额
     */
    private Integer countTodayTotalPrice;

    public Integer getCountTodayWorkOrder() {
        return countTodayWorkOrder;
    }

    public void setCountTodayWorkOrder(Integer countTodayWorkOrder) {
        this.countTodayWorkOrder = countTodayWorkOrder;
    }

    public Integer getCountTodayTotalPrice() {
        return countTodayTotalPrice;
    }

    public void setCountTodayTotalPrice(Integer countTodayTotalPrice) {
        this.countTodayTotalPrice = countTodayTotalPrice;
    }

    public boolean isHaveCheck() {
        return haveCheck;
    }

    public void setHaveCheck(boolean haveCheck) {
        this.haveCheck = haveCheck;
    }

    public Integer getMinCheckMoney() {
        return minCheckMoney;
    }

    public void setMinCheckMoney(Integer minCheckMoney) {
        this.minCheckMoney = minCheckMoney;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getMaxCheckMoney() {
        return maxCheckMoney;
    }

    public void setMaxCheckMoney(Integer maxCheckMoney) {
        this.maxCheckMoney = maxCheckMoney;
    }
}
