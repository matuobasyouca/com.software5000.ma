package com.software5000.ma.task.dto;

import com.software5000.base.NotDatabaseField;
import com.software5000.ma.entity.WeChatPayOrder;

import java.util.List;

/**
 * 对账单定时任务dto
 * Created by wujin on 2017/3/7.
 */
public class WeChatPayOrderTaskDto {
    private Integer businessId;
    @NotDatabaseField
    private List<WeChatPayOrder> weChatPayOrders;

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public List<WeChatPayOrder> getWeChatPayOrders() {
        return weChatPayOrders;
    }

    public void setWeChatPayOrders(List<WeChatPayOrder> weChatPayOrders) {
        this.weChatPayOrders = weChatPayOrders;
    }
}
