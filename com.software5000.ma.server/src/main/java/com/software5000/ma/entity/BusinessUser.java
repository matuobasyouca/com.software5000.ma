package com.software5000.ma.entity;

import com.software5000.base.Constant;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/**
 * Created by Administrator on 2017/2/24.
 */
public class BusinessUser  extends BaseEntity {
    public enum Daos {
        ;

        public String sqlMapname;

        private Daos(String name) {
            this.sqlMapname = name;
        }

        public String toString() {
            return this.sqlMapname;
        }
    }


    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 账号
     */
    private String userName;

    /**
     * 性别
     */
    private String sex;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 是否绑定微信
     */
    private Integer bindWeChat;

    /**
     * 微信用户的openid
     */
    private String wxOpenId;

    /**
     * 商家ID
     */
    private Integer businessId;

    /**
     * 操作员 or 商家管理员
     */
    private String mercharType;


    private String itemTypes;//开单项目

    private Double salary;//底薪

    private Integer state;//状态（是否离职）

    private Integer bossType;//是否是老板（即是否是运营人员建的账号）

    @NotDatabaseField
    private String stateDes;

    /**
     * 商家信息
     */
    @NotDatabaseField
    private Business business;


    public Integer getBossType() {
        return bossType;
    }

    public void setBossType(Integer bossType) {
        this.bossType = bossType;
    }

    public String getStateDes() {
        return Constant.enumValues.get(Constant.BusinessUserState.class.getSimpleName()+","+state);
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getItemTypes() {
        return itemTypes;
    }

    public void setItemTypes(String itemTypes) {
        this.itemTypes = itemTypes;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public Integer getBindWeChat() {
        return bindWeChat;
    }

    public void setBindWeChat(Integer bindWeChat) {
        this.bindWeChat = bindWeChat;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getMercharType() {
        return mercharType;
    }

    public void setMercharType(String mercharType) {
        this.mercharType = mercharType;
    }

    public Business getBusiness() {
        return business;
    }

    public void setBusiness(Business business) {
        this.business = business;
    }

    /**
     * 获取正确的类型值
     */
    public String getCurrentMerchantType() {
        //设置账号类型
        if("merchant".equals(this.mercharType)) {
            return Constant.BusinessUserType.MERCHANT.codeName;
        }else if("operator".equals(this.mercharType)) {
            return Constant.BusinessUserType.OPERATOR.codeName;
        }
        return this.mercharType;
    }
}
