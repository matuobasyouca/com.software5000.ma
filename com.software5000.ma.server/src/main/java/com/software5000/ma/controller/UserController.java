package com.software5000.ma.controller;

/**
 * Created by Jiang on 2017/06/30.
 */

import com.software5000.ma.entity.Car;
import com.software5000.ma.entity.User;
import com.software5000.ma.service.CarService;
import com.software5000.ma.service.UserService;
import com.software5000.ma.service.WorkOrderService;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    private Log log = LogFactory.getLog(UserController.class);

    @Resource
    private UserService userService;

    @Resource
    private CarService carService;

    @Resource
    private WorkOrderService workOrderService;

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

    /**
     * 检查车辆是否可以绑定
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/member/selectCarNumber", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCarNumber(@RequestBody Map paramMap) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        String carNumber = paramMap.get("carNumber").toString();
        String mobile = paramMap.get("mobile").toString();
        if (!userService.checkUser(mobile, carNumber)) {
            return returnResult.buildEnumResult(Constant.StateCode.CAR_HAVE_BIND);
        }
        return returnResult;
    }

    /**
     * 查询商家会员列表
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/member/selectBusinessUserByParam", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessUserByParam(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(userService.selectBusinessUserByParam(param));
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
     * 新增用户及商家客户
     *
     * @param param wxopenId,mobile,carNumber
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/member/insertUserAndMemberLvl", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertUserAndMemberLvl(@RequestBody Map param) throws SQLException {
        Object businessId = param.get("businessId");
        User user = userService.insertUserAndMemberLvl(param.get("wxopenId").toString(), param.get("mobile").toString(), param.get("carNumber").toString(), businessId == null ? null : (Integer) businessId);
        if (user == null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.INSERT_USER_ER);
        }
        return ReturnResult.buildSuccessMsg().setData(user.getId());
    }

    /**
     * 新增用户及商家客户
     *
     * @param param wxopenId,mobile,carNumber,businessId
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/member/insertUserAndMemberLvlApi", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertUserAndMemberLvlApi(@RequestBody Map param) throws SQLException {
        User user = userService.insertUserAndMemberLvl(param.get("wxOpenId").toString(), param.get("mobile").toString(), param.get("carNumber").toString(), param.get("businessId") == null ? null : Integer.valueOf(param.get("businessId").toString()));
        if (user == null) {
            user = userService.selectUserByCarNo(param.get("carNumber").toString());
        }
        return ReturnResult.buildSuccessMsg().setData(user);
    }


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

    /**
     * 根据车牌号查询用户
     *
     * @param param mobile,carNumber
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/member/selectUserByCarNoApi", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectUserByCarNoApi(@RequestBody Map param) throws SQLException {
        User user = userService.selectUserByCarNo(param.get("carNumber").toString());
        if (user != null && ValidUtil.isNotEmpty(user.getMobile()) && !param.get("mobile").toString().equals(user.getMobile())) {
            return new ReturnResult(Constant.StateCode.SUCCESS.codeName, null, user.getMobile());
        }
        return ReturnResult.buildSuccessMsg();
    }
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
     * 新增用户车辆
     *
     * @param param
     * @return
     * @throws SQLException
     * @throws ServiceException
     */
    @RequestMapping(value = {"/open/user/insertUserCar", "/open/user/insertUserAndCar"}, method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertUserCar(@RequestBody Map param) throws SQLException, ServiceException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        String carNum = null == param.get("carNumber") ? param.get("carNum").toString() : param.get("carNumber").toString();
        //判断是否有用户信息
        String mobile = null, wxOpenId = null;
        User user = null;
        if (!ValidUtil.isEmpty(param.get("mobile"))) {
            mobile = param.get("mobile").toString();
            user = userService.selectUserByMobile(mobile);
        }
        User user1 = null;
        if (!ValidUtil.isEmpty(param.get("wxOpenId"))) {
            wxOpenId = param.get("wxOpenId").toString();
            user1 = userService.selectUserByOpenId(wxOpenId);
        }
        if ((user != null && carService.selectUserCarById(user.getId()).size() >= Constant.getIntegerCodeValueByName(Constant.SettingValue.CAR_BIND_MAX_NUM)) || (user1 != null && carService.selectUserCarById(user1.getId()).size() >= Constant.getIntegerCodeValueByName(Constant.SettingValue.CAR_BIND_MAX_NUM))) {
            returnResult = ReturnResult.buildResult(Constant.SettingValue.CAR_BIND_MAX_NUM);
        } else {
            user = userService.insertUser(wxOpenId, mobile, carNum);
            if (user == null) {
                returnResult = ReturnResult.buildEnumResult(Constant.StateCode.CAR_HAVE_BIND);
            } else {
                List<Car> carNumber = carService.selectCarByCarNumber(new HashMap() {{
                    put("carNumber", carNum);
                }});
                if (carNumber != null && carNumber.size() > 0) {
                    returnResult.setData(carNumber.get(0));
                }
            }
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 删除用户车辆
     */
    @RequestMapping(value = "/open/user/deleteUserCar", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteUserCar(@RequestBody Map paramMap) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        //判断是否在工单内：状态为排队、施工、待付款
        if (workOrderService.selectNoCompleteWorkOrderByCarNumber(paramMap).size() > 0) {
            returnResult = ReturnResult.buildEnumResult(Constant.StateCode.CAR_DELETE_ERROR);
        } else {
            Car car = new Car();
            car.setId(Integer.parseInt(paramMap.get("id").toString()));
            carService.deleteCar(car);
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="open (开放)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据openId获取用户信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/open/user/selectUserByOpenId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectUserByOpenId(@RequestBody Map param) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        returnResult.setData(userService.selectUserByOpenId(param.get("wxOpenId").toString()));
        return returnResult;
    }

    /**
     * 通过openId查询车辆信息
     *
     * @param param
     * @return
     */
    @RequestMapping(value = "/open/user/selectCarListByOpenId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCarListByOpenId(@RequestBody Map param) throws SQLException, ServiceException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        User user = userService.selectUserByOpenId(param.get("wxOpenId").toString());
        if (user != null) {
            returnResult.setData(carService.selectUserCarById(user.getId()));
        }
        return returnResult;
    }

    /**
     * 根据openId查询车辆数量
     *
     * @param param
     * @return
     * @throws SQLException
     * @throws ServiceException
     */
    @RequestMapping(value = "/open/user/selectCarCountByOpenId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCarCountByOpenId(@RequestBody Map param) throws SQLException, ServiceException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        User user = userService.selectUserByOpenId(param.get("wxOpenId").toString());
        if (user != null) {
            returnResult.setData(carService.selectUserCarById(user.getId()).size());
        }
        return returnResult;
    }

    @RequestMapping(value = "/open/user/insertUserByOpen", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertUserByOpen(@RequestBody Map<String, String> param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(userService.insertUser(param.get("wxOpenId"), param.get("mobile"), param.get("carNumber")));
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}