package com.software5000.wx;

/**
 * Created by jiye on 2017/8/21.
 */

import com.riversoft.weixin.common.event.EventType;
import com.riversoft.weixin.common.message.XmlMessageHeader;
import com.riversoft.weixin.common.request.TextRequest;
import com.riversoft.weixin.common.util.URLEncoder;
import com.riversoft.weixin.mp.base.AppSetting;
import com.riversoft.weixin.mp.care.CareMessages;
import com.riversoft.weixin.mp.event.ticket.SceneSubEvent;
import com.riversoft.weixin.mp.message.MpXmlMessages;
import com.riversoft.weixin.mp.oauth2.MpOAuth2s;
import com.software5000.ma.entity.PackClusterBuyRecord;
import com.software5000.ma.service.PackClusterBuyRecordService;
import com.software5000.util.wxaes.WXBizMsgCrypt;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.entity.SystemCode;
import com.zscp.master.util.ValidUtil;
import com.software5000.util.SignUtil;
import com.software5000.util.wxaes.AesException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class WxController {
    private Log log = LogFactory.getLog(WxController.class);

    /**
     * 购买卡包后，关注的二维码中带的参数
     */
    private static final String BUY_PACK_CLUSTER_SCAN_SUBSCRIBE = "PackClusterBuy";

    @Resource
    private PackClusterBuyRecordService packClusterBuyRecordService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== home (对内) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="api (对外) ">
    /*================================== api (对外) start ==================================*/

    //<editor-fold desc="api (对外)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== api (对外) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="home&api (内&外) ">
    /*================================== home&api (内&外) start ==================================*/

    //<editor-fold desc="home&api (内&外)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== home&api (内&外) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="open (开放) ">
    /*================================== open (开放) start ==================================*/

    //<editor-fold desc="open (开放)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 微信跳转
     * request发送格式为（http://192.168.31.128/wechat/pay/weChatRedirect?toURI=192.168.31.128/ma/direct/a.html?d=a&b=1）
     *
     * @return
     */
    @RequestMapping(value = "open/wx/weChatRedirect", method = RequestMethod.GET)
    public String weixinRedirect(HttpServletRequest request) {
        String redirectUrl = Constant.redirectUrl;
        String queryString = request.getQueryString();
        String uri = queryString.replace("toURI=", "");
        if(uri.contains("?")){
            uri+="&code=";
        }else{
            uri += "?code=";
        }
        return "redirect:" + redirectUrl + URLEncoder.encode(uri);
    }

    @RequestMapping(value = "open/wx/selectWxopenId")
    @ResponseBody
    public ReturnResult selectWxopenId(@RequestBody Map paramMap) {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try {
            if (!ValidUtil.isEmpty(paramMap.get("wxCode"))) {
                returnResult.setData(MpOAuth2s.defaultOAuth2s().getAccessToken(paramMap.get("wxCode").toString()).getOpenId());
            }else{
                returnResult = ReturnResult.buildEnumResult(Constant.StateCode.CODE_EMPTY_ERR);
            }
        }catch (Exception e){
            log.error("WxController.selectWxopenId", e);
            returnResult = ReturnResult.buildEnumResult(Constant.StateCode.CODE_HAVE_USE_ERR);
        }

        return  returnResult;
    }


    @RequestMapping(value = "open/wx/dispatch", method = RequestMethod.GET)
    public void dispatch(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {

        PrintWriter out = response.getWriter();
        // 通过检验signature对请求进行校验，若校验成功则原样返回echostr，表示接入成功，否则接入失败
        out.print(getDecryptMsg(request,response, RequestMethod.GET));
        out.close();
    }


    @RequestMapping(value = "open/wx/dispatch", method = RequestMethod.POST)
    public void dispatchpost(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        request.setCharacterEncoding("UTF-8");
        String decryptMsg = getDecryptMsg(request,response, RequestMethod.POST);
        //将xml转换成对象
        log.info("decryptMsg=" + decryptMsg);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== open (开放) end ==================================*/
    //</editor-fold>


    private WXBizMsgCrypt getWXBizMsgCrypt() {
        AppSetting appSetting = AppSetting.defaultSettings();
        WXBizMsgCrypt messageDecryption = null;
        try {
            messageDecryption = new WXBizMsgCrypt(appSetting.getToken(), appSetting.getAesKey(), appSetting.getAppId());
        } catch (AesException e) {
            log.error("WxController.getWXBizMsgCrypt", e);
        }
        return messageDecryption;
    }

    private String getDecryptMsg(HttpServletRequest request,HttpServletResponse response, RequestMethod methodType) throws IOException, SQLException {
        String msg = "";
        // 微信加密签名
        String signature = methodType.equals(RequestMethod.GET) ? request.getParameter("signature") : request.getParameter("msg_signature");
        log.info("signature=" + signature);
        // 时间戳
        String timestamp = request.getParameter("timestamp");
        log.info("timestamp=" + timestamp);
        // 随机数
        String nonce = request.getParameter("nonce");
        log.info("nonce=" + nonce);
        // 随机字符串
        String echostr = request.getParameter("echostr");
        AppSetting appSetting = AppSetting.defaultSettings();
        log.info("echostr=" + echostr);
        if (RequestMethod.GET == methodType) {
            if (SignUtil.checkSignature(appSetting.getToken(), signature, timestamp, nonce)) {
                msg = echostr;
            }
        } else if (RequestMethod.POST == methodType) {
            InputStream inputStream = request.getInputStream();
            String postData = IOUtils.toString(inputStream, "UTF-8");
            log.info("postData=" + postData);
            try {
                msg = getWXBizMsgCrypt().decryptMsg(signature, timestamp, nonce, postData);
                XmlMessageHeader xmlMessageHeader = MpXmlMessages.fromXml(msg);
                String wxopenid = xmlMessageHeader.getFromUser();//用户的wxopenid
                String toUser = xmlMessageHeader.getToUser();//公众号信息
                if (xmlMessageHeader instanceof SceneSubEvent) {//扫描二维码
                    SceneSubEvent scanEvent = (SceneSubEvent) xmlMessageHeader;
                    if (EventType.subscribe == scanEvent.getEventType()) {//表示关注
                        if (scanEvent.getEventKey().equals("qrscene_" + BUY_PACK_CLUSTER_SCAN_SUBSCRIBE)) {//表示购买完卡包后的支付
                            Map map = new HashMap();
                            map.put("state",Constant.OrderState.PAID.codeName);
                            map.put("wxOpenId", wxopenid);
                            List<PackClusterBuyRecord> packClusterBuyRecords = packClusterBuyRecordService.selectLatelyPackClusterBuyRecord(map);
                            if(packClusterBuyRecords !=null && packClusterBuyRecords.size()>0) {
                                PackClusterBuyRecord packClusterBuyRecord = packClusterBuyRecords.get(0);
                                String content = String.format(Constant.getStringCodeValueByName(Constant.SubscribeText.PACK_CLUSTER_CONTENT.codeName), Constant.maUrl,packClusterBuyRecord.getPackClusterId(),packClusterBuyRecord.getId());
                                CareMessages.defaultCareMessages().text(wxopenid, content, toUser);
                            }
                        } else if (ValidUtil.isEmpty(scanEvent.getEventKey())) {//通过公众号搜索进行的关注
                            String content = Constant.getStringCodeValueByName(Constant.SubscribeText.SEARCH_CONTENT.codeName);
                            CareMessages.defaultCareMessages().text(wxopenid, content, toUser);
                        }
                    }
                } else if(xmlMessageHeader instanceof TextRequest){ //发送关键字文本消息
                    TextRequest textRequest = (TextRequest)xmlMessageHeader;
                    Collection<SystemCode> codesByType = Constant.getCodesByType(Constant.SubscribeText.class.getSimpleName());
                    if(ValidUtil.isNotEmpty(textRequest.getContent())){
                        for (SystemCode systemCode : codesByType) {
                            if(systemCode.getCodeName().equals(textRequest.getContent().toLowerCase())){
                                CareMessages.defaultCareMessages().text(wxopenid, systemCode.getCodeValue(), toUser);
                            }
                        }
                    }
                }
            } catch (AesException e) {
                log.error("WxController.getDecryptMsg", e);
            }
        }
        log.info("msg=" + msg);
        return msg;
    }

    public String setContent(String content) {
        if(content.contains("\"")){
            content = content.replaceAll("\"", "\\\\\"");
        }
        return content;
    }

}