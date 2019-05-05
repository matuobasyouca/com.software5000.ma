package com.software5000.ma.aliyunSms;

/**
 *
 * 短信记录
 * Created by wujin on 2017/1/8.
 */
public class SmsRecord {
    /**
     * 发送的次数
     */
    private Integer times;

    /**
     * 第一次发送短信时间
     */
    private Long firstSendTime;

    /**
     * 上一次发送短信时间，用来判断时间间隔
     */
    private Long lastSendTime;

    /**
     * 验证码有效期截至时间点
     */
    private Long expirationTime;
    /**
     * 最新的限制次数长度的时间戳 eg:若1小时内最多可以发送3次短信，则此集合存的是最新的（3+1）次发送时间戳，
     * 通过队列的长度及队列首尾时间相减来判断是否超过1小时，如果长度小于4，则规则通过，如果长度等于4，则将队列首尾相减
     * 如果超过1小时则规则通过，否则规则阻挡。
     */
    private LimitQueue<Long> limitTimeQueue;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 重新开放短信发送的时间
     */
    private Long reSendTime;
    /**
     * 携带的拓展参数
     */
    private String expand;

    public SmsRecord(Integer times, Long firstSendTime,Long lastSendTime, LimitQueue<Long> limitTimeQueue, String mobile, String expand,Long expirationTime) {
        this.times = times;
        this.firstSendTime = firstSendTime;
        this.lastSendTime = lastSendTime;
        this.limitTimeQueue = limitTimeQueue;
        this.mobile = mobile;
        this.expand = expand;
        this.expirationTime = expirationTime;
    }

    public Long getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Long expirationTime) {
        this.expirationTime = expirationTime;
    }

    public Integer getTimes() {
        return times;
    }

    public void setTimes(Integer times) {
        this.times = times;
    }

    public Long getFirstSendTime() {
        return firstSendTime;
    }

    public void setFirstSendTime(Long firstSendTime) {
        this.firstSendTime = firstSendTime;
    }

    public LimitQueue<Long> getLimitTimeQueue() {
        return limitTimeQueue;
    }

    public void setLimitTimeQueue(LimitQueue<Long> limitTimeQueue) {
        this.limitTimeQueue = limitTimeQueue;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getExpand() {
        return expand;
    }

    public void setExpand(String expand) {
        this.expand = expand;
    }

    public Long getReSendTime() {
        return reSendTime;
    }

    public void setReSendTime(Long reSendTime) {
        this.reSendTime = reSendTime;
    }

    public Long getLastSendTime() {
        return lastSendTime;
    }

    public void setLastSendTime(Long lastSendTime) {
        this.lastSendTime = lastSendTime;
    }

    @Override
    public String toString() {
        return "SmsRecord{" +
                "times=" + times +
                ", firstSendTime=" + firstSendTime +
                ", lastSendTime=" + lastSendTime +
                ", expirationTime=" + expirationTime +
                ", mobile='" + mobile + '\'' +
                ", reSendTime=" + reSendTime +
                ", expand='" + expand + '\'' +
                '}';
    }
}
