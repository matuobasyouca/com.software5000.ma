package com.software5000.ma.service;

/**
 * Created by luo on 2017/7/24.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.ma.dto.PayPackageOrderDto;
import com.software5000.ma.entity.BusinessPackageOrder;
import com.software5000.ma.entity.Car;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessPackageOrderService {
    private Log log = LogFactory.getLog(BusinessPackageOrderService.class);

    @Autowired
    private BaseDao baseDao;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    @Resource
    private FinanceService financeService;

    @Resource
    private UserService userService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private CarService carService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增套餐订单
     * @param businessPackageOrder
     * @return
     * @throws ServiceException
     */
    public BusinessPackageOrder insertBusinessPackageOrder(BusinessPackageOrder businessPackageOrder) throws ServiceException{
        try {
            businessPackageOrder=baseDao.insertEntity(businessPackageOrder);
            return businessPackageOrder;
        } catch (SQLException e) {
            log.error("保存packCard失败，packCard=" + businessPackageOrder,e);
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
     * 修改套餐订单
     * @param businessPackageOrder
     * @return
     * @throws ServiceException
     */
    public void updateBusinessPackageOrder(BusinessPackageOrder businessPackageOrder) throws ServiceException{
        try {
            baseDao.updateEntityOnlyHaveValue(businessPackageOrder,false);
        } catch (SQLException e) {
            log.error("修改packOrder失败，packOrder=" + businessPackageOrder,e);
            throw new ServiceException(Constant.StateCode.UPDATE_ERROR.codeName);
        }
    }

    /**
     * 微信付款回调逻辑处理
     * @param packageOrderId
     */
    public void updatePackageOrderByWetChatPay(Integer packageOrderId,String wxOpenId,Integer payOrderId) throws Exception {
        Map param=new HashMap();
        param.put("id",packageOrderId);
        BusinessPackageOrder businessPackageOrder = selectBusinessPackageOrderById(param);
        if (null != businessPackageOrder.getState() && Constant.OrderState.TO_BE_PAID.codeName.equals(businessPackageOrder.getState())) {
            businessPackageOrder.setState(Constant.OrderState.PAID.codeName);//付款
            businessPackageOrder.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
            updateBusinessPackageOrder(businessPackageOrder);
            memberPackageRecordService.insertMemberPackageRecord(businessPackageOrder);
            businessPackageOrder.setBusinessPackageName(businessPackageOrder.getBusinessPackage().getPackageName()+"/"+businessPackageOrder.getUser().getMobile());
            financeService.insertFinanceRecordByOrder(businessPackageOrder,payOrderId);
            Integer userId = businessPackageOrder.getUserId();
            if(userId!=null){
                List<Car> cars = carService.selectUserCarById(userId);
                userService.insertUser(wxOpenId,businessPackageOrder.getMobile(),cars.get(0).getCarNumber());
                memberLvlRecordService.upgradeMemberLvlRecord(businessPackageOrder.getUserId(),businessPackageOrder.getBusinessId(),businessPackageOrder.getTotalAmount());
            }
        }

    }

    public void updatePackageOrderByDate() throws ServiceException{
        Map orderInfoMap = new HashMap();
        orderInfoMap.put("state", Constant.OrderState.TO_BE_PAID.codeName);
        orderInfoMap.put("newState",Constant.OrderState.OVERDUE.codeName);
        orderInfoMap.put("date", DateUtils.formatDateTime(DateUtils.addDay(DateUtils.now(), -2)));
        try {
            this.baseDao.update(BusinessPackageOrder.Daos.updatePackageOrderByDate.sqlMapname,orderInfoMap);
        } catch (SQLException e) {
            log.error("未支付订单数据更新失败", e);
            throw new ServiceException(Constant.StateCode.UPDATE_ERROR.codeName);
        }
    }



    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据商家ID查询待支付订单
     * @return
     * @throws ServiceException
     */
    public PageInfo<PayPackageOrderDto> selectUnPayPackageOrderPage (Map param) throws ServiceException {
        try {
            return baseDao.selectListByPage(BusinessPackageOrder.Daos.selectPayPackageOrderByState.sqlMapname,param,Integer.parseInt(param.get("starPage").toString()),Integer.parseInt(param.get("pageSize").toString()),null);
        } catch (SQLException e) {
            log.error("查询数据失败", e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }

    /**
     * 根据商家ID查询待购买记录
     * @return
     * @throws ServiceException
     */
    public PageInfo<PayPackageOrderDto> selectPayPackageOrderPage (Map param) throws ServiceException {
        try {
            return baseDao.selectListByPage(BusinessPackageOrder.Daos.selectPayPackageOrderByState.sqlMapname,param,Integer.parseInt(param.get("starPage").toString()),Integer.parseInt(param.get("pageSize").toString()),null);
        } catch (SQLException e) {
            log.error("查询数据失败", e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }

    /**
     * 查询订单详情
     */
    public BusinessPackageOrder selectBusinessPackageOrder(BusinessPackageOrder businessPackageOrder){
        return baseDao.selectSingleEntity(businessPackageOrder);
    }

    public BusinessPackageOrder selectBusinessPackageOrderById(Map param) throws SQLException{
        return (BusinessPackageOrder)baseDao.selectObject(BusinessPackageOrder.Daos.selectBusinessPackageOrderById.sqlMapname,param);
    }

    /**
     * 查询未支付订单数
     */
    public Integer selectNoPaidPackageOrderCount(Map param) throws SQLException{
        return (Integer)baseDao.selectObject(BusinessPackageOrder.Daos.selectNoPaidPackageOrderCount.sqlMapname,param);
    }

    /**
     * 核查订单是否可以支付
     * @return
     */
    public ReturnResult checkCanPay(Integer id) throws ServiceException{
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        BusinessPackageOrder businessPackageOrder =new BusinessPackageOrder();
        businessPackageOrder.setId(id);
        businessPackageOrder = selectBusinessPackageOrder(businessPackageOrder);
        if(null==businessPackageOrder||!Constant.OrderState.TO_BE_PAID.codeName.equals(businessPackageOrder.getState())){
            returnResult = ReturnResult.buildResult(Constant.StateCode.ORDER_HAVE_FAIL_ERR.codeName);
        }
        return returnResult;
    }

    /**
     * 查询商家各状态下订单
     */
    public List<Map> selectCountByState(Map param) throws ServiceException{
        try{
            return (List<Map>)baseDao.selectList(BusinessPackageOrder.Daos.selectCountByState.sqlMapname,param);
        }catch (SQLException e) {
            log.error("查询失败，param="+param,e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }

    /**
     * 查询用户最早产生记录的情况
     */
    public Map selectInitialByOpenId(Map param) throws ServiceException{
        try{
            return (Map)baseDao.selectObject(BusinessPackageOrder.Daos.selectInitialByOpenId.sqlMapname,param);
        }catch (SQLException e) {
            log.error("查询失败，param="+param,e);
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>

}