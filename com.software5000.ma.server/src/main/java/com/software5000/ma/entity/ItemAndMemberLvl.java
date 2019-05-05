package com.software5000.ma.entity;

import com.software5000.base.entity.BaseEntity;

/**服务项会员等级**/
public class ItemAndMemberLvl extends BaseEntity {
    public enum Daos {
        selectItemAndMemberLvlByParam("ItemAndMemberLvl.selectItemAndMemberLvlByParam"),
        deleteItemAndMemberLvlByParam("ItemAndMemberLvl.deleteItemAndMemberLvlByParam"),
        ;

        public String sqlMapname;
        private Daos(String name) { this.sqlMapname = name; }
        public String toString() { return this.sqlMapname; }
    }

    /**
     * 会员等级id
     */
    private Integer memberLvlId;

    /**
     * 服务项目类型
     */
    private Integer itemType;

    /**
     * 服务项目id
     */
    private Integer serviceItemId;

    /**
     * 1.会员折扣，2.会员价
     */
    private Integer discountType;

    /**
     * 优惠
     */
    private Double discountNumber;

    public Integer getMemberLvlId() {
        return memberLvlId;
    }

    public void setMemberLvlId(Integer memberLvlId) {
        this.memberLvlId = memberLvlId;
    }

    public Integer getItemType() {
        return itemType;
    }

    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    public Integer getServiceItemId() {
        return serviceItemId;
    }

    public void setServiceItemId(Integer serviceItemId) {
        this.serviceItemId = serviceItemId;
    }

    public Integer getDiscountType() {
        return discountType;
    }

    public void setDiscountType(Integer discountType) {
        this.discountType = discountType;
    }

    public Double getDiscountNumber() {
        return discountNumber;
    }

    public void setDiscountNumber(Double discountNumber) {
        this.discountNumber = discountNumber;
    }
}
