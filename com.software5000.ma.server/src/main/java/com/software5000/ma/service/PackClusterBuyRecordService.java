package com.software5000.ma.service;

/**
 * Created by jiye on 2017/8/22.
 */

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.PageInfo;
import com.riversoft.weixin.mp.user.Users;
import com.riversoft.weixin.pay.payment.Payments;
import com.riversoft.weixin.pay.payment.bean.RefundRequest;
import com.riversoft.weixin.pay.payment.bean.RefundResponse;
import com.software5000.bank.dto.PayOrderDetailDto;
import com.software5000.bank.entity.PayOrder;
import com.software5000.bank.service.PayOrderService;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.ma.dto.PackClusterBuyRecordInfoDto;
import com.software5000.ma.dto.PackClusterPerDto;
import com.software5000.ma.entity.*;
import com.software5000.util.PayCommonUtil;
import com.software5000.util.WxMsgUtil;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.MathUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Service
public class PackClusterBuyRecordService {
    private Log log = LogFactory.getLog(PackClusterBuyRecordService.class);

    @Resource
    private BaseDao baseDao;

    @Resource
    private PackClusterService packClusterService;

    @Resource
    private PackClusterOpenRecordService packClusterOpenRecordService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private UserService userService;

    @Resource
    private FinanceService financeService;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    @Resource
    private WeChatPayOrderService weChatPayOrderService;


    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 支付购买拼团生成订单信息
     *
     * @param buyRecord
     * @return
     */
    public PayOrder insertPackClusterBuyRecordByPay(PackClusterBuyRecord buyRecord) throws SQLException {
        BusinessPackage businessPackage = packClusterService.selectPackClusterWithPackageById(buyRecord.getPackClusterId()).getBusinessPackage();
        //1.新增用户
        User user = userService.insertUserAndMemberLvl(buyRecord.getWxOpenId(), buyRecord.getMobile(), buyRecord.getCarNumber(),businessPackage.getBusinessId());;
        if(user == null){
            user = userService.selectUserByCarNo(buyRecord.getCarNumber());
        }
        //2.增加拼团购买记录
        buyRecord.setPayState(Constant.OrderState.TO_BE_PAID.codeName);
        if(user != null) {
            buyRecord.setUserId(user.getId());
        }
        PackClusterBuyRecord record = insertPackClusterBuyRecord(buyRecord);
        //3.增加payorder信息
        String orderNo = PayCommonUtil.getOutTradeNo(record.getId(), buyRecord.getPayMoney(), PayCommonUtil.TYPE_OF_PACK_CLUSTER, record.getOrderNo());
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderTitle("套餐支付");
        payOrder.setTotalPrice(buyRecord.getPayMoney());

        if (businessPackage.getValidTerm() == null || businessPackage.getValidTerm() == 0) {
            payOrder.setOrderService(businessPackage.getPackageName() + 1 + "份(有效期永久)");
        } else {
            payOrder.setOrderService(businessPackage.getPackageName() + 1 + "份(有效期" + businessPackage.getValidTerm() + "年)");
        }
        List<PackageAndItem> packageAndItems = businessPackage.getPackageAndItems();
        List<PayOrderDetailDto> dtos = new ArrayList<>();
        for (PackageAndItem packageAndItem : packageAndItems) {
            PayOrderDetailDto dto = new PayOrderDetailDto(packageAndItem.getServiceItem().getItemName(), packageAndItem.getUseTimes() + "次");
            dtos.add(dto);
        }
        payOrder.setOrderDetail(JSON.toJSONString(dtos));
        //是否有关注 么有的话跳到关注页，如果已经关注跳到参团详情页
        com.riversoft.weixin.mp.user.bean.User riverUser = Users.defaultUsers().get(buyRecord.getWxOpenId());
        String redirectUrl = Constant.maUrl;
        if(riverUser.isSubscribed()){//已经关注
            redirectUrl += String.format(Constant.getStringCodeValueByName(Constant.NormalSetting.PACK_CLUSTER_DETAIL_URL),buyRecord.getPackClusterId());
        }else{//未关注
            redirectUrl += Constant.getStringCodeValueByName(Constant.NormalSetting.PACK_CLUSTER_NOTE_URL);
        }
        payOrder.setRedirectUrl(redirectUrl);
        payOrder.setOrderNo(orderNo);
        payOrder.setBusinessId(businessPackage.getBusinessId());
        record.setOrderNo(orderNo);
        updatePackClusterBuyRecord(record);
        return payOrderService.insertPayOrderOrUpdateIfExit(payOrder);
    }


    public PackClusterBuyRecord insertPackClusterBuyRecord(PackClusterBuyRecord buyRecord) throws SQLException {
        return baseDao.insertEntity(buyRecord);
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 支付回调后调用更新拼团
     *
     * @param recordId
     */
    public void updatePackClusterBuyRecordByPay(Integer recordId) throws SQLException {
        PackClusterBuyRecord buyRecord = selectSinglePackClusterBuyRecordById(recordId);
        Integer openRecordId = buyRecord.getOpenRecordId();
        Integer packCulsterId = buyRecord.getPackClusterId();
        PackCluster packCluster = packClusterService.selectPackClusterWithPackageById(packCulsterId);

        boolean isNewOpen = false;//是否新开团
        if (openRecordId == null) {//表示自己开的团
            //增加开团记录
            PackClusterOpenRecord openRecord = new PackClusterOpenRecord();
            openRecord.setPackClusterId(packCulsterId);
            openRecord.setColonelOpenId(buyRecord.getWxOpenId());
            openRecord.setPackClusterNo(DateUtils.formatDate(new Date(), DateUtils.DATE_KEY_STR) + new Random().nextInt(10));
            Timestamp timestamp = new Timestamp(DateUtils.addHour(new Date(), packCluster.getClusterHour()).getTime());
            openRecord.setEndDay(packCluster.getEndDay().before(timestamp)?packCluster.getEndDay():timestamp);
            openRecord.setState(Constant.PackClusterOpenRecordState.ING.codeName);
            openRecord = packClusterOpenRecordService.insertPackClusterOpenRecord(openRecord);
            openRecordId = openRecord.getId();
            buyRecord.setOpenRecordId(openRecordId);
            isNewOpen = true;
        }

        //判断此订单付款完成的时候，该团是否已经成团了。(极限情况，刚好同时付款，目前考虑手动给用户付款，后续需要逻辑处理可以在这边实现)
        Integer count = selectCountHavePayOpenPackCulster(openRecordId);
        if(packCluster.getClusterNum()<= count){
            //1.退款
            RefundPackCluster refundPackCluster = refundPackClusterMoney(buyRecord,true);
            baseDao.insertEntity(refundPackCluster);
            //发送参团失败消息
            sendMsgForJoinGroupFail(recordId,buyRecord.getWxOpenId());
            return;
        }
        buyRecord.setPayTime(new Timestamp(new Date().getTime()));
        buyRecord.setPayState(Constant.OrderState.PAID.codeName);
        updatePackClusterBuyRecord(buyRecord);
        //1.判断是否已经成团
        count++;//数据库已经参团数+当前的这个参团
        if(packCluster.getClusterNum()<= count){
            //2.如果已经成团，a.加入商家套餐收入，b.加入返佣收入，c.发放套餐, d.更新为已成团, e.发送模版消息, f.加入微信提现
            List<PackClusterBuyRecord> packClusterBuyRecords = selectHavePayPackClusterBuyRecordByOpenClusterId(openRecordId);
            List<Finance> finances = new ArrayList<>();
            for (PackClusterBuyRecord packClusterBuyRecord : packClusterBuyRecords) {
                Finance f = new Finance();
                f.setBusinessId(packCluster.getBusinessId());
                int payMoney = MathUtil.mul(packCluster.getSalePrice(), 100).intValue();
                f.setUserPayMoney(payMoney);
                f.setMoney(payMoney);
                f.setFinanceType(Constant.FinanceType.PACKAGE_ORDER_TYPE.codeName);
                f.setExplainInfo(packCluster.getBusinessPackage().getPackageName()+"/"+packClusterBuyRecord.getMobile());
                f.setOrderId(packClusterBuyRecord.getId());
                f.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
                f.setUserId(packClusterBuyRecord.getUserId());
                f.setOrderNo(packClusterBuyRecord.getOrderNo());
                f.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
                PayOrder payOrder = payOrderService.selectSinglePayOrderByOrderNo(packClusterBuyRecord.getOrderNo());
                f.setPayOrderId(payOrder==null?null:payOrder.getId());
                finances.add(f);
            }
            //加入返佣
            Double subsidyPrice =  packCluster.getSubsidyPrice();
            if(subsidyPrice !=null && MathUtil.sub(subsidyPrice,0)>0){
                PackClusterOpenRecord packClusterOpenRecord = packClusterOpenRecordService.selectPackClusterOpenRecordById(openRecordId);
                Finance f = new Finance();
                f.setFinanceType(Constant.FinanceType.MMGOODCAR_ORDER_TYPE.codeName);
                f.setBusinessId(packCluster.getBusinessId());
                int payMoney = MathUtil.mul(subsidyPrice, 100).intValue();
                f.setUserPayMoney(payMoney);
                f.setMoney(payMoney);
                f.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
                f.setExplainInfo("拼团活动/"+packCluster.getBusinessPackage().getPackageName());
                f.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
                f.setOrderId(packClusterOpenRecord.getId());
                f.setOrderNo(packClusterOpenRecord.getPackClusterNo());
                finances.add(f);
            }
            //加入微信提现
            BusinessCheckMoney businessCheckMoney = weChatPayOrderService.selectBusinessCheckMoney(packCluster.getBusinessId());
            int inMoney = 0;
            for (Finance finance : finances) {
                inMoney += finance.getMoney();
            }
            inMoney += businessCheckMoney.getCanCheckMoney();
            businessCheckMoney.setCanCheckMoney(inMoney);
            weChatPayOrderService.updateBusinessCheckMoney(businessCheckMoney);

            financeService.insertFinanceRecordList(finances);
            //发放套餐
//            memberPackageRecordService.insertMemberPackageRecordByPackCluster(packClusterBuyRecords,packCluster.getPackId());
            //更新成已成团
            PackClusterOpenRecord openRecord = new PackClusterOpenRecord();
            openRecord.setId(openRecordId);
            openRecord.setState(Constant.PackClusterOpenRecordState.SUCCESS.codeName);
            packClusterOpenRecordService.updatePackClusterOpenRecord(openRecord);
            //发送拼团成功模版消息
            sendMsgForSpellGroupSuccess(openRecordId);
        }else{
            if(!isNewOpen) {
                //参团成功，发送消息提醒
                sendMsgForJoinGroup(openRecordId,buyRecord.getId(),buyRecord.getWxOpenId());
            }else{
                //开团成功，发送消息提醒
                sendMsgForOpenGroup(buyRecord.getId(),buyRecord.getWxOpenId());
            }
        }
    }


    public void updatePackClusterBuyRecord(PackClusterBuyRecord packClusterBuyRecord) throws SQLException {
        baseDao.updateEntityNotEmpty(packClusterBuyRecord);
    }

    /**
     * 参团失败进行退款，发送模版消息
     */
    public void updateRefundPackClusterBuyRecord() throws SQLException {
        //查询出所有
        List<PackClusterOpenRecord> packClusterOpenRecords = packClusterOpenRecordService.selectTimeOutPackCluster();
        List<RefundPackCluster> refundPackClusters = new ArrayList<>();
        for (PackClusterOpenRecord packClusterOpenRecord : packClusterOpenRecords) {
            packClusterOpenRecord.setState(Constant.PackClusterOpenRecordState.FAIL.codeName);
            packClusterOpenRecordService.updatePackClusterOpenRecord(packClusterOpenRecord);
            List<PackClusterBuyRecord> packClusterBuyRecords = packClusterOpenRecord.getPackClusterBuyRecords();
            for (PackClusterBuyRecord packClusterBuyRecord : packClusterBuyRecords) {
                refundPackClusters.add(refundPackClusterMoney(packClusterBuyRecord,null));
            }
            //参团失败，发送消息提醒
            sendMsgForSpellGroupFail(packClusterBuyRecords);
        }
        baseDao.insertEntityList(refundPackClusters);

    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 单纯查询参团记录信息不附带其他对象信息
     *
     * @param packClusterBuyRecord
     * @return
     */
    public PackClusterBuyRecord selectSinglePackClusterBuyRecord(PackClusterBuyRecord packClusterBuyRecord) {
        return baseDao.selectSingleEntity(packClusterBuyRecord);
    }

    /**
     * 是否已经参团
     *
     * @param map
     * @return
     * @throws SQLException
     */
    public ReturnResult selectHaveBuyPackCluster(Map map) throws SQLException {
        Integer packCulsterId = (Integer) map.get("packClusterId");//活动id
        PackClusterBuyRecord packClusterBuyRecord = new PackClusterBuyRecord();
        packClusterBuyRecord.setPackClusterId(packCulsterId);
        packClusterBuyRecord.setPayState(Constant.OrderState.PAID.codeName);
        String wxOpenId = map.getOrDefault("wxOpenId","").toString();
        if (ValidUtil.isNotEmpty(wxOpenId)) {
            packClusterBuyRecord.setWxOpenId(wxOpenId);
            PackClusterBuyRecord pbr = selectSinglePackClusterBuyRecord(packClusterBuyRecord);
            if (pbr != null) {
                return ReturnResult.buildEnumResult(Constant.StateCode.WX_HAVE_OPEN_CLUSTER).setData(pbr.getId());
            }
        }
        packClusterBuyRecord.setWxOpenId(null);
        String carNumber = map.getOrDefault("carNumber","").toString();
        if (ValidUtil.isNotEmpty(carNumber)) {
            packClusterBuyRecord.setCarNumber(carNumber);
            PackClusterBuyRecord pbr =  selectSinglePackClusterBuyRecord(packClusterBuyRecord);
            if (pbr != null) {
                return ReturnResult.buildEnumResult(Constant.StateCode.CAR_HAVE_OPEN_CLUSTER).setData(pbr.getId());
            }
        }
        packClusterBuyRecord.setCarNumber(null);
        String mobile = map.getOrDefault("mobile","").toString();
        if (ValidUtil.isNotEmpty(mobile)) {
            packClusterBuyRecord.setMobile(mobile);
            PackClusterBuyRecord pbr =  selectSinglePackClusterBuyRecord(packClusterBuyRecord);
            if (pbr != null) {
                return ReturnResult.buildEnumResult(Constant.StateCode.MOBILE_HAVE_OPEN_CLUSTER).setData(pbr.getId());
            }
        }
        return ReturnResult.buildSuccessMsg();
    }

    public PackClusterBuyRecord selectSinglePackClusterBuyRecordById(Integer recordId) {
        PackClusterBuyRecord param = new PackClusterBuyRecord();
        param.setId(recordId);
        return selectSinglePackClusterBuyRecord(param);
    }

    /**
     * 验证是否可以支付
     * @param orderId
     * @return
     */
    public ReturnResult selectCheckCanPay(Integer orderId) throws SQLException {
        Map map = new HashMap();
        map.put("packClusterBuyRecordId", orderId);
        PackClusterBuyRecord packClusterBuyRecord = null;
        try {
            packClusterBuyRecord = selectPackClusterBuyRecordDetail(map);
        } catch (SQLException e) {
            log.error("PackClusterBuyRecordService.selectCheckCanPay", e);
        }
        if(packClusterBuyRecord ==null ){
            return ReturnResult.buildEnumResult(Constant.StateCode.BUY_PACKAGE_FAILED);
        }else{
            if(!Constant.OrderState.TO_BE_PAID.codeName.equals(packClusterBuyRecord.getPayState())){
                return ReturnResult.buildEnumResult(Constant.StateCode.ORDER_HAS_BEEN_PAID);
            }
            //判断团是否已经满了
            PackCluster packCluster = packClusterBuyRecord.getPackCluster();
            Integer openRecordId = packClusterBuyRecord.getOpenRecordId();
            if(openRecordId != null) {
                Integer count = selectCountHavePayOpenPackCulster(openRecordId);
                if (packCluster != null && packCluster.getClusterNum()<= count){
                    return ReturnResult.buildEnumResult(Constant.StateCode.CLUSTER_HAVE_SUCCESS_EQ);
                }
            }
        }
        return ReturnResult.buildSuccessMsg();
    }


    /**
     * 参团人数
     * @param openPackClusterId
     * @return
     * @throws SQLException
     */
    public Integer selectCountHavePayOpenPackCulster(Integer openPackClusterId) throws SQLException {
        return (Integer)baseDao.selectObject(PackClusterBuyRecord.Daos.selectCountHavePayPackCluster.sqlMapname,openPackClusterId);
    }

    /**
     * 根据团id查询已经支付的参团信息
     * @param clusterId
     * @return
     */
    public List<PackClusterBuyRecord> selectHavePayPackClusterBuyRecordByOpenClusterId(Integer clusterId) throws SQLException {
        PackClusterBuyRecord param = new PackClusterBuyRecord();
        param.setPayState(Constant.OrderState.PAID.codeName);
        param.setOpenRecordId(clusterId);
        return baseDao.selectEntity(param);
    }

    /**
     * 根据购买拼团的id查询活动信息
     * @param buyRecordId
     * @return
     * @throws SQLException
     */
    public PackClusterBuyRecord selectPackClusterPeron(Integer buyRecordId) throws SQLException {
        PackClusterBuyRecord packClusterBuyRecord = selectLatelyPackClusterBuyRecord(new HashMap(){{put("id",buyRecordId);}}).get(0);
        Map param = new HashMap();
        param.put("id", packClusterBuyRecord.getPackClusterId());
        packClusterBuyRecord.setPackCluster(packClusterService.selectPackClusterInfoById(param));
        param.put("openRecordId",packClusterBuyRecord.getOpenRecordId());
        List<PackClusterPerDto> packClusterPerDtos = (List<PackClusterPerDto>) baseDao.selectList(PackClusterBuyRecord.Daos.selectPackClusterPerDto.sqlMapname, param);
        for (PackClusterPerDto packClusterPerDto : packClusterPerDtos) {
            packClusterPerDto.setWxHeadImg(Users.defaultUsers().get(packClusterPerDto.getWxOpenId()).getHeadImgUrl());
        }
        packClusterBuyRecord.setPackClusterPerDtos(packClusterPerDtos);

        return packClusterBuyRecord;
    }


    /**
     * 分页展示用户参与的所有拼团
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo selectPagePackClusterBuyRecordByOpenId(Map param) throws SQLException {
        return baseDao.selectListByPage(PackClusterBuyRecord.Daos.selectPagePackClusterBuyRecord.sqlMapname,
                param,
                (Integer) param.getOrDefault("startPage", 1),
                (Integer) param.getOrDefault("pageSize", 10),
                "payTime desc");
    }

    /**
     * 分页展示所有拼团
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo selectPagePackClusterBuyRecordByPage(Map param) throws SQLException {
        return baseDao.selectListByPage(PackClusterBuyRecord.Daos.selectPagePackClusterBuyRecordByPage.sqlMapname,
                param,
                (Integer) param.getOrDefault("startPage", 1),
                (Integer) param.getOrDefault("pageSize", 10),
                "payTime desc");
    }

    /**
     * 查询用户拼团详情
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public PackClusterBuyRecord selectPackClusterBuyRecordDetail(Map param) throws SQLException {
        PackClusterBuyRecord packClusterBuyRecord = (PackClusterBuyRecord) baseDao.selectObject(PackClusterBuyRecord.Daos.selectPackClusterBuyRecordDetail.sqlMapname, param);
        PackClusterBuyRecord buyRecord = new PackClusterBuyRecord();
        buyRecord.setOpenRecordId(packClusterBuyRecord.getOpenRecordId());
        if (ValidUtil.isEmpty(param.get("payState"))) {
            buyRecord.setPayState(Constant.OrderState.PAID.codeName);
        } else {
            buyRecord.setPayState(Constant.OrderState.HAVE_REFUND.codeName);
        }
        List<PackClusterBuyRecord> list = baseDao.selectEntity(buyRecord);
        packClusterBuyRecord.getPackCluster().setSuccessNum(list.size());
        return packClusterBuyRecord;
    }

    /**
     * 根据openRecordId查询拼团成功的记录
     * @param param
     * @return
     * @throws SQLException
     */
    public List<PackClusterBuyRecord>selectPackClusterBuyRecordList(Map param) throws SQLException {
        List<?> objects = baseDao.selectList(PackClusterBuyRecord.Daos.selectPackClusterBuyRecordDetail.sqlMapname, param);
        return (List<PackClusterBuyRecord>)objects;
    }

    /**
     * 查询最新一次参团信息
     * @return
     */
    public List<PackClusterBuyRecord> selectLatelyPackClusterBuyRecord(Map map) throws SQLException {
        return (List<PackClusterBuyRecord>) baseDao.selectList(PackClusterBuyRecord.Daos.selectLatelyPackClusterBuyRecord.sqlMapname, map);
    }

    /**
     * 统计用户不同拼团状态下记录的数量
     * @param openId
     * @return
     * @throws SQLException
     */
    public List<PackClusterBuyRecordInfoDto> selectPackClusterBuyRecordCountForState(String openId) throws SQLException {
        return (List<PackClusterBuyRecordInfoDto>)baseDao.selectList(PackClusterBuyRecord.Daos.selectPackClusterBuyRecordCountForState.sqlMapname,openId);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>

    /***
     * 开团成功发送消息
     */
    public void sendMsgForOpenGroup(Integer packClusterBuyRecordId,String openId){
        Map param=new HashMap();
        param.put("packClusterBuyRecordId",packClusterBuyRecordId);
        param.put("openId",openId);
        try {
            WxMsgUtil.sendMsgForOpenGroup(openId, selectPackClusterBuyRecordDetail(param));
        } catch (SQLException e) {
            log.error("发送开团成功消息失败",e);
        }
    }

    /***
     * 参团失败发送消息
     */
    public void sendMsgForJoinGroupFail(Integer packClusterBuyRecordId,String openId){
        Map param=new HashMap();
        param.put("packClusterBuyRecordId",packClusterBuyRecordId);
        param.put("openId",openId);
        try {
            WxMsgUtil.sendMsgForJoinGroupFail(openId, selectPackClusterBuyRecordDetail(param));
        } catch (SQLException e) {
            log.error("发送参团失败消息失败",e);
        }
    }

    /**
     * 参团成功发送消息
     * @param openRecordId
     * @param buyRecordId
     * @param wxOpenId
     */
    public void sendMsgForJoinGroup(Integer openRecordId,Integer buyRecordId,String wxOpenId){
        try {
        //参团成功，发送消息提醒
        Map param=new HashMap();
        param.put("packClusterBuyRecordId", buyRecordId);
        param.put("openId", wxOpenId);
        PackClusterBuyRecord packClusterBuyRecord=selectPackClusterBuyRecordDetail(param);
        WxMsgUtil.sendMsgForJoinGroup(wxOpenId, packClusterBuyRecord);
        } catch (SQLException e) {
            log.error("发送参团成功消息失败",e);
        }
    }

    /**
     * 发送拼团成功模版消息
     * @param openRecordId
     */
    public void sendMsgForSpellGroupSuccess(Integer openRecordId){
        try {
        Map param=new HashMap();
        param.put("openRecordId",openRecordId);
        param.put("payState",Constant.OrderState.PAID.codeName);//拼团购买记录为已支付的
        List<PackClusterBuyRecord> list=  this.selectPackClusterBuyRecordList(param);
        for (PackClusterBuyRecord packClusterBuyRecord:list){
            WxMsgUtil.sendMsgForSpellGroupSuccess(packClusterBuyRecord.getWxOpenId(), packClusterBuyRecord);
        }
        } catch (SQLException e) {
            log.error("发送拼团成功模版消息失败",e);
        }
    }

    /**
     * 发送拼团失败模版消息
     *
     * @param packClusterBuyRecords
     */
    public void sendMsgForSpellGroupFail(List<PackClusterBuyRecord> packClusterBuyRecords) {
        Map param = new HashMap();
        for (PackClusterBuyRecord packClusterBuyRecord : packClusterBuyRecords) {
            param.put("packClusterBuyRecordId", packClusterBuyRecord.getId());
            param.put("openId", packClusterBuyRecord.getWxOpenId());
            param.put("payState",Constant.OrderState.HAVE_REFUND.codeName);
            try {
                WxMsgUtil.sendMsgForSpellGroupFail(packClusterBuyRecord.getWxOpenId(), this.selectPackClusterBuyRecordDetail(param));
            } catch (SQLException e) {
                log.error("发送拼团失败模版消息失败", e);
            }
        }
    }

    /**
     * 拼团退款操作
     * @param packClusterBuyRecord
     * @param noNormalOpr 不正常操作进行的退款
     * @return
     * @throws SQLException
     */
    private  RefundPackCluster refundPackClusterMoney(PackClusterBuyRecord packClusterBuyRecord,Boolean noNormalOpr) throws SQLException {
        RefundRequest refundRequest = new RefundRequest();
        refundRequest.setRefundFee(packClusterBuyRecord.getPayMoney());
        refundRequest.setTradeNumber(packClusterBuyRecord.getOrderNo());
        refundRequest.setRefundNumber(packClusterBuyRecord.getOrderNo()+"_t");
        refundRequest.setTotalFee(packClusterBuyRecord.getPayMoney());
        RefundResponse refund = Payments.defaultOrders().refund(refundRequest);
        RefundPackCluster refundPackCluster = new RefundPackCluster();
        refundPackCluster.setOrderNo(refund.getTradeNumber());
        refundPackCluster.setRefundFee(refund.getRefundFee());
        refundPackCluster.setRefundId(refund.getRefundId());
        refundPackCluster.setRefundOrderNo(refund.getRefundNumber());
        refundPackCluster.setSettlementTotalFee(refund.getSettlementTotalFee());
        refundPackCluster.setTransactionId(refund.getTransactionId());
        refundPackCluster.setTotalFee(refund.getTotalFee());
        //购买记录更新为已退款
        packClusterBuyRecord.setPayState(Constant.OrderState.HAVE_REFUND.codeName);
        //正数变负数用于记录id
        if(noNormalOpr!=null && noNormalOpr) {
            packClusterBuyRecord.setOpenRecordId(0 - packClusterBuyRecord.getOpenRecordId());
        }
        updatePackClusterBuyRecord(packClusterBuyRecord);
        return refundPackCluster;
    }

}