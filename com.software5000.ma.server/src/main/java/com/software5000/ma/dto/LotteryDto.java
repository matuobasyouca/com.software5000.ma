package com.software5000.ma.dto;

/**
 * Created by jiye on 2017/7/7.
 */
public class LotteryDto {
    /**
     * 抽奖次数
     */
    private Integer lotteryTimes;
    private String carNumber;
    private String mobile;
    /**
     * 抽奖奖品
     */
    private String lotteryPrize;
    /**
     * 奖品所在序号
     */
    private Integer prizeNum;

    /**
     * 领取状态
     */
    private Integer receiveState;

    public Integer getReceiveState() {
        return receiveState;
    }

    public void setReceiveState(Integer receiveState) {
        this.receiveState = receiveState;
    }

    public Integer getPrizeNum() {
        return prizeNum;
    }

    public void setPrizeNum(Integer prizeNum) {
        this.prizeNum = prizeNum;
    }

    public String getLotteryPrize() {
        return lotteryPrize;
    }

    public void setLotteryPrize(String lotteryPrize) {
        this.lotteryPrize = lotteryPrize;
    }

    public Integer getLotteryTimes() {
        return lotteryTimes;
    }

    public void setLotteryTimes(Integer lotteryTimes) {
        this.lotteryTimes = lotteryTimes;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
