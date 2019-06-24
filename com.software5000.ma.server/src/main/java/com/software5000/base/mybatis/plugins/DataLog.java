package com.software5000.base.mybatis.plugins;

import com.software5000.base.entity.BaseEntity;

public class DataLog extends BaseEntity {

    public Long changeMainKey;

    public String changeMainTable;

    public int changeType;

    public String oldValueJson;

    public String newValueJson;

    public Long getChangeMainKey() {
        return changeMainKey;
    }

    public void setChangeMainKey(Long changeMainKey) {
        this.changeMainKey = changeMainKey;
    }

    public String getChangeMainTable() {
        return changeMainTable;
    }

    public void setChangeMainTable(String changeMainTable) {
        this.changeMainTable = changeMainTable;
    }

    public int getChangeType() {
        return changeType;
    }

    public void setChangeType(int changeType) {
        this.changeType = changeType;
    }

    public String getOldValueJson() {
        return oldValueJson;
    }

    public void setOldValueJson(String oldValueJson) {
        this.oldValueJson = oldValueJson;
    }

    public String getNewValueJson() {
        return newValueJson;
    }

    public void setNewValueJson(String newValueJson) {
        this.newValueJson = newValueJson;
    }
}
