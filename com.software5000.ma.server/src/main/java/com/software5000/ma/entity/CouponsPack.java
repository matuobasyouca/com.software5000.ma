package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;

/**
 * Created by Jiang on 2017/07/01.
 */
public class CouponsPack extends BaseEntity {

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 购买金额
     */
    private Double buyAmouont;

    /**
     * 卡券列表（逗号分割）
     */
    private String cpUuids;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Double getBuyAmouont() {
        return buyAmouont;
    }

    public void setBuyAmouont(Double buyAmouont) {
        this.buyAmouont = buyAmouont;
    }

    public String getCpUuids() {
        return cpUuids;
    }

    public void setCpUuids(String cpUuids) {
        this.cpUuids = cpUuids;
    }
}
