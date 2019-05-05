package com.software5000.ma.controller;

/**
 * Created by Administrator on 2017/7/19.
 */

import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultUtil;
import com.software5000.ma.entity.Car;
import com.software5000.ma.entity.MemberLvlRecord;
import com.software5000.ma.entity.User;
import com.software5000.ma.entity.WorkOrder;
import com.software5000.ma.service.*;
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
import java.util.List;
import java.util.Map;

@Controller
public class MemberController {
    private Log log = LogFactory.getLog(MemberController.class);

    @Resource
    private MemberLvlRecordService memberLvlRecordService;

    @Resource
    private UserService userService;

    @Resource
    private CarService carService;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    @Resource
    private WorkOrderService workOrderService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 会员详情页面—根据用户id添加用户车辆信息
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/member/insertCarNumberByParam", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertCarNumberByParam(@RequestBody Map paramMap) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        List<Car> list = carService.selectCarByCarNumber(paramMap);
        if (list.size() > 0) {
            //如果车辆没有绑定手机号或微信，则车辆可以再次绑定
            Car car = list.get(0);
            User user = userService.selectUserById(car.getUserId());
            if (ValidUtil.isEmpty(user.getMobile()) && ValidUtil.isEmpty(user.getWxOpenId())) {
                memberLvlRecordService.deleteMemberLvlRecord(car.getUserId());
                car.setUserId(Integer.valueOf(paramMap.get("userId").toString()));
                carService.updateCarChangeUserId(car);
                return returnResult.setData(carService.selectCarById(car.getId()));
            }
            //如果车牌已被绑定，则提示车牌已被绑定
            return returnResult.buildEnumResult(Constant.StateCode.CAR_HAVE_BIND);
        }
        return returnResult.setData(carService.insertCarNumberByParam(paramMap));
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 会员详情页面—修改会员信息
     *
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/home/member/updateMemberInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateMemberInfo(@RequestBody Map paramMap) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        User user = null;
        Integer userId = null;
        if (!ValidUtil.isEmpty(paramMap.get("userId"))) {
            userId = Integer.valueOf(paramMap.get("userId").toString());
            user = userService.selectUserById(userId);
            Car car = userService.selectCarInfo(userId).get(0);
            user = userService.insertUser(user.getWxOpenId(), paramMap.getOrDefault("mobile","").toString(), car.getCarNumber());
            if(user != null) {
                if (!ValidUtil.isEmpty(paramMap.get("realName"))) user.setRealName(paramMap.get("realName").toString());
                userService.updateUser(user);
            }
        }
            MemberLvlRecord memberLvlRecord = new MemberLvlRecord();
            if (!ValidUtil.isEmpty(paramMap.get("memberLvlRecordId"))) {
                memberLvlRecord.setId(Integer.valueOf(paramMap.get("memberLvlRecordId").toString()));
                if (!ValidUtil.isEmpty(paramMap.get("memberLvlId")))
                    memberLvlRecord.setMemberLvlId(Integer.valueOf(paramMap.get("memberLvlId").toString()));
                if (ValidUtil.isEmpty(paramMap.get("remarks"))) {
                    memberLvlRecord.setRemarks("");
                } else {
                    memberLvlRecord.setRemarks(paramMap.get("remarks").toString());
                }
                memberLvlRecordService.updateMemberLvlRecord(memberLvlRecord);
            }
        return returnResult;
    }

    /**
     * 会员详情页面—车辆解绑-删除该车辆
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/member/updateCarStateByParam", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateCarNumberByParam(@RequestBody Map paramMap) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        List<WorkOrder> workOrders = workOrderService.selectWorkOrderByCarNumber(paramMap);
        if (workOrders.size() > 0) {
            for (WorkOrder workOrder : workOrders) {
                if (!Constant.WorkOrderState.COMPLETE.codeName.equals(workOrder.getState())) {
                    return returnResult.buildEnumResult(Constant.StateCode.CAR_DELETE_ERROR);
                }
            }
        }
        Car car = new Car();
        car.setCarNumber(paramMap.get("carNumber").toString());
        carService.deleteCar(car);
        return returnResult;
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 商家查询拥有的会员资料
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/member/selectMemberInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMemberInfo(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(memberLvlRecordService.selectByPage(paramMap));
    }

    /**
     * 会员详情界面—通过会员等级id查询会员信息、车辆及会员等级
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/member/selectMemberDetailInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMemberDetailInfo(@RequestBody Map<String, Object> paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(memberLvlRecordService.selectMemberLvlRecordById(paramMap));
    }

    /**
     * 会员详情页面—查询手机号是否已存在
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/member/selectMemberInfoByMobile", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMemberInfoByMobile(@RequestBody Map paramMap) throws SQLException {
        if (!ValidUtil.isEmpty(userService.selectUserByMobile(paramMap.get("mobile").toString())))
            return ReturnResult.buildEnumResult(Constant.StateCode.MOBILE_HAVE_EXIST);
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 查询用户购买的套餐卡
     */
    /**
     * 查询套餐卡列表
     */
    @RequestMapping(value = "/home/member/selectMemberBusinessPackage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMemberBusinessPackage(@RequestBody Map param) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        param.put("isNoValid", false);
        param.put("businessId", UserDefaultUtil.getBusinessId());
        returnResult.setData(memberPackageRecordService.selectMemberPackageRecord(param));
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
    
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}