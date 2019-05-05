package com.software5000.ma.controller;

/**
 * Created by jiye on 2017/8/22.
 */

import com.software5000.ma.entity.PackClusterBuyRecord;
import com.software5000.ma.entity.User;
import com.software5000.ma.service.PackClusterBuyRecordService;
import com.software5000.ma.service.UserService;
import com.software5000.bank.entity.PayOrder;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Map;

@Controller
public class PackClusterBuyRecordController {
    private Log log = LogFactory.getLog(PackClusterBuyRecordController.class);

    @Resource
    private PackClusterBuyRecordService packClusterBuyRecordService;

    @Resource
    private UserService userService;

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

    /**
     * 支付购买拼团增加参团记录信息
     *
     * @param packClusterBuyRecord
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/packClusterBuyRecord/insertPackClusterBuyRecordByPay", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertPackClusterBuyRecord(@RequestBody PackClusterBuyRecord packClusterBuyRecord) throws SQLException {
        PayOrder payOrder = packClusterBuyRecordService.insertPackClusterBuyRecordByPay(packClusterBuyRecord);
        return ReturnResult.buildSuccessMsg().setData(Constant.getStringCodeValueByName(Constant.CashierDesk.CASHIER_DESK_URL_JSAPI) + "?payOrderId=" + payOrder.getId());
    }

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
     * 查询是否有参团记录信息
     *
     * @param map
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/packClusterBuyRecord/selectHaveBuyPackCluster", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectHaveBuyPackCluster(@RequestBody Map map) throws SQLException {
        ReturnResult returnResult = packClusterBuyRecordService.selectHaveBuyPackCluster(map);
        if (returnResult.getCode().equals(Constant.StateCode.SUCCESS.codeName)) {
            String carNumber = map.getOrDefault("carNumber", "").toString();
            String mobile = map.getOrDefault("mobile", "").toString();
            if (ValidUtil.isNotEmpty(carNumber)) {
                User user = userService.selectUserByCarNo(carNumber);
                if (user != null && ValidUtil.isNotEmpty(user.getMobile()) && !mobile.equals(user.getMobile())) {
                    returnResult = new ReturnResult(Constant.StateCode.SUCCESS.codeName, null, user.getMobile());
                }
            }
        }
        return returnResult;
    }

    /**
     * 用户中心分页查询我参与的拼团
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/open/packCluster/selectPagePackClusterBuyRecordByOpenId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPagePackClusterBuyRecordByOpenId(@RequestBody Map param) throws SQLException {
        if (ValidUtil.isEmpty(param.get("openId"))) return ReturnResult.buildEnumResult(Constant.StateCode.NO_USER);
        return ReturnResult.buildSuccessMsg().setData(packClusterBuyRecordService.selectPagePackClusterBuyRecordByOpenId(param));
    }

    /**
     * 根据购买吃参团id查询参团的活动信息
     *
     * @return
     */
    @RequestMapping(value = "/open/packClusterBuyRecord/selectPackClusterPeron", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPackClusterPerDto(@RequestBody Map<String, Integer> paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(packClusterBuyRecordService.selectPackClusterPeron(paramMap.get("buyRecordId")));
    }

    /**
     * 用户中心分页查询我参与的拼团
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/open/packCluster/selectPagePackClusterBuyRecordByPage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPagePackClusterBuyRecordByPage(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(packClusterBuyRecordService.selectPagePackClusterBuyRecordByPage(param));
    }

    /**
     * 用户中心查询拼团详情
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/packCluster/selectPackClusterBuyRecordDetail", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPackClusterBuyRecordDetail(@RequestBody Map param) throws SQLException {
        if (ValidUtil.isEmpty(param.get("packClusterBuyRecordId")))
            return ReturnResult.buildEnumResult(Constant.StateCode.SELECT_ERROR);
        if (ValidUtil.isEmpty(param.get("openId")))
            return ReturnResult.buildEnumResult(Constant.StateCode.NO_USER);
        return ReturnResult.buildSuccessMsg().setData(packClusterBuyRecordService.selectPackClusterBuyRecordDetail(param));
    }

    /**
     * 统计用户不同拼团状态下记录的数量
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/packClusterBuyRecord/selectPackClusterBuyRecordCount", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPackClusterBuyRecordCount(@RequestBody Map param) throws SQLException {
        if (ValidUtil.isEmpty(param.get("openId")))
            return ReturnResult.buildEnumResult(Constant.StateCode.NO_USER);
        return ReturnResult.buildSuccessMsg().setData(packClusterBuyRecordService.selectPackClusterBuyRecordCountForState(param.get("openId").toString()));
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}