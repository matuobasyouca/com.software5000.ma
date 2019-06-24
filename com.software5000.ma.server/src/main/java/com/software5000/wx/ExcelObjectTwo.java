package com.software5000.wx;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;

public class ExcelObjectTwo extends BaseRowModel {
    @ExcelProperty(index = 9)
    private String p1; // 维修车辆车牌号

    @ExcelProperty(index = 8)
    private String p2; // 维修车辆车架号

    @ExcelProperty(index = 14)
    private String p3; // 项目类型名称

    @ExcelProperty(index = 15)
    private String p4;//分类

    @ExcelProperty(index = 16)
    private String p5; // 项目名称

    @ExcelProperty(index = 22)
    private String p6; // 是否托外

    @ExcelProperty(index = 39)
    private String p7; // 车架号

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

    public String getP5() {
        return p5;
    }

    public void setP5(String p5) {
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
}
