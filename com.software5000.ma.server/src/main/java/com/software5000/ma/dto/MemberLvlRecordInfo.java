package com.software5000.ma.dto;

import com.software5000.base.entity.BaseEntity;
import com.software5000.ma.entity.Car;
import com.software5000.ma.entity.MemberLvl;
import com.software5000.ma.entity.User;

import java.util.List;

/**
 * 商家所有会员信息
 * Created by wujin on 2016/12/30.
 */
public class MemberLvlRecordInfo extends BaseEntity {
    /**
     * 用户
     */
    private Integer userId;

    /**
     * 会员等级
     */
    private Integer memberLvlId;

    /**
     * 累积消费金额
     */
    private java.lang.Double totalConsume;

    /**
     * 累计消费次数
     */
    private java.lang.Integer totalTimes;
    /**
     * 用户
     */
    private User user;

    /**
     * 会员等级
     */
    private MemberLvl memberLvl;

    /**
     * 车辆信息
     */
    private List<Car> cars;

    public Integer getTotalTimes() {
        return totalTimes;
    }

    public void setTotalTimes(Integer totalTimes) {
        this.totalTimes = totalTimes;
    }

    public List<Car> getCars() {
        return cars;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getMemberLvlId() {
        return memberLvlId;
    }

    public void setMemberLvlId(Integer memberLvlId) {
        this.memberLvlId = memberLvlId;
    }

    public Double getTotalConsume() {
        return totalConsume;
    }

    public void setTotalConsume(Double totalConsume) {
        this.totalConsume = totalConsume;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MemberLvl getMemberLvl() {
        return memberLvl;
    }

    public void setMemberLvl(MemberLvl memberLvl) {
        this.memberLvl = memberLvl;
    }


}
