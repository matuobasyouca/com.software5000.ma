package com.software5000.ma.dto;

/**
 * Created by luo on 2017/7/21.
 */
public class MerchantWorkbenchDto {

    private Integer noPayOrder;
    private Integer itemNum;
    private Integer memberNum;
    private Double yeCount;
    private Double todayCount;
    private Integer buyPackNum;
    private Integer noPayPackNum;
    private Integer allEvaluationNum;
    private Integer goodEvaluationNum;

    public Integer getNoPayOrder() {
        return noPayOrder;
    }

    public void setNoPayOrder(Integer noPayOrder) {
        this.noPayOrder = noPayOrder;
    }

    public Integer getItemNum() {
        return itemNum;
    }

    public void setItemNum(Integer itemNum) {
        this.itemNum = itemNum;
    }

    public Integer getMemberNum() {
        return memberNum;
    }

    public void setMemberNum(Integer memberNum) {
        this.memberNum = memberNum;
    }

    public Double getYeCount() {
        return yeCount;
    }

    public void setYeCount(Double yeCount) {
        this.yeCount = yeCount;
    }

    public Double getTodayCount() {
        return todayCount;
    }

    public void setTodayCount(Double todayCount) {
        this.todayCount = todayCount;
    }

    public Integer getBuyPackNum() {
        return buyPackNum;
    }

    public void setBuyPackNum(Integer buyPackNum) {
        this.buyPackNum = buyPackNum;
    }

    public Integer getNoPayPackNum() {
        return noPayPackNum;
    }

    public void setNoPayPackNum(Integer noPayPackNum) {
        this.noPayPackNum = noPayPackNum;
    }

    public Integer getAllEvaluationNum() {
        return allEvaluationNum;
    }

    public void setAllEvaluationNum(Integer allEvaluationNum) {
        this.allEvaluationNum = allEvaluationNum;
    }

    public Integer getGoodEvaluationNum() {
        return goodEvaluationNum;
    }

    public void setGoodEvaluationNum(Integer goodEvaluationNum) {
        this.goodEvaluationNum = goodEvaluationNum;
    }
}
