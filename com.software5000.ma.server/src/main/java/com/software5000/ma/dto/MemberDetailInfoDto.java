package com.software5000.ma.dto;


import com.software5000.ma.entity.Car;
import com.software5000.ma.entity.MemberPackageRecord;

import java.util.List;

/**
 * Created by wujin on 2017/1/2.
 */
public class MemberDetailInfoDto {
    /**
     * 用户id
     */
    private Integer id;
    /**
     * 用户真实名
     */
    private String realName;
    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 用户是否已经登陆过了
     */
    private Integer haveLoginOnce;

    /**
     * 用户会员等级
     */
    private String memberLvlName;


    /**
     * 用户拥有某商家套餐信息
     */
    private List<MemberPackageRecord> memberPackageRecords;
    /**
     * 用户拥有车辆的信息
     */
    private List<Car> cars;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Integer getHaveLoginOnce() {
        return haveLoginOnce;
    }

    public void setHaveLoginOnce(Integer haveLoginOnce) {
        this.haveLoginOnce = haveLoginOnce;
    }

    public String getMemberLvlName() {
        return memberLvlName;
    }

    public void setMemberLvlName(String memberLvlName) {
        this.memberLvlName = memberLvlName;
    }

    public List<MemberPackageRecord> getMemberPackageRecords() {
        return memberPackageRecords;
    }

    public void setMemberPackageRecords(List<MemberPackageRecord> memberPackageRecords) {
        this.memberPackageRecords = memberPackageRecords;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }
}
