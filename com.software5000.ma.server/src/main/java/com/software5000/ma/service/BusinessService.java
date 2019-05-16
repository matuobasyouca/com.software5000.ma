package com.software5000.ma.service;

/**
 * Created by jiye on 2017/7/20.
 */

import com.github.pagehelper.PageInfo;
import com.riversoft.weixin.mp.user.Users;
import com.riversoft.weixin.mp.user.bean.User;
import com.software5000.base.MyBaseDao;
import com.software5000.ma.entity.Business;
import com.software5000.ma.entity.BusinessUser;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.ServiceException;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BusinessService {
    private Log log = LogFactory.getLog(BusinessService.class);

    @Resource
    private MyBaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    public BusinessUser insertBusinessUser(BusinessUser businessUser) throws SQLException {
        return (BusinessUser) baseDao.insertEntity(businessUser);
    }

    /**
     * 新增商家信息
     *
     * @param business
     * @return Business
     * @throws SQLException
     */
    public Business insertBusiness(Business business) throws SQLException {
        return (Business) baseDao.insertEntity(business);
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 更新商家信息
     *
     * @param business
     * @throws SQLException
     */
    public void updateBusiness(Business business) throws SQLException {
        baseDao.updateEntity(business);
    }

    /**
     * 更新员工数据
     *
     * @param businessUser
     * @throws SQLException
     */
    public void updateBusinessUser(BusinessUser businessUser) throws SQLException {
        if (ValidUtil.isNotEmpty(businessUser.getMercharType()) && Constant.BusinessUserType.MERCHANT.codeName.equals(businessUser.getMercharType())) {
            businessUser.setItemTypes(null);
//            baseDao.updateEntityOnlyHaveValueAndNull(businessUser, Arrays.asList("itemTypes"), true);
        } else {
            baseDao.updateEntity(businessUser);
        }
    }

    /**
     * 更新商家
     *
     * @param business
     * @return void
     * @throws SQLException
     */
    public void updateBusinessNotEmpty(Business business) throws SQLException, ServiceException {
        baseDao.updateEntity(business);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据商家ID查询信息
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Business selectBusinessById(Integer id) throws SQLException {
        Business business = baseDao.selectEntityById(id, Business.class);
//        if (business != null && ValidUtil.isNotEmpty(business.getWxOpenId())) {
//            User user = Users.defaultUsers().get(business.getWxOpenId());
//            if (user != null) {
//            }
//        }
        business.setBossNickName("联盟商家");
        return business;
    }

    /**
     * 根据ID查询商家的顾问ID
     *
     * @param id
     * @return
     * @throws SQLException
     */
    public Business selectBusinessByIdForConsultantId(Integer id) throws SQLException {
        Business business = baseDao.selectEntityById(id, Business.class);
        return business;
    }

    /**
     * 根据手机号查询商家用户
     *
     * @param mobile
     * @return
     */
    public BusinessUser selectBusinessUserByMobile(String mobile) {
        if (ValidUtil.isEmpty(mobile)) return null;
        BusinessUser businessUser = new BusinessUser();
        businessUser.setMobile(mobile);
        return selectBusinessUserByEntity(businessUser);
    }

    /**
     * 根据商家账号查询对应的商家信息
     *
     * @param map
     * @return
     */
    public Business selectEnableBusByBusUserName(Map map) throws SQLException {
        return (Business) baseDao.selectObject(Business.Daos.selectEnableBusByBusUserName.sqlMapname, map);
    }

    /**
     * 根据商家用户名查询商家用户
     *
     * @param userName
     * @return
     */
    public BusinessUser selectBusinessUserByUserName(String userName) {
        if (ValidUtil.isEmpty(userName)) return null;
        BusinessUser businessUser = new BusinessUser();
        businessUser.setUserName(userName);
        return selectBusinessUserByEntity(businessUser);
    }

    /**
     * 根据实体查询商家用户
     *
     * @param businessUser
     * @return
     */
    public BusinessUser selectBusinessUserByEntity(BusinessUser businessUser) {
        return baseDao.selectSingleEntity(businessUser);
    }

    /**
     * 根据查询条件，获取工单列表
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo selectPageOperatorBusinessUser(Map<String, Object> param) throws SQLException {
        String orderBy = param.getOrDefault("orderBy", "id desc").toString();
        if (ValidUtil.isEmpty(orderBy)) {
            orderBy = "id desc";
        }
        Integer startPage = (Integer) param.getOrDefault("startPage", 1);
        Integer pageSize = (Integer) param.getOrDefault("pageSize", 10);
        Integer bossType = (Integer) param.getOrDefault("boosType", 2);
        Object stateObj = param.get("state");
        BusinessUser bu = new BusinessUser();
        bu.setBossType(bossType);
        bu.setState(stateObj == null ? null : (Integer) stateObj);
        PageInfo pageInfo = baseDao.selectEntitiesByPage( startPage, pageSize,bu, orderBy);
        ((List<BusinessUser>) pageInfo.getList()).forEach(businessUser -> businessUser.setPwd(null));
        return pageInfo;
    }

    /**
     * 更分页查询商家列表
     *
     * @param paramMap
     * @return PageInfo
     * @throws SQLException
     */
    public PageInfo selectBusinessInfoPage(Map paramMap) throws SQLException {
        //分页查询合作套餐信息
        String orderByStr = paramMap.getOrDefault("orderBy", "id desc").toString();
        if (ValidUtil.isEmpty(orderByStr)) {
            orderByStr = "id desc";
        }
        PageInfo pageInfo = baseDao.selectEntitiesByPage(Business.Daos.selectBusinessListPageByParam.sqlMapname, paramMap,
                (Integer) paramMap.getOrDefault("startPage", 1),
                (Integer) paramMap.getOrDefault("pageSize", 10),
                orderByStr
        );
        return pageInfo;
    }

    /**
     * 分页查询所有可用商家列表（用户端）
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public PageInfo selectBusinessPageForOpen(Map<String, Object> param) throws SQLException {

        //设置排序条件
        String orderByStr = "";
        if (ValidUtil.isEmpty(param.get("orderByStr"))) {
            orderByStr = ValidUtil.isEmpty(param.get("wxOpenId")) ? "" : "packageCount DESC";
            if (param.get("lng") != null && !"".equals(param.get("lng")) && param.get("lat") != null && !"".equals(param.get("lat"))) {
                orderByStr += ValidUtil.isEmpty(param.get("wxOpenId")) ? "" : ",";
                orderByStr += "distance ASC";
            }
        } else {
            orderByStr = param.get("orderByStr").toString();
        }

        return baseDao.selectEntitiesByPage(Business.Daos.selectBusinessForOpen.sqlMapname, param,
                Integer.valueOf(param.getOrDefault("startPage", 1).toString()),
                Integer.valueOf(param.getOrDefault("pageSize", 10).toString()),
                orderByStr
        );
    }

    /**
     * 获取商家信息（用户端）
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public Business selectBusinessForOpen(Map param) throws SQLException {
        return (Business) baseDao.selectObject(Business.Daos.selectBusinessForOpen.sqlMapname, param);
    }

    /**
     * 获取商家名称（emkt）
     *
     * @param param
     * @return
     * @throws SQLException
     */
    public Map<String, Business> selectEnableBusinessNameList(Map param) throws SQLException {
        List<Business> businessList = (List<Business>) baseDao.selectList(Business.Daos.selectEnableBusinessNameList.sqlMapname, param);
        String[] ids = param.get("businessIds").toString().split(",");
        Map map = new HashMap();
        for (Business business : businessList) {
            for (String id : ids) {
                if (business.getId().equals(Integer.valueOf(id))) {
                    map.put(id, business);
                }
            }
        }
        return map;
    }

    /**
     * 根据商家ids获取商家信息（多个）
     *
     * @param param
     * @return
     * @throws ServiceException
     */
    public List<Business> selectEnableBusinessListByIds(Map<String, Object> param) throws SQLException {
        return (List<Business>) baseDao.selectList(Business.Daos.selectEnableBusinessListByIds.sqlMapname, param);
    }

    public List<Business> selectBusinessByEntity(Business business) throws SQLException {
        return baseDao.selectEntity(business);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}