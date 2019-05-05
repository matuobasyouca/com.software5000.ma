package com.zscp.test;

import org.springframework.web.bind.annotation.RequestMethod;

import java.io.*;
import java.util.Base64;

/**
 * Created by wujin on 2016/12/21.
 */
public class MainTest {
    public static void main(String[] args) {
        System.out.println(RequestMethod.POST.equals(RequestMethod.POST));

//        String strImg = getImageStr("F:/86619-106.jpg");
//        String strImg ="";
//        System.out.println(strImg);
//        System.out.println(ImageUtil.generateImage(strImg, "F:/86619-107.jpg"));
//        System.out.println(System.getProperty("user.dir"));
    }

    /**
     * @return
     * @Description: 根据图片地址转换为base64编码字符串
     * @Author:
     * @CreateTime:
     */
    public static String getImageStr(String imgFile) {
        InputStream inputStream = null;
        byte[] data = null;
        try {
            inputStream = new FileInputStream(imgFile);
            data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 加密
        return  Base64.getEncoder().encodeToString(data);
    }

    /**
     * @param imgStr base64编码字符串
     * @param path   图片路径-具体到文件
     * @return
     * @Description: 将base64编码字符串转换为图片
     * @Author:
     * @CreateTime:
     */
    public static boolean generateImage(String imgStr, String path) {
        if (imgStr == null)
            return false;
        try {
// 解密
            byte[] b =Base64.getDecoder().decode(imgStr);
// 处理数据
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {
                    b[i] += 256;
                }
            }
            OutputStream out = new FileOutputStream(path);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
