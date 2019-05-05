package com.software5000.ma.task;

import com.software5000.base.Constant;
import com.software5000.util.PostUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.HashMap;
import java.util.List;

/**
 * 商家生成对账单的任务
 * Created by wujin on 2017/3/1.
 */
public class MerchantCheckMoneyOrderTask {

    protected Log log = LogFactory.getLog(this.getClass());

    /**
     * 回调营销平台进行优惠券核销确认
     * @param couponIds
     * @throws Exception
     */
    public void checkCouponData(List couponIds) throws Exception{
        PostUtil.postEMKT(Constant.Emkt.UPDATE_MACHECKS_COUPON_USED.codeName,new HashMap(){{ put("ids", couponIds);}});
    }
}
