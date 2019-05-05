package com.software5000.base.entity;

/**
 * Created by jinbo on 2017/3/25.
 */
public class AreaCode extends BaseEntity {
    public enum Daos {

        ;
        public String sqlMapname;

        private Daos(String name) {
            this.sqlMapname = name;
        }

        public String toString() {
            return this.sqlMapname;
        }
    }

    private String areaName;
    private String fullName;
    private Integer parentId;
    private Integer type;
    private Integer state;

    public AreaCode() {
    }

    public AreaCode(Integer id) {
        this.setId(id);
    }

    public String getAreaName() {
        return areaName;
    }

    public void setAreaName(String areaName) {
        this.areaName = areaName;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        try {
            return this.getId().equals(((AreaCode) obj).getId());
        } catch (Exception e) {
            return false;
        }
    }
}
