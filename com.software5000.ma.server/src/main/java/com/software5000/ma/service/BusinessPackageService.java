package com.software5000.ma.service;

/**
 * Created by jiye on 2017/7/19.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.base.MyBaseDao;
import com.software5000.ma.entity.BusinessPackage;
import com.software5000.ma.entity.PackageAndItem;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
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
public class BusinessPackageService {
    private Log log = LogFactory.getLog(BusinessPackageService.class);

    @Resource
    private MyBaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 保存套餐卡
     */
    public BusinessPackage insertBusinessPackage(BusinessPackage businessPackage) throws ServiceException {
        try {
            BusinessPackage b = (BusinessPackage) baseDao.insertEntity(businessPackage);
            List<PackageAndItem> items = businessPackage.getPackageAndItems();
            List<PackageAndItem> collect = items.stream().peek(packageAndItem -> packageAndItem.setBusinessPackageId(b.getId())).collect(Collectors.toList());
            List packageAndItems = baseDao.insertEntities(collect);
            b.setPackageAndItems(packageAndItems);
            businessPackage = b;
        } catch (Exception e) {
            log.error("保存businessPackage失败，businessPackage=" + businessPackage);
            throw new ServiceException(Constant.StateCode.SAVE_ERROR.codeName);
        }
        return businessPackage;
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 根据套餐ID删除套餐
     *
     * @param businessPackage
     */
    public void deleteBusinessPackage(BusinessPackage businessPackage) throws ServiceException {
        try {
            //先删除套餐下的套餐集
            baseDao.delete(BusinessPackage.Daos.deletePackageAndItem.toString(), businessPackage.getId());
            //再删除套餐
            baseDao.deleteEntity(businessPackage, "id");
        } catch (Exception e) {
            log.error("删除服务项失败，businessPackageId=" + businessPackage.getId());
            throw new ServiceException(Constant.StateCode.DELETE_ERROR.codeName);
        }
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 更新商家套餐
     *
     * @param businessPackage
     * @return
     */
    public BusinessPackage updateBusinessPackage(BusinessPackage businessPackage) throws ServiceException {
        List<PackageAndItem> packageAndItems = businessPackage.getPackageAndItems();
        try {
            if (packageAndItems != null && packageAndItems.size() > 0) {
                //因套餐项没有在使用可以直接删除旧的套餐项
                PackageAndItem packageAndItem = new PackageAndItem();
                packageAndItem.setBusinessPackageId(businessPackage.getId());
                baseDao.deleteEntity(packageAndItem,"id");
                //将要更改的套餐项新增
                List<PackageAndItem> collect = packageAndItems.stream().peek(item -> item.setBusinessPackageId(businessPackage.getId())).collect(Collectors.toList());
                baseDao.insertEntities(collect);
            }
            baseDao.updateEntity(businessPackage,"id",true);
        } catch (Exception e) {
            log.error("更新商家套餐失败，businessPackage=" + businessPackage.toString());
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
        return businessPackage;
    }

    /**
     * 更新商家套餐状态
     *
     * @param businessPackage
     * @return
     */
    public BusinessPackage updateBusinessPackageState(BusinessPackage businessPackage) throws ServiceException {
        try {
            baseDao.updateEntity(businessPackage,"id",true);
        } catch (Exception e) {
            log.error("更新商家套餐失败，businessPackage=" + businessPackage.toString());
            throw new ServiceException(Constant.StateCode.SELECT_ERROR.codeName);
        }
        return businessPackage;
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/
    /**
     *通过编号和名称查询套餐卡
     * @param businessId
     * @param businessPackageName
     * @return
     */
    public BusinessPackage selectBusinessPackageByBusinessIdAndPackageName(Integer businessId, String businessPackageName){
        BusinessPackage businessPackage = new BusinessPackage();
        businessPackage.setBusinessId(businessId);
        businessPackage.setPackageName(businessPackageName);
        return baseDao.selectSingleEntity(businessPackage);
    }

    /**
     * 套餐列表
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo<BusinessPackage> selectBusinessPackageByPage(Map param) throws SQLException {
        PageInfo<BusinessPackage> pageInfo=baseDao.selectEntitiesByPage(BusinessPackage.Daos.selectBusinessPackageIdsByPage.sqlMapname,param,Integer.parseInt(param.get("starPage").toString()),Integer.parseInt(param.get("pageSize").toString()));
        if (pageInfo.getList().size() > 0) {
            List ids = new ArrayList();
            pageInfo.getList().forEach(
                    businessPackage -> ids.add(businessPackage.getId())
            );
            param.put("packageIds",ids);
            pageInfo.setList((List<BusinessPackage>) baseDao.selectList(BusinessPackage.Daos.selectBusinessPackageByPage.sqlMapname,param));
        }
        return pageInfo;
    }

    /**
     * 套餐卡查询
     * @param businessPackage
     * @return
     */
    public BusinessPackage selectBusinessPackageByEntity(BusinessPackage businessPackage){
        return baseDao.selectSingleEntity(businessPackage);
    }

    public  BusinessPackage selectBusinessPackageById(Map param) throws SQLException{
        return (BusinessPackage)baseDao.selectObject(BusinessPackage.Daos.selectBusinessPackageById.sqlMapname,param);
    }

    /**
     * 套餐卡查询
     * @param param
     * @return
     */
    public List<BusinessPackage> selectBusinessPackageList(Map param) throws SQLException {
        return (List<BusinessPackage>) baseDao.selectList(BusinessPackage.Daos.selectBusinessPackageList.sqlMapname,param);
    }

    /**
     * 查询商家下的套餐卡数量
     */
    public Integer selectBusinessPackageCount(Map param) throws SQLException {
        return (Integer) baseDao.selectObject(BusinessPackage.Daos.selectBusinessPackageCount.sqlMapname,param);
    }

    /**
     * 获取用户端套餐列表
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo<BusinessPackage> selectBusinessPackageForOpen(Map<String, Object> param) throws SQLException {
        PageInfo<BusinessPackage> pageInfo = baseDao.selectEntitiesByPage(BusinessPackage.Daos.selectBusinessPackageIdsByPage.sqlMapname
                                                                     ,param
                                                                     ,Integer.valueOf(param.getOrDefault("startPage", 1).toString())
                                                                     ,Integer.valueOf(param.getOrDefault("pageSize", 10).toString())
                                                                     );
        if (pageInfo.getList().size() > 0) {
            List<Integer> ids = new ArrayList<Integer>();
            pageInfo.getList().forEach(businessPackage -> ids.add(businessPackage.getId()));
            param.put("packageIds",ids);
            pageInfo.setList((List<BusinessPackage>) baseDao.selectList(BusinessPackage.Daos.selectBusinessPackageByPage.sqlMapname,param));
        }
        return pageInfo;
    }

    /**
     * 获取用户端套餐列表
     * @param param
     * @return
     * @throws SQLException
     */
    public Map<String,BusinessPackage> selectBusinessPackageForOpenEmkt(Map<String, Object> param) throws SQLException {
        List<BusinessPackage> businessPackages=(List<BusinessPackage>)baseDao.selectList(BusinessPackage.Daos.selectBusinessPackageForOpenEmkt.sqlMapname,param);
        String[] ids = param.get("packIds").toString().split(",");
        Map map = new HashMap();
        for (BusinessPackage businessPackage : businessPackages){
            for (String id : ids){
                if (businessPackage.getId().equals(Integer.valueOf(id))){
                    map.put(id,businessPackage);
                }
            }
        }
        return map;
    }



    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}