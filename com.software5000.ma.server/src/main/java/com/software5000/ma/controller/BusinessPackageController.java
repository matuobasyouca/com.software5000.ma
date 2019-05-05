package com.software5000.ma.controller;

/**
 * Created by luo on 2017/7/22.
 */

import com.software5000.ma.entity.BusinessPackage;
import com.software5000.ma.entity.MemberPackageRecord;
import com.software5000.ma.service.BusinessPackageService;
import com.software5000.ma.service.MemberPackageRecordService;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultUtil;
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
public class BusinessPackageController {
    private Log log = LogFactory.getLog(BusinessPackageController.class);

    @Resource
    private BusinessPackageService businessPackageService;

    @Resource
    private MemberPackageRecordService memberPackageRecordService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增套餐卡
     */
    @RequestMapping(value = "/home/businessPackage/insertBusinessPackage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertBusinessPackage(@RequestBody BusinessPackage businessPackage) throws SQLException{
        Map param=new HashMap();
        param.put("packageName",businessPackage.getPackageName());
        List<BusinessPackage> busPacks=businessPackageService.selectBusinessPackageList(param);
        if(null!=busPacks&&busPacks.size()>0){
            return ReturnResult.buildEnumResult(Constant.StateCode.BUSINESSPACKAGENAME_HAVE_EXIST);
        }
        businessPackage.setBusinessId(UserDefaultUtil.getBusinessId());
        //套餐类型
        businessPackage.setPackageType(Constant.getIntegerCodeValueByName(Constant.PackageType.SALE_PACKAGE));
        //套餐状态，默认一开始新建的状态是下架中
        businessPackage.setPackageState(Constant.PackageState.SHELVE.codeName);
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try {
            returnResult.setData(businessPackageService.insertBusinessPackage(businessPackage));
        } catch (ServiceException e) {
            returnResult = ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 删除套餐卡
     */
    @RequestMapping(value = "/home/businessPackage/deleteBusinessPackage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deleteBusinessPackage(@RequestBody Map param) throws SQLException{
        BusinessPackage businessPackage=new BusinessPackage();
        businessPackage.setId(Integer.parseInt(param.get("businessPackageId").toString()));
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try{
            List<MemberPackageRecord> memberPackageRecords= memberPackageRecordService.selectMemberPackageRecord(param);
            if(null!=memberPackageRecords&&memberPackageRecords.size()>0) {
                businessPackage.setPackageState(Constant.PackageState.HIDE.codeName);
                businessPackageService.updateBusinessPackage(businessPackage);
            }else{
                businessPackageService.deleteBusinessPackage(businessPackage);
            }
        }catch (ServiceException e){
            returnResult = ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 修改套餐卡
     */
    @RequestMapping(value = "/home/businessPackage/updateBusinessPackage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessPackage(@RequestBody BusinessPackage businessPackage) throws SQLException{
        Map param=new HashMap();
        param.put("packageName",businessPackage.getPackageName());
        List<BusinessPackage> busPacks=businessPackageService.selectBusinessPackageList(param);
        if(null!=busPacks&&busPacks.size()>0&&!busPacks.get(0).getId().equals(businessPackage.getId())){
            return ReturnResult.buildEnumResult(Constant.StateCode.BUSINESSPACKAGENAME_HAVE_EXIST);
        }
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try{
            businessPackage.setBusinessId(UserDefaultUtil.getBusinessId());
            returnResult.setData(businessPackageService.updateBusinessPackage(businessPackage));
        }catch (ServiceException e){
            returnResult = ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /**
     * 修改套餐卡
     */
    @RequestMapping(value = "/home/businessPackage/updateBusinessPackageState", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updateBusinessPackageState(@RequestBody Map param) throws SQLException{
        BusinessPackage businessPackage=new BusinessPackage();
        businessPackage.setId(Integer.parseInt(param.get("id").toString()));
        businessPackage.setPackageState(Integer.parseInt(param.get("state").toString()));
        ReturnResult returnResult = ReturnResult.buildSuccessMsg();
        try{
            returnResult.setData(businessPackageService.updateBusinessPackage(businessPackage));
        }catch (ServiceException e){
            returnResult = ReturnResult.buildResult(e.getMessage());
        }
        return returnResult;
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 套餐卡列表
     */
    @RequestMapping(value = "/home/businessPackage/selectBusinessPackageByPage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPackageByPage(@RequestBody Map param) throws SQLException {
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        returnResult.setData(businessPackageService.selectBusinessPackageByPage(param));
        return returnResult;
    }

    /**
     * 商家下的商家套餐卡
     */
    @RequestMapping(value = "/home/businessPackage/selectBusinessPackageList", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPackageList(@RequestBody Map param) throws SQLException {
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        param.put("packageState",Constant.PackageState.SHELVE.codeName);
        returnResult.setData(businessPackageService.selectBusinessPackageByPage(param));
        return returnResult;
    }

    /**
     * 套餐卡详情
     */
    @RequestMapping(value = "/home/businessPackage/selectBusinessPackageById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPackageById(@RequestBody Map param) throws SQLException {
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        returnResult.setData(businessPackageService.selectBusinessPackageById(param));
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

    /**
     * 套餐卡详情
     */
    @RequestMapping(value = "/api/businessPackage/selectBusinessPackageByIdApi", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPackageByIdApi(@RequestBody Map param) throws SQLException {
        ReturnResult returnResult=ReturnResult.buildSuccessMsg();
        returnResult.setData(businessPackageService.selectBusinessPackageById(param));
        return returnResult;
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
     * 获取商家上架套餐列表（用户端）
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/businessPackage/selectBusinessPackageForOpen", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPackageForOpen(@RequestBody Map param) throws SQLException {
        param.put("packageState",Constant.PackageState.SHELVE.codeName);
        return ReturnResult.buildSuccessMsg().setData(businessPackageService.selectBusinessPackageForOpen(param));
    }

    /**
     * 获取商家套餐服务项
     * @param param
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/businessPackage/selectBusinessPackageForOpenEmkt", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectBusinessPackageForOpenEmkt(@RequestBody Map param) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(businessPackageService.selectBusinessPackageForOpenEmkt(param));
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}