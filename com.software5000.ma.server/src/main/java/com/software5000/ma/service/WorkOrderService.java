package com.software5000.ma.service;

/**
 * Created by Jiang on 2017/07/19.
 */

import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageInfo;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.MyBaseDao;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.mybatis.plugins.PermissionHelper;
import com.software5000.base.security.UserDefaultUtil;
import com.software5000.ma.dto.*;
import com.software5000.ma.entity.*;
import com.software5000.util.PostUtil;
import com.software5000.util.WxMsgUtil;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WorkOrderService {

    private Log log = LogFactory.getLog(WorkOrderService.class);

    @Resource
    private MyBaseDao baseDao;

    @Resource
    private UserService userService;

    @Resource
    private WeChatPayOrderService weChatPayOrderService;

    @Resource
    private FinanceService financeService;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private BusinessService businessService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新建工单信息
     * @param workOrder
     * @return
     * @throws SQLException
     */
    public WorkOrder insertWorkOrderForCreate(WorkOrder workOrder) throws SQLException {

        //设置订单号
        workOrder.setOrderNo(DateUtils.unixTimestampToDate(System.currentTimeMillis(), DateUtils.DATE_KEY_STR) + workOrder.getBusinessId());
        //设置工单状态
        workOrder.setState(Constant.WorkOrderState.SERVICE_ING.codeName);

        //根据车架号查询用户
        User user = userService.inserUserAndMemberLvlByException(null, workOrder.getMobile(), workOrder.getCarNumber(),null);
        if (user != null) {
            workOrder.setUserId(user.getId());

            //更新会员名称
            if(workOrder.getUser() != null && !ValidUtil.isEmpty(workOrder.getUser().getRealName())) {
                User nameUser = new User();
                nameUser.setId(workOrder.getUserId());
                nameUser.setRealName(workOrder.getUser().getRealName());
                baseDao.updateEntity(nameUser);
            }
        }

        //新增工单信息
        WorkOrder order = (WorkOrder) baseDao.insertEntity(workOrder);
        List<WorkOrderDetail> workOrderDetails = workOrder.getWorkOrderDetails();
        List<WorkOrderDetail> orderDetails = workOrderDetails.stream().peek(detail -> {
            detail.setWorkOrderId(order.getId());
        }).collect(Collectors.toList());

        //新增工单项目
        order.setWorkOrderDetails(baseDao.insertEntities(orderDetails));

        return order;
    }

    /**
     * 新增施工图片
     * @param workOrderImg
     * @return
     * @throws SQLException
     */
    public WorkOrderImg insertWorkOrderImg(WorkOrderImg workOrderImg) throws SQLException {
        return (WorkOrderImg) baseDao.insertEntity(workOrderImg);
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 根据工单ID，删除工单
     * @param id
     * @throws SQLException
     */
    public void deleteWorkOrderById(Integer id) throws SQLException {
        //删除
        baseDao.deleteEntity(new WorkOrder().setBaseId(id));
    }

    /**
     * 根据工单项目ID，删除项目
     * @param detailId
     * @throws SQLException
     */
    public void deleteWorkOrderDetailById(Integer detailId) throws SQLException {

        //根据ID获取工单项目信息
        WorkOrderDetail detail = baseDao.selectEntityById(detailId, WorkOrderDetail.class);
        if(detail != null) {
            //获取工单信息，判断状态
            WorkOrder workOrder = baseDao.selectEntityById(detail.getWorkOrderId(), WorkOrder.class);
            if(Constant.WorkOrderState.SERVICE_ING.codeName.equals(workOrder.getState())) {
                //删除工单项目
                baseDao.deleteEntity(new WorkOrderDetail().setBaseId(detailId));

                List<WorkOrderDetail> list = selectWorkOrderDetailByOrderId(detail.getWorkOrderId());
                if(list == null || list.size() == 0) {
                    //删除
                    baseDao.deleteEntity(new WorkOrder().setBaseId(detail.getWorkOrderId()));
                }
            }
        }
    }

    /**
     * 根据ID删除施工图片
     * @param id
     * @throws SQLException
     */
    public void deleteWorkOrderImgById(Integer id) throws SQLException {
        baseDao.deleteEntity(new WorkOrderImg().setBaseId(id));
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 更新工单
     * @param workOrder
     * @return
     * @throws SQLException
     */
    public void updateWorkOrder(WorkOrder workOrder) throws SQLException {
        baseDao.updateEntity(workOrder);
    }

    /**
     * 更新工单
     * @param workOrder
     * @throws SQLException
     */
    public void updateWorkOrderForEdit(WorkOrder workOrder) throws SQLException {

        //需要更新的工单项目
        List<WorkOrderDetail> workOrderDetails = workOrder.getWorkOrderDetails();
        //新的工单项目
        List<WorkOrderDetail> newWorkOrderDetails = new ArrayList<WorkOrderDetail>();

        //原来的订单详情
        if (workOrderDetails != null && workOrderDetails.size() > 0) {
            //获取旧的工单项目
            List<WorkOrderDetail> originalWorkDetails = selectWorkOrderDetailByOrderId(workOrder.getId());

            //过滤出新增->新增到数据库中
            List<WorkOrderDetail> insertCollect = workOrderDetails.stream().filter(workOrderDetail -> !originalWorkDetails.contains(workOrderDetail)).collect(Collectors.toList());
            insertCollect.forEach(insert -> insert.setWorkOrderId(workOrder.getId()));
            List list = baseDao.insertEntities(insertCollect);
            if (list != null && list.size() > 0) {
                newWorkOrderDetails.addAll(list);
            }

            //过滤出删除->删除原有存在库了的数据
            List<WorkOrderDetail> deleteCollect = originalWorkDetails.stream().filter(originalWorkDetail -> !workOrderDetails.contains(originalWorkDetail)).collect(Collectors.toList());
//            baseDao.deleteEntitys(deleteCollect);

            //过滤出不变的->更新内容到数据库
            List<WorkOrderDetail> updateCollect = workOrderDetails.stream().filter(workOrderDetail -> originalWorkDetails.contains(workOrderDetail)).collect(Collectors.toList());
            newWorkOrderDetails.addAll(updateCollect);
            updateCollect.forEach(u -> {
                try {
                    baseDao.updateEntity(u, "discountPrice,discountNumber,couponUuid,couponName,couponDeduct,workerId,salerId", true);
                } catch (Exception e) {
                    return;
                }
            });
            workOrder.setWorkOrderDetails(newWorkOrderDetails);
        }

        //判断使用的卡券是否有更新
        baseDao.updateEntity(workOrder, "couponUuid", true);

        //更新会员名称
        if(workOrder.getUser() != null && !ValidUtil.isEmpty(workOrder.getUser().getRealName())) {
            User user = new User();
            user.setId(workOrder.getUserId());
            user.setRealName(workOrder.getUser().getRealName());
            baseDao.updateEntity(user);
        }
    }

    /**
     * 结算工单
     * @param orderId
     * @throws SQLException
     */
    public ReturnResult updateWorkOrderForSettle(Integer orderId, Integer payOrderId, String wxOpenId) throws SQLException, IOException {

        //查询工单信息
        WorkOrder workOrder = selectWorkOrderById(orderId);
        workOrder.setWorkOrderDetails(selectWorkOrderDetailByOrderId(orderId));

        if(workOrder != null && Constant.WorkOrderState.NO_PAY.codeName.equals(workOrder.getState())) {

            //优惠券处理，成功后，执行结算
            if(isWorkOrderUseCoupons(orderId)) {
                List<String> uuidList = workOrder.getWorkOrderDetails().stream().map(detail -> {return detail.getCouponUuid(); }).collect(Collectors.toList());
                List<String> nameList = workOrder.getWorkOrderDetails().stream().map(detail -> {return detail.getCouponName(); }).collect(Collectors.toList());;
                List<String> deductList = workOrder.getWorkOrderDetails().stream().map(detail -> {return String.valueOf(detail.getCouponDeduct()); }).collect(Collectors.toList());;

                //校验车牌是否可用
                ReturnResult valResult = valCouponsCanUse(workOrder.getWorkOrderDetails());
                if(valResult != null) {
                    return valResult;
                }

                for(int i = 0 ; i < uuidList.size() ; i++) {
                    String uuid = uuidList.get(i); //卡券uuid
                    String name = nameList.get(i); //卡券名称
                    String deduct = deductList.get(i); //每张卡券的扣减

                    //如果券空，则继续
                    if(ValidUtil.isEmpty(uuid)) {
                        continue;
                    }

                    Map couponParam = new HashMap();
                    couponParam.put("businessId",workOrder.getBusinessId());//商家id
                    couponParam.put("checkMoney",deduct);//金额
                    couponParam.put("takeUuid",uuid);//优惠券的uuid
                    couponParam.put("workOrderNo",workOrder.getOrderNo()); //工单单号
                    ReturnResult returnResult = PostUtil.postEMKT(Constant.Emkt.UPDATE_COUPONS_USED.codeName, couponParam);
                    if(!returnResult.getCode().equals(Constant.StateCode.SUCCESS.codeName)) {
                        return returnResult;
                    }

                    //获取EMKT卡券信息
                    WeChatPayOrder weChatPayOrder = JSONObject.parseObject(PostUtil.postEMKT(Constant.Emkt.SELECT_COUPONUSED_FOR_MA_CHECK.codeName, new HashMap<String, String>() {{
                        put("takeUuid", uuid);
                    }}).getData().toString(),WeChatPayOrder.class);
                    if(weChatPayOrder != null) {
                        weChatPayOrder.setOrderType(Constant.WeChatPayOrderType.COUPON_ORDER_TYPE.codeName);
                        weChatPayOrder.setStatus(Constant.OrderState.PAID.codeName.toString());
                        weChatPayOrder.setShowName(name + (weChatPayOrder.getCheckMoneyFee() == 0 ? "(核销已达上限)" : "") + "/"+workOrder.getCarNumber());
                        weChatPayOrder.setTradingTime(new Timestamp(new Date().getTime()));
                        BusinessCheckMoney businessCheckMoney = weChatPayOrderService.selectBusinessCheckMoney(workOrder.getBusinessId());
                        Integer canCheckMoney = businessCheckMoney.getCanCheckMoney()+weChatPayOrder.getCheckMoneyFee();
                        weChatPayOrder.setRealTimeMoneyFee(canCheckMoney);
                        weChatPayOrderService.insertWeChatPayOrder(weChatPayOrder);
                        businessCheckMoney.setCanCheckMoney(canCheckMoney);
                        weChatPayOrderService.updateBusinessCheckMoney(businessCheckMoney);
                        financeService.insertFinanceRecordByOrder(weChatPayOrder, null);
                        //将卡券设置为已核销
                        List<String> couponIds = new ArrayList<String>();
                        couponIds.add(weChatPayOrder.getTradeNo());
                        PostUtil.postEMKT(Constant.Emkt.UPDATE_MACHECKS_COUPON_USED.codeName,new HashMap(){{ put("ids", couponIds);}});
                    }
                }
            }
            userService.insertUser(wxOpenId, workOrder.getMobile(), workOrder.getCarNumber());

            //更新工单中的信息
            workOrder.setState(Constant.WorkOrderState.COMPLETE.codeName);
            workOrder.setPayTime(new Timestamp(System.currentTimeMillis()));
            baseDao.updateEntity(workOrder,null, false);

            //更新会员及套餐信息
            updateMemberInfoByWorkOrder(workOrder, true);

            //新增付款记录信息
            financeService.insertFinanceRecordByOrder(workOrder, payOrderId);

            //发送结账消息提醒
            workOrder.setBusinessName(businessService.selectBusinessById(workOrder.getBusinessId()).getBusinessName());
            workOrder.setUser(userService.selectUserById(workOrder.getUserId()));
            WxMsgUtil.sendMsgForSettle(workOrder.getUser().getWxOpenId(), workOrder);

            //发送使用套餐提醒
            List<WxMsgDto> wxMsgDtoList = memberPackageRecordService.selectWorkOrderPackageMsg(workOrder.getId());
            if(wxMsgDtoList != null && wxMsgDtoList.size() > 0) {
                wxMsgDtoList.forEach(dto -> WxMsgUtil.sendMsgForConsumePackage(workOrder.getUser().getWxOpenId(), dto));
            }

        }else {
            return ReturnResult.buildEnumResult(Constant.StateCode.WORK_ORDER_STATE_ERROR);
        }
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 判断工单项目是否使用卡券
     * @param workOrderId
     * @return
     */
    public boolean isWorkOrderUseCoupons(Integer workOrderId) throws SQLException {
        List<WorkOrderDetail> list = this.selectWorkOrderDetailByOrderId(workOrderId);
        if(list != null) {
            for (WorkOrderDetail detail : list) {
                if(!ValidUtil.isEmpty(detail.getCouponUuid())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 校验项目卡券是否可用
     * @param detailList
     * @return
     * @throws SQLException
     * @throws IOException
     */
    private ReturnResult valCouponsCanUse(List<WorkOrderDetail> detailList) throws SQLException,IOException {
        if(detailList != null) {
            for(WorkOrderDetail detail : detailList) {
                if(!ValidUtil.isEmpty(detail.getCouponUuid())) {
                    //校验卡券是否可用
                    ReturnResult returnResult = PostUtil.postEMKT(Constant.Emkt.SELECT_COUPONS_USED_BY_UUID.codeName, new HashMap<String, String>() {{
                        put("takeUuid", detail.getCouponUuid());
                    }});
                    if (!returnResult.getCode().equals(Constant.StateCode.SUCCESS.codeName) || !Boolean.valueOf(returnResult.getData().toString())) {
                        return ReturnResult.buildEnumResult(Constant.StateCode.COUPON_INVALID);
                    }
                }
            }
        }
        return null;
    }

    /**
     * 更新会员及套餐信息
     * @param workOrder
     * @param flag ture : 结算   false : 反结算
     */
    public void updateMemberInfoByWorkOrder(WorkOrder workOrder, boolean flag) throws SQLException {

        Integer userId = workOrder.getUserId();
        Integer businessId = workOrder.getBusinessId();
        if (userId != null) {
            //遍历出使用套餐卡的服务项，进行套餐扣减
            Map<Integer, Integer> usePackageServiceItems = new HashMap<>();
            workOrder.getWorkOrderDetails().forEach(detail -> {
                if(detail.getServiceItemId() != null && detail.getUsePackageTimes()!=null && detail.getUsePackageTimes()>0) {
                    usePackageServiceItems.put(detail.getServiceItemId(), usePackageServiceItems.get(detail.getServiceItemId()) == null ? detail.getUsePackageTimes() : (usePackageServiceItems.get(detail.getServiceItemId()) + detail.getUsePackageTimes()));
                }
            });
            if (usePackageServiceItems.size() > 0) {
                //套餐服务项扣减
                if(flag) {
                    memberPackageRecordService.updateMemberPackageRecordByUse(userId, usePackageServiceItems, workOrder.getId());
                }else {
                    memberPackageRecordService.updateMemberPackageRecordByCounter(workOrder.getId());
                }
            }

            //处理会员(是否满足商家会员条件，如果满足则升级会员否则不变)
            memberLvlRecordService.upgradeMemberLvlRecord(userId, businessId, flag ? workOrder.getTotalPrice() : -workOrder.getTotalPrice());

            //处理余额
            if(workOrder.getBalanceDeduct() != null) {
                memberLvlRecordService.updateMemberLvl(userId, businessId, flag ? -workOrder.getBalanceDeduct() : workOrder.getBalanceDeduct(), 0D, false);
            }
        }
    }

    /**
     * 反结算工单
     * @param orderId
     * @throws SQLException
     */
    public void updateWorkOrderForAntiSettle(Integer orderId) throws SQLException {
        //设置状态
        WorkOrder workOrder = new WorkOrder();
        workOrder.setId(orderId);
        workOrder.setState(Constant.WorkOrderState.NO_PAY.codeName);
        workOrder.setPayTime(null);
        baseDao.updateEntity(workOrder, "payTime", true);

        //查询工单信息
        WorkOrder order = selectWorkOrderById(orderId);
        order.setWorkOrderDetails(selectWorkOrderDetailByOrderId(orderId));

        //更新会员及套餐信息
        updateMemberInfoByWorkOrder(order, false);

        //删除付款记录信息
        order.setOpposite(true);
        financeService.insertFinanceRecordByOrder(order, null);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据工单ID，获取工单信息
     * @param orderId
     * @return
     * @throws SQLException
     */
    public WorkOrder selectWorkOrderById(Integer orderId) throws SQLException {
        return baseDao.selectEntityById(orderId, WorkOrder.class);
    }

    /**
     * 根据工单ID，查询工单项目信息
     * @param workOrderId
     * @return
     * @throws SQLException
     */
    public List<WorkOrderDetail> selectWorkOrderDetailByOrderId(Integer workOrderId) throws SQLException {
        WorkOrderDetail workOrderDetail = new WorkOrderDetail();
        workOrderDetail.setWorkOrderId(workOrderId);
        return baseDao.selectEntity(workOrderDetail);
    }

    /**
     * 根据工单ID获取详情与项目明细
     * @param orderId
     * @return
     * @throws SQLException
     */
    public WorkOrder selectWorkOrderAndDetailByOrderId(Integer orderId) throws SQLException {
        WorkOrder workOrder = selectWorkOrderById(orderId);
        if(workOrder != null) {
            workOrder.setWorkOrderDetails(selectWorkOrderDetailByOrderId(orderId));
        }
        return workOrder;
    }

    /**
     * 根据条件获取工单详细信息（包括会员套餐）
     * @param param
     * @return
     * @throws SQLException
     */
    public ReturnResult selectMoreInfoByParam(Map<String, Object> param) throws SQLException {

        Map<String, Object> dataMap = new HashMap<String, Object>();
        //获取工单详情信息
        PermissionHelper.ignorePermissionThisTime();
        WorkOrder workOrder = (WorkOrder)baseDao.selectObject(WorkOrder.Daos.selectMoreInfoByParam.sqlMapname, param);
        dataMap.put("workOrder", workOrder);
        //设置会员信息
        if(workOrder != null) {
            param.put("carNumber", workOrder.getCarNumber());
            //判断如果参数中包含管理员
            if(!ValidUtil.isEmpty(param.get("operatorId"))) {
                param.put("orderId", workOrder.getId());
                PermissionHelper.ignorePermissionThisTime();
                dataMap.put("serviceIdList", baseDao.selectList(WorkOrder.Daos.selectServiceItemIdForOperator.sqlMapname, param));
            }
        }
        dataMap.put("user", (BusinessCarDto)userService.selectUserByCarNumberForBusiness(param));
        return ReturnResult.buildSuccessMsg().setData(dataMap);
    }

    /**
     * 根据查询条件，获取工单列表
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo<WorkOrder> selectWorkOrderPage(Map<String, Object> param) throws SQLException {
        PageInfo<WorkOrder> pageInfo = baseDao.selectEntitiesByPage(WorkOrder.Daos.selectWorkOrderPage.sqlMapname
                ,param
                ,Integer.valueOf(param.getOrDefault("startPage",1).toString())
                ,Integer.valueOf(param.getOrDefault("pageSize",10).toString())
                ,null);

        if(pageInfo != null && pageInfo.getList() != null) {

            //判断关账日
            String closeDateStart = ""; //可反结算的开始时间
            Business business = businessService.selectBusinessById(UserDefaultUtil.getBusinessId());
            if(business.getClosingDateNum() != null) {
                String closingDateNumStr = business.getClosingDateNum() < 10 ? ("0" + business.getClosingDateNum()) : business.getClosingDateNum().toString();
                if(business.getClosingDateNum().compareTo(Integer.valueOf(DateUtils.formatDate(new Date(), "dd"))) >= 0) {
                    String preMonth = DateUtils.formatDate(DateUtils.preMonth(new Date()), "yyyy-MM");
                    closeDateStart = preMonth + "-" + closingDateNumStr + " 23:59:59";
                }else {
                    String nextMonth = DateUtils.formatDate(DateUtils.nextMonth(new Date()), "yyyy-MM");
                    closeDateStart = DateUtils.formatDate(new Date(), "yyyy-MM") + "-" + closingDateNumStr + " 23:59:59";
                }
            }

            //判断是否显示反结算按钮
            for(WorkOrder order : pageInfo.getList()) {
                if(closeDateStart.compareTo(DateUtils.formatDate(order.getPayTime(), "yyyy-MM-dd HH:mm:ss")) > 0) {
                    order.setOpposite(false);
                }else {
                    order.setOpposite(true);
                }
            }
        }
        return pageInfo;
    }

    /**
     * 导出工单列表
     * @param param
     * @return
     * @throws SQLException
     */
    public List<WorkOrderExcelDto> selectWorkOrderForExcel(Map<String, Object> param) throws SQLException {
        return  (List<WorkOrderExcelDto>)baseDao.selectList(WorkOrder.Daos.selectWorkOrderForExcel.sqlMapname, param);
    }

    /**
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public List<String> selectWorkOrderStateCount(Map<String, Object> param) throws SQLException {
       return (List<String>)baseDao.selectList(WorkOrder.Daos.selectWorkOrderStateCount.sqlMapname, param);
    }

    /**
     * 核查订单是否可以支付
     * @return
     */
    public ReturnResult selectCheckCanPay(Integer orderId) throws SQLException, IOException {
        WorkOrder workOrder = selectWorkOrderById(orderId);
        workOrder.setWorkOrderDetails(selectWorkOrderDetailByOrderId(orderId));

        //校验车牌是否可用
        ReturnResult valResult = valCouponsCanUse(workOrder.getWorkOrderDetails());
        if(valResult != null) {
            return valResult;
        }

        if(!Constant.WorkOrderState.NO_PAY.codeName.equals(workOrder.getState())){
            return ReturnResult.buildEnumResult(Constant.StateCode.ORDER_HAVE_FAIL_ERR);
        }
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 根据车牌号查询所有订单
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public List<WorkOrder> selectWorkOrderByCarNumber(Map paramMap) throws SQLException {
        WorkOrder workOrder=new WorkOrder();
        workOrder.setCarNumber(paramMap.get("carNumber").toString());
        return baseDao.selectEntity(workOrder);
    }

    /**
     * 统计工单数量
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public Integer selectCountWorkOrder(Map paramMap) throws SQLException {
       return (Integer) baseDao.selectObject(WorkOrder.Daos.selectCountWorkOrder.sqlMapname, paramMap);
    }

    /**
     * 查询商家的未结算工单数量
     */
    public Integer selectWorkOrderNoPaidCount(Map paramMap) throws SQLException{
        return (Integer) baseDao.selectObject(WorkOrder.Daos.selectWorkOrderNoPaidCount.sqlMapname, paramMap);
    }

    /**
     * 根据查询条件，获取工单列表
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo<WorkOrder> selectWorkOrderPageForWap(Map<String, Object> param) throws SQLException {

        //获取满足条件的工单信息
        PageInfo<WorkOrder> pageInfo = baseDao.selectEntitiesByPage(WorkOrder.Daos.selectWorkOrderPage.sqlMapname
                                                               ,param
                                                               ,Integer.valueOf(param.getOrDefault("startPage",1).toString())
                                                               ,Integer.valueOf(param.getOrDefault("pageSize",10).toString())
                                                               ,null);

        //获取更多信息
        if(pageInfo.getList() != null && pageInfo.getList().size() > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("orderIds", pageInfo.getList().stream().map(s -> s.getId().toString()).collect(Collectors.joining(",")));
            pageInfo.setList((List<WorkOrder>)baseDao.selectList(WorkOrder.Daos.selectMoreInfoByParam.sqlMapname, map));
        }
        return pageInfo;
    }

    /**
     * 查询商家总的开单数和支付数
     * @param param
     * @return
     * @throws SQLException
     */
    public PaymentRateDto selectWorkOrderPaymentRate(Map param) throws SQLException {
        List<HashMap> hashMaps = (List<HashMap>) baseDao.selectList(WorkOrder.Daos.selectWorkOrderPaymentRate.sqlMapname, param);
        PaymentRateDto paymentRateDto = new PaymentRateDto();
        Integer cash = 0;//现金收款
        Integer wechat = 0;//微信收款
        Integer debts = 0;//挂账
        for (HashMap hashMap : hashMaps) {
            String payType = hashMap.get("payType").toString();
            if("cash".equals(payType)){
                cash += Integer.valueOf(hashMap.get("count").toString());
            }else if("wechat".equals(payType)){
                wechat+=Integer.valueOf(hashMap.get("count").toString());
            }else if("debts".equals(payType)){
                debts+=Integer.valueOf(hashMap.get("count").toString());
            }
        }
        paymentRateDto.setWxPayNum(wechat);
        Integer woNum = cash + wechat + debts;
        paymentRateDto.setWorkOrderNum(woNum);
        return paymentRateDto;
    }


    /**
     * 分页查询商家的开单数跟支付数
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo selectPagePaymentRateDto(Map param) throws SQLException {
        String orderBy = param.getOrDefault("orderBy","workOrderNum desc").toString();
        Integer startPage = Integer.valueOf(param.getOrDefault("startPage", 1).toString());
        Integer pageSize = Integer.valueOf(param.getOrDefault("pageSize", 10).toString());
        return baseDao.selectEntitiesByPage(WorkOrder.Daos.selectPagePaymentRateDto.sqlMapname, param, startPage, pageSize,orderBy);
    }

    /**
     * 查询结算的车牌工单数
     * @param param
     * @return
     * @throws SQLException
     */
    public List<WorkOrder> selectNoCompleteWorkOrderByCarNumber(Map<String, Object> param) throws SQLException {
        param.put("stateList",Arrays.asList(Constant.WorkOrderState.SERVICE_ING.codeName, Constant.WorkOrderState.NO_PAY.codeName,Constant.WorkOrderState.WAITING.codeName));
        return (List<WorkOrder>)baseDao.selectList(WorkOrder.Daos.selectMoreInfoByParam.sqlMapname, param);
    }


    /**
     * 不分页查询商家的开单数跟支付数
     * @param param
     * @return
     * @throws SQLException
     */
    public List<PaymentRateDto> selectPaymentRateDtoList(Map param) throws SQLException {
        return (List<PaymentRateDto>) baseDao.selectList(WorkOrder.Daos.selectPagePaymentRateDto.sqlMapname, param);
    }

    /**
     * 根据id查询详情
     * @param id
     * @return
     * @throws SQLException
     */
    public UserWorkOrderDto selectWorkOrderDtoById(Integer id) throws SQLException {
        Map idMap = new HashMap();
        idMap.put("id", id);
        return (UserWorkOrderDto) baseDao.selectObject(WorkOrder.Daos.selectWorkOrderDtoById.sqlMapname, idMap);
    }


    public PageInfo<WorkOrderDto> selectPageByState(Map paramMap) throws SQLException {
        PageInfo pageInfo = null;
        String orderByStr = "updateTime desc";
        if (paramMap.get("orderBy")!= null) orderByStr = paramMap.get("orderBy").toString();
        pageInfo = baseDao.selectEntitiesByPage(WorkOrder.Daos.selectWorkOrderByParam.sqlMapname, paramMap,Integer.parseInt( paramMap.get("startPage").toString()), Integer.parseInt( paramMap.get("pageSize").toString()), orderByStr);
        if (pageInfo.getList().size() > 0) {
            Map workIdsMap = new HashMap();
            List<Integer> workOrderIds = new ArrayList<>();
            ((List<WorkOrder>) pageInfo.getList()).forEach(workOrder -> workOrderIds.add(workOrder.getId()));
            workIdsMap.put("workOrderIds", workOrderIds);
            workIdsMap.put("orderBy", orderByStr);
            pageInfo.setList(baseDao.selectList(WorkOrder.Daos.selectPageByWorkOrderIds.sqlMapname, workIdsMap));
        }
        return pageInfo;
    }

    /**
     * 查询用户最早产生记录的情况
     */
    public Map selectInitialByOpenId(Map param) throws ServiceException {
        try{
            return (Map)baseDao.selectObject(WorkOrder.Daos.selectInitialByOpenId.sqlMapname,param);
        }catch (Exception e) {
            log.error("查询失败，param="+param,e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}