package com.software5000.ma.service;

import com.github.pagehelper.PageInfo;
import com.software5000.ma.entity.CooperComboOrder;
import com.software5000.ma.entity.User;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.ValidUtil;
import com.software5000.util.PostUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jinbo on 2017/5/4.
 */
@Service
public class CooperComboOrderService {
    protected Log log = LogFactory.getLog(this.getClass());

    @Autowired
    private BaseDao baseDao;

    @Autowired
    private CooperComboService cooperComboService;
    @Autowired
    private UserService userService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private FinanceService financeService;


    /**
     * 新增_合作套餐订单
     *
     * @param cooperComboOrder
     * @return CooperComboOrder
     * @throws SQLException
     */
    public CooperComboOrder insertCooperComboOrder(CooperComboOrder cooperComboOrder) throws SQLException {
        return baseDao.insertEntity(cooperComboOrder);
    }

    /**
     * 根据Id查询_合作套餐订单
     *
     * @param id
     * @return CooperComboOrder
     * @throws SQLException
     */
    public CooperComboOrder selectCooperComboOrderById(Integer id) throws SQLException {
        return baseDao.selectEntityById(id, CooperComboOrder.class);
    }

    /**
     * 根据Id查询_合作套餐订单带用户和套餐信息
     *
     * @param id
     * @return CooperComboOrder
     * @throws SQLException
     */
    public CooperComboOrder selectCooperComboOrderDetailById(Integer id) throws SQLException {
        return (CooperComboOrder) baseDao.selectObject(CooperComboOrder.Daos.selectCooperComboDetailById.sqlMapname, id);
    }


    /**
     * 编辑_合作套餐订单
     *
     * @param cooperComboOrder
     * @return CooperComboOrder
     * @throws SQLException
     */
    public void updateCooperComboOrder(CooperComboOrder cooperComboOrder) throws SQLException {
        baseDao.updateEntityNotEmpty(cooperComboOrder);
    }

    /**
     * 分页查询_合作套餐
     *
     * @param paramMap
     * @return PageInfo
     * @throws SQLException
     */
    public PageInfo selectCooperComboOrderPageByParam(Map paramMap) throws SQLException {
        //分页查询合作套餐信息
        String orderByStr = "id desc";
        if (ValidUtil.isEmpty(paramMap.get("orderBy"))) {
            orderByStr = paramMap.get("orderBy").toString();
        }

        PageInfo pageInfo = baseDao.selectListByPage(CooperComboOrder.Daos.selectCooperComboOrderPageByParam.sqlMapname, paramMap,
                (Integer) paramMap.getOrDefault("startPage", 1),
                (Integer) paramMap.getOrDefault("pageSize", 1),
                orderByStr
        );
        return pageInfo;
    }

    public void notifyCooperComboOrder(Integer cooperComboOrderId, String openId,Integer businessId,Integer payOrderId) throws SQLException, IOException, ServiceException {
        CooperComboOrder cooperComboOrder = selectCooperComboOrderDetailById(cooperComboOrderId);
        //判断是否可以增加用户
        User user = userService.selectUserByOpenIdOrMobileOrCar(openId, cooperComboOrder.getMobile(), cooperComboOrder.getCarNumber());
        if(user==null) {
            user = userService.insertUser(openId,cooperComboOrder.getMobile(),cooperComboOrder.getCarNumber());
        }
        if (null != cooperComboOrder.getState() && Constant.OrderState.TO_BE_PAID.codeName.equals(cooperComboOrder.getState().toString())) {
            //修改合作套餐订单状态
            CooperComboOrder cooperComboOrderTmp = new CooperComboOrder();
            cooperComboOrderTmp.setId(cooperComboOrder.getId());
            cooperComboOrderTmp.setState(Constant.OrderState.PAID.codeName);
            cooperComboOrderTmp.setUserId(user.getId());
            updateCooperComboOrder(cooperComboOrderTmp);
            //修改合作套餐卡的销售数量
            Map upNumParamMap = new HashMap();
            upNumParamMap.put("saleNum", cooperComboOrder.getQuantity());
            upNumParamMap.put("cooperComboId", cooperComboOrder.getCooperComboId());
            cooperComboService.updateCooperComboSaleNumByParam(upNumParamMap);
            //营销系统的更新
            Map paramMap = new HashMap();
            paramMap.put("openid", openId);
            paramMap.put("carnum", cooperComboOrder.getCarNumber());
            paramMap.put("mobile", cooperComboOrder.getMobile());
            paramMap.put("cpuuid", cooperComboOrder.getCooperCombo().getCouponUUID());
            paramMap.put("nums", cooperComboOrder.getQuantity());
            PostUtil.postEMKT(Constant.Emkt.INSERT_COUPON_USE.codeName, paramMap);
            //新增
            financeService.insertFinanceRecordByOrder(cooperComboOrder,payOrderId);
        }
        //给商家新增客户
        memberLvlRecordService.insertMemberLvlRecord(user.getId(), businessId);
    }

    /**
     * 核查订单是否可以支付
     *
     * @return
     */
    public ReturnResult checkCanPay(Integer id) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        CooperComboOrder cooperComboOrder = selectCooperComboOrderById(id);
        if (!Constant.OrderState.TO_BE_PAID.codeName.equals(cooperComboOrder.getState())) {
            returnResult = ReturnResult.buildEnumResult(Constant.StateCode.ORDER_HAVE_FAIL_ERR);
        }
        return returnResult;
    }
}
