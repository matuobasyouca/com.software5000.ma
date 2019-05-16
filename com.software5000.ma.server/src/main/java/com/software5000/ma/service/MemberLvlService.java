package com.software5000.ma.service;

/**
 * Created by Administrator on 2017/7/24.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.base.MyBaseDao;
import com.software5000.ma.entity.MemberLvl;
import com.software5000.base.BaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Map;

@Service
public class MemberLvlService {
    private Log log = LogFactory.getLog(MemberLvlService.class);

    @Resource
    private MyBaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增会员等级
     * @param memberLvl
     * @return
     * @throws SQLException
     */
    public MemberLvl insertMemberLvl(MemberLvl memberLvl) throws SQLException {
        return (MemberLvl) baseDao.insertEntity(memberLvl);
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
     * 修改会员等级信息
     * @param memberLvl
     * @throws SQLException
     */
    public void updateMemberLvl(MemberLvl memberLvl) throws SQLException {
        baseDao.updateEntity(memberLvl);
    }
    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据条件查询会员等级信息
     * @param memberLvl
     * @return
     * @throws SQLException
     */
    public MemberLvl selectMemberLvl(MemberLvl memberLvl) throws SQLException {
        return baseDao.selectSingleEntity(memberLvl);
    }

    /**
     * 查询商家会员等级信息
     * @return
     * @throws SQLException
     */
    public PageInfo<MemberLvl> selectMemberLvlPage(Map<String, Object> param) throws SQLException {
        return baseDao.selectEntitiesByPage(MemberLvl.Daos.selectMemberLvlPage.sqlMapname
                                        ,param
                                        ,Integer.valueOf(param.getOrDefault("startPage", "1").toString())
                                        ,Integer.valueOf(param.getOrDefault("pageSize", "10").toString())
                                        ,null);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}