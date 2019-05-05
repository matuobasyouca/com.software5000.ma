package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.sql.Timestamp;
import java.util.List;

/**
 * 拼团记录表
 * Created by luo on 2017/08/22.
 */
public class PackClusterOpenRecord extends BaseEntity {
    public enum Daos {
        selectSuccessNum("PackClusterOpenRecord.selectSuccessNum"),
        selectTimeOutPackCluster("PackClusterOpenRecord.selectTimeOutPackCluster"),
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
     * 拼团单号
     */
    private String packClusterNo;

    /**
     * 团长openId
     */
    private String colonelOpenId;

    /**
     *开团结束时间
     */
    private java.sql.Timestamp endDay;

    /**
     * 拼团状态(1-拼团中,2-拼团成功,3-拼团失败)
     */
    private Integer state;

    @NotDatabaseField
    private List<PackClusterBuyRecord> packClusterBuyRecords;

    public List<PackClusterBuyRecord> getPackClusterBuyRecords() {
        return packClusterBuyRecords;
    }

    public void setPackClusterBuyRecords(List<PackClusterBuyRecord> packClusterBuyRecords) {
        this.packClusterBuyRecords = packClusterBuyRecords;
    }

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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPackClusterId() {
        return packClusterId;
    }

    public void setPackClusterId(Integer packClusterId) {
        this.packClusterId = packClusterId;
    }

    public String getPackClusterNo() {
        return packClusterNo;
    }

    public void setPackClusterNo(String packClusterNo) {
        this.packClusterNo = packClusterNo;
    }
}
