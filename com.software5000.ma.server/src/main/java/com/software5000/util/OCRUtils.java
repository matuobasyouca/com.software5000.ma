package com.software5000.util;


import com.software5000.base.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.EntityBuilder;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by lsj on 2016/12/26.
 * 云脉图片识别API
 */
public class OCRUtils {
    public static Log log = LogFactory.getLog(OCRUtils.class);



    public static String scanPlate(byte[] file,String ext){
        String xml = getSendXML( "plate.scan",ext);
        return send(xml,file);
    }

    public static String scanDriving(byte[] file,String ext){
        String xml = getSendXML( "driving.scan",ext);
        return send(xml,file);
    }

    private static String getSendXML(String action,String ext) {
        String username= Constant.getCodeByName(Constant.YunmaiOCR.USER_NAME).getCodeValue();//替换OCR SDK开发者平台API帐号
        String password=Constant.getCodeByName(Constant.YunmaiOCR.PASS_WORD).getCodeValue();//替换OCR SDK开发者平台API密码
        ArrayList<String[]> arr = new ArrayList<String[]>();
        String key = UUID.randomUUID().toString();
        String time = String.valueOf(System.currentTimeMillis());
        String verify = MD5(action+username+key+time+password);
        arr.add(new String[] { "action", action});
        arr.add(new String[] { "client", username });
        arr.add(new String[] { "system", "web_saas"});
        arr.add(new String[] { "password", MD5(password)});
        arr.add(new String[] { "key", key });
        arr.add(new String[] { "time",time});
        arr.add(new String[] { "verify", verify });
        arr.add(new String[] { "ext",ext });
        arr.add(new String[] { "json", "1" });//返回结果是否转成json格式，不传及默认是xml格式，为1时：转换成json格式
        return getXML(arr,false);
    }
    public static String getXML(ArrayList<String[]> arr,boolean IsUpper) {
        if (arr == null || arr.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        String tag="";
        for (int idx = 0; idx < arr.size(); idx++) {
            tag=arr.get(idx)[0];
            if(IsUpper){
                tag=tag.toUpperCase();
            }
            sb.append("<");
            sb.append(tag);
            sb.append(">");
            sb.append(arr.get(idx)[1]);
            //sb.append(XMLFunctions.code(arr.get(idx)[1]));
            sb.append("</");
            sb.append(tag);
            sb.append(">");
        }
        return sb.toString();
    }
    private static String send(String xml,byte[] file){
        byte[] dest = new byte[xml.getBytes().length+file.length+"<file></file>".getBytes().length];
        int pos = 0;
        System.arraycopy(xml.getBytes(), 0, dest, pos, xml.getBytes().length);
        pos += xml.getBytes().length;
        System.arraycopy("<file>".getBytes(), 0, dest, pos, "<file>".getBytes().length);
        pos += "<file>".getBytes().length;
        System.arraycopy(file, 0, dest, pos, file.length);
        pos += file.length;
        System.arraycopy("</file>".getBytes(), 0, dest, pos, "</file>".getBytes().length);
        try {
            return httpClient(Constant.getCodeByName(Constant.YunmaiOCR.ENGINE_URL).getCodeValue(), dest);
        } catch (IOException e) {
            return "-1";
        }
    }
    @SuppressWarnings("finally")
    public static String httpClient(String url,byte[] content) throws IOException{
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpResponse response = null;
        HttpPost httpPost = new HttpPost(url);
        String responseString = null;
        try {
            EntityBuilder entityBuilder = EntityBuilder.create().setBinary(content);
//            entityBuilder.setContentEncoding("utf-8");
//            entityBuilder.setContentType(ContentType.create("text/plain","utf-8"));
//            httpPost.addHeader("ContentCharset","utf-8");
            HttpEntity httpEntity = entityBuilder.build();
            httpPost.setEntity(httpEntity);

            response = httpClient.execute(httpPost);

            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity,Charset.forName("utf-8"));
            EntityUtils.consume(entity);
        } catch (Exception e) {
            throw e;
        } finally {
            httpPost.releaseConnection();
        }
        return responseString;

//        HttpClient httpClient = new HttpClient();
//        HttpClientParams httparams = new HttpClientParams();
//        httpClient.setParams(httparams);
//
//        PostMethod method = new PostMethod(url);
//        RequestEntity requestEntity = new ByteArrayRequestEntity(content);
//        method.setRequestEntity(requestEntity);
//        String responseBody = null;
//        try {
//            method.getParams().setContentCharset("utf-8");
//            method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler());
//            int statusCode = httpClient.executeMethod(method);
//            if (statusCode != HttpStatus.SC_OK) {
//                log.debug("\r\nMethod failed: " + method.getStatusLine() + ",url:\r\n" + url + "\r\n");
//            }
//            StringBuffer resultBuffer = new StringBuffer();
//            BufferedReader in = new BufferedReader(new InputStreamReader(method.getResponseBodyAsStream(),
//                    method.getResponseCharSet()));
//            String inputLine = null;
//            while ((inputLine = in.readLine()) != null) {
//                resultBuffer.append(inputLine);
//                resultBuffer.append("\r\n");
//            }
//            in.close();
//            responseBody = resultBuffer.toString().trim();
//        } catch (Exception e) {
//            log.error(e);
//            responseBody = "-2";
//        } finally{
//            if (method != null) {
//                method.releaseConnection();
//                method = null;
//            }
//            return responseBody;
//        }

    }
    @SuppressWarnings("resource")
    public static byte[] file2byte(File file) throws IOException {
        byte[] bytes = null;
        if (file != null) {
            InputStream is = new FileInputStream(file);
            int length = (int) file.length();
            if (length > Integer.MAX_VALUE) // 当文件的长度超过了int的最大值
            {
                log.debug("this file is max ");
                return null;
            }
            bytes = new byte[length];
            int offset = 0;
            int numRead = 0;
            while (offset < bytes.length&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
                offset += numRead;
            }
            // 如果得到的字节长度和file实际的长度不一致就可能出错了
            if (offset < bytes.length) {
                return null;
            }
            is.close();
        }
        return bytes;
    }
    public final static String MD5(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = pwd.getBytes();

            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }
            return new String(str);

        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }
}
