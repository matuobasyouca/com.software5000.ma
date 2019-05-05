package com.software5000.ma.entity;

import com.software5000.base.Constant;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/**
 * 拼团活动图片
 * Created by luo on 2017/08/22.
 */
public class PackClusterImg extends BaseEntity {
    public enum Daos {
        ;
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 活动编号
     */
    private Integer packClusterId;

    /**
     * 图片
     */
    private String imgPath;

    /**
     * 图片序号
     */
    private Integer imgSort;

    /**
     * 图片地址（带域名）
     */
    @NotDatabaseField
    private String imgPathUrl;

    public Integer getPackClusterId() {
        return packClusterId;
    }

    public void setPackClusterId(Integer packClusterId) {
        this.packClusterId = packClusterId;
    }

    public String getImgPath() {
        return imgPath;
    }

    public void setImgPath(String imgPath) {
        this.imgPath = imgPath;
    }

    public Integer getImgSort() {
        return imgSort;
    }

    public void setImgSort(Integer imgSort) {
        this.imgSort = imgSort;
    }

    public String getImgPathUrl() {
        return Constant.imgUrl+this.imgPath;
    }

    public void setImgPathUrl(String imgPathUrl) {
        this.imgPathUrl = imgPathUrl;
    }
}
