package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.sql.Timestamp;
import java.util.List;

/**
 * 工单
 */
public class WorkOrder extends BaseEntity {
    public enum Daos {
        selectMoreInfoByParam("WorkOrder.selectMoreInfoByParam"),
        selectWorkOrderPage("WorkOrder.selectWorkOrderPage"),
        countServiceItemUseInWorkOrder("WorkOrder.countServiceItemUseInWorkOrder"),
        selectWorkOrderForExcel("WorkOrder.selectWorkOrderForExcel"),
        selectWorkOrderStateCount("WorkOrder.selectWorkOrderStateCount"),
        selectCountWorkOrder("WorkOrder.selectCountWorkOrder"),
        selectWorkOrderNoPaidCount("WorkOrder.selectWorkOrderNoPaidCount"),
        selectServiceItemIdForOperator("WorkOrder.selectServiceItemIdForOperator"),
        selectWorkOrderPaymentRate("WorkOrder.selectWorkOrderPaymentRate"),
        selectPagePaymentRateDto("WorkOrder.selectPagePaymentRateDto"),
        selectWorkOrderByParam("WorkOrder.selectWorkOrderByParam"),
        selectPageByWorkOrderIds("WorkOrder.selectPageByWorkOrderIds"),
        selectInitialByOpenId("WorkOrder.selectInitialByOpenId"),
        /**
         * 根据工单id查询工单详情
         */
        selectWorkOrderDtoById("WorkOrder.selectWorkOrderDtoById"),
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
     * 商家
     */
    private Integer businessId;

    /**
     * 商家名称
     */
    @NotDatabaseField
    private String businessName;

    /**
     * 订单编号
     */
    private String orderNo;

    /**
     * 总金额
     */
    private Double totalPrice;

    /**
     * 车牌号
     */
    private String carNumber;

    /**
     * 订单详情
     */
    @NotDatabaseField
    private List<WorkOrderDetail> workOrderDetails;

    /**
     * 订单施工图片
     */
    @NotDatabaseField
    private List<WorkOrderImg> workOrderImgs;

    /**
     * 下单用户
     */
    private Integer userId;

    /**
     * 用户姓名
     */
    @NotDatabaseField
    private String realName;

    /**
     * 下单用户
     */

    @NotDatabaseField
    private User user;

    /**
     * 订单状态
     */
    private Integer state;

    /**
     * 订单完成时间
     */
    private java.util.Date commitTime;

    /**
     * 联系方式
     */
    private String mobile;

    /**
     * 商家减免
     */
    private Double businessDeduct;

    /**
     * 材料费用
     */
    private Double materialCost;

    /**
     * 优惠券id
     */
    private String couponUuid;

    /**
     * 卡券名称
     */
    private String couponName;

    /**
     * 每张卡券的扣减
     */
    private String couponEveryDeduct;

    /**
     * 优惠券扣减
     */
    private Double couponDeduct;

    /**
     * 会员折扣
     */
    private Double discountDeduct;

    /**
     * 打折数
     */
    private Double discountNum;

    /**
     * 余额扣减
     */
    private Double balanceDeduct;

    /**
     * 付款方式
     */
    private Integer payType;

    /**
     * 付款时间
     */
    private Timestamp payTime;

    /**
     * 备注
     */
    private String comment;

    /**
     * 项目名称
     */
    @NotDatabaseField
    private String itemNames;

    /**
     * 是为否反结算工单
     */
    @NotDatabaseField
    private boolean isOpposite;

    /**
     * 会员等级名称
     */
    @NotDatabaseField
    private String lvlName;

    /**
     * 商家会员ID
     */
    @NotDatabaseField
    private Integer recordId;

    public boolean isOpposite() {
        return isOpposite;
    }

    public void setOpposite(boolean opposite) {
        isOpposite = opposite;
    }

    public Double getDiscountDeduct() {
        return discountDeduct;
    }

    public void setDiscountDeduct(Double discountDeduct) {
        this.discountDeduct = discountDeduct;
    }

    public Double getDiscountNum() {
        return discountNum;
    }

    public void setDiscountNum(Double discountNum) {
        this.discountNum = discountNum;
    }

    public String getCouponUuid() {
        return couponUuid;
    }

    public void setCouponUuid(String couponUuid) {
        this.couponUuid = couponUuid;
    }

    public Double getCouponDeduct() {
        return couponDeduct;
    }

    public void setCouponDeduct(Double couponDeduct) {
        this.couponDeduct = couponDeduct;
    }

    public Double getMaterialCost() {
        return materialCost;
    }

    public void setMaterialCost(Double materialCost) {
        this.materialCost = materialCost;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Double getBusinessDeduct() {
        return businessDeduct;
    }

    public void setBusinessDeduct(Double businessDeduct) {
        this.businessDeduct = businessDeduct;
    }

    public Integer getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    public String getOrderNo() {
        return this.orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Double getTotalPrice() {
        return this.totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getCarNumber() {
        return this.carNumber;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public List<WorkOrderDetail> getWorkOrderDetails() {
        return workOrderDetails;
    }

    public void setWorkOrderDetails(List<WorkOrderDetail> workOrderDetails) {
        this.workOrderDetails = workOrderDetails;
    }

    public List<WorkOrderImg> getWorkOrderImgs() {
        return workOrderImgs;
    }

    public void setWorkOrderImgs(List<WorkOrderImg> workOrderImgs) {
        this.workOrderImgs = workOrderImgs;
    }

    public Integer getUserId() {
        return this.userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }


    public java.util.Date getCommitTime() {
        return this.commitTime;
    }

    public void setCommitTime(java.util.Date commitTime) {
        this.commitTime = commitTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getPayType() {
        return payType;
    }

    public void setPayType(Integer payType) {
        this.payType = payType;
    }

    public Timestamp getPayTime() {
        return payTime;
    }

    public void setPayTime(Timestamp payTime) {
        this.payTime = payTime;
    }

    public String getItemNames() {
        return itemNames;
    }

    public void setItemNames(String itemNames) {
        this.itemNames = itemNames;
    }

    public String getCouponName() {
        return couponName;
    }

    public void setCouponName(String couponName) {
        this.couponName = couponName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getLvlName() {
        return lvlName;
    }

    public void setLvlName(String lvlName) {
        this.lvlName = lvlName;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public String getCouponEveryDeduct() {
        return couponEveryDeduct;
    }

    public void setCouponEveryDeduct(String couponEveryDeduct) {
        this.couponEveryDeduct = couponEveryDeduct;
    }

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public Double getBalanceDeduct() {
        return balanceDeduct;
    }

    public void setBalanceDeduct(Double balanceDeduct) {
        this.balanceDeduct = balanceDeduct;
    }
}