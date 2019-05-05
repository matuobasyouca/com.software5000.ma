package com.software5000.ma.dto;

import com.software5000.base.Constant;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;
import com.software5000.ma.entity.PackCluster;
import com.zscp.master.util.ValidUtil;

import java.sql.Timestamp;
import java.util.List;

public class PackClusterBuyRecordDto extends BaseEntity {

    /**
     * 开团id
     */
    private Integer openRecordId;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 活动编号
     */
    private Integer packClusterId;

    /**
     * 用户openId
     */
    private String wxOpenId;

    /**
     * 用户手机号
     */
    private String mobile;

    /**
     * 用户车牌号
     */
    private String carNumber;

    /**
     * 支付时间
     */
    private Timestamp payTime;

    /**
     * 支付金额
     */
    private Integer payMoney;

    /**
     * 支付状态(1-未支付,2已支付)
     */
    private Integer payState;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 拼团状态(1-拼团中,2-拼团成功,3-拼团失败)
     */
    @NotDatabaseField
    private Integer openState;

    @NotDatabaseField
    private Timestamp openCreateTime;

    @NotDatabaseField
    private PackCluster packCluster;

    @NotDatabaseField
    private List<PackClusterPerDto> packClusterPerDtos;

    @NotDatabaseField
    private String imgPath;

    @NotDatabaseField
    private java.sql.Timestamp endDay;

    @NotDatabaseField
    private String colonelOpenId;

    public String getColonelOpenId() {
        return colonelOpenId;
    }

    public void setColonelOpenId(String colonelOpenId) {
        this.colonelOpenId = colonelOpenId;
    }

    public Timestamp getEndDay() {
        return endDay;
    }

    public void setEndDay(Timestamp endDay) {
        this.endDay = endDay;
    }

    public Timestamp getOpenCreateTime() {
        return openCreateTime;
    }

    public void setOpenCreateTime(Timestamp openCreateTime) {
        this.openCreateTime = openCreateTime;
    }

    public Integer getOpenState() {
        return openState;
    }

    public void setOpenState(Integer openState) {
        this.openState = openState;
    }

    public List<PackClusterPerDto> getPackClusterPerDtos() {
        return packClusterPerDtos;
    }

    public void setPackClusterPerDtos(List<PackClusterPerDto> packClusterPerDtos) {
        this.packClusterPerDtos = packClusterPerDtos;
    }

    public PackCluster getPackCluster() {
        return packCluster;
    }

    public void setPackCluster(PackCluster packCluster) {
        this.packCluster = packCluster;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getOpenRecordId() {
        return openRecordId;
    }

    public void setOpenRecordId(Integer openRecordId) {
        this.openRecordId = openRecordId;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getPackClusterId() {
        return packClusterId;
    }

    public void setPackClusterId(Integer packClusterId) {
        this.packClusterId = packClusterId;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public Integer getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(Integer payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public String getImgSrc() {
        if (!ValidUtil.isEmpty(this.imgPath)) {
            return Constant.imgUrl + this.imgPath;
        }
        return "";
    }

    public Boolean getRefundState() {
        if (ValidUtil.isNotEmpty(payState) && ValidUtil.isNotEmpty(endDay)) {
            if (this.payState == 2 && this.endDay.getTime() < new Timestamp(System.currentTimeMillis()).getTime())
                return true;
        }
        return false;
    }
}
