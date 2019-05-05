package com.software5000.ma.controller;

/**
 * Created by Jiang on 2017/07/19.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.software5000.bank.dto.PayOrderDetailDto;
import com.software5000.bank.entity.PayOrder;
import com.software5000.bank.service.PayOrderService;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.mybatis.plugins.PermissionHelper;
import com.software5000.base.security.UserDefaultUtil;
import com.software5000.ma.dto.PaymentRateDto;
import com.software5000.ma.dto.WorkOrderExcelDto;
import com.software5000.ma.entity.*;
import com.software5000.ma.service.BusinessService;
import com.software5000.ma.service.UserService;
import com.software5000.ma.service.WeChatPayOrderService;
import com.software5000.ma.service.WorkOrderService;
import com.software5000.util.*;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.MathUtil;
import com.zscp.master.util.ValidUtil;
import com.zscp.master.util.image.ImageUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Controller
public class WorkOrderController {

    private Log log = LogFactory.getLog(WorkOrderController.class);

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private UserService userService;

    @Resource
    private WeChatPayOrderService weChatPayOrderService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private BusinessService businessService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 添加施工图片
     * @param workOrderImg
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/insertWorkOrderImg", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertWorkOrderImg(@RequestBody WorkOrderImg workOrderImg) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(workOrderService.insertWorkOrderImg(workOrderImg));
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 根据工单ID，删除工单
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/deleteWorkOrderById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteWorkOrderById(@RequestBody Map param) throws SQLException {

        //根据ID，查询工单信息
        WorkOrder workOrder = workOrderService.selectWorkOrderById(Integer.valueOf(param.get("orderId").toString()));
        //校验状态是否可以删除
        if(Constant.WorkOrderState.SERVICE_ING.codeName.equals(workOrder.getState())
        || Constant.WorkOrderState.NO_PAY.codeName.equals(workOrder.getState())) {
            workOrderService.deleteWorkOrderById(workOrder.getId());
        }else {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_STATE_ERROR);
        }
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 根据ID，删除工单项目
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/deleteWorkOrderDetailById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteWorkOrderDetailById(@RequestBody Map param) throws SQLException {

        //根据ID，查询工单信息
        WorkOrder workOrder = workOrderService.selectWorkOrderById(Integer.valueOf(param.get("orderId").toString()));
        //校验状态是否可以删除
        if(Constant.WorkOrderState.SERVICE_ING.codeName.equals(workOrder.getState())) {
            workOrderService.deleteWorkOrderDetailById(Integer.valueOf(param.get("detailId").toString()));
        }else {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_STATE_ERROR);
        }
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 根据ID，删除施工图片
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/deleteWorkOrderImgById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteWorkOrderImgById(@RequestBody Map param) throws SQLException {
        workOrderService.deleteWorkOrderImgById(Integer.valueOf(param.get("imgId").toString()));
        return ReturnResult.buildSuccessMsg();
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 保存工单信息
     * @param workOrder
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/updateWorkOrder", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateWorkOrder(@RequestBody WorkOrder workOrder) throws SQLException {
        //判断ID是否为空，为空，则新增
        workOrder.setBusinessId(UserDefaultUtil.getBusinessId());
        if(workOrder.getId() == null) {
            //获取商家ID
            workOrder.setBusinessId(UserDefaultUtil.getUser().getBusinessUser().getBusinessId());
            return ReturnResult.buildSuccessMsg().setData(workOrderService.insertWorkOrderForCreate(workOrder));
        }else {
            //查询工单信息
            PermissionHelper.ignorePermissionThisTime();
            WorkOrder oldOrder = workOrderService.selectWorkOrderById(workOrder.getId());
            if(oldOrder != null && !Constant.WorkOrderState.COMPLETE.codeName.equals(oldOrder.getState())) {
                User user=userService.inserUserAndMemberLvlByException(null,workOrder.getMobile(),workOrder.getCarNumber(),workOrder.getBusinessId());
                if(null!=user){
                    workOrder.setUserId(user.getId());
                }
                //进行更新操作
                workOrderService.updateWorkOrderForEdit(workOrder);
            }else {
                return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_STATE_ERROR);
            }
        }
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 完成工单
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/updateWorkOrderForFinish", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateWorkOrderForFinish(@RequestBody Map param) throws SQLException {

        //根据ID获取工单信息
        WorkOrder workOrder = workOrderService.selectWorkOrderById(Integer.valueOf(param.get("orderId").toString()));
        //判断状态
        if(Constant.WorkOrderState.SERVICE_ING.codeName.equals(workOrder.getState())) {
            //设置工单状态和完工时间，并更新
            workOrder.setState(Constant.WorkOrderState.NO_PAY.codeName);
            workOrder.setCommitTime(new Timestamp(System.currentTimeMillis()));
            workOrderService.updateWorkOrder(workOrder);
        }else {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_STATE_ERROR);
        }
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 结算工单
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/updateWorkOrderForSettle", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateWorkOrderForSettle(@RequestBody Map param) throws SQLException, IOException {
        workOrderService.updateWorkOrderForSettle(Integer.valueOf(param.get("orderId").toString()), null, null);
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 工单反结算
     * @param param
     * @return
     * @throws SQLException
     * @throws IOException
     */
    @RequestMapping(value = "/home/workOrder/updateWorkOrderForAntiSettle", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateWorkOrderForAntiSettle(@RequestBody Map param) throws SQLException, IOException {

        //获取工单信息
        WorkOrder workOrder = workOrderService.selectWorkOrderById(Integer.valueOf(param.get("orderId").toString()));

        //判断工单状态，已结算
        if(!Constant.WorkOrderState.COMPLETE.codeName.equals(workOrder.getState())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_STATE_ERROR);
        }

        //判断关账日
        Business business = businessService.selectBusinessById(UserDefaultUtil.getBusinessId());
        if(business.getClosingDateNum() != null) {
            String closingDateNumStr = business.getClosingDateNum() < 10 ? ("0" + business.getClosingDateNum()) : business.getClosingDateNum().toString();
            String closeDateStart = ""; //可反结算的开始时间
            String closeDateEnd = ""; //可反结算的结束时间

            if(business.getClosingDateNum().compareTo(Integer.valueOf(DateUtils.formatDate(new Date(), "dd"))) >= 0) {
                String preMonth = DateUtils.formatDate(DateUtils.preMonth(new Date()), "yyyy-MM");
                closeDateStart = preMonth + "-" + closingDateNumStr + " 23:59:59";
                closeDateEnd = DateUtils.formatDate(new Date(), "yyyy-MM") + "-" + closingDateNumStr + " 23:59:59";
            }else {
                String nextMonth = DateUtils.formatDate(DateUtils.nextMonth(new Date()), "yyyy-MM");
                closeDateStart = DateUtils.formatDate(new Date(), "yyyy-MM") + "-" + closingDateNumStr + " 23:59:59";
                closeDateEnd = nextMonth + "-" + closingDateNumStr + " 23:59:59";
            }

            if(!(closeDateStart.compareTo(DateUtils.formatDate(workOrder.getPayTime(), "yyyy-MM-dd HH:mm:ss")) < 0
            && closeDateEnd.compareTo(DateUtils.formatDate(workOrder.getPayTime(), "yyyy-MM-dd HH:mm:ss")) >= 0)) {
                return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_BUSINESS_CLOSING);
            }
        }

        //判断工单付款类型是否为非微信付款
        if(Constant.WorkOrderPayType.WEB_CHAT.codeName.equals(workOrder.getPayType())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_WEBCHAT_PAY);
        }

        //判断工单是否使用了卡券
        if(!ValidUtil.isEmpty(workOrder.getCouponUuid())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_USE_COUPON);
        }

        //判断项目是否使用了卡券
        if(workOrderService.isWorkOrderUseCoupons(workOrder.getId())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_USE_COUPON);
        }

        //进行反结算
        workOrderService.updateWorkOrderForAntiSettle(Integer.valueOf(param.get("orderId").toString()));
        return ReturnResult.buildSuccessMsg();
    }



    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据车牌查询商家会员列表
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/selectMerchantMemberPage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMerchantMemberPage(@RequestBody Map param) throws SQLException {
        param.put("businessId",UserDefaultUtil.getBusinessId());
        return ReturnResult.buildSuccessMsg().setData(userService.selectCarForMemberLv(param));
    }

    /**
     * 根据车牌查询会员信息和套餐信息
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/selectMerchantMember", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMerchantMember(@RequestBody Map param) throws SQLException {
        param.put("businessId",UserDefaultUtil.getBusinessId());
        return ReturnResult.buildSuccessMsg().setData(userService.selectUserByCarNumberForBusiness(param));
    }

    /**
     * 根据车牌号，获取工单详情（包含所有信息）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/selectWorkOrderByCarNumber", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderByCarNumber(@RequestBody Map param) throws SQLException {
        //设置状态：未完成
        List<Integer> stateList = new ArrayList<Integer>();
        stateList.add(Constant.WorkOrderState.SERVICE_ING.codeName);
        param.put("stateList", stateList);
        param.put("businessId", UserDefaultUtil.getBusinessId());
        if(Constant.BusinessUserType.OPERATOR.codeName.equals(UserDefaultUtil.getUser().getUserType())) {
            param.put("operatorId", UserDefaultUtil.getUserId());
        }
        return workOrderService.selectMoreInfoByParam(param);
    }

    /**
     * 根据工单ID，获取工单详情（包含所有信息）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/selectWorkOrderById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderById(@RequestBody Map param) throws SQLException {
        param.put("businessId", UserDefaultUtil.getBusinessId());
        if(Constant.BusinessUserType.OPERATOR.codeName.equals(UserDefaultUtil.getUser().getUserType())) {
            param.put("operatorId", UserDefaultUtil.getUserId());
        }
        return workOrderService.selectMoreInfoByParam(param);
    }

    /**
     * 根据查询条件获取工单列表（PC端）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/selectWorkOrderPage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderPage(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(workOrderService.selectWorkOrderPage(param));
    }

    /**
     * 根据查询条件获取工单列表（移动端）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/selectWorkOrderPageForWap", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderPageForWap(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(workOrderService.selectWorkOrderPageForWap(param));
    }

    /**
     * 根据车牌号获取可用卡券信息
     * @param param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/home/workOrder/selectCouponByCarNumber", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCouponByCarNumber(@RequestBody Map param) throws IOException {
        param.put("carNum", param.get("carNumber"));
        param.put("businessId", UserDefaultUtil.getBusinessId());
        return PostUtil.postEMKT(Constant.Emkt.SELECT_SINGLE_COUPON.codeName, param);
    }

    /**
     * 查询优惠券是否有效
     * @param param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/home/workOrder/selectCouponUsedByUuid", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCouponUsedByUuid(@RequestBody Map param) throws IOException {
        //卡券列表
        List<String> uuidList = Arrays.asList(param.get("uuid").toString().split(","));
        for(String uuid : uuidList) {
            //查看优惠券的信息是否还有效(无效就为false 有效就有true)
            ReturnResult returnResult = PostUtil.postEMKT(Constant.Emkt.SELECT_COUPONS_USED_BY_UUID.codeName, new HashMap<String, String>() {{
                put("takeUuid", uuid);
            }});
            //查询的优惠券是否成功
            if (!returnResult.getCode().equals(Constant.StateCode.SUCCESS.codeName) || !Boolean.valueOf(returnResult.getData().toString())) {
                //如果成功判断优惠券是否有效
                return ReturnResult.buildEnumResult(Constant.StateCode.COUPON_INVALID);
            }
        }
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 订单付款--收银台付款
     *
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/home/workOrder/selectWorkOrderDeskPay", method = RequestMethod.GET)
    @ResponseBody
    public void selectWorkOrderDeskPay(HttpServletRequest request, HttpServletResponse response) throws Exception {

        WorkOrder workOrder = workOrderService.selectWorkOrderAndDetailByOrderId(Integer.parseInt(request.getParameter("orderId")));
        WeChatPayOrder param = new WeChatPayOrder();
        param.setWorkOrderId(workOrder.getId());
        param.setOrderType(Constant.WeChatPayOrderType.WORK_ORDER_TYPE.codeName);

        //该订单已有记录，避免用户在重复 关闭点开二维码的时候重复插入数据
        WeChatPayOrder weChatPayOrder = weChatPayOrderService.selectWeChatPayOrderByWeChatOrderInfo(param);
        if (Constant.WorkOrderState.NO_PAY.codeName.equals(workOrder.getState())) {

            int tradeFee = MathUtil.mul(Double.parseDouble(request.getParameter("totalPrice")), 100).intValue(); // 价格   注意：价格的单位是分
            String out_trade_no = PayCommonUtil.getOutTradeNo(workOrder.getId(), tradeFee, PayCommonUtil.TYPE_OF_WORKORDER, weChatPayOrder == null ? null : weChatPayOrder.getTradeNo());
            String urlCode = "";
            //添加/修改微信订单
            if (weChatPayOrder == null) {
                weChatPayOrder = new WeChatPayOrder();
                weChatPayOrder.setConfirmState(Constant.getIntegerCodeValueByName(Constant.WeChatPayOrderState.NO_CONFIRM));
                weChatPayOrder.setOrderType(Constant.WeChatPayOrderType.WORK_ORDER_TYPE.codeName);
            }
            weChatPayOrder.setStatus(Constant.OrderState.TO_BE_PAID.codeName.toString());//未付款状态
            weChatPayOrder.setTradeNo(out_trade_no);
            weChatPayOrder.setTradeFee(tradeFee);
            weChatPayOrder.setCheckMoneyFee(tradeFee);
            weChatPayOrder.setPackageOrderId(Integer.parseInt(request.getParameter("orderId")));
            weChatPayOrder.setWorkOrderId(Integer.parseInt(request.getParameter("orderId")));
            weChatPayOrder.setShowName(workOrder.getCarNumber());
            weChatPayOrder.setTradingTime(workOrder.getUpdateTime());
            weChatPayOrder.setBusinessId(workOrder.getBusinessId());
            if (weChatPayOrder.getId() == null) {
                weChatPayOrderService.insertWeChatPayOrder(weChatPayOrder);
            } else {
                weChatPayOrderService.updateWeChatPayOrder(weChatPayOrder);
            }
            //生成收银台订单，后续独立收银台功能可以用http.post方式调用
            PayOrder payOrder = new PayOrder();
            payOrder.setOrderNo(weChatPayOrder.getTradeNo());
            payOrder.setOrderService(workOrder.getCarNumber());
            payOrder.setOrderTitle("工单支付");
            List dtos = new ArrayList();
            //订单明细
            List<WorkOrderDetail> workOrderDetails = workOrder.getWorkOrderDetails();
            Double packageDue = 0d;
            Double discountDeduct = workOrder.getDiscountDeduct() == null ? 0d : workOrder.getDiscountDeduct();
            Double couponDeduct = workOrder.getCouponDeduct() == null ? 0d : workOrder.getCouponDeduct();
            for(WorkOrderDetail detail:workOrderDetails){
                String detailKey = detail.getServiceItemName()+"x"+detail.getItemsTimes();
                if(detail.getUsePackageTimes()!=null&&detail.getUsePackageTimes()>0){
                    detailKey += "(用卡)";
                    packageDue = MathUtil.add(packageDue,MathUtil.mul(detail.getUsePackageTimes(),detail.getSalePrice()));
                }
                String detailValue = "+"+detail.getSalePrice();
                PayOrderDetailDto dto = new PayOrderDetailDto(detailKey,detailValue);
                dtos.add(dto);

                //如果项目进行了会员扣减
                if(MathUtil.sub(transToZero(detail.getDiscountPrice()),0)>0) {
                    discountDeduct = MathUtil.add(discountDeduct, detail.getDiscountPrice());
                }
                //如果项目使用了卡券
                if(MathUtil.sub(transToZero(detail.getCouponDeduct()),0)>0) {
                    couponDeduct = MathUtil.add(couponDeduct,detail.getCouponDeduct());
                }
            }
            //材料费用
            if(MathUtil.sub(transToZero(workOrder.getMaterialCost()),0)>0){
                PayOrderDetailDto dto = new PayOrderDetailDto("材料费用","+"+workOrder.getMaterialCost());
                dtos.add(dto);
            }
            //套餐扣减
            if(MathUtil.sub(transToZero(packageDue),0)>0){
                PayOrderDetailDto dto = new PayOrderDetailDto("套餐扣减","-"+packageDue);
                dtos.add(dto);
            }

            //商家扣减
            if(MathUtil.sub(transToZero(workOrder.getBusinessDeduct()),0)>0){
                PayOrderDetailDto dto = new PayOrderDetailDto("商家扣减","-"+workOrder.getBusinessDeduct());
                dtos.add(dto);
            }
            //中晟卡券
            if(MathUtil.sub(transToZero(couponDeduct),0)>0){
                PayOrderDetailDto dto = new PayOrderDetailDto("中晟卡券","-"+couponDeduct);
                dtos.add(dto);
            }
            //会员折扣
            if(MathUtil.sub(transToZero(discountDeduct),0)>0){
                PayOrderDetailDto dto = new PayOrderDetailDto("会员扣减","-"+discountDeduct);
                dtos.add(dto);
            }
            //会员余额支付
            if(MathUtil.sub(transToZero(workOrder.getBalanceDeduct()),0)>0){
                PayOrderDetailDto dto = new PayOrderDetailDto("会员余额支付","-"+workOrder.getBalanceDeduct());
                dtos.add(dto);
            }

            //获取公共的跳转地址
            payOrder.setRedirectUrl(PayCommonUtil.getCommonRedirectUrl(workOrder.getBusinessId(), payOrder.getOrderNo()));
            payOrder.setOrderDetail(JSON.toJSONString(dtos));
            payOrder.setTotalPrice(tradeFee);
            payOrder.setBusinessId(workOrder.getBusinessId());
            PayOrder order = payOrderService.insertPayOrderOrUpdateIfExit(payOrder);
            QRCodeUtil.createPayQRCode(order.getId(),response);
        }
    }

    /**
     * 转换空的金额为0
     * @param data
     * @return
     */
    private Double transToZero(Double data) {
        if(data == null) {
            return 0D;
        }
        return data;
    }

    /**
     * 导出工单列表
     * @param param
     * @param request
     * @param response
     * @return
     * @throws SQLException
     * @throws IOException
     * @throws ServiceException
     */
    @RequestMapping(value = "/home/workOrder/selectWorkOrderForExcel", method = RequestMethod.GET)
    public String exportWorkOrder(@RequestParam Map param, HttpServletRequest request, HttpServletResponse response) throws SQLException, IOException, ServiceException {
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            //组织数据  列明，字段名 、标题1、标题2
            String caption[] = {"工单编号", "创建日期", "车牌", "客户手机号", "服务项目", "项目费用/元", "附加费用/元", "会员折扣/元", "商家减扣/元", "扣券/元", "余额支付/元", "实收款/元", "付款方式", "付款时间", "备注"};
            String detail[] = {"orderNo", "createTime", "carNumber", "mobile", "serviceItemNames", "totalPrice", "materialCost", "discountDeduct", "businessDeduct", "couponDeduct", "balanceDeduct", "payPrice", "payType", "payTime", "comment"};

            Business business = (Business) UserDefaultUtil.getUser().getRealEntity();
            String title1 = business.getBusinessName() + "工单数据";
            String title2 = "     " + DateUtils.getNowTime("yyyy年MM月dd日 HH:mm:ss") + "导出";

            //查询出列表信息并组装成WorkBook
            List<WorkOrderExcelDto> dtoList = workOrderService.selectWorkOrderForExcel(param);
            List<Map<String, Object>> list = createWorkOrderExcelRecord(dtoList);
            HSSFWorkbook workbook = ExcelUtil.createWorkBook("工单", title1, title2, caption, list, detail);
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            workbook.write(os);
            byte[] content = os.toByteArray();
            InputStream is = new ByteArrayInputStream(content);

            String filename = ExcelUtil.processFileName(request, "工单" + DateUtils.unixTimestampToDate(new Date().getTime(), "yyyy_MM_dd") + ".xls");
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + filename);
            ServletOutputStream out = response.getOutputStream();

            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            // Simple read/write loop.
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            log.error("WorkOrderController.exportWorkOrder", e);
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
        return null;
    }

    /**
     * 生成导出的list数据
     * @param dtoList
     * @return
     */
    private List<Map<String, Object>> createWorkOrderExcelRecord(List<WorkOrderExcelDto> dtoList) {
        List ListData = new ArrayList();
        for (WorkOrderExcelDto dto : dtoList) {
            Map mapValue = new HashMap();
            mapValue.put("orderNo", dto.getOrderNo());
            mapValue.put("createTime", dto.getCreateTime());
            mapValue.put("carNumber", dto.getCarNumber());
            mapValue.put("mobile", dto.getMobile());
            mapValue.put("serviceItemNames", dto.getServiceItemNames());
            mapValue.put("totalPrice", dto.getTotalPrice());
            mapValue.put("materialCost", dto.getMaterialCost());
            mapValue.put("discountDeduct", dto.getDiscountDeduct());
            mapValue.put("businessDeduct", dto.getBusinessDeduct());
            mapValue.put("couponDeduct", dto.getCouponDeduct());
            mapValue.put("balanceDeduct", dto.getBalanceDeduct());
            mapValue.put("payPrice", dto.getPayPrice());
            mapValue.put("payType", dto.getPayType());
            mapValue.put("payTime", dto.getPayTime());
            mapValue.put("comment", dto.getComment());
            ListData.add(mapValue);
        }
        return ListData;
    }

    /**
     * 查询工单各个状态的数量
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/workOrder/selectWorkOrderStateCount", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderStateCount(@RequestBody Map param) throws SQLException {
        //获取状态值
        List<String> list = workOrderService.selectWorkOrderStateCount(param);
        Map<String, Object> dataMap = new HashMap<String, Object>();
        list.forEach(str -> {
            String[] strings = str.split(",");
            dataMap.put(strings[0], strings[1]);
        });
        return ReturnResult.buildSuccessMsg().setData(dataMap);
    }

    /**
     * 查询商家总的开单数和支付数
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = {"/home/workOrder/selectWorkOrderPaymentRate"}, method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderPaymentRate(@RequestBody Map paramMap) throws SQLException {
      return ReturnResult.buildSuccessMsg().setData(workOrderService.selectWorkOrderPaymentRate(paramMap));
    }

    /**
     *
     * 分页查询商家的开单数跟支付数
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = {"/home/workOrder/selectPagePaymentRateDto"}, method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPagePaymentRateDto(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(workOrderService.selectPagePaymentRateDto(paramMap));
    }

    /**
     * 下载商家数据的结果
     */
    @RequestMapping(value = "home/workOrder/selectDownloadBusinessData", method = RequestMethod.GET)
    public void selectUserInfoImportResult(HttpServletRequest request, HttpServletResponse response) throws IOException, ServiceException, SQLException {
        String createTimeStart = request.getParameter("createTimeStart");
        String createTimeEnd = request.getParameter("createTimeEnd");

        String[] caption = {"商家", "开单数据", "支付数", "支付率"};
        String[] detail = {"businessName", "workOrderNum", "wxPayNum", "orderRate"};
        Map paramMap = new HashMap();
        paramMap.put("orderBy","workOrderNum desc");
        paramMap.put("createTimeStart",ValidUtil.isEmpty(createTimeStart)?"":createTimeStart+" 00:00:00");
        paramMap.put("createTimeEnd",ValidUtil.isEmpty(createTimeEnd)?"":createTimeEnd+" 23:59:59");
        List<PaymentRateDto> paymentRateDtos = workOrderService.selectPaymentRateDtoList(paramMap);
        List listData = new ArrayList();
        for (PaymentRateDto paymentRateDto : paymentRateDtos) {
            Map mapValue = new HashMap();
            mapValue.put("businessName", paymentRateDto.getBusinessName());
            mapValue.put("workOrderNum", paymentRateDto.getWorkOrderNum());
            mapValue.put("wxPayNum", paymentRateDto.getWxPayNum());
            mapValue.put("orderRate", paymentRateDto.getOrderRate());
            listData.add(mapValue);
        }
        ExcelUtil.exportTemplate("商家数据", "商家数据", caption, listData, detail, response, request);
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
     * 车牌号识别-调用云脉api
     * @param paramMap
     * @return
     */
    @RequestMapping(value = {"/open/workOrder/selectScanPlateNumber"}, method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectScanPlateNumber(@RequestBody Map paramMap, HttpServletRequest request){

        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        String plateNumber = paramMap.get("carNumber").toString();

        //项目路径+CarNumberImg+fileName(图片名字是时间戳+随机数)
        String rootPath = "carNumberImg";
        String filePath = System.currentTimeMillis() + ((int) (Math.random() * 10)) + ".jpg";
        String imgFile =  request.getSession().getServletContext().getRealPath("")+File.separator+rootPath+File.separator +filePath;
        if(ImageUtil.generateImage(plateNumber, imgFile)){
            File file = new File(imgFile);
            try {
                String result = OCRUtils.scanPlate(OCRUtils.file2byte(file),imgFile.substring(imgFile.lastIndexOf(".")+1));
                JSONObject jsonObject = JSON.parseObject(result);
                String status = jsonObject.get("status").toString();
                if("ok".equals(status.toLowerCase())){
                    String reg = "[\\u4E00-\\u9FA5][A-Z][A-Za-z0-9]{5}";
                    String platenumber = jsonObject.getJSONObject("data").getJSONObject("item").get("platenumber").toString();
                    if(platenumber.matches(reg)){
                        returnResult.setData(result);
                    }else{
                        returnResult = ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_RECOGNITION_FAIL);
                    }
                }else{
                    returnResult = ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_RECOGNITION_FAIL);
                }
                file.delete();
            } catch (IOException e) {
                returnResult = returnResult.buildResult(e.getMessage());
                return  returnResult;
            }
        }else {
            returnResult = ReturnResult.buildEnumResult(Constant.StateCode.FILE_WRITING_FAILED);
        }
        return  returnResult;
    }


    /**
     * 获取消费记录列表（用户端）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/workOrder/selectWorkOrderPageForOpen", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderPageForOpen(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(workOrderService.selectWorkOrderPageForWap(param));
    }

    /**
     * 工单管理 根据状态分页查询
     */
    @RequestMapping(value = "/open/workOrder/selectPageByState", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageByState(@RequestBody Map paramMap) throws SQLException {
        List<String> stateArray = JSON.parseArray(paramMap.get("state").toString(), String.class);
        paramMap.put("stateList",stateArray);
        return ReturnResult.buildSuccessMsg().setData(workOrderService.selectPageByState(paramMap));
    }

    /**
     * 根据id查工单记录
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/workOrder/selectUserWorkOrderByWorkOrderId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectUserWorkOrderByWorkOrderId(@RequestBody Map paramMap) throws SQLException {

        return workOrderService.selectMoreInfoByParam(paramMap);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}