package com.software5000.ma.service;

/**
 * Created by Jiang on 2017/07/01.
 */

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.PageInfo;
import com.software5000.ma.entity.Business;
import com.software5000.ma.entity.CouponsPack;
import com.software5000.ma.entity.CouponsPackBuyRecord;
import com.software5000.bank.entity.PayOrder;
import com.software5000.bank.service.PayOrderService;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.MathUtil;
import com.software5000.util.PayCommonUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CouponsPackService {
    private Log log = LogFactory.getLog(CouponsPackService.class);

    @Resource
    private BaseDao baseDao;

    @Resource
    private CouponsPackService couponsPackService;

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private UserService userService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;


    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    //新增购买记录
    public String insertCouponsPackBuyRecord(CouponsPackBuyRecord record) throws SQLException {

        //作废掉，微信号或车牌号相关联的订单
        baseDao.update(CouponsPackBuyRecord.Daos.updateBuyRecordForInsert.sqlMapname, record);
        Integer packId = record.getPackId();
        CouponsPack couponsPack = couponsPackService.selectCouponsPackById(packId);
        record.setTradeFee(MathUtil.mul(couponsPack.getBuyAmouont(), 100).intValue()*record.getBuyCount());
        //新增购买记录
        record = baseDao.insertEntity(record);

        //根据packId，获取卡券礼包信息
        CouponsPack pack = couponsPackService.selectCouponsPackById(record.getPackId());
        if(pack != null) {
            //获取订单号
            String orderNo = PayCommonUtil.getOutTradeNo(record.getId(), new Double(pack.getBuyAmouont()*100).intValue(), PayCommonUtil.TYPE_OF_COUPONSPACK, record.getOrderNo());

            //组装生成的数据
            PayOrder payOrder = new PayOrder();
            payOrder.setOrderTitle(pack.getTitle());
            payOrder.setTotalPrice(new Double(pack.getBuyAmouont()*100).intValue());
            payOrder.setOrderService(record.getMobile() + "/" + record.getCarNumber());
            payOrder.setOrderContent(pack.getContent());
            payOrder.setOrderNo(orderNo);
            if(record.getBusinessId() != null && Constant.getStringCodeValueByName(Constant.CashierDesk.LOTTERY_BUSINESS_IDS).contains(","+ record.getBusinessId()+",")) {
                payOrder.setRedirectUrl(Constant.maUrl+Constant.getStringCodeValueByName(Constant.CashierDesk.LOTTERY_URL));
            }
            payOrder = payOrderService.insertPayOrderOrUpdateIfExit(payOrder);

            //更新订单号
            record.setOrderNo(orderNo);
            couponsPackService.updateBuyRecord(record);

            //返回链接
            return Constant.maUrl+Constant.getStringCodeValueByName(Constant.CashierDesk.CASHIER_DESK_URL_JSAPI) + "?payOrderId=" + payOrder.getId();
        }

        return null;
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
     * 更新购买记录
     * @param buyRecord
     * @throws SQLException
     */
    public void updateBuyRecord(CouponsPackBuyRecord buyRecord) throws SQLException {
        baseDao.updateEntityOnlyHaveValue(buyRecord, false);
    }

    /**
     * 将购买记录修改为已取消
     * @param recordId
     * @throws SQLException
     */
    public void updateBuyRecordForDel(Integer recordId) throws SQLException {
        CouponsPackBuyRecord buyRecord = new CouponsPackBuyRecord();
        buyRecord.setId(recordId);
        buyRecord.setState(Constant.CouponsPackBuyRecordState.CANCEL);
        baseDao.updateEntityOnlyHaveValue(buyRecord, false);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据ID，查询卡券礼包信息
     * @param packId
     * @return
     * @throws SQLException
     */
    public CouponsPack selectCouponsPackById(Integer packId) throws SQLException {
        return baseDao.selectEntityById(packId, CouponsPack.class);
    }

    /**
     * 根据实体，查询卡券礼包信息
     * @param couponsPack
     * @return
     * @throws SQLException
     */
    public CouponsPack selectCouponsPackByEntity( CouponsPack couponsPack) throws SQLException {
        return baseDao.selectSingleEntity(couponsPack);
    }

    /**
     * 根据条件，查询购买记录
     * @param param
     * @return
     * @throws SQLException
     */
    public List<CouponsPackBuyRecord> selectBuyRecordForCheck(Map<String, Object> param) throws SQLException {
        return (List<CouponsPackBuyRecord>)baseDao.selectList(CouponsPackBuyRecord.Daos.selectBuyRecordForCheck.sqlMapname ,param);
    }

    /**
     * 根据条件，判断该订单是否能够支付
     * @param recordId
     * @return
     * @throws SQLException
     */
    public ReturnResult selectBuyRecordForPayCheck(Integer recordId) throws SQLException {

        //查询记录信息
        CouponsPackBuyRecord buyRecord = baseDao.selectEntityById(recordId, CouponsPackBuyRecord.class);
        if(buyRecord != null) {
            //如果是已支付
            if(Constant.CouponsPackBuyRecordState.PAID.equals(buyRecord.getState())) {
                return ReturnResult.buildEnumResult(Constant.StateCode.ORDER_HAS_BEEN_PAID);
            }

            //如果是已取消的
            if(Constant.CouponsPackBuyRecordState.CANCEL.equals(buyRecord.getState())) {
                return ReturnResult.buildEnumResult(Constant.StateCode.ORDER_HAS_BEEN_CANCELLED);
            }
        }

        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 根据ID获取购买记录信息
     * @param id
     * @return
     * @throws SQLException
     */
    public CouponsPackBuyRecord selectBuyRecordById(Integer id) throws SQLException {
        return baseDao.selectEntityById(id, CouponsPackBuyRecord.class);
    }

    /**
     * 分页查询订单信息
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public PageInfo<CouponsPackBuyRecord> selectPageBuyRecord(Map paramMap) throws SQLException {
        Integer startPage = (Integer)paramMap.getOrDefault("startPage", 1);
        Integer pageSize = (Integer)paramMap.getOrDefault("pageSize", 10);
        String orderBy = paramMap.getOrDefault("orderBy", "r.id desc").toString();
        Object states = paramMap.get("states");
        if(states!=null){
            JSONArray states1 = (JSONArray) states;
            List<Object> stateList = new ArrayList<>();
            states1.forEach(s->{
                stateList.add(s);
            });
            paramMap.put("stateList", stateList);
        }
        return baseDao.selectListByPage(CouponsPackBuyRecord.Daos.selectPageBuyRecord.sqlMapname,paramMap, startPage, pageSize, orderBy);
    }

    /**
     * 查询加入99元的合作商家
     * @return
     * @throws SQLException
     */
    public List<Business> selectCooperBusiness() throws SQLException {
        return (List<Business>)baseDao.selectList(CouponsPackBuyRecord.Daos.selectCooperBusiness.sqlMapname);
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
}