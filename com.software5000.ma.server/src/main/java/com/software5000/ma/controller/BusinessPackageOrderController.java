package com.software5000.ma.controller;

/**
 * Created by luo on 2017/7/24.
 */

import com.alibaba.fastjson.JSON;
import com.software5000.bank.dto.PayOrderDetailDto;
import com.software5000.bank.entity.PayOrder;
import com.software5000.bank.service.PayOrderService;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultUtil;
import com.software5000.ma.entity.*;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class BusinessPackageOrderController {
    private Log log = LogFactory.getLog(BusinessPackageOrderController.class);

    @Resource
    private BusinessPackageOrderService businessPackageOrderService;

    @Resource
    private BusinessPackageService businessPackageService;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    @Resource
    private FinanceService financeService;

    @Resource
    private WeChatPayOrderService weChatPayOrderService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private UserService userService;

    @Resource
    private CarService carService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    //购买套餐卡
    @RequestMapping(value = "/home/businessPackage/insertBusinessPackageOrder", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult InsertBusinessPackageOrder(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        Integer packageId=Integer.parseInt(param.get("packageId").toString());
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
                    if(user !=null) {
                        user.setRealName(param.get("realName").toString());
                        userService.updateUser(user);
                        userId = user.getId();
                    }
                }

            }
        }catch (SQLException e){
            returnResult=ReturnResult.buildResult(e.getMessage());
            return returnResult;
        }
        Integer businessUserId=null!=param.get("businessUserId")?Integer.parseInt(param.get("businessUserId").toString()):null;
        Integer quantity=Integer.parseInt(param.get("quantity").toString());
        BusinessPackage businessPackage=new BusinessPackage();
        businessPackage.setId(packageId);
        businessPackage= businessPackageService.selectBusinessPackageByEntity(businessPackage);
        BusinessPackageOrder businessPackageOrder = new BusinessPackageOrder();
        //生成唯一订单编号
        businessPackageOrder.setOrderNumber(String.valueOf(System.currentTimeMillis()));
        //默认为未支付
        businessPackageOrder.setState(Constant.OrderState.TO_BE_PAID.codeName);//待付款
        businessPackageOrder.setUserId(userId);
        businessPackageOrder.setBusinessId(UserDefaultUtil.getBusinessId());
        businessPackageOrder.setBusinessPackageId(packageId);
        businessPackageOrder.setQuantity(quantity);
        businessPackageOrder.setBusinessUserId(businessUserId);
        businessPackageOrder.setTotalAmount(businessPackage.getSalePrice()*quantity);
        try{
            businessPackageOrder=businessPackageOrderService.insertBusinessPackageOrder(businessPackageOrder);
            returnResult.setData(businessPackageOrder.getId());
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
     * 套餐卡支付
     * @param param
     * @return
     */
    @RequestMapping(value = "/home/businessPackage/updateBusinessPackageOrderState", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessPackageOrderState(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try{
            BusinessPackageOrder businessPackageOrder=businessPackageOrderService.selectBusinessPackageOrderById(param);
            //支付套餐卡
            businessPackageOrder.setState(Constant.OrderState.PAID.codeName);//付款
            businessPackageOrder.setPayType(Constant.WorkOrderPayType.CASH.codeName);
            businessPackageOrderService.updateBusinessPackageOrder(businessPackageOrder);
            memberPackageRecordService.insertMemberPackageRecord(businessPackageOrder);
            businessPackageOrder.setBusinessPackageName(businessPackageOrder.getBusinessPackage().getPackageName()+"/"+businessPackageOrder.getUser().getMobile());
            financeService.insertFinanceRecordByOrder(businessPackageOrder,null);
            memberLvlRecordService.upgradeMemberLvlRecord(businessPackageOrder.getUserId(),businessPackageOrder.getBusinessId(),businessPackageOrder.getTotalAmount());
        }catch (ServiceException e){
            returnResult=ReturnResult.buildResult(e.getMessage());
        } catch (SQLException e) {
            returnResult=ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /**
     * 删除套餐订单
     */
    @RequestMapping(value = "/home/businessPackage/updateBusinessPackageOrderCancle", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessPackageOrderCancle(@RequestBody Map param) throws SQLException{
        BusinessPackageOrder businessPackageOrder=new BusinessPackageOrder();
        businessPackageOrder.setId(Integer.parseInt(param.get("businessPackageOrderId").toString()));
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try{
            businessPackageOrder=businessPackageOrderService.selectBusinessPackageOrder(businessPackageOrder);
            if(Constant.OrderState.TO_BE_PAID.codeName.equals(businessPackageOrder.getState())){
                businessPackageOrder.setState(Constant.OrderState.CANCEL_ORDER.codeName);
                businessPackageOrderService.updateBusinessPackageOrder(businessPackageOrder);
            }else{
                returnResult = returnResult.buildEnumResult(Constant.StateCode.BUSINESSPACKAGEORDER_NOT_BE_PAY);
            }
        }catch (ServiceException e){
            returnResult = returnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 待支付套餐卡列表
     */
    @RequestMapping(value="/home/businessPackage/selectNoPaidBusinessPackageOrder",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectNoPaidBusinessPackageOrder(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try {
            businessPackageOrderService.updatePackageOrderByDate();
            param.put("state",Constant.OrderState.TO_BE_PAID.codeName);
            returnResult.setData(businessPackageOrderService.selectUnPayPackageOrderPage(param));
        } catch (ServiceException e) {
            returnResult.buildResult(e.getMessage());
        }
        return  returnResult;
    }

    /**
     * 套餐卡购买记录
     */
    @RequestMapping(value="/home/businessPackage/selectPaidBusinessPackageOrder",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPaidBusinessPackageOrder(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try {
            param.put("state",Constant.OrderState.PAID.codeName);
            returnResult.setData(businessPackageOrderService.selectPayPackageOrderPage(param));
        } catch (ServiceException e) {
            returnResult.buildResult(e.getMessage());
        }
        return  returnResult;
    }



    /**
     * 套餐卡详情
     */
    @RequestMapping(value="/home/businessPackage/selectBusinessPackageOrderById",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPackageOrderById(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try{
            returnResult.setData(businessPackageOrderService.selectBusinessPackageOrderById(param));
        }catch(SQLException e){
            log.error("套餐卡查询失败:",e);
            returnResult.buildResult(Constant.StateCode.SELECT_ERROR.codeName);
        }
        return returnResult;
    }

    /**
     * 订单付款--收银台付款
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/home/businessPackage/selectPackageOrderDeskPay", method = RequestMethod.GET)
    @ResponseBody
    public void selectWorkOrderDeskPay(HttpServletRequest request, HttpServletResponse response) throws Exception {
        BusinessPackageOrder businessPackageOrder=new BusinessPackageOrder();
        Map map = new HashMap();
        map.put("id",Integer.parseInt(request.getParameter("packageOrderId")));
        businessPackageOrder= businessPackageOrderService.selectBusinessPackageOrderById(map);
        WeChatPayOrder param = new WeChatPayOrder();
        param.setPackageOrderId(businessPackageOrder.getId());
        param.setOrderType(Constant.WeChatPayOrderType.PACKAGE_ORDER_TYPE.codeName);
        //该订单已有记录，避免用户在重复 关闭点开二维码的时候重复插入数据
        WeChatPayOrder weChatPayOrder = weChatPayOrderService.selectWeChatPayOrderByWeChatOrderInfo(param);

        if (Constant.OrderState.TO_BE_PAID.codeName.equals(businessPackageOrder.getState())) {
            int tradeFee = MathUtil.mul(businessPackageOrder.getTotalAmount(), 100).intValue(); // 价格   注意：价格的单位是分
            String out_trade_no = PayCommonUtil.getOutTradeNo(businessPackageOrder.getId(), tradeFee, PayCommonUtil.TYPE_OF_PACKAGEORDER, weChatPayOrder == null ? null : weChatPayOrder.getTradeNo()); // 订单号
            if (weChatPayOrder == null) {
                weChatPayOrder = new WeChatPayOrder();
                weChatPayOrder.setConfirmState(Constant.getIntegerCodeValueByName(Constant.WeChatPayOrderState.NO_CONFIRM));
                weChatPayOrder.setOrderType(Constant.WeChatPayOrderType.PACKAGE_ORDER_TYPE.codeName);
            }

            //添加/修改微信订单
            weChatPayOrder.setStatus(Constant.OrderState.TO_BE_PAID.codeName.toString());//未付款状态
            weChatPayOrder.setTradeNo(out_trade_no);
            weChatPayOrder.setTradeFee(tradeFee);
            weChatPayOrder.setCheckMoneyFee(tradeFee);
            weChatPayOrder.setPackageOrderId(Integer.parseInt(request.getParameter("packageOrderId")));
            weChatPayOrder.setShowName(businessPackageOrder.getBusinessPackageName());
            weChatPayOrder.setTradingTime(businessPackageOrder.getUpdateTime());
            weChatPayOrder.setBusinessId(businessPackageOrder.getBusinessId());
            if (weChatPayOrder.getId() == null) {
                weChatPayOrderService.insertWeChatPayOrder(weChatPayOrder);
            } else {
                weChatPayOrderService.updateWeChatPayOrder(weChatPayOrder);
            }
            //生成收银台订单，后续独立收银台功能可以用http.post方式调用
            PayOrder payOrder = new PayOrder();
            payOrder.setOrderNo(weChatPayOrder.getTradeNo());
            if(businessPackageOrder.getBusinessPackage().getValidTerm()==null||businessPackageOrder.getBusinessPackage().getValidTerm()==0){
                payOrder.setOrderService(businessPackageOrder.getBusinessPackage().getPackageName()+businessPackageOrder.getQuantity()+"份(有效期永久)");
            }else{
                payOrder.setOrderService(businessPackageOrder.getBusinessPackage().getPackageName()+businessPackageOrder.getQuantity()+"份(有效期"+businessPackageOrder.getBusinessPackage().getValidTerm()+"年)");
            }
            payOrder.setBusinessId(weChatPayOrder.getBusinessId());
            payOrder.setOrderTitle("套餐支付");
            List<PackageAndItem> packageAndItems = businessPackageOrder.getBusinessPackage().getPackageAndItems();
            List<PayOrderDetailDto> dtos = new ArrayList<>();
            for (PackageAndItem packageAndItem:packageAndItems){
                PayOrderDetailDto dto = new PayOrderDetailDto(packageAndItem.getServiceItem().getItemName(),packageAndItem.getUseTimes()+"次");
                dtos.add(dto);
            }

            //获取公共的跳转地址
            payOrder.setRedirectUrl(PayCommonUtil.getCommonRedirectUrl(businessPackageOrder.getBusinessId(), payOrder.getOrderNo()));
            payOrder.setOrderDetail(JSON.toJSONString(dtos));
            payOrder.setTotalPrice(tradeFee);
            PayOrder order = payOrderService.insertPayOrderOrUpdateIfExit(payOrder);
            QRCodeUtil.createPayQRCode(order.getId(), response);
        }
    }

    /**
     * 套餐卡详情
     */
    @RequestMapping(value="/home/businessPackage/selectCountByState",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCountByState(@RequestBody Map param){
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        try{
            returnResult.setData(businessPackageOrderService.selectCountByState(param));
        }catch(ServiceException e){
            log.error("套餐卡查询失败:",e);
            returnResult.buildResult(Constant.StateCode.SELECT_ERROR.codeName);
        }
        return returnResult;
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

    /**
     * 根据用户openId查看已支付订单
     */
    @RequestMapping(value = "/open/businessPackage/selectUserMemberPackageRecord",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectUserMemberPackageRecord(@RequestBody Map param){
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try {
            returnResult.setData(memberPackageRecordService.selectMemberPackageRecordByOpenId(param));
        } catch (ServiceException e) {
            returnResult = ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /**
     * 根据用户openId查询各状态下的数据
     */
    @RequestMapping(value = "/open/businessPackage/selectMemberPackageRecordCount",method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMemberPackageRecordCount(@RequestBody Map param){
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try {
            returnResult.setData(memberPackageRecordService.selectMemberPackageRecordCount(param));
        } catch (ServiceException e) {
            returnResult = ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}