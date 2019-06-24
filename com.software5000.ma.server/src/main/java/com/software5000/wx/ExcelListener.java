package com.software5000.wx;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.FileUtil;
import org.apache.poi.ss.formula.functions.T;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static com.software5000.wx.ReadExcel.f3;

public class ExcelListener extends AnalysisEventListener {


    private List<Object> data = new ArrayList<Object>();

    @Override
    public void invoke(Object object, AnalysisContext context) {
        System.out.println(context.getCurrentSheet());
        data.add(object);
        if(data.size()>=100){
            doSomething();
            data = new ArrayList<Object>();
        }
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
        doSomething();
    }

    public void doSomething(){
        try {
            String s1="<img style='max-width:' src='imgs' />";
            String h = FileUtil.readString("/Users/simo/home/docs/明细，维保，照片/szcar.html","utf-8");
            String tcar1 = null;
            String tcar2 = null;

            Map<String, String> xmap = new HashMap<>();
            List<String> xmapkey = new ArrayList<>();

            Map<String, String> ymap = new HashMap<>();
            List<String> ymapkey = new ArrayList<>();

            for (Object o:data) {
                if(o instanceof ExcelObjectOne) {
                    ExcelObjectOne one = (ExcelObjectOne) o;

                    StringBuilder html = new StringBuilder();
                    html.append("<tr><td style='width:50%'>车牌</td><td>" + one.getP13() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>车型</td><td>" + one.getP2() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>售价</td><td>" + one.getP3() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>车身颜色</td><td>" + one.getP4() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>上牌时间</td><td>" + DateUtils.formatDate(one.getP5()) + "</td></tr>");
                    html.append("<tr><td style='width:50%'>公里数</td><td>" + one.getP6() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>所在地</td><td>" + one.getP7() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>排放标准</td><td>" + one.getP8() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>使用性质</td><td>" + one.getP9() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>车牌</td><td>" + one.getP10() + "</td></tr>");
                    html.append("<tr><td style='width:50%'>年检到期日</td><td>" + DateUtils.formatDate(one.getP11()) + "</td></tr>");
                    html.append("<tr><td style='width:50%'>保险到期日</td><td>" + DateUtils.formatDate(one.getP12()) + "</td></tr>");

                    FileUtil.write(new File(f3 + one.getP1() + ".html"),
                            h.replace("${p1_c1}", "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-1.jpg' />")
                                    .replace("${p1_c2}", html)
                                    .replace("${p4}","" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-1.jpg' />" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-2.jpg' /><br/>" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-3.jpg' />" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-4.jpg' /><br/>" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-5.jpg' />" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-6.jpg' /><br/>" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-7.jpg' />" +
                                            "<img src='imgs/" + one.getP1() + "/" + one.getP1() + "-8.jpg' />" +
                                            "")

                            ,
                            "utf-8");
//                System.out.println(o);
                }else if(o instanceof ExcelObjectTwo) {

                    ExcelObjectTwo one = (ExcelObjectTwo) o;

                    String bm = one.getP7().split("-")[1];
                    if(bm.trim().length()==1){
                        bm="0"+bm;
                    }
                    String ym = one.getP1()+","+one.getP7().split("-")[0]+"-"+bm;


//                    if(tcar1==null){
//                        tcar1 = one.getP1() + "," + one.getP7();
//                    }
//
//                    if(!tcar1.trim().equals(one.getP1()+","+one.getP7())){
//                        String wb = FileUtil.readString(ftwo, "utf-8");
//                        if(tcarhtml1!=null){
//                            tcarhtml1+="</td></tr>";
//                        }else {
//                            tcarhtml1 = "";
//                        }
//                        if(tcarhtml2!=null){
//                            tcarhtml2+="</td></tr>";
//                        }else {
//                            tcarhtml2 = "";
//                        }
//                        tcarhtml1+="<div style='color:white'>${p2_c1}</div>";
//                        tcarhtml2+="<div style='color:white'>${p3_c1}</div>";
//                        FileUtil.write(new File(ftwo),
//                                wb.replace("<div style='color:white'>${p2_c1}</div>",tcarhtml1)
//                                .replace("<div style='color:white'>${p3_c1}</div>",tcarhtml2)
//                                ,"utf-8");
//
//                        tcarhtml1 = null;
//                        tcarhtml2 = null;
//                        tcar1 = one.getP1() + "," + one.getP7();
//
//                    }

                    if (one.getP6()!=null&&one.getP6().trim().equals("是")) {
                        continue;
                    }

                    if(one.getP4().trim().equals("机电类")) {


                        if(!xmap.containsKey(ym)){
                            xmap.put(ym,null);
                            xmapkey.add(ym);
                        }

                        String tcarhtml1 = xmap.get(ym);
                        if (tcarhtml1 == null) {
                            tcarhtml1 = "<tr><td>" + one.getP7() + "</td><td>" + one.getP5() + ";";
                        } else {
                            tcarhtml1 += one.getP5() + ";";
                        }
                        xmap.put(ym,tcarhtml1);

//                        String wb = FileUtil.readString(ftwo,"utf-8");
//                        FileUtil.write(new File(ftwo), wb.replace("<div style='color:white'>${p2_c1}</div>","<tr><td>"+one.getP7()+"</td><td>"+one.getP5()+"</td></tr><div style='color:white'>${p2_c1}</div>"),"utf-8");
                    }else if(one.getP3().trim().equals("事故类")) {

                        if(!ymap.containsKey(ym)){
                            ymap.put(ym,null);
                            ymapkey.add(ym);
                        }

                        String tcarhtml2 = ymap.get(ym);
                        if (tcarhtml2 == null) {
                            tcarhtml2 = "<tr><td>" + one.getP7() + "</td><td>" + one.getP5() + ";";
                        } else {
                            tcarhtml2 += one.getP5() + ";";
                        }
                        ymap.put(ym,tcarhtml2);
//                        String wb = FileUtil.readString(ftwo,"utf-8");
//                        FileUtil.write(new File(ftwo), wb.replace("<div style='color:white'>${p3_c1}</div>","<tr><td>"+one.getP7()+"</td><td>"+one.getP5()+"</td></tr><div style='color:white'>${p3_c1}</div>"),"utf-8");
                    }


                }
            }


            Collections.sort(xmapkey);
            Collections.sort(ymapkey);

            for (String x : xmapkey) {
                String ftwo="/Users/simo/home/docs/明细，维保，照片/html/"+x.split(",")[0]+".html";
                        String wb = FileUtil.readString(ftwo, "utf-8");

                        FileUtil.write(new File(ftwo),
                                wb.replace("<div style='color:white'>${p2_c1}</div>",xmap.get(x)+"</td></tr><div style='color:white'>${p2_c1}</div>")
                                ,"utf-8");

            }
            for (String y : ymapkey) {
                String ftwo="/Users/simo/home/docs/明细，维保，照片/html/"+y.split(",")[0]+".html";
                String wb = FileUtil.readString(ftwo, "utf-8");

                FileUtil.write(new File(ftwo),
                        wb.replace("<div style='color:white'>${p3_c1}</div>",ymap.get(y)+"</td></tr><div style='color:white'>${p3_c1}</div>")
                        ,"utf-8");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}