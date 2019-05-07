package com.software5000.ma.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.riversoft.weixin.common.util.URLEncoder;
import com.riversoft.weixin.mp.user.Users;
import com.software5000.base.BaseController;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultZimpl;
import com.software5000.base.security.UserRepositoryZimpl;
import com.software5000.base.security.jwt.TokenResponse;
import com.software5000.ma.dto.MerchantLoginDto;
import com.software5000.ma.entity.Business;
import com.software5000.ma.entity.BusinessUser;
import com.software5000.ma.service.BusinessPackageOrderService;
import com.software5000.ma.service.BusinessService;
import com.software5000.ma.service.WorkOrderService;
import com.software5000.util.BatchTagUtil;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Map;

/**
 * 联盟商家控制类
 * Created by liushaojie on 2016/12/19.
 */
@Controller
public class AuthController {
    protected Log log = LogFactory.getLog(this.getClass());

//    private static CaptchaService captchaService = new DefaultManageableImageCaptchaService(
//            new FastHashMapCaptchaStore(), new GMailEngine(), 180,
//            100000, 75000);

    @Resource
    UserRepositoryZimpl shiroBoundary;

    @Autowired
    private BusinessService businessService;

    @Resource
    private BusinessPackageOrderService businessPackageOrderService;

    @Resource
    private WorkOrderService workOrderService;

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

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== open (开放) end ==================================*/
    //</editor-fold>


    /**
     * 根据联盟商家用户名、密码验证登陆
     */
    @RequestMapping(value = "/merchant/login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult login(@RequestBody String userNameAndPwdJson,HttpServletRequest request, HttpServletResponse response) {
        ReturnResult returnResult = ReturnResult.buildResult(Constant.StateCode.LOGIN_SUCCESS.codeName);

        JSONObject jsonObject = JSON.parseObject(userNameAndPwdJson);
        String userId = jsonObject.getString("userId");
        String userType = jsonObject.getString("userType");
        log.debug("userNameAndPwdJson:"+userNameAndPwdJson);

        try {
            String password = new String(Base64.getDecoder().decode(jsonObject.getString("password")),"utf-8");

            response.addCookie(BaseController.cleanTokenCookies());

                TokenResponse token = null;
                Subject cuser = SecurityUtils.getSubject();

                UserDefaultZimpl t = new UserDefaultZimpl(userId, password, userType);
                cuser.login(t);

                UserDefaultZimpl user = UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils();
                token = shiroBoundary.createToken(user);

                SavedRequest savedRequest = WebUtils.getAndClearSavedRequest(request);

                response.addCookie(BaseController.setTokenCookies(token));
                if(user.getRealEntity() instanceof Business){
                    if(Constant.BusinessLineState.BUSINESS_ONLINE.codeName.equals(((Business)user.getRealEntity()).getBusinessLineState())) {
                        String wxOpenId = jsonObject.getString("wxOpenId");
                        BusinessUser businessUser = user.getBusinessUser();
                        if(ValidUtil.isNotEmpty(wxOpenId)) {
                            //为用户打标签
                            BatchTagUtil.batchTagging(wxOpenId,Constant.NormalSetting.BUSINESS_USER_TAGS);
                        }
                        returnResult = ReturnResult.buildResult(Constant.StateCode.LOGIN_SUCCESS.codeName);
                        MerchantLoginDto merchantLoginDto = new MerchantLoginDto();
                        merchantLoginDto.setUserType(businessUser.getMercharType());
                        merchantLoginDto.setBusinessId(((Business) user.getRealEntity()).getId());
                        merchantLoginDto.setBusinessName(((Business) user.getRealEntity()).getBusinessName());
                        merchantLoginDto.setUserName(businessUser.getUserName());
                        merchantLoginDto.setUserOpenId(businessUser.getWxOpenId()); //用户已有的openId

                        merchantLoginDto.setRealName(businessUser.getRealName());
//                    merchantLoginDto.setLoginOpenId(openid); //当前登录该用户的openId
                        userType = merchantLoginDto.getUserType();
                        if (savedRequest != null)
                            merchantLoginDto.setRdUrl(savedRequest.getRequestUrl());
                        else {
                            if (userType.equals(Constant.UserType.MERCHANT)) {
                                merchantLoginDto.setRdUrl(Constant.getCodeByName(Constant.SecuritySetting.MERCHANT_DEAFULT_URL).getCodeValue());
                            } else if (userType.equals(Constant.UserType.OPERATOR)) {
                                merchantLoginDto.setRdUrl(Constant.getCodeByName(Constant.SecuritySetting.OPERATOR_DEAFULT_URL).getCodeValue());
                            }
                        }
//                    returnResult.setData(((Business)user.getRealEntity()).getBusinessName()+","+businessUser.getMercharType()+","+user.getBusinessUser().getRealName());
                        returnResult.setData(merchantLoginDto);
                    }else{
                        response.addCookie(BaseController.cleanTokenCookies());
                        returnResult = ReturnResult.buildEnumResult(Constant.StateCode.BUSINESS_ONLINE_FAIL);
                    }
                }
        } catch (Exception e) {
            response.addCookie(BaseController.cleanTokenCookies());
            log.error("认证失败！", e);
            returnResult = ReturnResult.buildResult(Constant.StateCode.LOGIN_FAIL.codeName);
        }
        return returnResult;
    }

    /**
     * 根据运营用户名、密码验证登陆
     */
    @RequestMapping(value = "/operator/login", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult operatorLogin(@RequestBody String userNameAndPwdJson, HttpServletRequest request, HttpServletResponse response) {
        ReturnResult returnResult = ReturnResult.buildEnumResult(Constant.StateCode.LOGIN_SUCCESS);
        JSONObject jsonObject = JSON.parseObject(userNameAndPwdJson);
        String userId = jsonObject.getString("userId");
        String userType = jsonObject.getString("userType");
        log.debug("userNameAndPwdJson:"+userNameAndPwdJson);

        try {
            String password = new String(Base64.getDecoder().decode(jsonObject.getString("password")),"utf-8");

            response.addCookie(BaseController.cleanTokenCookies());


            TokenResponse token = null;
            Subject cuser = SecurityUtils.getSubject();

            UserDefaultZimpl t = new UserDefaultZimpl(userId, password,userType);
            cuser.login(t);

            String wxOpenId = jsonObject.getString("wxOpenId");
            if(ValidUtil.isNotEmpty(wxOpenId)) {
                //为用户打标签
                BatchTagUtil.batchTagging(wxOpenId,Constant.NormalSetting.OPERATOR_USER_TAGS);
            }
            UserDefaultZimpl user = UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils();
            token = shiroBoundary.createToken(user);
            response.addCookie(BaseController.setTokenCookies(token));
            returnResult.setData(user.getRealEntity());
        } catch (Exception e) {
            response.addCookie(BaseController.cleanTokenCookies());
            log.error("认证失败！", e);
            returnResult = ReturnResult.buildEnumResult(Constant.StateCode.LOGIN_FAIL);
        }
        return returnResult;
    }

    /**
     * 登陆页面封装
     *
     * @return
     */
    @RequestMapping(value = "/connectLogin")
    public String login(HttpServletRequest request) {
        String url = Constant.redirectUrl;
        String localUrl = request.getServerName() + ":" + request.getServerPort() + "/merchant/login.html?code=";
        return "redirect:" + url + URLEncoder.encode(localUrl);
    }

    /**
     * 登陆页面封装
     *
     * @return
     */
    @CrossOrigin
    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult logout(HttpServletResponse response) {
        response.addCookie(BaseController.cleanTokenCookies());
        return ReturnResult.buildEnumResult(Constant.StateCode.SUCCESS);
    }

    /**
     * 设置禁止客户端缓存的Header.
     */
    private void setDisableCacheHeader(HttpServletResponse response) {
        //Http 1.0 header
        response.setDateHeader("Expires", 1L);
        response.addHeader("Pragma", "no-cache");
        //Http 1.1 header
        response.setHeader("Cache-Control", "no-cache, no-store, max-age=0");
    }

    /**
     * 根据openId获取用户昵称
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/selectWxNickNameByOpenId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWxNickNameByOpenId(@RequestBody Map param) throws SQLException {
        com.riversoft.weixin.mp.user.bean.User user = Users.defaultUsers().get(param.get("openId").toString());
        return ReturnResult.buildEnumResult(Constant.StateCode.SUCCESS).setData(user.getNickName());
    }

    /**
     * 根据openId获取用户的最早关系数据
     */
    @RequestMapping(value="/open/customer/selectInitialByOpenId",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectInitialByOpenId(@RequestBody Map param) throws SQLException{
        ReturnResult returnResult=ReturnResult.buildEnumResult(Constant.StateCode.SUCCESS);
        try {
            Map wokerMap=workOrderService.selectInitialByOpenId(param);
            Map packageOrderMap=businessPackageOrderService.selectInitialByOpenId(param);
            Map map=null==wokerMap&&null==packageOrderMap?null:null==wokerMap?packageOrderMap:null==packageOrderMap?wokerMap: DateUtils.dateToUnixTimestamp(packageOrderMap.get("time").toString())>DateUtils.dateToUnixTimestamp(wokerMap.get("time").toString())?packageOrderMap:wokerMap;
            returnResult.setData(map);
        } catch (ServiceException e) {
            log.error("认证失败！", e);
            returnResult.setData("");
        }
        return returnResult;
    }

    /**
     * 查询用户是否关注
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/customer/selectWxIsSubscribed", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWxIsSubscribed(@RequestBody Map param) throws SQLException {
        com.riversoft.weixin.mp.user.bean.User user = Users.defaultUsers().get(param.get("wxOpenId").toString());
        return ReturnResult.buildEnumResult(Constant.StateCode.SUCCESS).setData(user == null ? false : user.isSubscribed());
    }

}
