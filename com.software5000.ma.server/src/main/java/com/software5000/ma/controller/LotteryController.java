package com.software5000.ma.controller;

/**
 * Created by janzho on 2017/6/30.
 */

import com.software5000.bank.entity.PayOrder;
import com.software5000.bank.service.PayOrderService;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.ma.dto.LotteryDto;
import com.software5000.ma.entity.*;
import com.software5000.ma.service.*;
import com.software5000.util.PayCommonUtil;
import com.software5000.util.PostUtil;
import com.zscp.master.util.RandomUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.*;

@Controller
public class LotteryController {

    @Resource
    private PayOrderService payOrderService;

    @Resource
    private CouponsPackService couponsPackService;

    @Resource
    private WeChatPayOrderService weChatPayOrderService;

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private BusinessPackageOrderService businessPackageOrderService;

    @Resource
    private LotteryAwardService lotteryAwardService;

    @Resource
    private UserService userService;

    @Resource
    private FinanceService financeService;

    private Log log = LogFactory.getLog(LotteryController.class);

    private String[] PRIZE = new String[]{"空调清洗1次", "免费洗车1次", "10元现金券", "手机支架", "二维码停车贴"};

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== home (对内) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="api (对外) ">
    /*================================== api (对外) start ==================================*/

    //<editor-fold desc="api (对外)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== api (对外) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="home&api (内&外) ">
    /*================================== home&api (内&外) start ==================================*/

    //<editor-fold desc="home&api (内&外)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home&api (内&外)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== home&api (内&外) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="open (开放) ">
    /*================================== open (开放) start ==================================*/

    //<editor-fold desc="open (开放)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 抽奖-获取抽奖次数
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/open/lottery/selectTimes", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectTimes(@RequestBody Map params) throws SQLException, ServiceException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        PayOrder payOrder = new PayOrder();
        payOrder = payOrderService.selectSinglePayOrderByOrderNo(params.get("orderNo").toString());
        String orderNo = payOrder.getOrderNo();
        Integer tradeId = Integer.valueOf(orderNo.split("_")[0]);
        String type = orderNo.split("_")[2];
        String carNumber = "";
        String mobile = "";

       if(type.equals(PayCommonUtil.TYPE_OF_WORKORDER)){
            WorkOrder workOrder = workOrderService.selectWorkOrderById(tradeId);
            carNumber = workOrder.getCarNumber();
            mobile = workOrder.getMobile();
        }else if(type.equals(PayCommonUtil.TYPE_OF_PACKAGEORDER)){
            Map param = new HashMap();
            param.put("id", tradeId);
            BusinessPackageOrder businessPackageOrder = businessPackageOrderService.selectBusinessPackageOrderById(param);
            Integer userId = businessPackageOrder.getUserId();
            List<Car> cars = userService.selectCarInfo(userId);
            Car car = cars.get(new Random().nextInt(cars.size()));
            mobile = businessPackageOrder.getUser().getMobile();
            carNumber = car.getCarNumber();
        }
        LotteryDto lottyDto = new LotteryDto();
        LotteryAward lotteryAward = new LotteryAward();
        lotteryAward.setPayOrderId(payOrder.getId());
        LotteryAward lotteryAward1 = lotteryAwardService.selectSingleLotteryAwardByEntity(lotteryAward);
        lottyDto.setLotteryTimes(lotteryAward1 == null ? 1 : 0);
        lottyDto.setLotteryPrize(lotteryAward1 == null?null:lotteryAward1.getRewordName());
        lottyDto.setPrizeNum(lotteryAward1 == null?null:Arrays.asList(PRIZE).indexOf(lotteryAward1.getRewordName()));
        lottyDto.setCarNumber(carNumber);
        lottyDto.setMobile(mobile);
        lottyDto.setReceiveState(lotteryAward1 == null?null:lotteryAward1.getState());
        return returnResult.setData(lottyDto);
    }

    /**
     * 抽奖-随机获取奖品
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/open/lottery/selectPrize", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPrize(@RequestBody Map params) throws ServiceException, SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();

        int[] prizeNum = new int[]{5, 20, 25, 30, 20};
        int leng = 0;
        int realPrize = 4;
        for (int x : prizeNum) {
            leng += x;
        }
        for (int i = 0; i < prizeNum.length; i++) {
            int r = RandomUtil.nextInt(0, leng);
            if (r < prizeNum[i]) {
                realPrize = i;
                break;
            } else {
                leng -= prizeNum[i];
            }
        }
        Integer payId = Integer.valueOf(params.get("id").toString());
        LotteryAward lotteryAward = new LotteryAward();
        lotteryAward.setPayOrderId(payId);
        lotteryAward.setRewordName(PRIZE[realPrize]);
        //实物默认为已经领取
        lotteryAward.setState(realPrize>=3?Constant.LotteryAwardState.HAVE_Receive.codeName:Constant.LotteryAwardState.NO_Receive.codeName);
        PayOrder payOrder = payOrderService.selectSinglePayOrderById(payId);
        lotteryAward.setWxOpenId(payOrder.getWxOpenId());
        Finance finance = financeService.selectFinanceByPayOrderId(payId);
        lotteryAward.setBusinessId(finance==null?null:finance.getBusinessId());
        lotteryAward.setCarNumber(params.get("carNumber").toString());
        lotteryAward.setMobile(params.get("mobile").toString());
        lotteryAwardService.insertLotteryAward(lotteryAward);
        return returnResult.setData(realPrize);
    }

    /**
     * 向用户发放抽中的卡券
     *
     * @param opts {carNumber,mobile,id}
     * @return
     */
    @RequestMapping(value = {"/open/lottery/insertMaCouponUsed"}, method = RequestMethod.POST)
    @ResponseBody
    public Object getMobileByUserId(@RequestBody Map opts) throws Exception {
        String[] uuid = Constant.getStringCodeValueByName("lotteryCoupons").split(",");
        Map param = new HashMap();
        PayOrder payOrder = payOrderService.selectSinglePayOrderById(Integer.valueOf(opts.get("id").toString()));
        LotteryAward la = new LotteryAward();
        la.setPayOrderId(payOrder.getId());
        LotteryAward lotteryAward = lotteryAwardService.selectSingleLotteryAwardByEntity(la);
        if(Constant.LotteryAwardState.HAVE_Receive.codeName.equals(lotteryAward.getState())){
            return ReturnResult.buildEnumResult(Constant.StateCode.LOTTERY_HAVE_RECEIVE);
        }
        String orderNo = payOrder.getOrderNo().toString();
        String type = orderNo.split("_")[2];
        String openId = "";
        //如果是99元套餐，从couponsPackBuyRecord里取openId，否则在WeChatPayOrder里取
        if(type.equals(PayCommonUtil.TYPE_OF_EMKT_COUPONSPACK)){
            openId = payOrder.getWxOpenId();
        }else{
            WeChatPayOrder weChatPayOrder = new WeChatPayOrder();
            weChatPayOrder.setTradeNo(orderNo);
            openId = weChatPayOrderService.selectWeChatPayOrderByWeChatOrderInfo(weChatPayOrder).getOpenId().toString();
        }
        param.put("openid", openId);
        param.put("carnum", opts.get("carNumber"));
        param.put("mobile", opts.get("mobile"));
        param.put("cpuuid", uuid[Arrays.asList(PRIZE).indexOf(lotteryAward.getRewordName())]);
        param.put("nums", 1);
        ReturnResult returnResult = PostUtil.postEMKT(Constant.Emkt.INSERT_COUPON_USE.codeName, param);
        if(returnResult.getCode().equals(Constant.StateCode.SUCCESS.codeName)){
            lotteryAward.setState(Constant.LotteryAwardState.HAVE_Receive.codeName);
            lotteryAward.setWxOpenId(openId);
            lotteryAwardService.updateLotteryAward(lotteryAward);
        }
        return returnResult;
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-FOLD>


    /*=================================== open (开放) end ==================================*/
    //</editor-fold>
}