package com.software5000.ma.service;

/**
 * Created by jiye on 2017/8/7.
 */

import com.software5000.ma.entity.LotteryAward;
import com.software5000.base.BaseDao;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.SQLException;

@Service
public class LotteryAwardService {
    private Log log = LogFactory.getLog(LotteryAwardService.class);

    @Resource
    private BaseDao baseDao;

    //<editor-fold desc="insert (增)">
    /* ----------------------------------------------------------- insert (增) start ----------------------------------------------------------------*/
    public LotteryAward insertLotteryAward(LotteryAward lotteryAward) throws SQLException {
        return baseDao.insertEntity(lotteryAward);
    }
    /* ----------------------------------------------------------- insert (增) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="delete (删)">
    /* ----------------------------------------------------------- delete (删) start ----------------------------------------------------------------*/
    
    /* ----------------------------------------------------------- delete (删) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="update (改)">
    /* ----------------------------------------------------------- update (改) start ----------------------------------------------------------------*/

    public void updateLotteryAward(LotteryAward lotteryAward) throws SQLException {
        baseDao.updateEntityNotEmpty(lotteryAward);
    }

    /* ----------------------------------------------------------- update (改) end ----------------------------------------------------- -----------*/
    //</editor-fold>


    //<editor-fold desc="select (查)">
    /* ----------------------------------------------------------- select (查) start ----------------------------------------------------------------*/

    public LotteryAward selectSingleLotteryAwardByEntity(LotteryAward lotteryAward){
        return baseDao.selectSingleEntity(lotteryAward);
    }

    /* ----------------------------------------------------------- select (查) end ----------------------------------------------------- -----------*/
    //</editor-fold>


}