package com.zscp.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by chenchen on 2016/12/5.
 */
public class CheckDbFormat {


    private Log log = LogFactory.getLog(this.getClass());
    String entityPath;
//    String sqlPath = "c:\\Users\\Administrator\\Desktop\\work-docs\\zscp_pachong.sql";

    public static Map<String, List<String>> tables = new HashMap<>();

    private void transformSqlFile(String sqlPath) {

        String sqlStr = readFile(sqlPath, "utf-8");

        List<String> sqlNames = new ArrayList<>();

        for (String[] str : matchAll(sqlStr, "`([^`]*?)`")
                ) {
            sqlNames.add(str[1]);
//            log.info("sql names : " + str[1]);

            sqlStr = sqlStr.replace(str[1], str[1].replaceAll("_(.)", "_" + ("$1").toUpperCase()));

            log.info("replaceing  [" + str[1] + "] with value [" + str[1].replaceAll("\\_(.)", "_" + ("$1").toUpperCase()) + "]");
        }
        log.info("sqlString is under line");
        log.info("---------------------------------------------------------------------------------");
//        log.info(sqlStr);
    }

    /**
     * 确认实体与脚本直接存在正确的对应关系
     * 规则为 <code>DROP TABLE IF EXISTS `表名`;</code> 开始，到下一个空行处结束。
     * 认定为是一段完整表结构语句
     *
     * @param sqlPath
     */
    private String changeSqlFormat(String sqlPath) {
        String sqlStr = readFile(sqlPath, "utf-8");
        List<String[]> tablesStr = matchAll(sqlStr, "DROP TABLE IF EXISTS `[^`]*?`;.*?\r\n\r\n");
//        try {
//            String s = readFile(sqlPath.replace(".sql", "_data.sql"), "utf-8");
//            String src = s.replaceAll("Table structure for .*? Records of", "");
//            writeFile(src,sqlPath+".notable","utf-8");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        StringBuilder changeNames = new StringBuilder();

        // change ids
        String rex2 = "PRIMARY KEY \\(`[^`]*?`\\)";
        sqlStr = sqlStr.replaceAll(rex2, "PRIMARY KEY (`id`)");


//        String rex = "`[^`]*?` int(11) NOT NULL AUTO_INCREMENT,";
        sqlStr = sqlStr.replaceAll("`[^`]*?` int\\(11\\) NOT NULL AUTO\\_INCREMENT", "`id` int(11) NOT NULL AUTO_INCREMENT")
                .replaceAll("`[^`]*?` int\\(10\\) unsigned NOT NULL AUTO\\_INCREMENT", "`id` int(10) unsigned NOT NULL AUTO_INCREMENT")
                .replaceAll("`[^`]*?` int\\(11\\) unsigned NOT NULL AUTO\\_INCREMENT", "`id` int(11) unsigned NOT NULL AUTO_INCREMENT");




        for (String[] s : tablesStr
                ) {
//            System.out.println(s[0]);
            String tableName = match(s[0], "CREATE TABLE `([^`]*?)`")[1];
            if (tableName.indexOf("_") != -1) {
                sqlStr = sqlStr.replace("`" + tableName + "`", "`" + lineToHumpTablename(tableName) + "`");
                changeNames.append("`");
                changeNames.append(tableName);
                changeNames.append("`,`");
                changeNames.append(lineToHumpTablename(tableName));
                changeNames.append("`#");
            }
            List<String> cols = new ArrayList<>();
            for (String[] col : matchAll(s[0], "(^\\s+)`([^`]*?)`")
                    ) {
                sqlStr = sqlStr.replaceAll(col[0], col[1]+"`" + lineToHumpFieldname(col[2]) + "`");

            }
        }
        sqlStr = sqlStr.replace("`time`","`realTime`").replace("`money`","`realMoney`").replace("`name`","`realName`").replace("`value`","`realValue`")
                .replace("`zone`","`realZone`").replace("`path`","`realPath`").replace("`type`","`realType`").replace("`user`","`realUser`").replace("`date`","`realDate`")
        .replace("`local_version_id`","`localVersionId`").replace("KEY `open_code` (`open_id`,`rule_code`) USING BTREE","KEY `openCode` (`openId`,`ruleCode`) USING BTREE")
                .replace("KEY `open_id` (`open_id`) USING BTREE","KEY `openId` (`openId`) USING BTREE")
                .replace("KEY `theme_id` (`theme_id`) USING BTREE","KEY `themeId` (`themeId`) USING BTREE")
                .replace("`provinceId` int(11) NOT NULL","`id` int(11) NOT NULL")
                .replace("`cityId` int(11) NOT NULL,","`id` int(11) NOT NULL,")
                .replace("`local_version_id`","`localVersionId`")
                .replace("`local_version_id`","`localVersionId`")
                .replace("`local_version_id`","`localVersionId`")
                ;

//        /^[DC )-ST].*

        try {
//            writeFile(changeNames.toString(),"C:\\ROOT\\projects\\sql\\zscp_changenames.txt","utf-8");
//            writeFile(changeNames.toString(),"c:\\Users\\Administrator\\Desktop\\work-docs\\dbsql\\zscp_changenames.txt","utf-8");
            try {
                String s = readFile(sqlPath.replace(".sql", "_data.sql"), "utf-8");
                for (String change :
                        changeNames.toString().split("#")) {
                    s= s.replace("INSERT INTO "+change.split(",")[0],"INSERT INTO "+ change.split(",")[1]);
                }
                writeFile(s,sqlPath+".notable","utf-8");
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sqlStr;
    }
    public static String readFile(String filePath, String charset) {
        FileInputStream fs;
        String content = "";
        try {
            fs = new FileInputStream(filePath);
            byte data[] = new byte[10240000];
            int len = fs.read(data);
            System.out.println("length is : "+len);
            while (len != -1) {
                System.out.println(new Date()+"now length is : "+len);
                content = content + new String(data, 0, len, charset);
                len = fs.read(data);
            }
            fs.close();
        } catch (FileNotFoundException e) {

        } catch (IOException e) {

        }
        return content;
    }

    public  void compareToEntity(String pathname){
        File[] files = new File(pathname).listFiles();
        for (File f : files
                ) {
            if (tables.keySet().contains(f.getName())) {
                log.info("OK -> 这个类 ["+f.getName()+"]  没有直接对应的数据库表格。");
            }else{
                log.info("Failed -> 这个类 ["+f.getName()+"]  没有直接对应的数据库表格。");
            }
        }
    }
    private void checkSqlFormat(String sqlPath) {
        String sqlStr = readFile(sqlPath, "utf-8");
        List<String[]> tablesStr = matchAll(sqlStr, "DROP TABLE IF EXISTS `[^`]*?`;.*?\r\n\r\n");

        for (String[] s : tablesStr
                ) {
//            System.out.println(s[0]);
            String tableName = match(s[0], "CREATE TABLE `([^`]*?)`")[1];
            if (tableName.indexOf("_") != -1) {
                log.info("表名 [" + tableName + "] 包含 '_' 下划线，请注意");
            }
            List<String> cols = new ArrayList<>();
            for (String[] col : matchAll(s[0], "^\\s+`([^`]*?)`")
                    ) {
//                System.out.println(col[1]);
                cols.add(col[1]);

//                log.info("表名 ["+tableName+"] 字段名 ["+col[1]+"] 包含 '_' 下划线，请注意");
            }
            tables.put(tableName, cols);


//            log.info(sqlStr);
//            log.info("表名 ["+tableName+"] 检查完毕！-----------------------------------------------\r\n\r\n\r\n");
//            return sqlStr;
        }

    }

    public static String[] match(String s, String pattern) {
        Matcher m = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL).matcher(s);

        while (m.find()) {
            int n = m.groupCount();
            String[] ss = new String[n + 1];
            for (int i = 0; i <= n; i++) {
                ss[i] = m.group(i);
            }
            return ss;
        }
        return null;
    }


    public static List<String[]> matchAll(String s, String pattern) {
        Matcher m = Pattern.compile(pattern, Pattern.MULTILINE | Pattern.DOTALL).matcher(s);
        List<String[]> result = new ArrayList<String[]>();

        while (m.find()) {
            int n = m.groupCount();
            String[] ss = new String[n + 1];
            for (int i = 0; i <= n; i++) {
                ss[i] = m.group(i);
            }
            result.add(ss);
        }
        return result;
    }

    /**
     * 首字母无需大写
     *
     * @param str
     * @return
     */
    public static String lineToHumpFieldname(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    public static void writeFile(String src, String filePath, String charset) throws Exception {
        FileOutputStream fs = new FileOutputStream(filePath);
        fs.write(src.getBytes(charset));
        fs.close();
    }

    /**
     * 表名转换为类名，首字母需要大写
     *
     * @param str
     * @return
     */
    public static String lineToHumpTablename(String str) {
        Pattern linePattern = Pattern.compile("_(\\w)");
        str = str.toLowerCase();
        Matcher matcher = linePattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(sb, matcher.group(1).toUpperCase());
        }
        matcher.appendTail(sb);
        return new StringBuilder().append(sb.substring(0, 1).toUpperCase() + sb.substring(1)).toString();
//        return sb.toString();
    }

    public static void main(String[] args) {

        CheckDbFormat checkDbFormat = new CheckDbFormat();

//        String sqlPath = "c:\\ROOT\\projects\\sql\\zscp_test.sql";
        String sqlPath = "c:\\Users\\Administrator\\Desktop\\work-docs\\dbsql\\zscp_test.sql";
//        String newSqlPath=
//        String filePath = "c:\\ROOT\\projects\\sql\\zscp_test_change.sql";
        String filePath = "c:\\Users\\Administrator\\Desktop\\work-docs\\dbsql\\zscp_test_change.sql";
        try {
            writeFile(checkDbFormat.changeSqlFormat(sqlPath), filePath, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }

//        checkDbFormat.checkSqlFormat(filePath);
//        checkDbFormat.compareToEntity("X:\\Home_Workspace\\eclipse4.5-jee\\zscp_back\\src\\com\\zscp\\model\\");
//        new CheckDbFormat().checkSqlFormat();
//        checkDbFormat.transformSqlFile();
    }

}
