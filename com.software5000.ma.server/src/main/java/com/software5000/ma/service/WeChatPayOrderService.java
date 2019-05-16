package com.software5000.ma.service;

import com.github.pagehelper.PageInfo;
import com.riversoft.weixin.common.util.RandomStringGenerator;
import com.riversoft.weixin.mp.user.Users;
import com.riversoft.weixin.mp.user.bean.User;
import com.riversoft.weixin.pay.transfer.Transfers;
import com.riversoft.weixin.pay.transfer.bean.TransferRequest;
import com.riversoft.weixin.pay.transfer.bean.TransferResponse;
import com.software5000.base.MyBaseDao;
import com.software5000.ma.dto.CheckMoneyDto;
import com.software5000.ma.dto.FinanceInOrOutComeDto;
import com.software5000.ma.dto.PaymentDto;
import com.software5000.ma.entity.Business;
import com.software5000.ma.entity.BusinessCheckMoney;
import com.software5000.ma.entity.WeChatPayOrder;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 微信支付订单服务类
 * Created by lsj on 2017/1/10.
 */
@Service
public class WeChatPayOrderService {

    protected Log log = LogFactory.getLog(this.getClass());

    @Resource
    private MyBaseDao baseDao;

    @Resource
    private BusinessService businessService;

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private FinanceService financeService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 添加微信订单
     * @param weChatPayOrder
     * @return
     * @throws ServiceException
     */
    public WeChatPayOrder insertWeChatPayOrder(WeChatPayOrder weChatPayOrder) throws SQLException {
        return (WeChatPayOrder) baseDao.insertEntity(weChatPayOrder);
    }

    /**
     * 商家提现
     * @param payTotalFee
     * @param request
     * @return
     * @throws SQLException
     */
    public ReturnResult insertDrawMoney(Integer payTotalFee,Integer businessId,HttpServletRequest request) throws SQLException {
        BusinessCheckMoney businessCheckMoney = selectBusinessCheckMoney(businessId);
        if(businessCheckMoney.getCanCheckMoney()-payTotalFee<0) {//要提现的金额比累计可以提现的金额少
            return ReturnResult.buildEnumResult(Constant.StateCode.CHECK_MONEY_NO_CORRECT);
        }
        if (payTotalFee < 1 * 100) { //微信规则最小金额应该>=1元
            return ReturnResult.buildEnumResult(Constant.StateCode.SETTLE_MONEY_TOO_LITTLE_ERROR);
        }
        if (payTotalFee > 20000 * 100) { //微信规则最大金额应该<=2W元
            return ReturnResult.buildEnumResult(Constant.StateCode.SETTLE_MONEY_TOO_MUCH_ERROR);
        }
        Business business = businessService.selectBusinessById(businessId);
        if (ValidUtil.isEmpty(business.getWxOpenId())) {//商家的微信openid必须存在
            return ReturnResult.buildEnumResult(Constant.StateCode.NO_BIND_WEIXIN_ERROR);
        }
        TransferRequest transferRequest = new TransferRequest();
        transferRequest.setPartnerTradeNo(RandomStringGenerator.getRandomStringByLength(16));
        transferRequest.setOpenId(business.getWxOpenId());
        transferRequest.setCheckName("NO_CHECK");
        transferRequest.setAmount(payTotalFee);
        transferRequest.setDesc("商家核销订单");
        transferRequest.setClientIp(request.getRemoteAddr());
        TransferResponse transfer = Transfers.defaultTransfers().transfer(transferRequest);
        if(transfer.getReturnCode().equals("SUCCESS")&&transfer.getResultCode().equals("SUCCESS")){
            BusinessCheckMoney bck = selectBusinessCheckMoney(businessId);
            //保存核销订单
            WeChatPayOrder wco = new WeChatPayOrder();
            wco.setBusinessId(business.getId());
            wco.setOrderType(Constant.WeChatPayOrderType.SETTLEMENT_ORDER_TYPE.codeName);
            wco.setTradeFee(payTotalFee);
            wco.setCheckMoneyFee(payTotalFee);
            wco.setTradeNo(transfer.getPartnerTradeNo());
            wco.setStatus(Constant.OrderState.PAID.codeName.toString());
            wco.setRealTimeMoneyFee(bck.getCanCheckMoney()-payTotalFee);
            wco.setPrepayId(transfer.getPaymentNo());
            wco.setShowName("商家核销订单");
            wco.setTradingTime(new Timestamp(System.currentTimeMillis()));
            insertWeChatPayOrder(wco);
            //更新提现信息
            bck.setCanCheckMoney(bck.getCanCheckMoney()-payTotalFee);
            bck.setHaveCheckMoney(bck.getHaveCheckMoney()+payTotalFee);
            updateBusinessCheckMoney(bck);
        }else{
            log.error(transfer.getReturnMessage()+","+ transfer.getErrorCode() + "，" + transfer.getErrorCodeDesc());
            return ReturnResult.buildEnumResult(Constant.StateCode.SETTLE_ERROR);
        }
        return ReturnResult.buildSuccessMsg();
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
     * 跟新微信订单
     */
    public void updateWeChatPayOrder(WeChatPayOrder weChatPayOrder) throws SQLException {
        baseDao.updateEntity(weChatPayOrder);
    }


    /**
     * 更新可提现金额及已提现金额
     * @param checkMoney
     */
    public synchronized  void updateBusinessCheckMoney(BusinessCheckMoney checkMoney) throws SQLException {
        baseDao.updateEntity(checkMoney);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 查询微信订单
     */
    public WeChatPayOrder selectWeChatPayOrderByWeChatOrderInfo(WeChatPayOrder weChatPayOrder){
        return baseDao.selectSingleEntity(weChatPayOrder);

    }

    /**
     * 查询商家财务对账的基本信息,如果不存在则新增
     * @return
     * @throws SQLException
     */
    public BusinessCheckMoney selectBusinessCheckMoney(Integer businessId) throws SQLException {
        BusinessCheckMoney bcm = null;
        Object o = baseDao.selectObject(WeChatPayOrder.Daos.selectBusinessCheckMoney.sqlMapname,businessId);
        if(o==null){
            BusinessCheckMoney businessCheckMoney = new BusinessCheckMoney();
            businessCheckMoney.setBusinessId(businessId);
            businessCheckMoney.setCanCheckMoney(0);
            businessCheckMoney.setHaveCheckMoney(0);
            bcm = (BusinessCheckMoney) baseDao.insertEntity(businessCheckMoney);
        }else{
            bcm = (BusinessCheckMoney)o;
        }
        return  bcm;
    }

    /**
     * 分页查询微信订单明细
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public PageInfo selectPageWechatPayOrder(Map paramMap) throws SQLException {
        String orderBy = paramMap.getOrDefault("orderBy", "id desc").toString();
        if(ValidUtil.isEmpty(orderBy)) orderBy = "id desc";
        Integer startPage = (Integer)paramMap.getOrDefault("startPage", 1);
        Integer pageSize = (Integer)paramMap.getOrDefault("pageSize", 10);
        return baseDao.selectEntitiesByPage(WeChatPayOrder.Daos.selectPageWechatPayOrder.sqlMapname,paramMap,startPage, pageSize, orderBy);
    }

    /**
     * 微信提现信息及开单数
     * @return
     */
    public CheckMoneyDto selectCheckMoneyDto(Integer businessId) throws SQLException {
        CheckMoneyDto checkMoneyDto = new CheckMoneyDto();
        checkMoneyDto.setMaxCheckMoney(Constant.getIntegerCodeValueByName(Constant.CheckMoneyOrder.MAX_MONEY) * 100);
        checkMoneyDto.setMinCheckMoney(Constant.getIntegerCodeValueByName(Constant.CheckMoneyOrder.MIN_MONEY) * 100);
        Business business = businessService.selectBusinessById(businessId);
        if(business!=null && ValidUtil.isNotEmpty(business.getWxOpenId())){
            User user = Users.defaultUsers().get(business.getWxOpenId());
            if(user!=null) {
                checkMoneyDto.setNickName(user.getNickName());
            }
        }
        //是否已经提现
        Map paramMap = new HashMap();
        String startTime = DateUtils.formatDate(new Date(),DateUtils.DATE_SMALL_STR)+" 00:00:00";
        String endTime = DateUtils.formatDate(new Date(),DateUtils.DATE_SMALL_STR)+" 23:59:59";
        paramMap.put("startTime", startTime);
        paramMap.put("endTime",endTime);
        paramMap.put("orderType", Constant.WeChatPayOrderType.SETTLEMENT_ORDER_TYPE.codeName);
        List<WeChatPayOrder> weChatPayOrders = selectWechatPayOrder(paramMap);
        checkMoneyDto.setHaveCheck(weChatPayOrders!=null&&weChatPayOrders.size()>0);
        //开单数量
        checkMoneyDto.setCountTodayWorkOrder(workOrderService.selectCountWorkOrder(paramMap));
        //今日营业额
        paramMap.put("createTimeStart", startTime);
        paramMap.put("createTimeEnd",endTime);
        FinanceInOrOutComeDto financeInOrOutComeDto = financeService.selectFinanceInOrOutComeDto(paramMap);
        checkMoneyDto.setCountTodayTotalPrice(financeInOrOutComeDto.getInComeMoney());
        return checkMoneyDto;
    }

    /**
     * 分页查询微信订单明细
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public List<WeChatPayOrder> selectWechatPayOrder(Map paramMap) throws SQLException {
        return (List<WeChatPayOrder>) baseDao.selectList(WeChatPayOrder.Daos.selectPageWechatPayOrder.sqlMapname,paramMap);
    }

    public List selecPaymentBusiness() throws SQLException {
        return baseDao.selectList(WeChatPayOrder.Daos.selecPaymentBusiness.sqlMapname);
    }

    /**
     * 分页查询收支信息
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public PageInfo<PaymentDto> selectPaymentPage(Map paramMap) throws SQLException {
        Integer startPage = (Integer)paramMap.getOrDefault("startPage", 1);
        Integer pageSize = (Integer)paramMap.getOrDefault("pageSize", 10);
        String orderBy = paramMap.getOrDefault("orderBy", "b.id asc").toString();

        return baseDao.selectEntitiesByPage(WeChatPayOrder.Daos.selectPaymentPage.sqlMapname,paramMap, startPage, pageSize, orderBy);
    }

    /**
     * 统计所有收支
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public PaymentDto selectTotalSum(Map paramMap) throws SQLException {
        return (PaymentDto)baseDao.selectObject(WeChatPayOrder.Daos.selectTotalSum.sqlMapname,paramMap);
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
}
