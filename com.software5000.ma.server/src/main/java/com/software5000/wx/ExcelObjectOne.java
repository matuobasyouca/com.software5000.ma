package com.software5000.wx;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

import java.util.Date;

public class ExcelObjectOne extends BaseRowModel {
    @ExcelProperty(index = 12)
    private String p1; // 车牌

    @ExcelProperty(index = 1)
    private String p2; // 车型

    @ExcelProperty(index = 2)
    private String p3; // 售价

    @ExcelProperty(index = 3)
    private String p4;//车身颜色

    @ExcelProperty(index = 4,format = "yyyy/MM/dd")
    private Date p5; // 上牌时间

    @ExcelProperty(index = 5)
    private String p6; // 公里数

    @ExcelProperty(index = 6)
    private String p7; // 公里数

    @ExcelProperty(index = 7)
    private String p8; // 公里数

    @ExcelProperty(index = 8)
    private String p9; // 公里数

    @ExcelProperty(index = 9)
    private String p10; // 公里数

    @ExcelProperty(index = 10,format = "yyyy/MM/dd")
    private Date p11; // 公里数

    @ExcelProperty(index = 11,format = "yyyy/MM/dd")
    private Date p12; // 公里数

    @ExcelProperty(index = 0)
    private String p13; // 公里数

    public String getP1() {
        return p1;
    }

    public void setP1(String p1) {
        this.p1 = p1;
    }

    public String getP2() {
        return p2;
    }

    public void setP2(String p2) {
        this.p2 = p2;
    }

    public String getP3() {
        return p3;
    }

    public void setP3(String p3) {
        this.p3 = p3;
    }

    public String getP4() {
        return p4;
    }

    public void setP4(String p4) {
        this.p4 = p4;
    }

    public Date getP5() {
        return p5;
    }

    public void setP5(Date p5) {
        this.p5 = p5;
    }

    public String getP6() {
        return p6;
    }

    public void setP6(String p6) {
        this.p6 = p6;
    }

    public String getP7() {
        return p7;
    }

    public void setP7(String p7) {
        this.p7 = p7;
    }

    public String getP8() {
        return p8;
    }

    public void setP8(String p8) {
        this.p8 = p8;
    }

    public String getP9() {
        return p9;
    }

    public void setP9(String p9) {
        this.p9 = p9;
    }

    public String getP10() {
        return p10;
    }

    public void setP10(String p10) {
        this.p10 = p10;
    }

    public Date getP11() {
        return p11;
    }

    public void setP11(Date p11) {
        this.p11 = p11;
    }

    public Date getP12() {
        return p12;
    }

    public void setP12(Date p12) {
        this.p12 = p12;
    }

    public String getP13() {
        return p13;
    }

    public void setP13(String p13) {
        this.p13 = p13;
    }
}
