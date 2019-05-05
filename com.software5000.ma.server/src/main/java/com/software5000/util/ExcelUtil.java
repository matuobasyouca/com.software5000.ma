package com.software5000.util;

import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by Jiang on 2017/07/13.
 */
public class ExcelUtil {

    private static Log log = LogFactory.getLog(ExcelUtil.class);

    private static final String EXCEL_XLS = "xls";
    private static final String EXCEL_XLSX = "xlsx";

    /**
     * 根据文件类型，获取workbook
     * @param in
     * @param fileName
     * @return
     * @throws IOException
     */
    private static Workbook getWorkbok(InputStream in,String fileName) throws IOException{
        Workbook wb = null;
        if(fileName.endsWith(EXCEL_XLS)){  //Excel 2003
            wb = new HSSFWorkbook(in);
        }else if(fileName.endsWith(EXCEL_XLSX)){  // Excel 2007/2010
            wb = new XSSFWorkbook(in);
        }
        return wb;
    }

    /**
     * 读取Excel表格表头的内容
     * @return String 表头内容的数组
     */
    public static String[] readExcelTitle(MultipartFile file) {
        try {
            Workbook wb = getWorkbok(file.getInputStream(), file.getOriginalFilename());
            Sheet sheet = wb.getSheetAt(0);
            Row row = sheet.getRow(0);
            // 标题总列数
            int colNum = row.getPhysicalNumberOfCells();
            String[] title = new String[colNum];
            for (int i = 0; i < colNum; i++) {
                title[i] = getCellValue(row.getCell((short) i));
            }
            return title;
        } catch (IOException e) {
            log.error("ExcelUtil.readExcelTitle", e);
        }
        return null;
    }

    /**
     * 读取Excel数据内容
     * @return Map 包含单元格数据内容的Map对象
     */
    public static Map<Integer, List<String>> readExcelContent(MultipartFile file, int contentLine) {
        Map<Integer, List<String>> content = new HashMap<Integer, List<String>>();
        List<String> strList = new ArrayList<String>();
        try {
            Workbook wb = getWorkbok(file.getInputStream(), file.getOriginalFilename());
            Sheet sheet = wb.getSheetAt(0);
            // 得到总行数
            int rowNum = sheet.getLastRowNum();
            Row row = sheet.getRow(0);
            int colNum = row.getPhysicalNumberOfCells();
            // 正文内容应该从第二行开始,第一行为表头的标题
            for (int i = contentLine; i <= rowNum; i++) {
                row = sheet.getRow(i);
                if(row==null) continue;
                int j = 0;
                while (j < colNum) {
                    strList.add(getCellValue(row.getCell((short) j)));
                    j++;
                }
                content.put(i, strList);
                strList = new ArrayList<String>();
            }
        } catch (IOException e) {
            log.error("ExcelUtil.readExcelContent", e);
        }
        return content;
    }

    /**
     * 获取单元格数据内容为字符串类型的数据
     *
     * @param cell Excel单元格
     * @return String 单元格数据内容
     */
    private String getStringCellValue(HSSFCell cell) {
        String strCell = "";
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf(cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("") || strCell == null) {
            return "";
        }
        if (cell == null) {
            return "";
        }
        return strCell;
    }

    /**
     * 获取单元格数据内容为日期类型的数据
     *
     * @param cell
     *            Excel单元格
     * @return String 单元格数据内容
     */
    private String getDateCellValue(HSSFCell cell) {
        String result = "";
        try {
            int cellType = cell.getCellType();
            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
                Date date = cell.getDateCellValue();
                result = (date.getYear() + 1900) + "-" + (date.getMonth() + 1)
                        + "-" + date.getDate();
            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
                String date = getStringCellValue(cell);
                result = date.replaceAll("[年月]", "-").replace("日", "").trim();
            } else if (cellType == HSSFCell.CELL_TYPE_BLANK) {
                result = "";
            }
        } catch (Exception e) {
            log.error("ExcelUtil.getDateCellValue", e);
        }
        return result;
    }

    /**
     * 根据HSSFCell类型设置数据
     * @param cell
     * @return
     */
    private static String getCellFormatValue(HSSFCell cell) {
        String cellvalue = "";
        if (cell != null) {
            // 判断当前Cell的Type
            switch (cell.getCellType()) {
                // 如果当前Cell的Type为NUMERIC
                case HSSFCell.CELL_TYPE_NUMERIC:
                case HSSFCell.CELL_TYPE_FORMULA: {
                    // 判断当前的cell是否为Date
                    if (HSSFDateUtil.isCellDateFormatted(cell)) {
                        // 如果是Date类型则，转化为Data格式

                        //方法1：这样子的data格式是带时分秒的：2011-10-12 0:00:00
                        //cellvalue = cell.getDateCellValue().toLocaleString();

                        //方法2：这样子的data格式是不带带时分秒的：2011-10-12
                        Date date = cell.getDateCellValue();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        cellvalue = sdf.format(date);
                    }
                    // 如果是纯数字
                    else {
                        // 取得当前Cell的数值
                        cellvalue = String.valueOf(cell.getNumericCellValue());
                    }
                    break;
                }
                // 如果当前Cell的Type为STRIN
                case HSSFCell.CELL_TYPE_STRING:
                    // 取得当前的Cell字符串
                    cellvalue = cell.getRichStringCellValue().getString();
                    break;
                // 默认的Cell值
                default:
                    cellvalue = " ";
            }
        } else {
            cellvalue = "";
        }
        return cellvalue;

    }

    /**
     * 获得单元格的值
     *
     * @param cell
     * @return
     */
    /**
     * 获得单元格的值
     *
     * @param cell
     * @return
     */
    public static String getCellValue(Cell cell) {
        String con = null;
        if (cell == null)
            return null;
        // 如果为百分数
        if (cell.getCellStyle().getDataFormatString().indexOf("%") != -1) {
            Double numericCellValue = null;
            try {
                numericCellValue = Double.parseDouble(String.format("%.2f",
                        (cell.getNumericCellValue() * 100)));
            } catch (Exception e) {
                numericCellValue = null;
            }
            if (numericCellValue != null) {
                con = numericCellValue + "%";
                return con;
            }
        }
        if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
            con = cell.getStringCellValue();
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                con = sdf.format(date);
            } else {
                DecimalFormat df = new DecimalFormat("0.######");
                con = df.format(cell.getNumericCellValue());
            }
        } else if (cell.getCellType() == HSSFCell.CELL_TYPE_FORMULA) {
            if (HSSFDateUtil.isCellDateFormatted(cell)) {
                Date date = cell.getDateCellValue();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                con = sdf.format(date);
            } else {
                cell.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
                DecimalFormat df = new DecimalFormat("0.######");
                con = df.format(cell.getNumericCellValue());
            }
        }
        return con;
    }

    /**
     *
     * sheetName：表头名称； title1：表头第一行列名；title2：表头小标题
     * caption：表头第二行列名；dataList：导出的数据；detail：导出的表体字段
     *
     */
    public static HSSFWorkbook createWorkBook(String sheetName,String title1 ,String title2, String[] caption, List<Map<String, Object>> dataList, String[] detail){
        HSSFWorkbook workbook = new HSSFWorkbook();
        int columnSize = caption.length-1;
        //设置sheet的名字
        HSSFSheet sheet = (HSSFSheet) workbook.createSheet(sheetName);
        //sheet默认页样式
        sheet.setDefaultRowHeight((short)360);//设置行高
        //表头样式
        HSSFFont headFont = createFont(workbook,(short)16, false);
        HSSFCellStyle headCellStryle=createStyle(workbook,headFont);

        //表头样式
        HSSFFont headFont2 = createFont(workbook,(short)13, false);
        // 列名样式
        HSSFFont  captionFont = createFont(workbook,(short)11, false);
        HSSFCellStyle captionCellStryle=createStyle(workbook,captionFont);
        //普通单元格样式
        HSSFFont  font = createFont(workbook,(short)10, false);
        HSSFCellStyle style=createStyle(workbook,font);
        /*设置表格宽度*/
        for(int i = 0; i <= columnSize; i++){
            sheet.setColumnWidth(i, 35*120);
        }
        sheet.setColumnWidth(columnSize, 35*190);
        HSSFRow row;
        HSSFCell cell;
        if(!ValidUtil.isEmpty(title1)) {
            // 第一行表头标题
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, columnSize));
            row = sheet.createRow(0);
            row.setHeight((short) 0x319);
            cell = row.createCell(0);
            cell.setCellStyle(headCellStryle);
            cell.setCellType(HSSFCell.CELL_TYPE_STRING);

            if(!StringUtils.isEmpty(title2)){
                HSSFRichTextString ts= new HSSFRichTextString(title1+title2);
                ts.applyFont(0,title1.length(),headFont);
                ts.applyFont(title1.length(),title1.length()+title2.length(),headFont2);
                cell.setCellValue(ts);
            }else{
                cell.setCellValue(title1);
            }
        }

        // 第二行表头列名
        row = sheet.createRow(ValidUtil.isEmpty(title1) ? 0 : 1);
        row.setHeight((short) 0x200);
        for (int i = 0; i <= columnSize; i++) {
            cell = row.createCell(i);
            cell.setCellValue(caption[i]);
            cell.setCellStyle(style);
        }

        // 设置列值-内容
        if(dataList != null) {
            for (int i = 0; i < dataList.size(); i++) {
                row = sheet.createRow(i + (ValidUtil.isEmpty(title1) ? 1 : 2));
                for (int j = 0; j < detail.length; j++) {
                    Map tempmap = (HashMap) dataList.get(i);
                    Object data = tempmap.get(detail[j]);
                    cell = row.createCell(j);
                    cell.setCellStyle(style);
                    setCellValue(cell,data);
                }
            }
        }

        return  workbook;
    }



    /**设置字体大小，颜色，样式，是否加粗*/
    private static  HSSFFont createFont(HSSFWorkbook workbook, short fontHeightInPoints, boolean isBlod) {
        HSSFFont font =  workbook.createFont();
        //字体大小
        font.setFontHeightInPoints(fontHeightInPoints);
        //字体颜色
        font.setColor(IndexedColors.BLACK.getIndex());
        //字体样式
        font.setFontName("宋体");
        //是否加粗
        font.setBold(isBlod);
        return font;
    }

    /**设置字体居中显示，背景色，边框*/
    private static HSSFCellStyle createStyle(HSSFWorkbook workbook, HSSFFont font) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        //居中
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        //背景颜色
        cellStyle.setFillForegroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillBackgroundColor(IndexedColors.WHITE.index);
        cellStyle.setFillPattern(FillPatternType.FINE_DOTS);
        //边框
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);
        return cellStyle;
    }

    /**根据内容设置单元格格式*/
    private static void  setCellValue(HSSFCell contentCell,Object data) {
        HSSFCellStyle contextstyle =contentCell.getCellStyle();
        if (!StringUtils.isEmpty(data)) {
            Boolean isNum = false;//data是否为数值型
            Boolean isInteger=false;//data是否为整数
            Boolean isPercent=false;//data是否为百分数
            //判断data是否为数值型
            isNum = data.toString().matches("^(-?\\d+)(\\.\\d+)?$");
            //判断data是否为整数（小数部分是否为0）
            isInteger=data.toString().matches("^[-\\+]?[\\d]*$");
            //判断data是否为百分数（是否包含“%”）
            isPercent=data.toString().contains("%");
            //如果单元格内容是数值类型，涉及到金钱（金额、本、利），则设置cell的类型为数值型，设置data的类型为数值类型

            if (isNum && !isPercent) {
                if (isInteger && data instanceof String ) {
                    contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("0"));//数据格式只显示整数
                    // 设置单元格格式
                    contentCell.setCellStyle(contextstyle);
                    // 设置单元格内容为double类型
                    contentCell.setCellValue(data.toString());
                }else{
                    contextstyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("#,##0.00"));//保留两位小数点
                    // 设置单元格格式
                    contentCell.setCellStyle(contextstyle);
                    // 设置单元格内容为double类型
                    contentCell.setCellValue(Double.parseDouble(data.toString()));
                }
            } else {
                contentCell.setCellStyle(contextstyle);
                // 设置单元格内容为字符型
                contentCell.setCellValue(data.toString());
            }
        }else{
            contentCell.setCellStyle(contextstyle);
            contentCell.setCellType(CellType.BLANK);
        }
    }
    //解决设置名称时的乱码
    public static String processFileName(HttpServletRequest request, String fileNames) {
        String codedfilename = null;
        try {
            String agent = request.getHeader("USER-AGENT");
            if (null != agent && -1 != agent.indexOf("MSIE") || null != agent
                    && -1 != agent.indexOf("Trident")) {// ie

                String name = java.net.URLEncoder.encode(fileNames, "UTF8");

                codedfilename = name;
            } else if (null != agent && -1 != agent.indexOf("Mozilla")) {// 火狐,chrome等

                codedfilename = new String(fileNames.getBytes("UTF-8"), "iso-8859-1");
            }
        } catch (Exception e) {
            log.error("ExcelUtil.processFileName", e);
        }
        return codedfilename;
    }

    /**
     * 判断数据内容是否全为空
     * @param list
     * @return
     */
    public static boolean isEmptyList(List<String> list) {
        if(list != null) {
            for(String str : list) {
                if(!ValidUtil.isEmpty(str)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * 设置下载文件中文件的名称
     *
     * @param filename
     * @param request
     * @return
     */
    public static String encodeFilename(String filename, HttpServletRequest request) {
        try {
            return new String(filename.getBytes("gbk"),
                    "iso-8859-1");
        } catch (UnsupportedEncodingException e) {
            log.error("ExcelUtil.encodeFilename", e);
        }
        return filename;
    }

    /**
     * 封装下载excel公共方法
     *
     * @param fileName
     * @param sheetName
     * @param caption
     * @param dataLists
     * @param detail
     * @param response
     * @param request
     * @throws IOException
     */
    public static void exportTemplate(String fileName, String sheetName, String[] caption, List dataLists, String[] detail, HttpServletResponse response, HttpServletRequest request) throws IOException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //查询出列表信息并组装成WorkBook
            HSSFWorkbook workbook = ExcelUtil.createWorkBook(sheetName, null, null, caption, dataLists, detail);
            String filename = ExcelUtil.processFileName(request, fileName + ".xls");
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);

            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            ServletOutputStream out = response.getOutputStream();
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            log.error("ExcelUtil.exportTemplate", e);
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
    }
}
