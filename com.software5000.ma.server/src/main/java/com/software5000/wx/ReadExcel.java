package com.software5000.wx;

import com.alibaba.excel.EasyExcelFactory;
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.zscp.master.util.FileUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadExcel {
    public static Map<String, String> cars = new HashMap<>();
    public static Map<String, Map<String, String>> wbs = new HashMap<>();

    public static String f1="/Users/simo/home/docs/明细，维保，照片/车辆明细.xlsx";
    public static String f2="/Users/simo/home/docs/明细，维保，照片/车辆维保记录.xlsx";

    public static String f3 = "/Users/simo/home/docs/明细，维保，照片/html/";

    public static void main(String[] args) {
        try {
            InputStream inputStream =   new FileInputStream(new File(f1));
            // 解析每行结果在listener中处理
//            AnalysisEventListener excelListener = new AnalysisEventListener();
            ExcelListener excelListener = new ExcelListener();
            EasyExcelFactory.readBySax(inputStream, new Sheet(1, 1,ExcelObjectOne.class), excelListener);

            inputStream =   new FileInputStream(new File(f2));
            EasyExcelFactory.readBySax(inputStream, new Sheet(1, 1,ExcelObjectTwo.class), excelListener);

            System.out.println(1);
//            excelReader.read();
        } catch (Exception e) {

        } finally {
            try {
//                inputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
//        return ExcelUtil.readExcel(new File("C:\\Users\\Simo\\Desktop\\ss.xlsx"), new ExcelObjectOne());
    }
}
