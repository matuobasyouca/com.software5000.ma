package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;


/**
 * Created by jiye on 2017/7/10.
 */
public class UserExcelData extends BaseEntity {
    private String realName; //客户姓名
    private String carNumber;//车牌号
    private String mobile; //客户手机号
    private String packageName;//套餐名称
    private String validDate;//有效期
    private String serviceItem1;
    private String restTime1;
    private String serviceItem2;
    private String restTime2;
    private String serviceItem3;
    private String restTime3;
    private Integer successFlag;//是否可以导入（1为成功数据,2为失败数据）
    private String remark;
    private Integer businessId;
    private String memberBalance;//会员余额
    private String serviceItem4;
    private String restTime4;
    private String serviceItem5;
    private String restTime5;

    public String getServiceItem4() {
        return serviceItem4;
    }

    public void setServiceItem4(String serviceItem4) {
        this.serviceItem4 = serviceItem4;
    }

    public String getRestTime4() {
        return restTime4;
    }

    public void setRestTime4(String restTime4) {
        this.restTime4 = restTime4;
    }

    public String getServiceItem5() {
        return serviceItem5;
    }

    public void setServiceItem5(String serviceItem5) {
        this.serviceItem5 = serviceItem5;
    }

    public String getRestTime5() {
        return restTime5;
    }

    public void setRestTime5(String restTime5) {
        this.restTime5 = restTime5;
    }

    public String getMemberBalance() {
        return memberBalance;
    }

    public void setMemberBalance(String memberBalance) {
        this.memberBalance = memberBalance;
    }

    public Integer getSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(Integer successFlag) {
        this.successFlag = successFlag;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getValidDate() {
        return validDate;
    }

    public void setValidDate(String validDate) {
        this.validDate = validDate;
    }

    public String getServiceItem1() {
        return serviceItem1;
    }

    public void setServiceItem1(String serviceItem1) {
        this.serviceItem1 = serviceItem1;
    }

    public String getRestTime1() {
        return restTime1;
    }

    public void setRestTime1(String restTime1) {
        this.restTime1 = restTime1;
    }

    public String getServiceItem2() {
        return serviceItem2;
    }

    public void setServiceItem2(String serviceItem2) {
        this.serviceItem2 = serviceItem2;
    }

    public String getRestTime2() {
        return restTime2;
    }

    public void setRestTime2(String restTime2) {
        this.restTime2 = restTime2;
    }

    public String getServiceItem3() {
        return serviceItem3;
    }

    public void setServiceItem3(String serviceItem3) {
        this.serviceItem3 = serviceItem3;
    }

    public String getRestTime3() {
        return restTime3;
    }

    public void setRestTime3(String restTime3) {
        this.restTime3 = restTime3;
    }
}
