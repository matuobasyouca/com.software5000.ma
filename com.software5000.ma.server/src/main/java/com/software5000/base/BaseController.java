package com.software5000.base;

import com.alibaba.fastjson.JSONObject;
import com.riversoft.weixin.common.WxClient;
import com.riversoft.weixin.common.decrypt.SHA1;
import com.riversoft.weixin.common.jsapi.JsAPISignature;
import com.riversoft.weixin.mp.MpWxClientFactory;
import com.riversoft.weixin.mp.base.AppSetting;
import com.software5000.bank.entity.PayOrder;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.entity.SystemCode;
import com.software5000.base.mybatis.plugins.CommonDataInterceptor;
import com.software5000.base.security.jwt.TokenResponse;
import com.software5000.ma.entity.*;
import com.software5000.ma.service.*;
import com.software5000.util.PayCommonUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

import static java.util.stream.Collectors.toMap;

//@Controller
public class BaseController {

    protected Log log = LogFactory.getLog(BaseController.class);

    @Autowired
    protected ServletContext servletContext;

    @Resource
    private MyBaseDao baseDao;

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private BusinessPackageOrderService businessPackageOrderService;

    @Resource
    private CooperComboOrderService cooperComboOrderService;

    @Resource
    private CouponsPackService couponsPackService;

    @Resource
    private FinanceService financeService;

    @RequestMapping("/mzscp/upLoadFile")
    @ResponseBody
    public ReturnResult uploadImage(HttpServletRequest request) throws IOException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        long  startTime=System.currentTimeMillis();
        //将当前上下文初始化给  CommonsMutipartResolver （多部分解析器）....

        CommonsMultipartResolver multipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        //检查form中是否有enctype="multipart/form-data"
        if(multipartResolver.isMultipart(request))
        {
            //将request变成多部分request
            MultipartHttpServletRequest multiRequest=(MultipartHttpServletRequest)request;
            //获取multiRequest 中所有的文件名
            Iterator iter=multiRequest.getFileNames();

            while(iter.hasNext())
            {
                //一次遍历所有文件
                MultipartFile file=multiRequest.getFile(iter.next().toString());
                if(file!=null)
                {
                    String filePath = "D:/testUploadFile/";
                    if(!new File(filePath).exists()) {
                        (new File(filePath)).mkdirs();
                    }
                    String path=filePath+file.getOriginalFilename();
                    //上传
                    file.transferTo(new File(path));
                }

            }
        }
        long  endTime=System.currentTimeMillis();
        return  returnResult;

    }

    /**
     * 返回省级菜单
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/mzscp/selectProvinces", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectProvinces() {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();

        returnResult.setData(Constant.getAreaCodesByParentId(100000));
        return returnResult;
    }

    /**
     * 返回省级菜单
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/mzscp/selectAreaCode", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectAreaCodes(@RequestBody Map parm) {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        returnResult.setData(Constant.getAreaCodesByParentId( Integer.parseInt(parm.get("areaId").toString())));
        return returnResult;
    }

    @RequestMapping(path = "/mzscp/refreshMem")
    @ResponseBody
    public String refreshMem(HttpServletRequest request) {

        try {
            Constant.initCodes(((List<SystemCode>)baseDao.selectEntity(new SystemCode())).stream().collect(toMap(SystemCode::getCodeName, (p) -> p)));
            log.info("刷新缓存数据成功！");
            return " refresh memory successful . ";
        } catch (Exception e) {
            log.error("初始化缓存数据错误！", e);
            return " refresh memory failed , messgage : " + e.getMessage() + " . ";
        }

    }

    /**
     * 选择constant-enums配置属性值
     * @param param
     * @return
     */
    @RequestMapping(value = "/open/mzscp/selectConstantTypes", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectConstantTypes(@RequestBody Map param) {
        String[] codeTypes = param.get("codeTypes").toString().split(",");
        Map<String, List<String[]>> codeMap = new HashMap<>();
        for (String codeType : codeTypes) {
            codeMap.put(codeType, Constant.enums.get(codeType));
        }
        return ReturnResult.buildSuccessMsg().setData(codeMap);
    }

    @RequestMapping(path = "open/mzscp/refreshPayOrderBusinessId")
    @ResponseBody
    public String refreshPayOrderBusinessId(HttpServletRequest request) {

        try {
            List<PayOrder> payOrders = baseDao.selectEntity(new PayOrder());
            for (PayOrder payOrder : payOrders) {
                String orderNo = payOrder.getOrderNo();
                if (ValidUtil.isNotEmpty(orderNo)) {
                    String[] split = orderNo.split("_");
                    Integer id = Integer.valueOf(split[0]);
                    String type = split[2];
                    if(type.equals(PayCommonUtil.TYPE_OF_WORKORDER)){//工单
                        WorkOrder workOrder = workOrderService.selectWorkOrderById(id);
                        if(workOrder != null) {
                            payOrder.setBusinessId(workOrder.getBusinessId());
                        }
                    } else if(type.equals(PayCommonUtil.TYPE_OF_PACKAGEORDER)){//套餐
                        BusinessPackageOrder businessPackageOrder = businessPackageOrderService.selectBusinessPackageOrderById(new HashMap() {{
                            put("id", id);
                        }});
                        if(businessPackageOrder!=null) {
                            payOrder.setBusinessId(businessPackageOrder.getBusinessId());
                        }
                    } else if(type.equals(PayCommonUtil.TYPE_OF_COOPERCOMBOORDER)){//原诚品合作
                        CooperComboOrder cooperComboOrder = cooperComboOrderService.selectCooperComboOrderById(id);
                        if(cooperComboOrder!=null) {
                            payOrder.setBusinessId(cooperComboOrder.getBusinessId());
                        }
                    } else if(type.equals(PayCommonUtil.TYPE_OF_COUPONSPACK)){//原99卡包
                        CouponsPackBuyRecord couponsPackBuyRecord = couponsPackService.selectBuyRecordById(id);
                        if(couponsPackBuyRecord!=null) {
                            payOrder.setBusinessId(couponsPackBuyRecord.getBusinessId());
                        }
                    } else if(type.equals(PayCommonUtil.TYPE_OF_EMKT_COUPONSPACK)){//卡包
                        Finance finance = financeService.selectFinanceByOrderNo(orderNo);
                        if(finance!=null) {
                            payOrder.setBusinessId(finance.getBusinessId());
                        }
                    }
                    CommonDataInterceptor.ignoreDataThisTime();
                    baseDao.updateEntity(payOrder);
                }
            }
            return "success";
        } catch (SQLException e) {
            log.error("BaseController.refreshPayOrderBusinessId", e);
            return "error";
        }

    }

    public static Cookie cleanTokenCookies(){
        SecurityUtils.getSubject().logout();
        Cookie cookie = new Cookie("ztoken", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(Constant.doMain);
        return cookie;
    }

    public static Cookie setTokenCookies(TokenResponse token ){
        Cookie cookie = new Cookie("ztoken", token.getToken().toString());
        cookie.setMaxAge(Integer.MAX_VALUE);
        cookie.setPath("/");
        cookie.setDomain(Constant.doMain);
        return cookie;
    }

    /**
     * 获取jssdk
     *
     * @return
     */
    @RequestMapping(value = "/open/mzscp/selectWxJsAPI")
    @ResponseBody
    public ReturnResult selectWxJsAPI(HttpServletRequest request, @RequestBody Map paramMap) {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try {
            WxClient wxClient = MpWxClientFactory.getInstance().with(AppSetting.defaultSettings());
            JSONObject jsonObject = JSONObject.parseObject(wxClient.get("https://api.weixin.qq.com/cgi-bin/ticket/getticket?type=jsapi", true));
            String ticket = jsonObject.get("ticket").toString();
            String nonce = UUID.randomUUID().toString();
            Long timestamp = (System.currentTimeMillis() / 1000);
            String url = paramMap.get("url").toString();

            JsAPISignature jsApi = new JsAPISignature();
            jsApi.setAppId(AppSetting.defaultSettings().getAppId());
            jsApi.setNonce(nonce);
            jsApi.setTimestamp(timestamp);
            jsApi.setUrl(url);
            jsApi.setSignature(SHA1.getSHA1("jsapi_ticket=" + ticket + "&noncestr=" + nonce + "&timestamp=" + Long.valueOf(timestamp) + "&url=" + url));
            returnResult.setData(jsApi);
        } catch (Exception e) {
            log.error("获取微信jsapi出错", e);
        }
        return returnResult;
    }

    /**
     * 重定向（用于分享）
     * @param request
     * @return
     */
    @RequestMapping(value = "/open/mzscp/redirctUrl")
    public String redirctUrl(HttpServletRequest request) {
        return "redirect:" + request.getParameter("url");
    }
}
