package com.software5000.ma.entity;

import com.software5000.base.Constant;
import com.software5000.base.entity.BaseEntity;

/**
 * 订单图片
 * Created by hejianrong on 2017/2/13.
 */
public class WorkOrderImg extends BaseEntity {

    public enum Daos {
        ;
        public String sqlMapname;
        private Daos(String name) {
            this.sqlMapname = name;
        }
        public String toString() {
            return this.sqlMapname;
        }
    }

    /**
     * 图片地址
     */
    private String orderImg;

    /**
     * 工单ID
     */
    private Integer workOrderId;

    public String getOrderImgSrc() {
        return Constant.imgUrl+ orderImg;
    }

    public String getOrderImg() {
        return orderImg;
    }

    public void setOrderImg(String orderImg) {
        this.orderImg = orderImg;
    }

    public Integer getWorkOrderId() {
        return workOrderId;
    }

    public void setWorkOrderId(Integer workOrderId) {
        this.workOrderId = workOrderId;
    }
}
