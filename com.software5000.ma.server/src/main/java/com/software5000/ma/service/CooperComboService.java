package com.software5000.ma.service;

/**
 * Created by luo on 2017/7/24.
 */

import com.github.pagehelper.PageInfo;
import com.software5000.base.MyBaseDao;
import com.software5000.ma.entity.CooperCombo;
import com.software5000.base.BaseDao;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

@Controller
public class CooperComboService {
    private Log log = LogFactory.getLog(CooperComboService.class);

    @Autowired
    private MyBaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    /**
     * 修改_合作套餐数量
     * @param  paramMap
     * @return void
     * @throws SQLException
     */
    public void updateCooperComboSaleNumByParam(Map paramMap) throws SQLException {
        baseDao.update(CooperCombo.Daos.updateCooperComboSaleNumById.sqlMapname,paramMap);
    }


    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    /**
     * 分页查询_合作套餐
     * @param  paramMap
     * @return PageInfo
     * @throws SQLException
     */
    public PageInfo selectCooperComboPageByParam(Map paramMap) throws SQLException, IOException {
        //分页查询合作套餐信息
        String orderByStr = "id desc";
        if(!ValidUtil.isEmpty(paramMap.get("orderBy"))){
            orderByStr = paramMap.get("orderBy").toString();
        }
        PageInfo pageInfo =baseDao.selectEntitiesByPage(CooperCombo.Daos.selectCooperComboPageByParam.sqlMapname,paramMap,
                (Integer)paramMap.getOrDefault("startPage",1),
                (Integer)paramMap.getOrDefault("pageSize",10),
                orderByStr
        );
        //获取优惠券相关信息
        List<CooperCombo> comboList = pageInfo.getList();
        pageInfo.setList(comboList);
        return  pageInfo;
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>

}