package com.software5000.ma.service;

/**
 * Created by luo on 2017/7/24.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.ma.entity.RechargeOrder;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class RechargeOrderService {
    private Log log = LogFactory.getLog(RechargeOrderService.class);

    @Autowired
    private BaseDao baseDao;

    @Resource
    private FinanceService financeService;

    @Resource
    private UserService userService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增充值
     * @param rechargeOrder
     * @return
     * @throws ServiceException
     */
    public RechargeOrder insertRechargeOrder(RechargeOrder rechargeOrder) throws ServiceException{
        try {
            rechargeOrder=baseDao.insertEntity(rechargeOrder);
            return rechargeOrder;
        } catch (SQLException e) {
            log.error("保存rechargeOrder失败，rechargeOrder=" + rechargeOrder,e);
            throw new ServiceException(Constant.StateCode.SAVE_ERROR.codeName);
        }
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
     * 修改充值记录
     * @param rechargeOrder
     * @return
     * @throws ServiceException
     */
    public void updateRechargeOrder(RechargeOrder rechargeOrder) throws ServiceException{
        try {
            baseDao.updateEntityOnlyHaveValue(rechargeOrder,false);
        } catch (SQLException e) {
            log.error("修改rechargeOrder失败，rechargeOrder=" + rechargeOrder,e);
            throw new ServiceException(Constant.StateCode.UPDATE_ERROR.codeName);
        }
    }

    /**
     * 微信付款回调逻辑处理
     * @param orderId
     */
    public void updateRechargeOrderByWetChatPay(Integer orderId,String wxOpenId,Integer payOrderId) throws Exception {
        RechargeOrder rechargeOrder=baseDao.selectEntityById(orderId,RechargeOrder.class);
        if (null != rechargeOrder.getState() && Constant.OrderState.TO_BE_PAID.codeName.equals(rechargeOrder.getState())) {
            rechargeOrder.setState(Constant.OrderState.PAID.codeName);//付款
            rechargeOrder.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
            updateRechargeOrder(rechargeOrder);
            financeService.insertFinanceRecordByOrder(rechargeOrder,payOrderId);
            //财务核算
            //增加用户余额修改用户等级
            memberLvlRecordService.updateMemberLvl(rechargeOrder.getUserId(),rechargeOrder.getBusinessId(),rechargeOrder.getReChargeMoney(),rechargeOrder.getGrantMoney(),true);
            Integer userId = rechargeOrder.getUserId();
            if(userId!=null){
                userService.updateWxopenIdByUserId(userId,wxOpenId);
            }
        }
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     *充值记录查询
     */
    public PageInfo<RechargeOrder> selectPayPackageOrderPage(Map param)throws ServiceException{
        try {
            PageInfo pageInfo=baseDao.selectListByPage(RechargeOrder.Daos.selectPayRechargeOrderByState.sqlMapname,param,Integer.parseInt(param.get("startPage").toString()),Integer.parseInt(param.get("pageSize").toString()),"o.updateTime DESC");
            if(null!=pageInfo.getList()&&pageInfo.getList().size()>0){
                param.put("id",pageInfo.getList());
                pageInfo.setList(selectRechargeOrderById(param));
            }
            return pageInfo;
        } catch (SQLException e) {
            log.error("查询数据失败", e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }


    /**
     * 查询订单详情
     */
    public RechargeOrder selectRechargeOrder(RechargeOrder rechargeOrder){
        return baseDao.selectSingleEntity(rechargeOrder);
    }

    public List<RechargeOrder> selectRechargeOrderById(Map param) throws SQLException{
        return (List<RechargeOrder>)baseDao.selectList(RechargeOrder.Daos.selectRechargeOrderById.sqlMapname,param);
    }

    /**
     * 核查订单是否可以支付
     * @return
     */
    public ReturnResult checkCanPay(Integer id) throws ServiceException{
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        RechargeOrder rechargeOrder =new RechargeOrder();
        rechargeOrder.setId(id);
        rechargeOrder = selectRechargeOrder(rechargeOrder);
        if(null==rechargeOrder||!Constant.OrderState.TO_BE_PAID.codeName.equals(rechargeOrder.getState())){
            returnResult = ReturnResult.buildResult(Constant.StateCode.ORDER_HAVE_FAIL_ERR.codeName);
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>

}