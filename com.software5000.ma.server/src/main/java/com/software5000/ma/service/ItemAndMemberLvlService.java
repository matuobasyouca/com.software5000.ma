package com.software5000.ma.service;


import com.software5000.ma.entity.ItemAndMemberLvl;
import com.software5000.base.BaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Service
public class ItemAndMemberLvlService {
    private Log log = LogFactory.getLog(ItemAndMemberLvlService.class);

    @Resource
    private BaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/

    /**
     * 新增会员折扣设置
     *
     * @param itemAndMemberLvl
     * @return
     * @throws SQLException
     */
    public ItemAndMemberLvl insertItemAndMemberLvl(ItemAndMemberLvl itemAndMemberLvl) throws SQLException {
        return baseDao.insertEntity(itemAndMemberLvl);
    }

    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/

    /**
     * 根据参数删除折扣设置记录
     *
     * @param param
     * @throws SQLException
     */
    public void deleteItemAndMemberLvlByParam(Map param) throws SQLException {
        baseDao.delete(ItemAndMemberLvl.Daos.deleteItemAndMemberLvlByParam.sqlMapname, param);
    }

    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 更新会员折扣设置
     *
     * @param itemAndMemberLvl
     * @return
     * @throws SQLException
     */
    public void updateItemAndMemberLvl(ItemAndMemberLvl itemAndMemberLvl) throws SQLException {
        baseDao.updateEntity(itemAndMemberLvl);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 根据参数查询会员折扣信息
     * @param param
     * @return
     * @throws SQLException
     */
    public List<ItemAndMemberLvl> selectItemAndMemberLvlByParam(Map param) throws SQLException {
        return (List<ItemAndMemberLvl>) baseDao.selectList(ItemAndMemberLvl.Daos.selectItemAndMemberLvlByParam.sqlMapname,param);
    }
    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}