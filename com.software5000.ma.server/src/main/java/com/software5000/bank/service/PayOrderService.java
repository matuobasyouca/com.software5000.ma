package com.software5000.bank.service;

/**
 * Created by jiye on 2017/7/1.
 */

import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.bank.entity.PayOrder;
import com.software5000.base.MyBaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

@Service
public class PayOrderService {
    private Log log = LogFactory.getLog(PayOrderService.class);

    @Resource
    private MyBaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    public PayOrder insertPayOrder(PayOrder payOrder) throws SQLException {
        return (PayOrder) baseDao.insertEntity(payOrder);
    }

    /**
     * 新增收银台订单,如果存在则更新,仅用于各处的调用收银台时使用，收银台本身的订单更新不可调用，否则状态及跳转地址将被替换
     * @return
     */
    public PayOrder insertPayOrderOrUpdateIfExit(PayOrder payOrder) throws SQLException {
        //目前的状态跟付款完成地址统一，后续有变动则调用方自己设置
        payOrder.setPayState(Constant.OrderState.TO_BE_PAID.codeName);
        PayOrder havePayOrder = selectSinglePayOrderByOrderNo(payOrder.getOrderNo());
        if(havePayOrder!=null){
            payOrder.setId(havePayOrder.getId());
            updatePayOrder(payOrder);
        }else{
            payOrder = insertPayOrder(payOrder);
        }
        return payOrder;
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    public void updatePayOrder(PayOrder payOrder) throws SQLException {
        baseDao.updateEntity(payOrder,null, false);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    public PayOrder selectSinglePayOrderById(Integer payOrderId) throws SQLException {
        return baseDao.selectEntityById(payOrderId,PayOrder.class);
    }

    /**
     * 根据单号查询
     * @param orderNo
     * @return
     */
    public PayOrder selectSinglePayOrderByOrderNo(String orderNo) throws SQLException {
        PayOrder payOrder = new PayOrder();
        payOrder.setOrderNo(orderNo);
        return baseDao.selectSingleEntity(payOrder);
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}