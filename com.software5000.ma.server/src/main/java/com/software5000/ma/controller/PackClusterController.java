package com.software5000.ma.controller;

/**
 * Created by jiye on 2017/8/24.
 */

import com.software5000.ma.entity.PackCluster;
import com.software5000.ma.entity.PackClusterOpenRecord;
import com.software5000.ma.service.PackClusterOpenRecordService;
import com.software5000.ma.service.PackClusterService;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
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
import java.util.Map;

@Controller
public class PackClusterController {
    private Log log = LogFactory.getLog(PackClusterController.class);

    @Resource
    private PackClusterService packClusterService;

    @Resource
    private PackClusterOpenRecordService packClusterOpenRecordService;

    //<editor-fold desc="home (对内) ">
    /*================================== home (对内) start ==================================*/

    //<editor-fold desc="home (对内)->insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增活动信息
     *
     * @param packCluster
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/packCluster/insertPackCluster", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult insertPackCluster(@RequestBody PackCluster packCluster) throws SQLException {
        packCluster.setState(Constant.PackageState.SHELVE.codeName);
        packCluster.setCanEdit(Constant.canEdit.CANEDIT.codeName);
        packClusterService.insertPackCluster(packCluster);
        return ReturnResult.buildSuccessMsg();
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 修改活动信息
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/packCluster/deletePackCluster", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult deletePackCluster(@RequestBody  Map paramMap) throws SQLException {
        Integer id=Integer.valueOf(paramMap.get("id").toString());
        PackClusterOpenRecord packClusterOpenRecord=new PackClusterOpenRecord();
        packClusterOpenRecord.setPackClusterId(id);
        packClusterOpenRecord=packClusterOpenRecordService.selectSinglePackClusterOpenRecordByEntity(packClusterOpenRecord);
        if(null==packClusterOpenRecord) {
            packClusterService.deletePackCluster(id);
            return ReturnResult.buildSuccessMsg();
        }else{
            ReturnResult returnResult=ReturnResult.buildEnumResult(Constant.StateCode.ERROR);
            returnResult.setMsg("该拼团有团参与活动不能删除！");
            return returnResult;
        }
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 修改活动信息
     *
     * @param packCluster
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/packCluster/updatePackCluster", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updatePackCluster(@RequestBody PackCluster packCluster) throws SQLException {
        PackClusterOpenRecord packClusterOpenRecord=new PackClusterOpenRecord();
        packClusterOpenRecord.setPackClusterId(packCluster.getId());
        packClusterOpenRecord=packClusterOpenRecordService.selectSinglePackClusterOpenRecordByEntity(packClusterOpenRecord);
        if(null==packClusterOpenRecord) {
            packClusterService.updatePackCluster(packCluster);
            return ReturnResult.buildSuccessMsg();
        }else{
            ReturnResult returnResult=ReturnResult.buildEnumResult(Constant.StateCode.ERROR);
            returnResult.setMsg("该拼团有团参与活动不能修改！");
            return returnResult;
        }
    }

    /**
     * 上下架拼团活动
     *
     * @param packCluster
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/packCluster/updatePackClusterState", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult updatePackClusterState(@RequestBody PackCluster packCluster) throws SQLException {
        packClusterService.updatePackClusterNotEmpty(packCluster);
        return ReturnResult.buildSuccessMsg();
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="home (对内)->select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 查询活动记录信息
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/packCluster/selectPackClusterByPage", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPackClusterByPage(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(packClusterService.selectPackClusterByPage(paramMap));
    }

    /**
     * 查询活动记录信息
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/home/packCluster/selectPackClusterInfoById", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPackClusterInfoById(@RequestBody Map paramMap) throws SQLException {
        PackCluster packCluster=packClusterService.selectPackClusterInfoById(paramMap);
        paramMap.put("state",Constant.PackClusterOpenRecordState.SUCCESS.codeName);
        packCluster.setSuccessNum(packClusterOpenRecordService.selectSuccessNum(paramMap));
        return ReturnResult.buildSuccessMsg().setData(packCluster);
    }

    /**
     * 查询拼团情况
     */
    @RequestMapping(value = "/home/packCluster/selectPackClusterOpenNum", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPackClusterOpenNum(@RequestBody Map paramMap) throws SQLException {
        Map param=new HashMap();
        param.put("allNum",packClusterOpenRecordService.selectSuccessNum(paramMap));
        paramMap.put("state",Constant.PackClusterOpenRecordState.SUCCESS.codeName);
        param.put("successNum",packClusterOpenRecordService.selectSuccessNum(paramMap));
        return ReturnResult.buildSuccessMsg().setData(param);
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

    /**
     * 查询活动记录信息
     *
     * @param paramMap
     * @return
     * @throws SQLException
     */
    @RequestMapping(value = "/open/packCluster/selectPackClusterInfo", method = RequestMethod.POST)
    @ResponseBody
    public ReturnResult selectPackClusterInfo(@RequestBody Map paramMap) throws SQLException {
        return ReturnResult.buildSuccessMsg().setData(packClusterService.selectPackClusterInfoById(paramMap));
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>
    
    
    /*=================================== open (开放) end ==================================*/
    //</editor-fold>

}