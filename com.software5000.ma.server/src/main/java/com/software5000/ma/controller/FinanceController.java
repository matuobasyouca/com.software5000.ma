package com.software5000.ma.controller;

/**
 * 财务模块控制类
 * Created by jiye on 2017/7/21.
 */

import com.alibaba.fastjson.JSON;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultUtil;
import com.software5000.ma.dto.MerchantWorkbenchDto;
import com.software5000.ma.dto.PackClusterBuyRecordDto;
import com.software5000.ma.entity.BusinessCheckMoney;
import com.software5000.ma.entity.Finance;
import com.software5000.ma.service.*;
import com.zscp.master.util.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class FinanceController {
    private Log log = LogFactory.getLog(FinanceController.class);


    @Resource
    private FinanceService financeService;

    @Resource
    private WorkOrderService workOrderService;

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private BusinessPackageOrderService businessPackageOrderService;

    @Resource
    private BusinessPackageService businessPackageService;

    @Resource
    private ServiceItemService serviceItemService;

    @Resource
    private WeChatPayOrderService weChatPayOrderService;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;
    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 商家提现
     *
     * @param moneyMap 传过来的数据单位为分
     * @return
     */
    @RequestMapping(value = "home/finance/insertDrawMoney", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertDrawMoney(@RequestBody Map<String, Integer> moneyMap, HttpServletRequest request) throws SQLException {
        Integer payTotalFee = moneyMap.get("money");
        return weChatPayOrderService.insertDrawMoney(payTotalFee, UserDefaultUtil.getBusinessId(), request);
    }

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

    /**
     * 查询总收入与总支出
     *
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "home/finance/selectFinanceInOrOutComeDto", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectFinanceInOrOutComeDto(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(financeService.selectFinanceInOrOutComeDto(paramMap));
    }

    /**
     * 查询可提现金额及已提现金额
     *
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "home/finance/selectBusinessCheckMoney", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWechatPayTotalPriceDto() throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(weChatPayOrderService.selectBusinessCheckMoney(UserDefaultUtil.getBusinessId()));
    }

    /**
     * 分页查询财务明细
     *
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "home/finance/selectPageFinance", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageFinance(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(financeService.selectPageFinance(paramMap));
    }

    /**
     * 分页查询微信提现明细
     *
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "home/finance/selectPageWechatPayOrder", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageWechatPayOrder(@RequestBody Map paramMap) throws SQLException {
        paramMap.put("orderType", Constant.WeChatPayOrderType.SETTLEMENT_ORDER_TYPE.codeName);
        return ReturnResult.buildSuccessMsg().setData(weChatPayOrderService.selectPageWechatPayOrder(paramMap));
    }


    /**
     * 查询可提现的基本信息(可提现最小金额，最大金额，微信昵称，是否已经提现过,开单数，单日营业额等)
     *
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "home/finance/selectCheckMoneyDto", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCheckMoneyDto() throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(weChatPayOrderService.selectCheckMoneyDto(UserDefaultUtil.getBusinessId()));
    }

    /**
     * 分页收支数据
     *
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = "home/finance/selectPaymentPage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageBuyRecord(@RequestBody Map pageInfo) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(weChatPayOrderService.selectPaymentPage(pageInfo));
    }

    /**
     * 统计收支数据
     *
     * @param pageInfo
     * @return
     */
    @RequestMapping(value = "home/finance/selectTotalSum", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectTotalSum(@RequestBody Map pageInfo) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(weChatPayOrderService.selectTotalSum(pageInfo));
    }

    /**
     * 查询有消费的商家
     *
     * @param
     * @return
     */
    @RequestMapping(value = "home/finance/selecPaymentBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selecPaymentBusiness() throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(weChatPayOrderService.selecPaymentBusiness());
    }

    /**
     * 商家登陆首页数据
     */
    @RequestMapping(value = "/home/finance/selectWorkbench", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectWorkOrderPage(@RequestBody Map param) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        param.put("businessId", UserDefaultUtil.getBusinessId());
        String yesDay = DateUtils.formatDate(DateUtils.addDay(new Date(), -1));
        String today = DateUtils.formatDate();
        MerchantWorkbenchDto merchantWorkbenchDto = new MerchantWorkbenchDto();
        merchantWorkbenchDto.setAllEvaluationNum(0);
        merchantWorkbenchDto.setGoodEvaluationNum(0);
        Integer packageCount = businessPackageService.selectBusinessPackageCount(param);
        Integer noPaidCount = businessPackageOrderService.selectNoPaidPackageOrderCount(param);
        Integer itemCount = serviceItemService.selectItemCount(param);
        Integer userCount = memberLvlRecordService.selectUserCount(param);
        Integer workNoPaidCount = workOrderService.selectWorkOrderNoPaidCount(param);
        merchantWorkbenchDto.setBuyPackNum(null == packageCount ? 0 : packageCount);
        merchantWorkbenchDto.setNoPayPackNum(null == noPaidCount ? 0 : noPaidCount);
        merchantWorkbenchDto.setItemNum(null == itemCount ? 0 : itemCount);
        merchantWorkbenchDto.setMemberNum(null == userCount ? 0 : userCount);
        merchantWorkbenchDto.setNoPayOrder(null == workNoPaidCount ? 0 : workNoPaidCount);
        param.put("createTimeStart", yesDay);
        param.put("createTimeEnd", today);
        Integer financeTd = financeService.selectFinanceByDate(param);
        merchantWorkbenchDto.setYeCount((double) (null == financeTd ? 0 : financeTd / 100.00));
        param.put("createTimeStart", today);
        param.remove("createTimeEnd");
        Integer financeYd = financeService.selectFinanceByDate(param);
        merchantWorkbenchDto.setTodayCount((double) (null == financeYd ? 0 : financeYd / 100.00));
        returnResult.setData(merchantWorkbenchDto);
        return returnResult;
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== home (对内) end ==================================*/
    //</editor-fold>

    //<editor-fold desc="api (对外) ">
    /*================================== api (对外) start ==================================*/

    //<editor-fold desc="api (对外)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 查询有消费的商家
     *
     * @param
     * @return
     */
    @RequestMapping(value = "api/finance/insertFinanceFromEmkt", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertFinanceFromEmkt(@RequestBody Finance finance) throws SQLException {
        finance.setFinanceType(Constant.FinanceType.MMGOODCAR_ORDER_TYPE.codeName);
        finance.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
        finance.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
        return ReturnResult.buildSuccessMsg().setData(financeService.insertFinanceRecordByOrder(finance, null));
    }

    /**
     * 记支出
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/home/finace/InsertFinaceOtherPay", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult InsertFinaceOtherPay(@RequestBody Map param) throws SQLException {
        Finance finance = new Finance();
        finance.setFinanceType(Constant.FinanceType.OTHERPAY_TYPE.codeName);
        finance.setInOrOutCome(Constant.InOrOutCome.OUT_COME.codeName);
        finance.setMoney(Integer.parseInt(param.get("money").toString()));
        finance.setExplainInfo(param.get("explainInfo").toString());
        finance.setPayType(Constant.WorkOrderPayType.CASH.codeName);
        finance.setBusinessId(UserDefaultUtil.getBusinessId());
        return ReturnResult.buildSuccessMsg().setData(financeService.InsertFinaceOtherPay(finance));
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * emkt支付后的回调
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/api/finace/updateBusinessCheckMoneyEmkt", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessCheckMoneyEmkt(@RequestBody Map param) throws SQLException {
        //加入微信提现
        Integer inMoney = Integer.valueOf(param.get("inMoney").toString());
        BusinessCheckMoney businessCheckMoney = weChatPayOrderService.selectBusinessCheckMoney(Integer.valueOf(param.get("id").toString()));
        inMoney += businessCheckMoney.getCanCheckMoney();
        businessCheckMoney.setCanCheckMoney(inMoney);
        weChatPayOrderService.updateBusinessCheckMoney(businessCheckMoney);
        List<Finance> finances = JSON.parseArray(param.get("finances").toString(), Finance.class);
        financeService.insertFinanceRecordList(finances);
        //发放套餐
        List<PackClusterBuyRecordDto> packClusterBuyRecords = JSON.parseArray(param.get("packClusterBuyRecords").toString(), PackClusterBuyRecordDto.class);
        memberPackageRecordService.insertMemberPackageRecordByPackCluster(packClusterBuyRecords, Integer.valueOf(param.get("packId").toString()));
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * crm线索费用
     */
    @RequestMapping(value = "/api/finace/updateBusinessCheckMoneyCrm", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessCheckMoneyCrm(@RequestBody Map param) throws SQLException{
        //加入微信提现
        Integer inMoney = Integer.valueOf(param.get("balanceMoney").toString());
        Integer businessId=Integer.valueOf(param.get("maBusinessId").toString());
        BusinessCheckMoney businessCheckMoney = weChatPayOrderService.selectBusinessCheckMoney(businessId);
        inMoney += businessCheckMoney.getCanCheckMoney();
        businessCheckMoney.setCanCheckMoney(inMoney);
        weChatPayOrderService.updateBusinessCheckMoney(businessCheckMoney);
        Finance finance = new Finance();
        finance.setBusinessId(businessId);
        if("1".equals(param.get("type").toString())){
            finance.setFinanceType(Constant.FinanceType.BUY_DEMAND.codeName);
            finance.setExplainInfo(Constant.FinanceType.BUY_DEMAND.colDesc+"/编号："+param.get("demandId").toString());
        }else{
            finance.setFinanceType(Constant.FinanceType.SELL_DEMAND.codeName);
            finance.setExplainInfo(Constant.FinanceType.SELL_DEMAND.colDesc+"/编号："+param.get("demandId").toString());
        }
        finance.setMoney(Integer.valueOf(param.get("balanceMoney").toString()));
        finance.setInOrOutCome(Constant.InOrOutCome.IN_COME.codeName);
        finance.setPayType(Constant.WorkOrderPayType.WEB_CHAT.codeName);
        financeService.insertFinanceRecord(finance);
        return ReturnResult.buildSuccessMsg();
    }

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
    
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}