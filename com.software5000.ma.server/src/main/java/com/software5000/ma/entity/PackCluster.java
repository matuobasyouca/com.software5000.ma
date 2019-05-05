package com.software5000.ma.entity;

import com.software5000.base.Constant;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;
import com.zscp.master.util.ValidUtil;

import java.sql.Timestamp;
import java.util.List;

/**
 * 拼团活动表
 * Created by luo on 2017/08/22.
 */
public class PackCluster extends BaseEntity {
    public enum Daos {
        selectPackClusterById("PackCluster.selectPackClusterById"),
        selectPackClusterByPage("PackCluster.selectPackClusterByPage"),
        updatePackClusterState("PackCluster.updatePackClusterState"),
        ;
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 活动名称
     */
    private String clusterName;

    /**
     * 分享图片
     */
    private String shareImgPath;

    /**
     *活动开始时间
     */
    private java.sql.Timestamp beginDay;

    /**
     *活动结束时间
     */
    private java.sql.Timestamp endDay;

    /**
     *商家编号
     */
    private Integer businessId;

    /**
     *拼团价格
     */
    private Double salePrice;

    /**
     *补贴价格
     */
    private Double subsidyPrice;

    /**
     *成团人数
     */
    private Integer clusterNum;

    /**
     *拼团时限（小时）
     */
    private Integer clusterHour;

    /**
     *活动说明
     */
    private String description;

    /**
     *活动状态
     */
    private Integer state;

    /**
     * 拼团套餐id
     */
    private Integer packId;

    /**
     * 是否可以修改删除
     */
    private Integer canEdit;

    /**
     * 分享描述
     */
    private String shareDescription;

    /**
     * 附加文本
     */
    private String attachedText;

    /**
     * 活动图片
     */
    @NotDatabaseField
    private List<PackClusterImg> packClusterImgs;

    @NotDatabaseField
    private BusinessPackage businessPackage;

    /**
     * 已成团数
     */
    @NotDatabaseField
    private Integer successNum;

    /**
     *商家名称
     */
    @NotDatabaseField
    private String businessName;

    /**
     *活动状态
     */
    @NotDatabaseField
    private String stateDesc;

    /**
     * 套餐名
     */
    @NotDatabaseField
    private String packageName;

    /**
     * 图片地址（带域名）
     */
    @NotDatabaseField
    private String shareImgPathUrl;

    public String getAttachedText() {
        return attachedText;
    }

    public void setAttachedText(String attachedText) {
        this.attachedText = attachedText;
    }

    public String getShareDescription() {
        return shareDescription;
    }

    public void setShareDescription(String shareDescription) {
        this.shareDescription = shareDescription;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getSuccessNum() {
        return successNum;
    }

    public void setSuccessNum(Integer successNum) {
        this.successNum = successNum;
    }

    public BusinessPackage getBusinessPackage() {
        return businessPackage;
    }

    public void setBusinessPackage(BusinessPackage businessPackage) {
        this.businessPackage = businessPackage;
    }

    public String getClusterName() {
        return clusterName;
    }

    public void setClusterName(String clusterName) {
        this.clusterName = clusterName;
    }

    public String getShareImgPath() {
        return shareImgPath;
    }

    public void setShareImgPath(String shareImgPath) {
        this.shareImgPath = shareImgPath;
    }

    public Timestamp getBeginDay() {
        return beginDay;
    }

    public void setBeginDay(Timestamp beginDay) {
        this.beginDay = beginDay;
    }

    public Timestamp getEndDay() {
        return endDay;
    }

    public void setEndDay(Timestamp endDay) {
        this.endDay = endDay;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public Double getSalePrice() {
        return salePrice;
    }

    public void setSalePrice(Double salePrice) {
        this.salePrice = salePrice;
    }

    public Double getSubsidyPrice() {
        return subsidyPrice;
    }

    public void setSubsidyPrice(Double subsidyPrice) {
        this.subsidyPrice = subsidyPrice;
    }

    public Integer getClusterNum() {
        return clusterNum;
    }

    public void setClusterNum(Integer clusterNum) {
        this.clusterNum = clusterNum;
    }

    public Integer getClusterHour() {
        return clusterHour;
    }

    public void setClusterHour(Integer clusterHour) {
        this.clusterHour = clusterHour;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPackId() {
        return packId;
    }

    public void setPackId(Integer packId) {
        this.packId = packId;
    }

    public List<PackClusterImg> getPackClusterImgs() {
        return packClusterImgs;
    }

    public void setPackClusterImgs(List<PackClusterImg> packClusterImgs) {
        this.packClusterImgs = packClusterImgs;
    }

    public Integer getCanEdit() {
        return canEdit;
    }

    public void setCanEdit(Integer canEdit) {
        this.canEdit = canEdit;
    }

    public String getStateDesc() {
        return Constant.enumValues.get(Constant.PackageState.class.getSimpleName()+","+state);
    }

    public void setStateDesc(String stateDesc) {
        this.stateDesc = stateDesc;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getShareImgPathUrl() {
        if(!ValidUtil.isEmpty(this.shareImgPath)){
            return Constant.imgUrl+this.shareImgPath;
        }else{
            return null;
        }

    }

    public void setShareImgPathUrl(String shareImgPathUrl) {
        this.shareImgPathUrl = shareImgPathUrl;
    }
}
