package com.software5000.ma.service;

/**
 * Created by jiye on 2017/7/19.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.ma.entity.MemberItemUseRecord;
import com.software5000.ma.entity.ServiceItem;
import com.software5000.base.BaseDao;
import com.software5000.base.ServiceException;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ServiceItemService {
    private Log log = LogFactory.getLog(ServiceItemService.class);

    @Resource
    private BaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增服务项使用记录
     * @param memberItemUseRecord
     * @return
     * @throws SQLException
     */
    public MemberItemUseRecord insertMemberItemUseRecord(MemberItemUseRecord memberItemUseRecord) throws SQLException {
        return baseDao.insertEntity(memberItemUseRecord);
    }

    /**
     * 批量新增服务项
     * @param serviceItems
     * @return
     * @throws SQLException
     */
    public List<ServiceItem> insertServiceItems(List<ServiceItem> serviceItems) throws SQLException {
        return baseDao.insertEntityList(serviceItems);
    }

    /**
     * 保存服务项
     *
     * @param serviceItem
     * @return 保存是否成功
     */
    public ServiceItem insertServiceItem(ServiceItem serviceItem) throws SQLException {
        return baseDao.insertEntity(serviceItem);
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/


    /**
     * 根据服务项ID删除服务项
     *
     * @param serviceItemId
     */
    public void deleteServiceItem(Integer serviceItemId) throws SQLException {
        baseDao.deleteEntityById(serviceItemId, ServiceItem.class);
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 更新服务项
     *
     * @param serviceItem
     * @throws ServiceException
     */
    public void updateServiceItem(ServiceItem serviceItem) throws SQLException {
        baseDao.updateEntityNotEmpty(serviceItem);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据商家id跟服务项名查询出服务项
     * @param businessId
     * @param itemName
     * @return
     */
    public ServiceItem selectServiceItemByBusinessIdAndItemName(Integer businessId,String itemName){
        ServiceItem serviceItem = new ServiceItem();
        serviceItem.setBusinessId(businessId);
        serviceItem.setItemName(itemName);
        return baseDao.selectSingleEntity(serviceItem);
    }

    /**
     * 根据联盟商家Id查询服务项
     *
     * @param paramMap
     * @return
     */
    public PageInfo selectPageServiceItem(Map paramMap) throws SQLException {
        Integer startPage = (Integer)paramMap.getOrDefault("startPage",1);
        Integer pageSize = (Integer)paramMap.getOrDefault("pageSize",10);
        String orderBy = "topState desc,sort asc,updateTime desc";
        if(ValidUtil.isNotEmpty(paramMap.get("orderBy"))){
            orderBy = paramMap.get("orderBy").toString();
        }
        return baseDao.selectListByPage(ServiceItem.Daos.selectPageServiceItem.sqlMapname,paramMap,startPage,pageSize,orderBy);
    }

    /**
     * 根据联盟商家Id查询服务项
     *
     * @param paramMap
     * @return
     */
    public List selectServiceItemList(Map paramMap) throws SQLException {
        return baseDao.selectList(ServiceItem.Daos.selectPageServiceItem.sqlMapname, paramMap);
    }

    /**
     * 统计服务项在套餐中的应用数量
     *
     * @param serviceItemId
     * @return
     * @throws SQLException
     */
    public Integer selectCountServiceItemHaveBuyNum(Integer serviceItemId) throws SQLException {
        Integer count = (Integer) baseDao.selectObject(ServiceItem.Daos.countServiceItemUseInPackage.sqlMapname, serviceItemId);
        return count;
    }

    /**
     * 查询服务项
     * @param serviceItem
     * @return
     */
    public ServiceItem selectServiceItem(ServiceItem serviceItem){
        return baseDao.selectSingleEntity(serviceItem);
    }

    /**
     * 根据服务项名称查询服务项
     * @param itemName
     * @return
     */
    public ServiceItem selectServiceItemByItemName(String itemName){
        ServiceItem paramServiceItem = new ServiceItem();
        paramServiceItem.setItemName(itemName);
        return selectServiceItem(paramServiceItem);
    }

    /**
     * 查询商家下的项目数
     */
    public Integer selectItemCount(Map param) throws SQLException {
        return (Integer) baseDao.selectObject(ServiceItem.Daos.selectItemCount.sqlMapname,param);
    }

    /**
     * 根据项目分类展示服务项
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public List<ServiceItem> selectServiceItemDiscountNumber(Map param) throws SQLException {
        return (List<ServiceItem>) baseDao.selectList(ServiceItem.Daos.selectServiceItemDiscountNumber.sqlMapname, param);
    }

    /**
     * 获取创建工单时，用到的服务项（包含会员折扣）
     * @param paramMap
     * @return
     * @throws SQLException
     */
    public PageInfo selectPageServiceItemForWorkOrder(Map paramMap) throws SQLException {
        Integer startPage = Integer.valueOf(paramMap.getOrDefault("startPage","1").toString());
        Integer pageSize = Integer.valueOf(paramMap.getOrDefault("pageSize","10").toString());
        return baseDao.selectListByPage(ServiceItem.Daos.selectPageServiceItemForWorkOrder.sqlMapname,paramMap,startPage,pageSize,"");
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}