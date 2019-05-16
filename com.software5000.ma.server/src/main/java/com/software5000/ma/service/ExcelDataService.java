package com.software5000.ma.service;

/**
 * Created by jiye on 2017/7/19.
 */

import com.software5000.base.MyBaseDao;
import com.software5000.ma.entity.ServiceItemExcelData;
import com.software5000.ma.entity.UserExcelData;
import com.software5000.base.BaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;

@Service
public class ExcelDataService {
    private Log log = LogFactory.getLog(ExcelDataService.class);

    @Resource
    private MyBaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    public List insertUserExcelData(List<UserExcelData> userExcelDatas) throws SQLException {
        return baseDao.insertEntities(userExcelDatas);
    }

    public List insertServiceItemExcelData(List<ServiceItemExcelData> itemExcelDatas) throws SQLException {
        return baseDao.insertEntities(itemExcelDatas);
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    public void deleteUserExcelDataByBusinessId(Integer businessId) throws SQLException {
        UserExcelData userExcelData = new UserExcelData();
        userExcelData.setBusinessId(businessId);
        baseDao.deleteEntity(userExcelData);
    }

    public void deleteServiceExcelDataByBusinessId(Integer businessId) throws SQLException {
        ServiceItemExcelData itemExcelData = new ServiceItemExcelData();
        itemExcelData.setBusinessId(businessId);
        baseDao.deleteEntity(itemExcelData);
    }
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 批量更新数据
     * @param userExcelDatas
     * @throws SQLException
     */
    public void updateUserExcelData(List<UserExcelData> userExcelDatas) throws SQLException {
        for (UserExcelData userExcelData : userExcelDatas) {
            baseDao.updateEntity(userExcelData);
        }
    }

    /**
     * 批量更新数据
     * @param itemExcelDatas
     * @throws SQLException
     */
    public void updateServiceItemExcelData(List<ServiceItemExcelData> itemExcelDatas) throws SQLException {
        for (ServiceItemExcelData itemExcelData : itemExcelDatas) {
            baseDao.updateEntity(itemExcelData);
        }
    }
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 查询导入的会员信息的结果
     * @param businessId
     * @return
     * @throws SQLException
     */
    public List<UserExcelData>  selectUserInfoImportResult(Integer businessId) throws SQLException {
        UserExcelData userExcelData = new UserExcelData();
        userExcelData.setBusinessId(businessId);
        return baseDao.selectEntity(userExcelData);
    }

    /**
     * 查询导入的会员信息的结果
     * @param businessId
     * @return
     * @throws SQLException
     */
    public List<ServiceItemExcelData>  selectItemImportResult(Integer businessId) throws SQLException {
        ServiceItemExcelData itemExcelData = new ServiceItemExcelData();
        itemExcelData.setBusinessId(businessId);
        return baseDao.selectEntity(itemExcelData);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}