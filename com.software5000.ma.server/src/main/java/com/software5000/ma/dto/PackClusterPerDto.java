package com.software5000.ma.dto;

/**
 * 参团人的信息
 * Created by jiye on 2017/8/24.
 */
public class PackClusterPerDto {
    private String wxHeadImg;//微信头像
    private String wxOpenId;//微信id
    private String headerWxId;//团长微信id
    private Boolean header;//是否团长

    public String getWxHeadImg() {
        return wxHeadImg;
    }

    public void setWxHeadImg(String wxHeadImg) {
        this.wxHeadImg = wxHeadImg;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getHeaderWxId() {
        return headerWxId;
    }

    public void setHeaderWxId(String headerWxId) {
        this.headerWxId = headerWxId;
    }

    public Boolean getHeader() {
        return headerWxId.equals(wxOpenId);
    }

    public void setHeader(Boolean header) {
        this.header = header;
    }
}
