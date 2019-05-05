package com.software5000.bank.dto;

/**
 * Created by jiye on 2017/7/4.
 */
public class PayOrderDetailDto {
    /**
     * detailKey
     */
    private String dk;
    /**
     * detailValue
     */
    private String dv;

    public PayOrderDetailDto(String dk, String dv) {
        this.dk = dk;
        this.dv = dv;
    }

    public String getDk() {
        return dk;
    }

    public void setDk(String dk) {
        this.dk = dk;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }
}
