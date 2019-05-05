package com.software5000.ma.entity;

import com.software5000.base.Constant;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/** 财务记录 */
public class Finance extends BaseEntity {
    public enum Daos {
        selectFinanceInOrOutComeDto("Finance.selectFinanceInOrOutComeDto"),
        selectPageFinance("Finance.selectPageFinance"),
        selectFinanceByDate("Finance.selectFinanceByDate"),
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    
    /**
    * 商家id 
    */
    private Integer businessId;
    
    /**
    * 类型 
    */
    private Integer financeType;
    
    /**
    * 说明 
    */
    private String explainInfo;
    
    /**
    * 商家收入金额
    */
    private Integer money;

    /**
     * 用户付款金额
     */
    private Integer userPayMoney;
    
    /**
    * 收款方式 
    */
    private Integer payType;
    
    /**
    * 各个订单id 
    */
    private Integer orderId;

    /**
     * 收银台id
     */
    private Integer payOrderId;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 收入 or 支出 (1表示收入，2表示支出)
     */
    private Integer inOrOutCome;

    @NotDatabaseField
    private String inOrOutComeDes;

    @NotDatabaseField
    private String payTypeDes;

    @NotDatabaseField
    private String financeTypeDes;


    public Integer getUserPayMoney() {
        return userPayMoney;
    }

    public void setUserPayMoney(Integer userPayMoney) {
        this.userPayMoney = userPayMoney;
    }

    public String getFinanceTypeDes() {
        return Constant.enumValues.get(Constant.FinanceType.class.getSimpleName()+","+financeType);
    }

    public String getPayTypeDes() {
        return Constant.enumValues.get(Constant.WorkOrderPayType.class.getSimpleName()+","+payType);
    }

    public String getInOrOutComeDes() {
        return Constant.enumValues.get(Constant.InOrOutCome.class.getSimpleName()+","+inOrOutCome);
    }
    public Integer getInOrOutCome() {
        return inOrOutCome;
    }

    public void setInOrOutCome(Integer inOrOutCome) {
        this.inOrOutCome = inOrOutCome;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public Integer getPayOrderId() {
        return payOrderId;
    }

    public void setPayOrderId(Integer payOrderId) {
        this.payOrderId = payOrderId;
    }

    public Integer getBusinessId(){
      return this.businessId;
    }
    
    public void setBusinessId(Integer businessId){
      this.businessId = businessId;
    }
    
    public Integer getFinanceType(){
      return this.financeType;
    }
    
    public void setFinanceType(Integer financeType){
      this.financeType = financeType;
    }

    public String getExplainInfo() {
        return explainInfo;
    }

    public void setExplainInfo(String explainInfo) {
        this.explainInfo = explainInfo;
    }

    public Integer getMoney(){
      return this.money;
    }
    
    public void setMoney(Integer money){
      this.money = money;
    }
    
    public Integer getPayType(){
      return this.payType;
    }
    
    public void setPayType(Integer payType){
      this.payType = payType;
    }
    
    public Integer getOrderId(){
      return this.orderId;
    }
    
    public void setOrderId(Integer orderId){
      this.orderId = orderId;
    }
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}