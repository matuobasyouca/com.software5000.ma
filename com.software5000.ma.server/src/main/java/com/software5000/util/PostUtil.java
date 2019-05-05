package com.software5000.util;

import com.alibaba.fastjson.JSON;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.jwt.JWTOrFormAuthenticationFilter;
import com.zscp.master.util.HttpUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by wujin on 2017/5/2.
 */
public class PostUtil {
    protected static Log log = LogFactory.getLog("PostUtil");
    public static ReturnResult postEMKT(String url, Map postDataMap) throws IOException{
        String emktDomain = Constant.emktUrl;
//        String emktDomain = "http://192.168.31.141:90";
        String token = Constant.getStringCodeValueByName(Constant.Emkt.MA_TOKEN.codeName);
        String result = HttpUtil.postData(emktDomain+url, JSON.toJSONString(postDataMap), new HashMap<String, String>() {{
            put(JWTOrFormAuthenticationFilter.AUTHORIZATION_HEADER, token);
            put("Content-Type", "application/json");
        }});
        log.debug("--------Emkt--------:"+result);
        return JSON.parseObject(result,ReturnResult.class);
    }


    public static ReturnResult postCRM(String url, Object postData) throws IOException{
        String crmDomain = Constant.crmUrl;
//        String emktDomain = "http://192.168.31.141:90";
        String token = Constant.getStringCodeValueByName(Constant.Emkt.MA_TOKEN.codeName);
        String result = HttpUtil.postData(crmDomain+url, JSON.toJSONString(postData), new HashMap<String, String>() {{
            put(JWTOrFormAuthenticationFilter.AUTHORIZATION_HEADER, token);
            put("Content-Type", "application/json");
        }});
        log.debug("--------crm--------:"+result);
        return JSON.parseObject(result,ReturnResult.class);
    }
}
