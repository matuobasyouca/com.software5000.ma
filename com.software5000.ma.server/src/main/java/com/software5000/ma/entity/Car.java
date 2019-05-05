package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/**
 * 用户车辆实体
 * Created by wujin on 2016/12/22.
 */
public class Car extends BaseEntity {
    /**
     * 车牌号
     */
    private String carNumber;
    /**
     * 车架号
     */
    private String frameNumber;

    /**
     * 车辆状态
     */
    private Integer carState;

    /**
     * 所属用户
     */
    @NotDatabaseField
    private User user;

    /**
     * 所属用户
     */
    private Integer userId;

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber.toUpperCase();
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getCarState() {
        return carState;
    }

    public void setCarState(Integer carState) {
        this.carState = carState;
    }
}
