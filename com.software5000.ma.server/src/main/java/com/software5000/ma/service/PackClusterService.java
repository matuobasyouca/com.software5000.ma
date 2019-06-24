package com.software5000.ma.service;

/**
 * Created by jiye on 2017/8/23.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.base.MyBaseDao;
import com.software5000.base.ValueUpdatePolicy;
import com.software5000.ma.entity.BusinessPackage;
import com.software5000.ma.entity.PackCluster;
import com.software5000.ma.entity.PackClusterImg;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PackClusterService {
    private Log log = LogFactory.getLog(PackClusterService.class);

    @Resource
    private MyBaseDao baseDao;

    @Resource
    private BusinessPackageService businessPackageService;

    @Resource
    private PackClusterOpenRecordService packClusterOpenRecordService;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新建拼团活动
     * @param packCluster
     * @return
     */
    public void insertPackCluster(PackCluster packCluster) throws SQLException {
        PackCluster p= (PackCluster) baseDao.insertEntity(packCluster);
        insertPackClusterImgs(packCluster.getPackClusterImgs(),p.getId());
    }

    /**
     * 保存活动图片
     * @param packClusterImgs
     * @param id
     * @throws SQLException
     */
    private void insertPackClusterImgs(List<PackClusterImg> packClusterImgs, Integer id)throws SQLException{
        if(null!=packClusterImgs&&packClusterImgs.size()>0){
            List<PackClusterImg> collect =packClusterImgs.stream().peek(packClusterImg ->packClusterImg.setPackClusterId(id)).collect(Collectors.toList());
            baseDao.insertEntities(collect);
        }
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 删除拼团活动
     * @param id
     * @return
     */
    public void  deletePackCluster(Integer id) throws SQLException {
        PackCluster packCluster = new PackCluster();
        packCluster.setId(id);
        baseDao.deleteEntity(packCluster);
        deletePackClusterImg(id);
    }

    /**
     * 删除拼团活动图片
     * @param id
     * @throws SQLException
     */
    private void deletePackClusterImg(Integer id)throws SQLException{
        PackClusterImg packClusterImg=new PackClusterImg();
        packClusterImg.setPackClusterId(id);
        baseDao.deleteEntity(packClusterImg);
    }
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 修改拼团活动
     * @param packCluster
     * @return
     */
    public void updatePackCluster(PackCluster packCluster)throws SQLException{
        List<String> fileNames=new ArrayList<String>();
        baseDao.updateEntity(packCluster,null, ValueUpdatePolicy.WITH_EMPTY_WITH_NULL);
        deletePackClusterImg(packCluster.getId());
        insertPackClusterImgs(packCluster.getPackClusterImgs(),packCluster.getId());
    }

    /**
     * 上下架拼团活动
     * @param packCluster
     * @return
     */
    public void updatePackClusterNotEmpty(PackCluster packCluster)throws SQLException{
        baseDao.updateEntity(packCluster);
    }

    /**
     * 定时下架拼团活动
     * @param param
     * @return
     */
    public void updatePackClusterState(Map param) throws SQLException{
        baseDao.update(PackCluster.Daos.updatePackClusterState.sqlMapname,param);
    }
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    public PackCluster selectSinglePackCluster(PackCluster packCluster){
        return baseDao.selectSingleEntity(packCluster);
    }

    public PackCluster selectSinglePackClusterById(Integer id){
        PackCluster packCluster = new PackCluster();
        packCluster.setId(id);
        return selectSinglePackCluster(packCluster);
    }

    /**
     * 通过编号查询拼团活动详情
     */
    public PackCluster selectPackClusterInfoById(Map param) throws SQLException{
        return (PackCluster)baseDao.selectObject(PackCluster.Daos.selectPackClusterById.sqlMapname,param);
    }

    /**
     * 查询活动信息及套餐信息
     * @param id
     * @return
     * @throws SQLException
     */
    public PackCluster selectPackClusterWithPackageById(Integer id) throws SQLException {
        PackCluster packCluster = new PackCluster();
        packCluster.setId(id);
        PackCluster packCluster1 = selectSinglePackCluster(packCluster);
        BusinessPackage businessPackageId = businessPackageService.selectBusinessPackageById(new HashMap() {{
            put("businessPackageId", packCluster1.getPackId());
        }});
        packCluster1.setBusinessPackage(businessPackageId);
        return packCluster1;
    }

    /**
     * 查询拼团活动列表
     */
    public PageInfo<PackCluster> selectPackClusterByPage(Map param) throws SQLException{
        PageInfo pageInfo=baseDao.selectEntitiesByPage(PackCluster.Daos.selectPackClusterByPage.sqlMapname,param,Integer.valueOf(param.getOrDefault("startPage",1).toString()),Integer.valueOf(param.getOrDefault("pageSize",10).toString()),null);
        List<PackCluster> packClusters=pageInfo.getList();
        for(PackCluster packCluster:packClusters){
            packCluster.setSuccessNum(packClusterOpenRecordService.selectSuccessNum(Constant.PackClusterOpenRecordState.SUCCESS.codeName,packCluster.getId()));
        }
        pageInfo.setList(packClusters);
        return pageInfo;
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}