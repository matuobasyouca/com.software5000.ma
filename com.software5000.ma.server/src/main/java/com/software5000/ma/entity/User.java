package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.util.List;

/**
 * 用户表
 */
public class User extends BaseEntity {
    public enum Daos {
        /**
         * 根据车牌号查询用户
         */
        selectUserByCarNo("User.selectUserByCarNo"),
        /**
         * 查询商家会员信息中的所有车牌号
         */
        selectBusinessCarByParam("User.selectBusinessCarByParam"),
        /**
         * 根据车牌号获取会员信息（包含到店信息）
         */
        selectUserByCarNumberForBusiness("User.selectUserByCarNumberForBusiness"),
        /**
         * 根据openId获取
         */
        selectUserByOpenId("User.selectUserByOpenId"),

        selectUserByOpenIdOrMobileOrCar("User.selectUserByOpenIdOrMobileOrCar"),
        updateCarChangeUserId("User.updateCarChangeUserId"),
        /**
         *根据条件查询会员信息
         */
        selectBusinessUserByParam("User.selectBusinessUserByParam"),
        ;

        public String sqlMapname;

        private Daos(String name) {
            this.sqlMapname = name;
        }

        public String toString() {
            return this.sqlMapname;
        }
    }

    public User() {
    }

    public User(String mobile) {
        this.mobile = mobile;
    }

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 昵称
     */
    private String nickname;
    /**
     * 性别
     */
    private String sex;

    /**
     * 生日
     */
    private java.util.Date birthday;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 密码
     */
    private String pwd;

    /**
     * 地区
     */
    private String area;

    /**
     * 车
     */
    @NotDatabaseField
    private List<Car> cars;
    /**
     * 是否绑定微信
     */
    private Integer bindWeChat;

    /**
     * 是否已经登陆过一次
     */
    private Integer haveLoginOnce;

    /**
     * 微信用户的openid
     */
    private String wxOpenId;

    /**
     * 头像图片
     */
    @NotDatabaseField
    private String headImgUrl;
    /**
     * 用户套餐
     */
    @NotDatabaseField
    private List<MemberPackageRecord> memberPackageRecords;

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    /**
     * 用户会员
     *
     * @return
     */
    @NotDatabaseField
    private List<MemberLvlRecord> memberLvlRecords;

    public Integer getHaveLoginOnce() {
        return haveLoginOnce;
    }

    public void setHaveLoginOnce(Integer haveLoginOnce) {
        this.haveLoginOnce = haveLoginOnce;
    }

    public List<MemberLvlRecord> getMemberLvlRecords() {
        return memberLvlRecords;
    }

    public void setMemberLvlRecords(List<MemberLvlRecord> memberLvlRecords) {
        this.memberLvlRecords = memberLvlRecords;
    }

    public List<MemberPackageRecord> getMemberPackageRecords() {
        return memberPackageRecords;
    }

    public void setMemberPackageRecords(List<MemberPackageRecord> memberPackageRecords) {
        this.memberPackageRecords = memberPackageRecords;
    }

    public String getRealName() {
        return this.realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return this.sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public java.util.Date getBirthday() {
        return this.birthday;
    }

    public void setBirthday(java.util.Date birthday) {
        this.birthday = birthday;
    }

    public String getMobile() {
        return this.mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getArea() {
        return this.area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public String getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(String headImgUrl) {
        this.headImgUrl = headImgUrl;
    }
}