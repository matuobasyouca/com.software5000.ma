package com.software5000.ma.service;

/**
 * Created by jiye on 2017/8/23.
 */

import com.software5000.ma.entity.PackCluster;
import com.software5000.ma.entity.PackClusterOpenRecord;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PackClusterOpenRecordService {
    private Log log = LogFactory.getLog(PackClusterOpenRecordService.class);

    @Resource
    private BaseDao baseDao;

    @Resource
    private PackClusterService packClusterService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增开团记录
     * @param packClusterOpenRecord
     * @return
     */
    public PackClusterOpenRecord insertPackClusterOpenRecord(PackClusterOpenRecord packClusterOpenRecord) throws SQLException {
        PackCluster packCluster=new PackCluster();
        packCluster.setId(packClusterOpenRecord.getPackClusterId());
        packCluster.setCanEdit(Constant.canEdit.CANTEDIT.codeName);
        packClusterService.updatePackClusterNotEmpty(packCluster);
        return baseDao.insertEntity(packClusterOpenRecord);
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/
    public void updatePackClusterOpenRecord(PackClusterOpenRecord packClusterOpenRecord) throws SQLException {
         baseDao.updateEntityNotEmpty(packClusterOpenRecord);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    public PackClusterOpenRecord selectPackClusterOpenRecordById(Integer id){
        PackClusterOpenRecord param = new PackClusterOpenRecord();
        param.setId(id);
        return selectSinglePackClusterOpenRecordByEntity(param);
    }

    public PackClusterOpenRecord selectSinglePackClusterOpenRecordByEntity(PackClusterOpenRecord packClusterOpenRecord){
       return baseDao.selectSingleEntity(packClusterOpenRecord);
    }

    /**
     * 查询已经到期的拼团
     * @return
     */
    public List<PackClusterOpenRecord> selectTimeOutPackCluster() throws SQLException {
      return (List<PackClusterOpenRecord>) baseDao.selectList(PackClusterOpenRecord.Daos.selectTimeOutPackCluster.sqlMapname);
    }


    /**
     * 根据条件查询团数量
     */
    public Integer selectSuccessNum(Map param) throws SQLException{
        return (Integer)baseDao.selectObject(PackClusterOpenRecord.Daos.selectSuccessNum.sqlMapname,param);
    }

    /**
     * 根据条件查询团数量
     */
    public Integer selectSuccessNum(Integer satate,Integer id) throws SQLException{
        Map param=new HashMap();
        param.put("state",satate);
        param.put("id",id);
        return selectSuccessNum(param);
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}