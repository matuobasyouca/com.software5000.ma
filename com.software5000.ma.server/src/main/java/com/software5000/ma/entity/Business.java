package com.software5000.ma.entity;

import com.software5000.base.Constant;
import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.AreaCode;
import com.software5000.base.entity.BaseEntity;
import com.zscp.master.util.ValidUtil;

import java.util.ArrayList;
import java.util.List;

/** 联盟商家 */
public class Business extends BaseEntity {
    public enum Daos {
        selectBusinessListPageByParam("Business.selectBusinessListPageByParam"),
        selectEnableBusinessListByIds("Business.selectEnableBusinessListByIds"),
        selectBusinessForOpen("Business.selectBusinessForOpen"),
        //emkt查询商家名称
        selectEnableBusinessNameList("Business.selectEnableBusinessNameList"),
        selectEnableBusByBusUserName("Business.selectEnableBusByBusUserName"),
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    
    /**
    * 商家名 
    */
    private String businessName;

    /**
    * 商家描述
    */
    private String businessDetail;

    /**
    * 状态
    */
    private Integer businessEnable;

    /**
     * 商家online状态
     */
    private  Integer businessLineState;

    /**
     * 地址
     */
    private String   businessAreaCode;

    /**
    * 地址
    */
    private String businessAddress;

    /**
    * 联系电话
    */
    private String businessTel;

    /**
    * 用户名
    */
    private String userName;

    /**
    * 密码
    */
    private String pwd;

    /**
    * 图片
    */
    private String imgPath;

    /**
    * 微信
    */
    private String businessWexin;

    /**
    * 坐标
    */
    private String businessCoordinate;

    /**
    * 排序
    */
    private String businessOrder;

    /**
    * 商家服务
    */
    private String businessServices;

    /**
     * 商家等级
     */
    private Integer businessLevel;

    /**
     * 提现微信(老板的微信)
     */
    private String wxOpenId;

    /**
     * 默认车牌
     */
    private String defaultCar;

    /**
     * 商家每月关账日
     */
    private Integer closingDateNum;
    /**
     * 顾问ID
     */
    private Integer consultantId;

    /**
     *顾问姓名
     */
    private String consultantName;
    /**
     *顾问部门
     */
    private String consultantDepartment;

    /**
     * 顾问部门id
     */
    private Integer consultantDepartmentId;

    public String getConsultantName() {
        return consultantName;
    }

    public void setConsultantName(String consultantName) {
        this.consultantName = consultantName;
    }

    public String getConsultantDepartment() {
        return consultantDepartment;
    }

    public void setConsultantDepartment(String consultantDepartment) {
        this.consultantDepartment = consultantDepartment;
    }

    public Integer getConsultantDepartmentId() {
        return consultantDepartmentId;
    }

    public void setConsultantDepartmentId(Integer consultantDepartmentId) {
        this.consultantDepartmentId = consultantDepartmentId;
    }

    public Integer getConsultantId() {
        return consultantId;
    }

    public void setConsultantId(Integer consultantId) {
        this.consultantId = consultantId;
    }

    @NotDatabaseField
    private String bossNickName;
    @NotDatabaseField
    private Integer bossId;

    /**
     * 套餐数量
     */
    @NotDatabaseField
    private Integer packageCount;

    @NotDatabaseField
    private String distance;
    @NotDatabaseField
    private String businessAreaCodeDes;
    @NotDatabaseField
    private Double memberBalance;

    public Double getMemberBalance() {
        return memberBalance;
    }

    public void setMemberBalance(Double memberBalance) {
        this.memberBalance = memberBalance;
    }

    public String getBusinessAreaCodeDes() {
        String des = "";
        if(ValidUtil.isNotEmpty(businessAreaCode)){
            String[] split = businessAreaCode.split(",");
            if(split.length==3){
                for (int i = 0; i < split.length; i++) {
                    AreaCode areaCode = Constant.getAreaObjById(split[i]);
                    des += areaCode.getFullName();
                }
                return des;
            }
        }
        return businessAreaCodeDes;
    }

    public Integer getBossId() {
        return bossId;
    }

    public void setBossId(Integer bossId) {
        this.bossId = bossId;
    }


    public String getBossNickName() {
        return bossNickName;
    }

    public void setBossNickName(String bossNickName) {
        this.bossNickName = bossNickName;
    }

    public Integer getClosingDateNum() {
        return closingDateNum;
    }

    public void setClosingDateNum(Integer closingDateNum) {
        this.closingDateNum = closingDateNum;
    }

    public String getWxOpenId() {
        return wxOpenId;
    }

    public void setWxOpenId(String wxOpenId) {
        this.wxOpenId = wxOpenId;
    }

    public String getBusinessName(){
      return this.businessName;
    }

    public void setBusinessName(String businessName){
      this.businessName = businessName;
    }

    public String getBusinessDetail(){
      return this.businessDetail;
    }

    public void setBusinessDetail(String businessDetail){
      this.businessDetail = businessDetail;
    }

    public Integer getBusinessEnable(){
      return this.businessEnable;
    }

    public void setBusinessEnable(Integer businessEnable){
      this.businessEnable = businessEnable;
    }

    public String getBusinessAddress(){
      return this.businessAddress;
    }

    public void setBusinessAddress(String businessAddress){
      this.businessAddress = businessAddress;
    }

    public String getBusinessTel(){
      return this.businessTel;
    }

    public void setBusinessTel(String businessTel){
      this.businessTel = businessTel;
    }

    public String getUserName(){
      return this.userName;
    }

    public void setUserName(String userName){
      this.userName = userName;
    }

    public String getPwd(){
      return this.pwd;
    }

    public void setPwd(String pwd){
      this.pwd = pwd;
    }

    public String getImgPath(){
      return this.imgPath;
    }

    public void setImgPath(String imgPath){
      this.imgPath = imgPath;
    }

    public String getBusinessWexin(){
      return this.businessWexin;
    }

    public void setBusinessWexin(String businessWexin){
      this.businessWexin = businessWexin;
    }

    public String getBusinessCoordinate(){
      return this.businessCoordinate;
    }

    public void setBusinessCoordinate(String businessCoordinate){
      this.businessCoordinate = businessCoordinate;
    }

    public String getBusinessOrder(){
      return this.businessOrder;
    }

    public void setBusinessOrder(String businessOrder){
      this.businessOrder = businessOrder;
    }

    public String getBusinessServices(){
      return this.businessServices;
    }

    public void setBusinessServices(String businessServices){
      this.businessServices = businessServices;
    }

    public Integer getBusinessLevel(){
      return this.businessLevel;
    }

    public void setBusinessLevel(Integer businessLevel){
      this.businessLevel = businessLevel;
    }

    public Integer getBusinessLineState() {
        return businessLineState;
    }

    public void setBusinessLineState(Integer businessLineState) {
        this.businessLineState = businessLineState;
    }

    public String getBusinessAreaCode() {
        return businessAreaCode;
    }

    public void setBusinessAreaCode(String businessAreaCode) {
        this.businessAreaCode = businessAreaCode;
    }

    public String getDefaultCar() {
        return defaultCar;
    }

    public void setDefaultCar(String defaultCar) {
        this.defaultCar = defaultCar;
    }

    public Integer getPackageCount() {
        return packageCount;
    }

    public void setPackageCount(Integer packageCount) {
        this.packageCount = packageCount;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    /******************其他业务信息********************/

    public List getAreaList() {
        List areaList = new ArrayList();
        if(!ValidUtil.isEmpty(businessAreaCode)){
            String [] codeArray = businessAreaCode.split(",");
            for (String code :codeArray){
                areaList.add(Constant.getAreaObjById(code)) ;
            }
        }
        return areaList;
    }

    public String getBusinessLineStateDes(){
        if(!ValidUtil.isEmpty(businessLineState)){
            return  Constant.enumValues.get(Constant.BusinessLineState.class.getSimpleName()+","+businessLineState);
        }
        return null;
    }
    public  String getImageShowPath(){
        if(!ValidUtil.isEmpty(imgPath)){
            return  Constant.imgUrl+imgPath;
        }
        return null;
    }
}