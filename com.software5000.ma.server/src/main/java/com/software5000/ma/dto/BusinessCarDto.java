package com.software5000.ma.dto;

import com.software5000.ma.entity.MemberPackageRecord;
import com.zscp.master.util.ValidUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * Created by Jiang on 2017/07/21.
 */
public class BusinessCarDto {

    /**
     * 会员ID
     */
    private Integer userId;

    /**
     * 会员名称
     */
    private String realName;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 会员信息描述
     */
    private String lvlName;

    /**
     * 商家会员记录ID
     */
    private Integer recordId;

    /**
     * 到店次数
     */
    private Integer visitCount;

    /**
     * 上次到店
     */
    private Timestamp lastVisitTime;

    /**
     * 套餐列表
     */
    private List<MemberPackageRecord> packageList;

    /**
     * 折扣
     */

    private Double discount;

    private String wxOpenId;
    /**
     * 备注
     */
    private String remarks;

    /**
     * 会员余额
     */
    private Double memberBalance;

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public String getCarNumber() {
        return ValidUtil.isEmpty(carNumber) ? "" : carNumber.toUpperCase();
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }

    public Integer getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(Integer visitCount) {
        this.visitCount = visitCount;
    }

    public Timestamp getLastVisitTime() {
        return lastVisitTime;
    }

    public void setLastVisitTime(Timestamp lastVisitTime) {
        this.lastVisitTime = lastVisitTime;
    }

    public List<MemberPackageRecord> getPackageList() {
        return packageList;
    }

    public void setPackageList(List<MemberPackageRecord> packageList) {
        this.packageList = packageList;
    }

    public Double getDiscount() {
        return discount;
    }

    public void setDiscount(Double discount) {
        this.discount = discount;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public Double getMemberBalance() {
        return memberBalance;
    }

    public void setMemberBalance(Double memberBalance) {
        this.memberBalance = memberBalance;
    }
}
