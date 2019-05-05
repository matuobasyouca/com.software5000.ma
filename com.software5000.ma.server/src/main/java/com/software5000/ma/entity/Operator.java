package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;

/**
 * Created by luo on 2017/5/02.
 */

/** 运营*/
public class Operator extends BaseEntity {
    public enum Daos {
        ;
        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 运营账号
     */
    private String userName;
    /**
     * 密码
     */
    private String password;
    /**
     * 真实姓名
     */
    private String realName;
    /**
     * 状态（0-有效，1-无效）
     */
    private Integer state;

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public String getRealName() {
        return realName;
    }

    public Integer getState() {
        return state;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    /*** 其他业务数据 ***************/
}
