package com.software5000.base;

import java.sql.SQLException;

import javax.annotation.Resource;

import com.software5000.base.entity.AreaCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import com.software5000.base.entity.SystemCode;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;

@Component
public class InitDataListener{

    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private BaseDao baseDao;

    public void initDate() throws Exception {
        try {
            Constant.initCodes(baseDao.selectEntity(new SystemCode()).stream().collect(toMap(SystemCode::getCodeName , (p)->p)));
            //将地区信息放到内存中
            Constant.initAreaCode(baseDao.selectEntity(new AreaCode()));
            log.info("初始化缓存数据成功！");
        } catch (SQLException e) {
            log.error("初始化缓存数据错误！",e);
        }
    }

}