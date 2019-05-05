package com.software5000.util;

import com.zscp.master.util.FileUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.zip.CRC32;

public class CheckSumUtil {
   public static int calculateChecksum()  {
       String str= null;
       try {
           str = FileUtil.readString(new java.io.File("E:\\Workspaces\\IDEA2016\\ZSCP\\crm\\com.zscp.saas.crm.dev.alpha\\src\\main\\resources\\db\\migrate\\V2_2_3__Update.sql"),"utf-8");
       } catch (IOException e) {
           e.printStackTrace();
       }
       final CRC32 crc32 = new CRC32();

        BufferedReader bufferedReader = new BufferedReader(new StringReader(str));
        try {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                crc32.update(line.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            String message = "Unable to calculate checksum";
        }

        return (int) crc32.getValue();
    }

    public static void main(String[] args) {
        System.out.println(calculateChecksum());
    }
}
