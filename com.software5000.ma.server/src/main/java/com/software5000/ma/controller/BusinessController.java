package com.software5000.ma.controller;

/**
 * Created by jiye on 2017/7/20.
 */

import com.software5000.ma.dto.MaBusinessDto;
import com.software5000.ma.entity.Business;
import com.software5000.ma.entity.BusinessUser;
import com.software5000.ma.entity.MemberLvl;
import com.software5000.ma.entity.User;
import com.software5000.ma.service.BusinessService;
import com.software5000.ma.service.MemberLvlService;
import com.software5000.ma.service.UserService;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.PasswordEncryption;
import com.software5000.base.security.UserDefaultUtil;
import com.zscp.master.util.ValidUtil;
import com.software5000.util.ImageUploadUtil;
import com.software5000.util.PostUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;

@Controller
public class BusinessController {
    private Log log = LogFactory.getLog(BusinessController.class);

    @Resource
    private BusinessService businessService;

    @Resource
    private MemberLvlService memberLvlService;

    @Resource
    private UserService userService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增员工
     *
     * @param businessUser
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/insertBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertBusinessUser(@RequestBody BusinessUser businessUser) throws SQLException {
        BusinessUser busUser = businessService.selectBusinessUserByMobile(businessUser.getMobile());
        if (busUser != null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MOBILE_HAVE_EXIST);
        }
        //如果没有用户名则将手机号作为用户名
        if (ValidUtil.isEmpty(businessUser.getUserName())) {
            businessUser.setUserName(businessUser.getMobile());
        }
        //判断用户名是否存在
        BusinessUser bu = businessService.selectBusinessUserByUserName(businessUser.getUserName());
        if (bu != null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.BUSINESS_ACCOUNT_EXISTS);
        }
        businessUser.setPwd(PasswordEncryption.toPasswd(businessUser.getPwd()));
        businessUser.setState(Constant.BusinessUserState.ON_WORK.codeName);
        if (businessUser.getBusinessId() == null) {
            businessUser.setBusinessId(UserDefaultUtil.getBusinessId());
        }
        businessService.insertBusinessUser(businessUser);
        return ReturnResult.buildSuccessMsg();
    }


    /**
     * 增加商家信息
     *
     * @param business
     * @return
     */
    @RequestMapping(value = "/home/business/insertBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertBusiness(@RequestBody Business business) throws SQLException, ServiceException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        //项目路径+WorkOrderImg+fileName(图片名字是时间戳+随机数)
        String rootPath = Constant.getStringCodeValueByName(Constant.ImagePath.IMAGE_SAVEROOT_PATH.codeName);
        String path = Constant.getStringCodeValueByName(Constant.ImagePath.MERCHANT_LOGO_IMG.codeName);
        String link = ImageUploadUtil.uploadImage(business.getImgPath(), path, rootPath);
        business.setImgPath(link);
        business.setBusinessEnable(Constant.BusinessState.VALID_BUSINESS.codeName);
        returnResult.setData(businessService.insertBusiness(business));
        return returnResult;
    }


    /**
     * 新增商家会员等级
     *
     * @param memberLvl
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/insertMemberLvl", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertMemberLvl(@RequestBody MemberLvl memberLvl) throws SQLException {

        //校验名称唯一性
        MemberLvl searchLvl = new MemberLvl();
        searchLvl.setBusinessId(UserDefaultUtil.getBusinessId());
        searchLvl.setLvlName(memberLvl.getLvlName());
        searchLvl = memberLvlService.selectMemberLvl(searchLvl);
        if (searchLvl != null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MEMBERLVL_NAME_EXIST);
        }

        //校验充值金额唯一性
        searchLvl = new MemberLvl();
        searchLvl.setBusinessId(UserDefaultUtil.getBusinessId());
        searchLvl.setRechargeMoney(memberLvl.getRechargeMoney());
        searchLvl = memberLvlService.selectMemberLvl(searchLvl);
        if (searchLvl != null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MEMBERLVL_SET_MONEY_EXIST);
        }

        //设置商家ID
        memberLvl.setBusinessId(UserDefaultUtil.getBusinessId());
        memberLvl.setState(Constant.MemberLvlState.ENABLE.codeName);
        return ReturnResult.buildSuccessMsg().setData(memberLvlService.insertMemberLvl(memberLvl));
    }

    /**
     * 设置商家的门店顾问
     *
     * @param maBusinessDto
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/home/business/insertMaBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertMaBusiness(@RequestBody MaBusinessDto maBusinessDto) throws IOException, SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        String[] businessIds = maBusinessDto.getId().toString().split(",");
        for (int i = 0; i < businessIds.length; i++) {
            Integer id = Integer.valueOf(businessIds[i]);
            Business business = new Business();
            business.setId(id);
            business.setConsultantId(maBusinessDto.getEmployeeId());
            business.setConsultantName(maBusinessDto.getSaleName());
            business.setConsultantDepartment(maBusinessDto.getDepartmentName());
            business.setConsultantDepartmentId(maBusinessDto.getDepartmentId());
            try {
                businessService.updateBusiness(business);
            } catch (SQLException e) {
                log.error("设置门店顾问出错", e);
                returnResult = ReturnResult.buildEnumResult(Constant.StateCode.ERROR);
            }
        }
        return returnResult;
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
     * 更新员工
     *
     * @param businessUser
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/updateBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessUser(@RequestBody BusinessUser businessUser) throws SQLException {
        BusinessUser busUser = businessService.selectBusinessUserByMobile(businessUser.getMobile());
        if (busUser != null && !busUser.getId().equals(businessUser.getId())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MOBILE_HAVE_EXIST);
        }
        if (ValidUtil.isEmpty(businessUser.getUserName())) {
            businessUser.setUserName(businessUser.getMobile());
        }
        //判断用户名是否存在
        BusinessUser bu = businessService.selectBusinessUserByUserName(businessUser.getUserName());
        if (bu != null && !bu.getId().equals(businessUser.getId())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.BUSINESS_ACCOUNT_EXISTS);
        }
        if (ValidUtil.isNotEmpty(businessUser.getPwd())) {
            businessUser.setPwd(PasswordEncryption.toPasswd(businessUser.getPwd()));
        }
        businessService.updateBusinessUser(businessUser);
        return ReturnResult.buildSuccessMsg();
    }


    /**
     * 更新商家
     *
     * @param business
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/updateBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusiness(@RequestBody Business business) throws SQLException, ServiceException {
        businessService.updateBusinessNotEmpty(business);
        return ReturnResult.buildSuccessMsg();
    }


    /**
     * 更新默认车牌
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/updateBusinessDefaultCar", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessDefaultCar(@RequestBody Map param) throws SQLException {
        Business business = new Business();
        business.setId(UserDefaultUtil.getBusinessId());
        business.setDefaultCar(param.get("defaultCar").toString());
        businessService.updateBusiness(business);
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 修改
     *
     * @param memberLvl
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/updateMemberLvl", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateMemberLvl(@RequestBody MemberLvl memberLvl) throws SQLException {

        //校验名称唯一性
        MemberLvl searchLvl = new MemberLvl();
        searchLvl.setBusinessId(UserDefaultUtil.getBusinessId());
        searchLvl.setLvlName(memberLvl.getLvlName());
        searchLvl = memberLvlService.selectMemberLvl(searchLvl);
        if (searchLvl != null && !searchLvl.getId().equals(memberLvl.getId())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MEMBERLVL_NAME_EXIST);
        }

        //校验充值金额唯一性
        searchLvl = new MemberLvl();
        searchLvl.setBusinessId(UserDefaultUtil.getBusinessId());
        searchLvl.setRechargeMoney(memberLvl.getRechargeMoney());
        searchLvl = memberLvlService.selectMemberLvl(searchLvl);
        if (searchLvl != null && !searchLvl.getId().equals(memberLvl.getId())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MEMBERLVL_SET_MONEY_EXIST);
        }
        memberLvlService.updateMemberLvl(memberLvl);
        return ReturnResult.buildSuccessMsg();
    }

    /**
     * 更新商家用户的openid如果是boss则将微信号更新到商家上
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/updateUserOpenId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateUserOpenId(@RequestBody Map param) throws SQLException {
        BusinessUser businessUser = UserDefaultUtil.getUser().getBusinessUser();
        String openId = param.get("openId").toString();
        BusinessUser update = new BusinessUser();
        update.setId(businessUser.getId());
        update.setWxOpenId(openId);
        businessService.updateBusinessUser(update);
        if (Constant.BusinessUserBossType.IS_BOSS.codeName.equals(businessUser.getBossType())) {
            Business realEntity = (Business) UserDefaultUtil.getUser().getRealEntity();
            if (ValidUtil.isEmpty(realEntity.getWxOpenId())) {
                Business business = new Business();
                business.setId(realEntity.getId());
                business.setWxOpenId(openId);
                businessService.updateBusiness(business);
            }
        }
        return ReturnResult.buildEnumResult(Constant.StateCode.SUCCESS);
    }

    /**
     * 修改商家的顾问
     *
     * @param maBusinessDto
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/updateMaBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateMaBusiness(@RequestBody MaBusinessDto maBusinessDto) throws IOException, SQLException {
        Business business = new Business();
        business.setId(maBusinessDto.getBusinessId());
        business.setConsultantId(maBusinessDto.getEmployeeId());
        business.setConsultantName(maBusinessDto.getSaleName());
        business.setConsultantDepartment(maBusinessDto.getDepartmentName());
        business.setConsultantDepartmentId(maBusinessDto.getDepartmentId());
        businessService.updateBusiness(business);
        return ReturnResult.buildSuccessMsg();
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据商家ID获取商家信息
     *
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/selectBusinessInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessInfo(@RequestBody(required = false) Map<String, Integer> param) throws SQLException {
        Integer id = param.getOrDefault("id", UserDefaultUtil.getBusinessId());
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessById(id));
    }

    /**
     * 获取商家用户的信息
     *
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/selectBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessUser(@RequestBody(required = false) Map<String, Integer> param) throws SQLException {
        BusinessUser businessUser = new BusinessUser();
        businessUser.setId(param.get("id") == null ? UserDefaultUtil.getUserId() : param.get("id"));
        businessUser = businessService.selectBusinessUserByEntity(businessUser);
        businessUser.setBusiness(businessService.selectBusinessById(businessUser.getBusinessId()));
        return ReturnResult.buildSuccessMsg().setData(businessUser);
    }

    /**
     * 分页查询员工数据
     *
     * @param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/selectPageOperatorBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPageOperatorBusinessUser(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(businessService.selectPageOperatorBusinessUser(param));
    }

    /**
     * 根据商家id查找老板账号
     *
     * @return
     */
    @RequestMapping(value = "/home/business/selectBossBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBossBusinessUser(@RequestBody(required = false) Map<String, Integer> param) {
        Integer businessId = param.getOrDefault("businessId", UserDefaultUtil.getBusinessId());
        BusinessUser businessUser = new BusinessUser();
        businessUser.setBusinessId(businessId);
        businessUser.setBossType(Constant.BusinessUserBossType.IS_BOSS.codeName);
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessUserByEntity(businessUser));
    }

    /**
     * 查看所有联盟商家分页信息-根据条件
     *
     * @return
     */
    @RequestMapping(value = "/home/business/selectBusinessListPage")
    @ResponseBody
    public ReturnResult selectBusinessListPage(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessInfoPage(paramMap));
    }


    /**
     * 获取商家会员等级列表
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/business/selectMemberLvlList", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMemberLvlList(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(memberLvlService.selectMemberLvlPage(param));
    }

    /**
     * 联盟商家查看门店销售顾问
     *
     * @param maBusinessDto
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/home/business/selectMaBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMaBusiness(@RequestBody MaBusinessDto maBusinessDto) throws IOException {
        return PostUtil.postCRM(Constant.Crm.SELECT_MABUSINESS.codeName, maBusinessDto);
    }

    /**
     * 根据条件查询员工
     *
     * @param param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/home/business/selectCrmEmployees", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCrmEmployees(@RequestBody Map param) throws IOException {
        return PostUtil.postCRM(Constant.Crm.SELECT_EMPLOYEES.codeName, param);
    }

    /**
     * 查询所有销售门店
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/home/business/selectCrmMatchDepartments", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCrmMatchDepartments() throws IOException {
        return PostUtil.postCRM(Constant.Crm.SELECT_MATCH_DEPARTMENTS.codeName, null);
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
     * 新增员工forapi
     *
     * @param businessUser
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/business/insertBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertBusinessUserForApi(@RequestBody BusinessUser businessUser) throws SQLException {
        BusinessUser busUser = businessService.selectBusinessUserByMobile(businessUser.getMobile());
        if (busUser != null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MOBILE_HAVE_EXIST);
        }
        //如果没有用户名则将手机号作为用户名
        if (ValidUtil.isEmpty(businessUser.getUserName())) {
            businessUser.setUserName(businessUser.getMobile());
        }
        //判断用户名是否存在
        BusinessUser bu = businessService.selectBusinessUserByUserName(businessUser.getUserName());
        if (bu != null) {
            return ReturnResult.buildEnumResult(Constant.StateCode.BUSINESS_ACCOUNT_EXISTS);
        }
        businessUser.setPwd(PasswordEncryption.toPasswd(businessUser.getPwd()));
        businessUser.setState(Constant.BusinessUserState.ON_WORK.codeName);
        if (businessUser.getBusinessId() == null) {
            businessUser.setBusinessId(UserDefaultUtil.getBusinessId());
        }
        businessService.insertBusinessUser(businessUser);
        return ReturnResult.buildSuccessMsg();
    }
    /**
     * 设置商家的门店顾问FORAPI
     *
     * @param param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/api/business/insertMaBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertMaBusinessForApi(@RequestBody Map param) throws IOException, SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        String[] businessIds = param.get("businessIds").toString().split(",");
        for (int i = 0; i < businessIds.length; i++) {
            Integer id = Integer.valueOf(businessIds[i]);
            Business business = new Business();
            business.setId(id);
            business.setConsultantId(Integer.valueOf(param.get("employeeId").toString()));
            business.setConsultantName(param.get("saleName").toString());
            business.setConsultantDepartment(param.get("departmentName").toString());
            business.setConsultantDepartmentId(Integer.valueOf(param.get("departmentId").toString()));
            try {
                businessService.updateBusiness(business);
                returnResult=ReturnResult.buildEnumResult(Constant.StateCode.SUCCESS);
            } catch (SQLException e) {
                log.error("设置门店顾问出错", e);
                returnResult = ReturnResult.buildEnumResult(Constant.StateCode.ERROR);
            }
        }
        return returnResult;
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
     * 更新员工forapi
     *
     * @param businessUser
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/business/updateBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessUserForApi(@RequestBody BusinessUser businessUser) throws SQLException {
        BusinessUser busUser = businessService.selectBusinessUserByMobile(businessUser.getMobile());
        if (busUser != null && !busUser.getId().equals(businessUser.getId())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.MOBILE_HAVE_EXIST);
        }
        if (ValidUtil.isEmpty(businessUser.getUserName())) {
            businessUser.setUserName(businessUser.getMobile());
        }
        //判断用户名是否存在
        BusinessUser bu = businessService.selectBusinessUserByUserName(businessUser.getUserName());
        if (bu != null && !bu.getId().equals(businessUser.getId())) {
            return ReturnResult.buildEnumResult(Constant.StateCode.BUSINESS_ACCOUNT_EXISTS);
        }
        if (ValidUtil.isNotEmpty(businessUser.getPwd())) {
            businessUser.setPwd(PasswordEncryption.toPasswd(businessUser.getPwd()));
        }
        businessService.updateBusinessUser(businessUser);
        return ReturnResult.buildSuccessMsg();
    }
    /**
     * 更新商家forapi
     *
     * @param business
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/business/updateBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessForAPI(@RequestBody Business business) throws SQLException, ServiceException {
        businessService.updateBusinessNotEmpty(business);
        return ReturnResult.buildSuccessMsg();
    }
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="api (对外)->select (查)">

    /**
     * 根据商家ID获取商家信息FORAPI
     *
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/business/selectBusinessInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessInfoForApi(@RequestBody Map<String, Integer> param) throws SQLException {
        Integer id = Integer.valueOf(param.get("id").toString());
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessById(id));
    }
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    /**
     * 查看所有联盟商家分页信息-根据条件FORAPI
     *
     * @return
     */
    @RequestMapping(value = "/api/business/selectBusinessListPage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessListPageForApi(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessInfoPage(paramMap));
    }
    /**
     * 获取商家用户的信息forapi
     *
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/business/selectBusinessUser", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessUserForApi(@RequestBody(required = false) Map<String, Integer> param) throws SQLException {
        BusinessUser businessUser = new BusinessUser();
        businessUser.setId(param.get("id") == null ? UserDefaultUtil.getUserId() : param.get("id"));
        businessUser = businessService.selectBusinessUserByEntity(businessUser);
        businessUser.setBusiness(businessService.selectBusinessById(businessUser.getBusinessId()));
        return ReturnResult.buildSuccessMsg().setData(businessUser);
    }
    /**
     * 根据条件查询员工FORAPI
     *
     * @param param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/api/business/selectMaEmployees", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCrmEmployeesForApi(@RequestBody Map param) throws IOException {
        return PostUtil.postCRM(Constant.Crm.SELECT_EMPLOYEES.codeName, param);
    }
    /**
     * 增加商家信息FORAPI
     *
     * @param business
     * @return
     */
    @RequestMapping(value = "/api/business/insertBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertBusinessForApi(@RequestBody Business business) throws SQLException, ServiceException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        //项目路径+WorkOrderImg+fileName(图片名字是时间戳+随机数)
        /*String rootPath = Constant.getStringCodeValueByName(Constant.ImagePath.IMAGE_SAVEROOT_PATH.codeName);
        String path = Constant.getStringCodeValueByName(Constant.ImagePath.MERCHANT_LOGO_IMG.codeName);
        String link = ImageUploadUtil.uploadImage(business.getImgPath(), path, rootPath);
        business.setImgPath(link);*/
        business.setBusinessEnable(Constant.BusinessState.VALID_BUSINESS.codeName);
        returnResult.setData(businessService.insertBusiness(business));
        return returnResult;
    }



    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    /**
     * 分页查询商家列表（我的商家，附近商家）FORAPI
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/business/selectBusinessPageForApi", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPageForApi(@RequestBody Map param) throws SQLException {

        //设置用户openId
        if (!ValidUtil.isEmpty(param.get("wxOpenId"))) {
            User user = userService.selectUserByOpenId(param.get("wxOpenId").toString());
            if (user != null) {
                param.put("userId", user.getId());
            }
        }

        //设置商家可用标志
        param.put("enable", Constant.BusinessState.VALID_BUSINESS.codeName);
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessPageForOpen(param));
    }
    /**
     * 查询所有销售门店
     *
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/api/business/selectMaMatchDepartments", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectCrmMatchDepartmentsForApi() throws IOException {
        return PostUtil.postCRM(Constant.Crm.SELECT_MATCH_DEPARTMENTS.codeName, null);
    }

    /**
     * 根据商家ids获取商家信息（多个）
     *
     * @return
     */
    @RequestMapping(value = "/api/business/selectEnableBusinessListByIds", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectEnableBusinessListByIds(@RequestBody Map paramMap) throws SQLException {
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        paramMap.put("enable", Constant.BusinessState.VALID_BUSINESS.codeName);
        returnResult.setData(businessService.selectEnableBusinessListByIds(paramMap));
        return returnResult;
    }
    /**
     * 根据商家账号查询对应的商家信息
     * @return
     */
    @RequestMapping(value = "/api/business/selectEnableBusByBusUserName", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectEnableBusByBusUserName(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(businessService.selectEnableBusByBusUserName(paramMap));
    }

    /**
     * 根据销售顾问账号查询对应的商家信息
     * @return
     */
    @RequestMapping(value = "/api/business/selectEnableBusByBusSaleId", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectEnableBusByBusSaleId(@RequestBody Map paramMap) throws SQLException {
        paramMap.get("saleId");
        Business business = new Business();
        business.setConsultantId(Integer.valueOf(paramMap.get("saleId").toString()));
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessByEntity(business));
    }

    /**
     * 获取商家列表
     * @param business
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/api/business/selectBusinessList", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessList(@RequestBody Business business) throws SQLException{
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessByEntity(business));
    }
    /**
     * 联盟商家查看门店销售顾问FORAPI
     *
     * @param param
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/api/business/selectMaBusiness", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectMaBusinessForApi(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessById(Integer.valueOf(param.get("businessId").toString())));
    }
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
     * 分页查询所有可用商家列表（用户端）
     *
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/business/selectBusinessPageForOpen", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPageForOpen(@RequestBody Map param) throws SQLException {

        //设置用户openId
        if (!ValidUtil.isEmpty(param.get("wxOpenId"))) {
            User user = userService.selectUserByOpenId(param.get("wxOpenId").toString());
            if (user != null) {
                param.put("userId", user.getId());
            }
        }

        //设置商家可用标志
        param.put("enable", Constant.BusinessState.VALID_BUSINESS.codeName);
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessPageForOpen(param));
    }

    /**
     * 获取商家信息（用户端）
     *
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/business/selectBusinessForOpen", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessForOpen(@RequestBody Map<String, Object> param) throws SQLException {
        //设置商家可用标志
        param.put("enable", Constant.BusinessState.VALID_BUSINESS.codeName);
        //设置用户openId
        if (!ValidUtil.isEmpty(param.get("wxOpenId"))) {
            User user = userService.selectUserByOpenId(param.get("wxOpenId").toString());
            if (user != null) {
                param.put("userId", user.getId());
            }
        }

        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessForOpen(param));
    }

    /**
     * 获取商家列表（EMKT）
     *
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/open/business/selectEnableBusinessList", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectEnableBusinessList(@RequestBody Map paramMap) throws SQLException {
        paramMap.put("enable", Constant.BusinessState.VALID_BUSINESS.codeName);
        return ReturnResult.buildSuccessMsg().setData(businessService.selectBusinessInfoPage(paramMap));
    }

    /**
     * 获取商家名称（EMKT）
     * @param paramMap
     * @return
     */
    @RequestMapping(value = "/open/business/selectEnableBusinessNameList", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectEnableBusinessNameList(@RequestBody Map paramMap) throws SQLException{
        return ReturnResult.buildSuccessMsg().setData(businessService.selectEnableBusinessNameList(paramMap));
    }





    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}