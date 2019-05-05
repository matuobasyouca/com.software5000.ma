package com.software5000.ma.entity;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;

/**
 * Created by wujin on 2017/1/5.
 */
public class BookingDetail extends BaseEntity {
    public enum Daos {
        //TODO
        ;

        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 预约id
     */
    private Integer bookingId;

    /**
     * 预约实体
     */
    @NotDatabaseField
    private Booking booking;

    /**
     * 预约服务项id
     */
    private Integer serviceItemId;

    /**
     * 预约的服务项
     */
    @NotDatabaseField
    private ServiceItem serviceItem;

    public Integer getBookingId() {
        return bookingId;
    }

    public void setBookingId(Integer bookingId) {
        this.bookingId = bookingId;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public Integer getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Integer serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public ServiceItem getServiceItem() {
        return serviceItem;
    }

    public void setServiceItem(ServiceItem serviceItem) {
        this.serviceItem = serviceItem;
    }

}
