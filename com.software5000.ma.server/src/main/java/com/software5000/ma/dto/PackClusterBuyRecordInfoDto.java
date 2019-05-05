package com.software5000.ma.dto;

import com.software5000.base.Constant;
import com.zscp.master.util.ValidUtil;

import java.sql.Timestamp;

/**
 * Created by xiejianxiong on 2017/8/24.
 */
public class PackClusterBuyRecordInfoDto {

    /**
     * 拼团记录的id
     */
    private Integer id;

    /**
     *拼团价
     */
    private Double psalePrice;

    /**
     *支付状态(1-未支付,2已支付)
     */
    private Integer payState;

    /**
     * 商家名
     */
    private String businessName;

    /**
     * 套餐名
     */
    private String packageName;

    /**
     * 有效期
     */
    private Integer validTerm;

    /**
     * 原价
     */
    private Double bsalePrice;

    /**
     * 拼团状态(1-拼团中,2-拼团成功,3-拼团失败)
     */
    private Integer state;

    /**
     *支付时间
     */
    private Timestamp payTime;

    /**
     * 图片
     */
    private String imgPath;

    /**
     * 拼团活动的Id
     */
    private Integer packClusterId;

    private Timestamp endDay;

    /**
     * 退款中记录标记
     */
    private String des;

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    /**
     * 统计不同拼团状态下的数量
     */
    private Integer count;

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Timestamp getEndDay() { return endDay; }

    public void setEndDay(Timestamp endDay) { this.endDay = endDay; }

    public Integer getPackClusterId() {return packClusterId;}

    public void setPackClusterId(Integer packClusterId) {this.packClusterId = packClusterId;}

    public String getImgPath() {return imgPath;}

    public void setImgPath(String imgPath) {this.imgPath = imgPath;}

    public Integer getId() {return id;}

    public void setId(Integer id) {this.id = id;}

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public Double getPsalePrice() {
        return psalePrice;
    }

    public void setPsalePrice(Double psalePrice) {
        this.psalePrice = psalePrice;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public Integer getValidTerm() {
        return validTerm;
    }

    public void setValidTerm(Integer validTerm) {
        this.validTerm = validTerm;
    }

    public Double getBsalePrice() {
        return bsalePrice;
    }

    public void setBsalePrice(Double bsalePrice) {
        this.bsalePrice = bsalePrice;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getImgSrc() {
        if(!ValidUtil.isEmpty(this.imgPath)) {
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

    public String getStateDes() {
        if (this.state != null) {
            return Constant.enumValues.get(Constant.PackClusterOpenRecordState.class.getSimpleName() + "," + this.state);
        }
        return null;
    }
}
