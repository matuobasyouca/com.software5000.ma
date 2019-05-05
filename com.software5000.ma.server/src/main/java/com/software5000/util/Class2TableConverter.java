package com.software5000.util;

import com.software5000.base.NotDatabaseField;
import com.software5000.base.entity.BaseEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Simo on 2016/12/12.
 */
public class Class2TableConverter {
    private static Log log = LogFactory.getLog(Class2TableConverter.class);

    private static String needClasses = "";

    private static Vector<String> vecFile = new Vector<String>();

    private static String ddlSql = "\r\n\r\nCREATE TABLE `${table.name}` (\r\n`id` int(11) NOT NULL AUTO_INCREMENT, \r\n ${colums} `createTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',\r\n`updateTime` timestamp NOT NULL DEFAULT '2016-1-1 00:00:00',\r\n PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_general_ci;\r\n\r\n";
    private static String fieldStr = "`${column.name}` ${column.type} NULL,\r\n";


    private static final Map<String, String> classTypeMap;

    static {
        classTypeMap = new HashMap<>();
        classTypeMap.put("java.lang.String", "VARCHAR(255)");
        classTypeMap.put("java.math.BigDecimal","DECIMAL(10,2)");
        classTypeMap.put("java.util.Date","DATETIME");
        classTypeMap.put("java.lang.Boolean", "TINYINT");
        classTypeMap.put("java.lang.Byte", "SMALLINT");
        classTypeMap.put("java.lang.Character", "SMALLINT");
        classTypeMap.put("java.lang.Double", "DOUBLE");
        classTypeMap.put("java.lang.Float", "FLOAT");
        classTypeMap.put("java.lang.Short", "SMALLINT");
        classTypeMap.put("java.lang.Integer", "INT");
        classTypeMap.put("java.lang.Long", "BIGINT");
        classTypeMap.put("java.sql.Timestamp", "TIMESTAMP");
    }

    public static Vector<String> getClassFiles(String fileAbsolutePath) {
        File file = new File(fileAbsolutePath);
        File[] subFile = file.listFiles();

        for (int iFileLength = 0; iFileLength < subFile.length; iFileLength++) {
            // 判断是否为文件夹
            if (!subFile[iFileLength].isDirectory()) {
                String fileName = subFile[iFileLength].getAbsolutePath();
                // 判断是否为.class结尾
                if (!fileName.trim().toLowerCase().endsWith(".class")) {
                   continue;
                }
                if (needClasses.length()==0) {
                    vecFile.add(fileName);
                }else if(needClasses.length()>=0||needClasses.indexOf(subFile[iFileLength].getName())!=-1) {
                    vecFile.add(fileName);
                }
            }else{
                getClassFiles(subFile[iFileLength].getAbsolutePath());
            }
        }
        return vecFile;
    }



    public static void writeFile(String src, String filePath, String charset) throws Exception {
        FileOutputStream fs = new FileOutputStream(filePath);
        fs.write(src.getBytes(charset));
        fs.close();
    }

    public static void main(String[] args) {
        try {
            File baseProject = new File(Class2TableConverter.class.getResource("/").getFile());
            Vector<String> classFiles = getClassFiles(baseProject.getAbsolutePath());

            List<String> classNames = new ArrayList<>();
            StringBuilder sqlStrs = new StringBuilder();

//            try {
//                classFiles.stream().filter(f -> {
//                    try {
//                        return Class.forName(f.getAbsolutePath().replace(baseProject.getAbsolutePath(), "").replace(".class", "").replace("/", ".")).isInstance(BaseEntity.class);
//                    } catch (ClassNotFoundException e) {
//                        e.printStackTrace();
//                    }
//                }).map()
//            System.out.println(SystemCode.class.getGenericSuperclass() == BaseEntity.class);

            StringBuilder allSql = new StringBuilder();

            for (String classFile :
                    classFiles) {
//                System.out.println(classFile);
//                System.out.println(classFile.replace(baseProject.getAbsolutePath()+"\\", "").replace(".class", "").replace("\\", "."));
                try {
                    String className = classFile.replace(baseProject.getAbsolutePath() + "\\", "").replace(".class", "").replace("\\", ".");

                    if(className.toLowerCase().indexOf("util")>=0) continue;
                    if (Class.forName(className).getGenericSuperclass() == BaseEntity.class) {
                        // 存在一个有效实体
    //                    System.out.println(classFile);
                        String tname = new File(classFile).getName().replace(".class","");
                        StringBuilder colsStr = new StringBuilder();
    //                    GenericsUtils.get
                        Field[] fields = Class.forName(className).getDeclaredFields();
                        for (Field f :
                                fields) {
    //                        System.out.println(f.getName());
                            if(f.getAnnotation(NotDatabaseField.class) != null)
                                continue;

                            if(classTypeMap.get(f.getType().getName()) != null)
                                colsStr.append(fieldStr.replace("${column.name}", f.getName()).replace("${column.type}", classTypeMap.get(f.getType().getName())));
                            else
    //                            System.out.println(tname + " 表 "+ f.getName() +" 字段 "+ f.getType() + "不存在对应类型");
                                log.debug(tname + " 表 "+ f.getName() +" 字段 "+ f.getType() + "不存在对应类型");
                        }
    //                    System.out.println(colsStr.toString());
                        allSql.append(ddlSql.replace("${table.name}", tname).replace("${colums}", colsStr.toString()));

                    }
                } catch (Exception e) {
                    log.error(e);
                }
            }
            writeFile(allSql.toString(), "d:\\projects.sql", "utf-8");
//            System.out.println("生成 x:\\projects.sql 完成，请确认。");
            log.debug("生成 x:\\projects.sql 完成，请确认。");
        } catch (Exception e) {
            log.error(e);
        }


    }
}