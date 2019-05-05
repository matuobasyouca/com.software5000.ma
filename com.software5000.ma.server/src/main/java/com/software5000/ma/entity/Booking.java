package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

import java.sql.Timestamp;
import java.util.List;

/** 用户预约 */
public class Booking extends BaseEntity {
    public enum Daos {
        selectBookingAllDataByParam("Booking.selectBookingAllDataByParam"),
        countBooking("Booking.countBooking"),
        selectBookingByList("Booking.selectBookingByList"),
        selectBookingDataByIds("Booking.selectBookingDataByIds"),
        selectUserCenterBooking("Booking.selectUserCenterBooking"),
        selectSingleBookingDetailById("Booking.selectSingleBookingDetailById")
        ;
    
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 预约编号
     */
    private String bookingNo;

    /**
     * 预约的用户
     */
    @NotDatabaseField
    private User user;

    /**
     * 用户id
     */
    private Integer userId;

    /**
    * 无效字段
    */
    @NotDatabaseField
    private java.util.Date bookingTime;

    /**
    * 预约商家
    */
    private Integer businessId;

    /**
    * 预约商家
    */

    @NotDatabaseField
    private Business business;

    /**
    * 预约状态
    */
    private Integer state;

    /**
    * 预约服务的时间段
    */
    private Timestamp serviceTime;

    /**
    * 预约的服务
    */
    @NotDatabaseField
    private List<BookingDetail> bookingDetails;


    public String getBookingNo() {
        return bookingNo;
    }

    public void setBookingNo(String bookingNo) {
        this.bookingNo = bookingNo;
    }

    public java.util.Date getBookingTime(){
      return this.bookingTime;
    }

    public void setBookingTime(java.util.Date bookingTime){
      this.bookingTime = bookingTime;
    }

    public Integer getBusinessId(){
      return this.businessId;
    }

    public void setBusinessId(Integer businessId){
      this.businessId = businessId;
    }


    public Business getBusiness(){
      return this.business;
    }

    public void setBusiness(Business business){
      this.business = business;
    }

    public Integer getState(){
      return this.state;
    }

    public void setState(Integer state){
      this.state = state;
    }

    public Timestamp getServiceTime() {
        return serviceTime;
    }

    public void setServiceTime(Timestamp serviceTime) {
        this.serviceTime = serviceTime;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<BookingDetail> getBookingDetails() {
        return bookingDetails;
    }

    public void setBookingDetails(List<BookingDetail> bookingDetails) {
        this.bookingDetails = bookingDetails;
    }
}