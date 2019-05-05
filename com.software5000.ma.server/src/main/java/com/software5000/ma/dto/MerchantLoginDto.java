package com.software5000.ma.dto;

/**
 * Created by cc on 2017/3/7.
 */
public class MerchantLoginDto {

    private Integer businessId;
    private String businessName;
    private String userName;
    private String userType;
    private String userOpenId;
    private String loginOpenId;
    private String rdUrl;
    private String realName;

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getRdUrl() {
        return rdUrl;
    }

    public void setRdUrl(String rdUrl) {
        this.rdUrl = rdUrl;
    }

    public String getUserOpenId() {
        return userOpenId;
    }

    public void setUserOpenId(String userOpenId) {
        this.userOpenId = userOpenId;
    }

    public String getLoginOpenId() {
        return loginOpenId;
    }

    public void setLoginOpenId(String loginOpenId) {
        this.loginOpenId = loginOpenId;
    }
}
