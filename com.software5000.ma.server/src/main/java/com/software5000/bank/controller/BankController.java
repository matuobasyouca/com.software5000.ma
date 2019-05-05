package com.software5000.bank.controller;

import com.alibaba.fastjson.JSON;
import com.riversoft.weixin.common.util.JsonMapper;
import com.riversoft.weixin.common.util.URLEncoder;
import com.riversoft.weixin.common.util.XmlObjectMapper;
import com.riversoft.weixin.mp.oauth2.MpOAuth2s;
import com.riversoft.weixin.mp.user.Users;
import com.riversoft.weixin.mp.user.bean.User;
import com.riversoft.weixin.pay.base.PaySetting;
import com.riversoft.weixin.pay.payment.Payments;
import com.riversoft.weixin.pay.payment.Signatures;
import com.riversoft.weixin.pay.payment.bean.Signature;
import com.riversoft.weixin.pay.payment.bean.UnifiedOrderResponse;
import com.riversoft.weixin.pay.util.SignatureUtil;
import com.software5000.bank.dto.PayOrderDetailDto;
import com.software5000.bank.dto.PaymentNotificationWithCoupons;
import com.software5000.bank.dto.WeixinJSBridgeDto;
import com.software5000.bank.entity.PayOrder;
import com.software5000.bank.service.PayOrderService;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.mybatis.plugins.PermissionHelper;
import com.software5000.ma.entity.BusinessCheckMoney;
import com.software5000.ma.entity.PackageAndItem;
import com.software5000.ma.entity.WeChatPayOrder;
import com.software5000.ma.service.*;
import com.software5000.util.PayCommonUtil;
import com.software5000.util.PostUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by jiye on 2017/7/1.
 */
@Controller
public class BankController {

    @Autowired
    private PayOrderService payOrderService;

    @Autowired
    private WeChatPayOrderService weChatPayOrderService;

    @Resource
    private CouponsPackService couponsPackService;

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private CooperComboOrderService cooperComboOrderService;

    @Resource
    private BusinessPackageOrderService businessPackageOrderService;

    @Resource
    private PackClusterBuyRecordService packClusterBuyRecordService;

    @Resource
    private RechargeOrderService rechargeOrderService;

    protected Log log = LogFactory.getLog(this.getClass());


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

    /**
     * 插入支付订单信息
     *
     * @param payOrder
     * @return
     */
    @RequestMapping(value = "/api/bank/insertPayOrder", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertPayOrder(@RequestBody PayOrder payOrder) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(payOrderService.insertPayOrder(payOrder));
    }

    /**
     * 增加payorder信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/api/bank/insertPayOrderOrUpdateIfExitApi", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertPayOrderOrUpdateIfExitApi(@RequestBody Map param) throws SQLException {
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderTitle("套餐支付");
        payOrder.setTotalPrice(Integer.valueOf(param.get("payMoney").toString()));
        payOrder.setOrderService(param.get("orderService").toString());
        List<PackageAndItem> packageAndItems = JSON.parseArray(param.get("packageAndItems").toString(), PackageAndItem.class);
        List<PayOrderDetailDto> dtos = new ArrayList<>();
        for (PackageAndItem packageAndItem : packageAndItems) {
            PayOrderDetailDto dto = new PayOrderDetailDto(packageAndItem.getServiceItem().getItemName(), packageAndItem.getUseTimes() + "次");
            dtos.add(dto);
        }
        payOrder.setOrderDetail(JSON.toJSONString(dtos));
        //是否有关注 么有的话跳到关注页，如果已经关注跳到参团详情页
        com.riversoft.weixin.mp.user.bean.User riverUser = Users.defaultUsers().get(param.get("wxOpenId").toString());
        String redirectUrl = Constant.maUrl;
        if (riverUser.isSubscribed()) {//已经关注
            redirectUrl += String.format(Constant.getStringCodeValueByName(Constant.NormalSetting.PACK_CLUSTER_DETAIL_URL), Integer.valueOf(param.get("packClusterId").toString()));
        } else {//未关注
            redirectUrl += Constant.getStringCodeValueByName(Constant.NormalSetting.PACK_CLUSTER_NOTE_URL);
        }
        payOrder.setRedirectUrl(redirectUrl);
        payOrder.setOrderNo(param.get("orderNo").toString());
        payOrder.setBusinessId(Integer.valueOf(param.get("businessId").toString()));
        payOrder = payOrderService.insertPayOrderOrUpdateIfExit(payOrder);
        return ReturnResult.buildSuccessMsg().setData(Constant.getStringCodeValueByName(Constant.CashierDesk.CASHIER_DESK_URL_JSAPI) + "?payOrderId=" + payOrder.getId());
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 更新支付订单信息
     *
     * @param payOrder
     * @return
     */
    @RequestMapping(value = "/api/bank/updatePayOrder", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updatePayOrder(@RequestBody PayOrder payOrder) throws SQLException {
        payOrderService.updatePayOrder(payOrder);
        return ReturnResult.buildSuccessMsg();
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 获取订单是否已经支付成功
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/api/bank/selectOrderForPaidByOrderNo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPayOrderByOrderNo(@RequestBody Map param) throws SQLException {
        PayOrder payOrder = payOrderService.selectSinglePayOrderByOrderNo(param.get("orderNo").toString());
        return ReturnResult.buildSuccessMsg().setData(payOrder == null ? false : (Constant.OrderState.PAID.codeName.equals(payOrder.getPayState()) ? true : false));
    }

    /**
     * 根据单号查询
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/api/bank/selectSinglePayOrderByOrderNoEmkt", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectSinglePayOrderByOrderNoEmkt(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(payOrderService.selectSinglePayOrderByOrderNo(param.get("orderNo").toString()));
    }

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
     * 2.微信跳转获取code
     */
    @RequestMapping(value = "/open/bank/redirectPay", method = RequestMethod.GET)
    public String weixinRedirect(@RequestParam String payOrderId) {
        String redirectUrl = Constant.redirectUrl;
        String notifyUrl = Constant.maUrl + Constant.getStringCodeValueByName(Constant.CashierDesk.CASHIER_DESK_URL_JSAPI);
        notifyUrl = notifyUrl.replace("http://", "");
        notifyUrl += "?payOrderId=" + payOrderId + "&code=";
        return "redirect:" + redirectUrl + URLEncoder.encode(notifyUrl);
    }

    /**
     * 3.返回PayOrder信息
     */
    @RequestMapping(value = "/open/bank/selectPayOrder")
    @ResponseBody
    public ReturnResult prePayOrder(@RequestBody Map<String, Integer> payOrderIdMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(payOrderService.selectSinglePayOrderById(payOrderIdMap.get("payOrderId")));
    }

    /**
     * 4.生成预支付订单 AJAX返回
     */
    @RequestMapping(value = "/open/bank/unifiedOrder")
    @ResponseBody
    public ReturnResult unifiedOrder(@RequestBody Map payOrderIdAndCodeMap, HttpServletRequest request) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        Integer payOrderId = (Integer) payOrderIdAndCodeMap.get("payOrderId");
        String code = payOrderIdAndCodeMap.get("code").toString();
        PayOrder payOrder = payOrderService.selectSinglePayOrderById(payOrderId);
        if (payOrder == null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.SELECT_ERROR);
        }
        UnifiedOrderResponse unifiedOrderResponse = null;
        String openid = null;
        try {
            openid = MpOAuth2s.defaultOAuth2s().getAccessToken(code).getOpenId();
            unifiedOrderResponse = Payments.defaultOrders().unifiedOrder(PayCommonUtil.getUnifiedOrderRequest(null,
                    request.getRemoteAddr(), payOrder.getOrderTitle(), Constant.maUrl + Constant.getStringCodeValueByName(Constant.CashierDesk.PAY_ORDER_NOTIFY_URL), openid, payOrder.getOrderNo(),
                    null, "JSAPI", payOrder.getTotalPrice()));
        } catch (Exception e) {
            log.error("code已经使用过了需要重新获取");
            unifiedOrderResponse = null;
        }

        if (unifiedOrderResponse == null || openid == null)
            return ReturnResult.buildEnumResult(Constant.StateCode.CODE_HAVE_USE_ERR);
        if ("SUCCESS".equals(unifiedOrderResponse.getReturnCode())
                && "SUCCESS".equals(unifiedOrderResponse.getResultCode())) {
            Signature js = Signatures.defaultSignatures().createJsSignature(unifiedOrderResponse.getPrepayId());
            User user = Users.defaultUsers().get(openid);
            WeixinJSBridgeDto jsBridgeDto = new WeixinJSBridgeDto();
            jsBridgeDto.setAppId(js.getAppId());
            jsBridgeDto.setNonceStr(js.getNonce());
            jsBridgeDto.setTimeStamp(js.getTimestamp());
            jsBridgeDto.setPackagevar(unifiedOrderResponse.getPrepayId());
            jsBridgeDto.setPaySign(js.getSignature());
            jsBridgeDto.setSignType("MD5");
            jsBridgeDto.setSubscribe(user.isSubscribed() ? 1 : 0);
            returnResult.setData(jsBridgeDto);
            payOrder.setPrepayId(unifiedOrderResponse.getPrepayId());
            payOrderService.updatePayOrder(payOrder);
        } else {
            returnResult = new ReturnResult(unifiedOrderResponse.getErrorCode(), unifiedOrderResponse.getErrorCodeDesc(), null);
        }
        return returnResult;
    }

    /**
     * 5.付款之前先调用接口判断是否可以付款,
     * 暂时先直接用service方法，后续如果需要 将用http.post调用接口方式
     */
    @RequestMapping(value = "/open/bank/checkPay")
    @ResponseBody
    public ReturnResult checkPay(@RequestBody Map orderNoMap, HttpServletRequest request) throws Exception {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        String orderNo = orderNoMap.get("orderNo").toString();
        String[] split = orderNo.split("_");
        if (split.length == 4) {
            String utilType = split[2];
            Integer orderId = Integer.valueOf(split[0]);
            PermissionHelper.ignorePermissionThisTime();
            if (utilType.equals(PayCommonUtil.TYPE_OF_WORKORDER)) {//类型为工单
                returnResult = workOrderService.selectCheckCanPay(orderId);
            } else if (utilType.equals(PayCommonUtil.TYPE_OF_PACKAGEORDER)) {//类型为套餐
                returnResult = businessPackageOrderService.checkCanPay(orderId);
            } else if (utilType.equals(PayCommonUtil.TYPE_OF_COOPERCOMBOORDER)) {//类型为诚品合作套餐
                returnResult = cooperComboOrderService.checkCanPay(orderId);
            } else if (utilType.equals(PayCommonUtil.TYPE_OF_COUPONSPACK)) {//类型为线下扫码购买卡包 --已经不用考虑旧数据暂时保留
                returnResult = couponsPackService.selectBuyRecordForPayCheck(orderId);
            } else if (utilType.equals(PayCommonUtil.TYPE_OF_RECHARGEORDER)) {//会员充值
                returnResult = rechargeOrderService.checkCanPay(orderId);
            } else if (utilType.equals(PayCommonUtil.TYPE_OF_EMKT_COUPONSPACK)) {//类型为emkt线下扫码购买卡包
                PayOrder payOrder = payOrderService.selectSinglePayOrderByOrderNo(orderNo);
                if (ValidUtil.isNotEmpty(payOrder.getPayCheckUrl())) {
                    // Post验证是否可以支付
                    returnResult = PostUtil.postEMKT(payOrder.getPayCheckUrl(), new HashMap<String, Integer>() {{
                        put("orderId", orderId);
                    }});
                }
            } else if (utilType.equals(PayCommonUtil.TYPE_OF_PACK_CLUSTER)) {//拼团
                Map map = new HashMap();
                map.put("orderId", orderId);
                returnResult = PostUtil.postEMKT(Constant.Emkt.SELECT_CHECK_CAN_PAY.codeName, map);
            }
        }
        return returnResult;
    }

    /**
     * 6.支付完成回调
     *
     * @param request
     * @param response
     */
    @RequestMapping(value = "/open/bank/payOrderNotify", method = RequestMethod.POST)
    public void payOrderNotify(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String resXml = "";//给回传给微信的消息内容
        // 这里可能会有使用优惠券，所以需要自定义带优惠券信息的通知消息
        PaymentNotificationWithCoupons nonEmptyPaymentNotification = XmlObjectMapper.nonEmptyMapper().fromXml(getReceiveXml(request), PaymentNotificationWithCoupons.class);
        log.debug("nonEmptyMapper的结果：" + JSON.toJSONString(nonEmptyPaymentNotification));
        //对支付结果通知的内容做签名验证
        String wxchatSign = nonEmptyPaymentNotification.getSign();//微信返回的签名
        nonEmptyPaymentNotification.setSign(null);//生成签名不能将sign加入进行计算
        String sign = SignatureUtil.sign(JsonMapper.nonEmptyMapper().getMapper().convertValue(nonEmptyPaymentNotification, SortedMap.class), PaySetting.defaultSetting().getKey());//生成签名
        log.debug("计算出来的签名:" + sign);
        resXml = "<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml> ";
        if (wxchatSign.equals(sign) && "SUCCESS".equals(nonEmptyPaymentNotification.getResultCode()) && "SUCCESS".equals(nonEmptyPaymentNotification.getReturnCode())) {
            String out_trade_no = nonEmptyPaymentNotification.getTradeNumber();

            //3.修改payOrder状态
            //根据订单编号更新payOrder状态为已支付
            PayOrder payOrder = payOrderService.selectSinglePayOrderByOrderNo(out_trade_no);
            if (payOrder != null) {
                payOrder.setPayState(Constant.OrderState.PAID.codeName);
                payOrder.setTransactionId(nonEmptyPaymentNotification.getTransactionId());
                payOrder.setWxOpenId(nonEmptyPaymentNotification.getOpenId());
                payOrderService.updatePayOrder(payOrder);
            }

            //后边调用各自的逻辑，后续调用payOrder的notifyUrl，现在直接在此处调用
            //1.wechatPayOrder 统一处理 (排除线下扫码支付购买套餐)
            String[] split = out_trade_no.split("_");
            if (split.length == 4 && split[2].equals(PayCommonUtil.TYPE_OF_EMKT_COUPONSPACK)) {//线下扫码付款购买卡包service处理
                // 更新购买卡包逻辑
                PostUtil.postEMKT(payOrder.getNotifyUrl(), new HashMap<String, String>() {{
                    put("orderNo", out_trade_no);
                    put("payOrderId", payOrder.getId().toString());
                }});
            } else if (split.length == 4 && split[2].equals(PayCommonUtil.TYPE_OF_PACK_CLUSTER)) {//参团
                Map map = new HashMap();
                map.put("id", Integer.valueOf(split[0]));
                PostUtil.postEMKT(Constant.Emkt.UPDATE_PACKCLUSTER_BUY_RECORD_BY_PAY.codeName, map);
            } else {
                WeChatPayOrder weChatPayOrder = handleWechatPayOrder(out_trade_no, nonEmptyPaymentNotification);
                //2.根据分类分别调用各自的service处理各自的逻辑
                if (Constant.WeChatPayOrderType.WORK_ORDER_TYPE.codeName.equals(weChatPayOrder.getOrderType())) {//工单service处理
                    workOrderService.updateWorkOrderForSettle(Integer.valueOf(split[0]), payOrder.getId(), nonEmptyPaymentNotification.getOpenId());
                } else if (Constant.WeChatPayOrderType.PACKAGE_ORDER_TYPE.codeName.equals(weChatPayOrder.getOrderType())) {//套餐service处理
                    businessPackageOrderService.updatePackageOrderByWetChatPay(weChatPayOrder.getPackageOrderId(), nonEmptyPaymentNotification.getOpenId(), payOrder.getId());
                } else if (Constant.WeChatPayOrderType.COMBO_ORDER_TYPE.codeName.equals(weChatPayOrder.getOrderType())) {//诚品合作service处理
                    cooperComboOrderService.notifyCooperComboOrder(weChatPayOrder.getPackageOrderId(), nonEmptyPaymentNotification.getOpenId(), weChatPayOrder.getBusinessId(), payOrder.getId());
                } else if (Constant.WeChatPayOrderType.RECHARGE_ORDER_TYPE.codeName.equals(weChatPayOrder.getOrderType())) {//充值service处理
                    rechargeOrderService.updateRechargeOrderByWetChatPay(weChatPayOrder.getPackageOrderId(), nonEmptyPaymentNotification.getOpenId(), payOrder.getId());
                }
            }

        } else {
            resXml = "<xml><return_code><![CDATA[FAIL]]></return_code><return_msg><![CDATA[报文为空]]></return_msg></xml> ";
        }
        BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
        out.write(resXml.getBytes("UTF-8"));
        out.flush();
        out.close();
    }

    /**
     * 根据订单号查询收银台记录
     *
     * @param orderNoMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/bank/selectSinglePayOrderByOrderNo")
    @ResponseBody
    public ReturnResult selectSinglePayOrderByOrderNo(@RequestBody Map<String, String> orderNoMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(payOrderService.selectSinglePayOrderByOrderNo(orderNoMap.get("orderNo")));
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    /*=================================== open (开放) end ==================================*/
    //</editor-fold>


    /*=================================== 私有方法 ==================================*/

    /**
     * 获取微信回调的xml信息
     *
     * @param request
     * @return
     * @throws Exception
     */
    private String getReceiveXml(HttpServletRequest request) throws Exception {
        InputStream inputStream = request.getInputStream();
        StringBuffer sb = new StringBuffer();
        String s;
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
        while ((s = in.readLine()) != null) {
            sb.append(s);
        }
        in.close();
        inputStream.close();
        log.debug("接收到的xml：" + sb.toString() + "，结束");
        return sb.toString();
    }

    /**
     * 统一处理WechatPayOrder
     *
     * @param out_trade_no
     * @param nonEmptyPaymentNotification
     */
    private WeChatPayOrder handleWechatPayOrder(String out_trade_no, PaymentNotificationWithCoupons nonEmptyPaymentNotification) throws SQLException {
        WeChatPayOrder weChatPayOrder = new WeChatPayOrder();
        //解析out_trade_no 得到类型跟id进行查询订单
        String[] split = out_trade_no.split("_");
        //如果长度不为4表示旧的逻辑的数据，还是按原来的逻辑查询
        if (split.length != 4) {
            weChatPayOrder.setTradeNo(out_trade_no);
            weChatPayOrder = weChatPayOrderService.selectWeChatPayOrderByWeChatOrderInfo(weChatPayOrder);
        } else {
            Integer id = Integer.valueOf(split[0]);
            Integer type = convertOrderType(split[2]);
            weChatPayOrder.setTradeFee(nonEmptyPaymentNotification.getTotalFee());
            weChatPayOrder.setPackageOrderId(id);
            weChatPayOrder.setOrderType(type);
            weChatPayOrder = weChatPayOrderService.selectWeChatPayOrderByWeChatOrderInfo(weChatPayOrder);
        }
        if (Constant.OrderState.TO_BE_PAID.codeName.toString().equals(weChatPayOrder.getStatus())) {
            BusinessCheckMoney businessCheckMoney = weChatPayOrderService.selectBusinessCheckMoney(weChatPayOrder.getBusinessId());
            weChatPayOrder.setOpenId(nonEmptyPaymentNotification.getOpenId());
            weChatPayOrder.setStatus(Constant.OrderState.PAID.codeName.toString());
            Integer canCheckMoney = businessCheckMoney.getCanCheckMoney() + weChatPayOrder.getCheckMoneyFee();
            weChatPayOrder.setRealTimeMoneyFee(canCheckMoney);
            businessCheckMoney.setCanCheckMoney(canCheckMoney);
            weChatPayOrderService.updateBusinessCheckMoney(businessCheckMoney);
        }
        weChatPayOrderService.updateWeChatPayOrder(weChatPayOrder);
        return weChatPayOrder;
    }

    /**
     * 根据PayCommonUtil中的type转数据库SystemCode中对应的codeValue
     *
     * @return
     */
    private Integer convertOrderType(String utilType) {
        Integer type = -1;
        if (utilType.equals(PayCommonUtil.TYPE_OF_WORKORDER)) {//类型为工单
            type = Constant.WeChatPayOrderType.WORK_ORDER_TYPE.codeName;
        } else if (utilType.equals(PayCommonUtil.TYPE_OF_PACKAGEORDER)) {//类型为套餐
            type = Constant.WeChatPayOrderType.PACKAGE_ORDER_TYPE.codeName;
        } else if (utilType.equals(PayCommonUtil.TYPE_OF_COOPERCOMBOORDER)) {//类型为诚品合作套餐
            type = Constant.WeChatPayOrderType.COMBO_ORDER_TYPE.codeName;
        } else if (utilType.equals(PayCommonUtil.TYPE_OF_RECHARGEORDER)) {//类型为充值
            type = Constant.WeChatPayOrderType.RECHARGE_ORDER_TYPE.codeName;
        }
        return type;
    }

}
