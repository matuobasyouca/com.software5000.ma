package com.software5000.ma.controller;

/**
 * Created by luo on 2017/7/24.
 */

import com.software5000.bank.entity.PayOrder;
import com.software5000.bank.service.PayOrderService;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultUtil;
import com.software5000.ma.entity.RechargeOrder;
import com.software5000.ma.entity.User;
import com.software5000.ma.entity.WeChatPayOrder;
import com.software5000.ma.service.*;
import com.software5000.util.PayCommonUtil;
import com.software5000.util.QRCodeUtil;
import com.zscp.master.util.MathUtil;
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
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class RechargeOrderController {
    private Log log = LogFactory.getLog(RechargeOrderController.class);

    @Resource
    private RechargeOrderService rechargeOrderService;

    @Resource
    private UserService userService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private WeChatPayOrderService weChatPayOrderService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private FinanceService financeService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    //充值订单
    @RequestMapping(value = "/home/member/insertRechargeOrder", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertRechargeOrder(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        Integer userId=null;
        User user=new User();
        try {
            if(null!=param.get("userId")){
                userId=Integer.parseInt(param.get("userId").toString());
                user.setId(userId);
                user.setRealName(param.get("realName").toString());
                userService.updateUser(user);
            }else{
                String carNums=param.get("carNums").toString();
                String mobile =param.get("mobile").toString();
                for(String carNum:carNums.split(",")){
                    user=userService.insertUser(null,mobile,carNum);
                }
                user.setRealName(param.get("realName").toString());
                userService.updateUser(user);
                userId=user.getId();
            }
        }catch (SQLException e){
            returnResult=ReturnResult.buildResult(e.getMessage());
            return returnResult;
        }
        Double reChargeMoney=Double.parseDouble(null==param.get("reChargeMoney")?"0":"".equals(param.get("reChargeMoney").toString())?"0":param.get("reChargeMoney").toString());
        Double grantMoney=Double.parseDouble(null==param.get("grantMoney")?"0":"".equals(param.get("grantMoney").toString())?"0":param.get("grantMoney").toString());
        RechargeOrder rechargeOrder = new RechargeOrder();
        //生成唯一订单编号
        rechargeOrder.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        //默认为未支付
        rechargeOrder.setState(Constant.OrderState.TO_BE_PAID.codeName);//待付款
        rechargeOrder.setUserId(userId);
        rechargeOrder.setBusinessId(UserDefaultUtil.getBusinessId());
        rechargeOrder.setReChargeMoney(reChargeMoney);
        rechargeOrder.setGrantMoney(grantMoney);
        try{
            rechargeOrder=rechargeOrderService.insertRechargeOrder(rechargeOrder);
            returnResult.setData(rechargeOrder.getId());
        }catch (Exception e){
            returnResult=ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 充值支付
     * @param param
     * @return
     */
    @RequestMapping(value = "/home/member/updateRechargeOrderState", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateRechargeOrderState(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try{
            RechargeOrder rechargeOrder=new RechargeOrder();
            rechargeOrder.setId(Integer.parseInt(param.get("id").toString()));
            rechargeOrder=rechargeOrderService.selectRechargeOrder(rechargeOrder);
            //支付套餐卡
            rechargeOrder.setState(Constant.OrderState.PAID.codeName);//付款
            rechargeOrder.setPayType(Constant.WorkOrderPayType.CASH.codeName);
            rechargeOrderService.updateRechargeOrder(rechargeOrder);
            //增加用户余额修改用户等级
            financeService.insertFinanceRecordByOrder(rechargeOrder,null);
            memberLvlRecordService.updateMemberLvl(rechargeOrder.getUserId(),rechargeOrder.getBusinessId(),rechargeOrder.getReChargeMoney(),rechargeOrder.getGrantMoney(),true);
        }catch (Exception e){
            returnResult=ReturnResult.buildEnumResult(Constant.StateCode.ERROR);
            returnResult.setMsg(e.getMessage());
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 充值记录
     */
    @RequestMapping(value="/home/member/selectRechargeOrderPage",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectRechargeOrderPage(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try {
            param.put("state",Constant.OrderState.PAID.codeName);
            returnResult.setData(rechargeOrderService.selectPayPackageOrderPage(param));
        } catch (ServiceException e) {
            returnResult.buildResult(e.getMessage());
        }
        return  returnResult;
    }



    /**
     * 充值详情
     */
    @RequestMapping(value="/home/member/selectRechargeOrderById",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectRechargeOrderByIds(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try{
            List id=new ArrayList();
            id.add(Integer.parseInt(param.get("id").toString()));
            param.put("id",id);
            returnResult.setData(rechargeOrderService.selectRechargeOrderById(param).get(0));
        }catch(SQLException e){
            log.error("套餐卡查询失败:",e);
            returnResult.buildResult(Constant.StateCode.SELECT_ERROR.codeName);
        }
        return returnResult;
    }

    /**
     * 充值付款--收银台付款
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/home/member/selectRechargeOrderDeskPay", method = RequestMethod.GET)
    @ResponseBody
    public void selectRechargeOrderDeskPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        RechargeOrder rechargeOrder=new RechargeOrder();
        rechargeOrder.setId(Integer.parseInt(request.getParameter("rechargeOrderId")));
        rechargeOrder= rechargeOrderService.selectRechargeOrder(rechargeOrder);
        WeChatPayOrder param = new WeChatPayOrder();
        param.setPackageOrderId(rechargeOrder.getId());
        param.setOrderType(Constant.WeChatPayOrderType.RECHARGE_ORDER_TYPE.codeName);
        //该订单已有记录，避免用户在重复 关闭点开二维码的时候重复插入数据
        WeChatPayOrder weChatPayOrder = weChatPayOrderService.selectWeChatPayOrderByWeChatOrderInfo(param);

        if (Constant.OrderState.TO_BE_PAID.codeName.equals(rechargeOrder.getState())) {
            int tradeFee = MathUtil.mul(rechargeOrder.getReChargeMoney(), 100).intValue(); // 价格   注意：价格的单位是分
            String out_trade_no = PayCommonUtil.getOutTradeNo(rechargeOrder.getId(), tradeFee, PayCommonUtil.TYPE_OF_RECHARGEORDER, weChatPayOrder == null ? null : weChatPayOrder.getTradeNo()); // 订单号
            if (weChatPayOrder == null) {
                weChatPayOrder = new WeChatPayOrder();
                weChatPayOrder.setConfirmState(Constant.getIntegerCodeValueByName(Constant.WeChatPayOrderState.NO_CONFIRM));
                weChatPayOrder.setOrderType(Constant.WeChatPayOrderType.RECHARGE_ORDER_TYPE.codeName);
            }

            //添加/修改微信订单
            weChatPayOrder.setStatus(Constant.OrderState.TO_BE_PAID.codeName.toString());//未付款状态
            weChatPayOrder.setTradeNo(out_trade_no);
            weChatPayOrder.setTradeFee(tradeFee);
            weChatPayOrder.setCheckMoneyFee(tradeFee);
            weChatPayOrder.setPackageOrderId(Integer.parseInt(request.getParameter("rechargeOrderId")));
            weChatPayOrder.setShowName("会员充值");
            weChatPayOrder.setTradingTime(rechargeOrder.getUpdateTime());
            weChatPayOrder.setBusinessId(rechargeOrder.getBusinessId());
            if (weChatPayOrder.getId() == null) {
                weChatPayOrderService.insertWeChatPayOrder(weChatPayOrder);
            } else {
                weChatPayOrderService.updateWeChatPayOrder(weChatPayOrder);
            }
            //生成收银台订单，后续独立收银台功能可以用http.post方式调用
            PayOrder payOrder = new PayOrder();
            payOrder.setOrderNo(weChatPayOrder.getTradeNo());
            payOrder.setOrderService("会员充值");
            payOrder.setBusinessId(weChatPayOrder.getBusinessId());
            payOrder.setOrderTitle("会员充值");

            //获取公共的跳转地址
            payOrder.setTotalPrice(tradeFee);
            PayOrder order = payOrderService.insertPayOrderOrUpdateIfExit(payOrder);
            QRCodeUtil.createPayQRCode(order.getId(), response);
        }
    }

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

}