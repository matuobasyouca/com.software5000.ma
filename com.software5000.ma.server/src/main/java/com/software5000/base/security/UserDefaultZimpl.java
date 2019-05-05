package com.software5000.base.security;

import com.software5000.base.entity.BaseEntity;
import com.software5000.ma.entity.BusinessUser;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;

import java.util.HashSet;
import java.util.Set;

public class UserDefaultZimpl implements AuthenticationToken {

    public UserDefaultZimpl() {
    }

    public UserDefaultZimpl(String userId, String token) {
        this.userId = userId;
        this.password = "";
        this.token = token;
    }

    public UserDefaultZimpl(String userId, String password, String userType) {
        this.userId = userId;
        this.password = password;
        this.userType = userType;
    }


    public UserDefaultZimpl(String userId, String password, String userType, BaseEntity realEntity) {
        this.userId = userId;
        this.password = password;
        this.userType = userType;
        this.realEntity = realEntity;
    }

    public UserDefaultZimpl(String userId, String password, String userType, String loginType) {
        this.userId = userId;
        this.password = password;
        this.userType = userType;
        this.loginType = loginType;
    }

    public UserDefaultZimpl(String userId, String password, String userType, BaseEntity realEntity, BusinessUser businessUser) {
        this.userId = userId;
        this.password = password;
        this.userType = userType;
        this.realEntity = realEntity;
        this.businessUser = businessUser;
    }

    private String userId;
    private String password;
    private String userType;
    private String loginType;
    private String token;
    private BaseEntity realEntity;
    private BusinessUser businessUser;

    public Object getPrincipal() {
        return getUserId();
    }

    public Object getCredentials() {
        return getPassword();
    }

    public String toString() {
        return this.getUserId();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public BaseEntity getRealEntity() {
        return realEntity;
    }

    public BusinessUser getBusinessUser() {
        return businessUser;
    }

    public void setBusinessUser(BusinessUser businessUser) {
        this.businessUser = businessUser;
    }

    public void setRealEntity(BaseEntity realEntity) {
        this.realEntity = realEntity;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Set<String> getRoles() {
        Set<String> roles = new HashSet<>();
        roles.add(this.userType);
        return roles;
    }

    public static UserDefaultZimpl getUserDefaultZimplFromSecurityUtils() {
        try {
            return (UserDefaultZimpl) SecurityUtils.getSubject().getPrincipal();
        } catch (Exception e) {
            return null;
        }
    }
}
