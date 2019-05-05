package com.software5000.bank.dto;

/**
 * Created by jiye on 2017/7/2.
 */
public class WeixinJSBridgeDto {
    private String appId;
    private Long timeStamp;//到前端必须转为String否则ios支付会有问题
    private String nonceStr;
    private String packagevar;
    private String signType;
    private String paySign;
    /**
     * 是否已经关注微信公众号 1已关注，0未关注
     */
    private Integer subscribe;

    public Integer getSubscribe() {
        return subscribe;
    }

    public void setSubscribe(Integer subscribe) {
        this.subscribe = subscribe;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackagevar() {
        return packagevar;
    }

    public void setPackagevar(String packagevar) {
        this.packagevar = packagevar;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
