package com.software5000.ma.service;

/**
 * Created by jiye on 2017/7/24.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.entity.BaseEntity;
import com.software5000.ma.dto.FinanceInOrOutComeDto;
import com.software5000.ma.entity.*;
import com.zscp.master.util.MathUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceService {
    private Log log = LogFactory.getLog(FinanceService.class);

    @Resource
    private BaseDao baseDao;


    @Resource
    private WeChatPayOrderService weChatPayOrderService;
    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增财务明细
     *
     * @param finance
     * @return
     * @throws SQLException
     */
    public Finance insertFinanceRecord(Finance finance) throws SQLException {
        return baseDao.insertEntity(finance);
    }

    /**
     * 批量新增财务明细
     *
     * @param finance
     * @return
     * @throws SQLException
     */
    public List<Finance> insertFinanceRecordList(List<Finance> finance) throws SQLException {
        return baseDao.insertEntityList(finance);
    }

    /**
     * 新增财务记录
     *
     * @param t
     * @param payOrderId 如果是在线付款需要传payOrderId,如果线下支付则传null
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T extends BaseEntity> Finance insertFinanceRecordByOrder(T t, Integer payOrderId) throws SQLException {
        if (t instanceof WorkOrder) {//工单结算 OR 工单反结算
            WorkOrder workOrder = (WorkOrder) t;
            return insertFinanceRecordByWorkOrder(workOrder, payOrderId);
        } else if (t instanceof WeChatPayOrder && Constant.WeChatPayOrderType.COUPON_ORDER_TYPE.codeName.equals(((WeChatPayOrder) t).getOrderType())) {//卡券核销
            WeChatPayOrder weChatPayOrder = (WeChatPayOrder) t;
            return insertFinanceRecordByCouponOrder(weChatPayOrder);
        } else if (t instanceof BusinessPackageOrder) {//套餐订单
            BusinessPackageOrder businessPackageOrder = (BusinessPackageOrder) t;
            return insertFinanceRecordByBusinessPackageOrder(businessPackageOrder, payOrderId);
        } else if (t instanceof RechargeOrder) {//充值
            RechargeOrder rechargeOrder = (RechargeOrder) t;
            return insertFinanceRecordByRechargeOrder(rechargeOrder, payOrderId);
        } else if (t instanceof CooperComboOrder) {//诚品洗车卡
            CooperComboOrder cooperComboOrder = (CooperComboOrder) t;
            return insertFinanceRecordByCooperComboOrder(cooperComboOrder, payOrderId);
        } else if(t instanceof Finance){ //卡包佣金
            Finance finance = (Finance) t;
            if(finance.getMoney()>0) {
                BusinessCheckMoney businessCheckMoney = weChatPayOrderService.selectBusinessCheckMoney(finance.getBusinessId());
                Integer canCheckMoney = businessCheckMoney.getCanCheckMoney() + finance.getMoney();
                businessCheckMoney.setCanCheckMoney(canCheckMoney);
                weChatPayOrderService.updateBusinessCheckMoney(businessCheckMoney);
            }
            return insertFinanceRecord(finance);
        }
        return null;
    }

    /**
     * 新增工单的财务明细
     *
     * @param workOrder
     * @param payOrderId 如果是在线付款需要传payOrderId,如果线下支付则传null
     * @return
     * @throws SQLException
     */
    private Finance insertFinanceRecordByWorkOrder(WorkOrder workOrder, Integer payOrderId) throws SQLException {
        Finance finance = new Finance();
        finance.setBusinessId(workOrder.getBusinessId());
        finance.setPayType(workOrder.getPayType());
        finance.setExplainInfo(workOrder.getCarNumber());
        finance.setFinanceType(workOrder.isOpposite() ? Constant.FinanceType.OPPOSITE_WORK_ORDER_TYPE.codeName : Constant.FinanceType.WORK_ORDER_TYPE.codeName);
        finance.setInOrOutCome(workOrder.isOpposite() ? Constant.InOrOutCome.OUT_COME.codeName : Constant.InOrOutCome.IN_COME.codeName);
        int money = MathUtil.mul(workOrder.getTotalPrice(), 100).intValue();
        finance.setMoney(money);
        finance.setUserPayMoney(money);
        finance.setOrderId(workOrder.getId());
        finance.setPayOrderId(payOrderId);
        finance.setOrderNo(workOrder.getOrderNo());
        finance.setUserId(workOrder.getUserId());
        return insertFinanceRecord(finance);
    }


    /**
     * 新增卡券的财务明细
     *
     * @param weChatPayOrder
     * @return
     * @throws SQLException
     */
    private Finance insertFinanceRecordByCouponOrder(WeChatPayOrder weChatPayOrder) throws SQLException {
        Finance finance = new Finance();
        finance.setBusinessId(weChatPayOrder.getBusinessId());
        finance.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
        finance.setExplainInfo(weChatPayOrder.getShowName());
        finance.setFinanceType(Constant.FinanceType.COUPON_ORDER_TYPE.codeName);
        finance.setMoney(weChatPayOrder.getCheckMoneyFee());
        finance.setUserPayMoney(0);
        finance.setOrderId(weChatPayOrder.getPackageOrderId());
        finance.setOrderNo(weChatPayOrder.getTradeNo());
        finance.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
        return insertFinanceRecord(finance);
    }

    /**
     * 新增套餐的财务明细
     *
     * @param businessPackageOrder
     * @return
     * @throws SQLException
     */
    private Finance insertFinanceRecordByBusinessPackageOrder(BusinessPackageOrder businessPackageOrder, Integer payOrderId) throws SQLException {
        Finance finance = new Finance();
        finance.setBusinessId(businessPackageOrder.getBusinessId());
        finance.setPayType(businessPackageOrder.getPayType());
        finance.setExplainInfo(businessPackageOrder.getBusinessPackageName());
        finance.setFinanceType(Constant.FinanceType.PACKAGE_ORDER_TYPE.codeName);
        int money = MathUtil.mul(businessPackageOrder.getTotalAmount(), 100).intValue();
        finance.setMoney(money);
        finance.setUserPayMoney(money);
        finance.setOrderId(businessPackageOrder.getId());
        finance.setOrderNo(businessPackageOrder.getOrderNumber());
        finance.setPayOrderId(payOrderId);
        finance.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
        finance.setUserId(businessPackageOrder.getUserId());
        return insertFinanceRecord(finance);
    }

    /**
     * 新增充值的财务明细
     *
     * @param rechargeOrder
     * @return
     * @throws SQLException
     */
    private Finance insertFinanceRecordByRechargeOrder(RechargeOrder rechargeOrder, Integer payOrderId) throws SQLException {
        Finance finance = new Finance();
        finance.setBusinessId(rechargeOrder.getBusinessId());
        finance.setPayType(rechargeOrder.getPayType());
        User user=new User();
        user.setId(rechargeOrder.getUserId());
        user=baseDao.selectSingleEntity(user);
        if(null!=user){
            finance.setExplainInfo(user.getMobile());
        }
        finance.setFinanceType(Constant.FinanceType.RECHARGE_TYPE.codeName);
        int money = MathUtil.mul(rechargeOrder.getReChargeMoney(), 100).intValue();
        finance.setMoney(money);
        finance.setUserPayMoney(money);
        finance.setOrderId(rechargeOrder.getId());
        finance.setOrderNo(rechargeOrder.getOrderNumber());
        finance.setPayOrderId(payOrderId);
        finance.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
        finance.setUserId(rechargeOrder.getUserId());
        return insertFinanceRecord(finance);
    }

    /**
     * 新增诚品洗车卡的财务明细
     *
     * @param cooperComboOrder
     * @return
     * @throws SQLException
     */
    private Finance insertFinanceRecordByCooperComboOrder(CooperComboOrder cooperComboOrder, Integer payOrderId) throws SQLException {
        Finance finance = new Finance();
        finance.setBusinessId(cooperComboOrder.getBusinessId());
        finance.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
        finance.setExplainInfo(cooperComboOrder.getComboName() + "/" + cooperComboOrder.getCarNumber());
        finance.setFinanceType(Constant.FinanceType.MMGOODCAR_ORDER_TYPE.codeName);
        int money = MathUtil.mul(cooperComboOrder.getBackAmount(), 100).intValue();
        finance.setMoney(money);
        finance.setUserPayMoney(money);
        finance.setOrderId(cooperComboOrder.getId());
        finance.setOrderNo(cooperComboOrder.getOrderNumber());
        finance.setPayOrderId(payOrderId);
        finance.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
        finance.setUserId(cooperComboOrder.getUserId());
        return insertFinanceRecord(finance);
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 查询总收入与总支出
     * @param map
     * @return
     * @throws SQLException
     */
    public FinanceInOrOutComeDto selectFinanceInOrOutComeDto(Map map) throws SQLException {
        List<HashMap> hashMaps = (List<HashMap>) baseDao.selectList(Finance.Daos.selectFinanceInOrOutComeDto.sqlMapname, map);
        FinanceInOrOutComeDto financeInOrOutComeDto = new FinanceInOrOutComeDto();
        for (HashMap hashMap : hashMaps) {
           if("inCome".equals(hashMap.get("inOrOutCome"))){
               financeInOrOutComeDto.setInComeMoney(Integer.valueOf(hashMap.get("sumMoney").toString()));
           }else if("outCome".equals(hashMap.get("inOrOutCome"))){
               financeInOrOutComeDto.setOutComeMoney(Integer.valueOf(hashMap.get("sumMoney").toString()));
           }
        }
        return financeInOrOutComeDto;
    }


    /**
     * 分页查询财务明细
     * @param map
     * @return
     */
    public PageInfo selectPageFinance(Map map) throws SQLException {
        String orderBy = map.get("orderBy").toString();
        if(ValidUtil.isEmpty(orderBy)) orderBy = "id desc";
        Integer startPage = (Integer) map.getOrDefault("startPage", 1);
        Integer pageSize = (Integer) map.getOrDefault("pageSize", 10);
        return baseDao.selectListByPage(Finance.Daos.selectPageFinance.sqlMapname, map, startPage, pageSize, orderBy);
    }

    /**
     * 查询一定时间内商家的营业额
     */
    public Integer selectFinanceByDate(Map map) throws SQLException {
        return (Integer)baseDao.selectObject(Finance.Daos.selectFinanceByDate.sqlMapname, map);
    }

    public Finance selectFinanceByPayOrderId(Integer payOrderId){
        Finance finance = new Finance();
        finance.setPayOrderId(payOrderId);
        return baseDao.selectSingleEntity(finance);
    }

    public Finance selectFinanceByOrderNo(String orderNo){
        Finance finance = new Finance();
        finance.setOrderNo(orderNo);
        return baseDao.selectSingleEntity(finance);
    }

    /**
     * 新增记录支出
     * @param finance
     * @return
     * @throws SQLException
     */
    public Finance InsertFinaceOtherPay(Finance finance) throws SQLException {
        return baseDao.insertEntity(finance);
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}