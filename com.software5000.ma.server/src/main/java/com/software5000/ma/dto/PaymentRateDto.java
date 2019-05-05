package com.software5000.ma.dto;

import com.zscp.master.util.MathUtil;

/**
 * Created by jiye on 2017/7/28.
 */
public class PaymentRateDto {
    /**
     * 开单数
     */
    private Integer workOrderNum;

    /**
     * 微信支付数
     */
    private Integer wxPayNum;

    /**
     * 商家名称
     */
    private String businessName;

    /**
     * 商家Id
     */
    private Integer id;

    /**
     * 开单率（微信支付数/开单数）
     */
    private String orderRate;


    public String getOrderRate() {
        if(wxPayNum==null||workOrderNum==null||workOrderNum==0||wxPayNum==0) return "0%";
        return MathUtil.mul(MathUtil.div(wxPayNum.doubleValue(), workOrderNum.doubleValue(), 4),100)+"%";
    }

    public void setOrderRate(String orderRate) {
        this.orderRate = orderRate;
    }

    public Integer getWorkOrderNum() {
        return workOrderNum;
    }

    public void setWorkOrderNum(Integer workOrderNum) {
        this.workOrderNum = workOrderNum;
    }

    public Integer getWxPayNum() {
        return wxPayNum;
    }

    public void setWxPayNum(Integer wxPayNum) {
        this.wxPayNum = wxPayNum;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
