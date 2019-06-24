package com.zscp.test;


import com.software5000.base.BaseDao;
import com.software5000.base.mybatis.plugins.PermissionHelper;
import com.software5000.ma.entity.Car;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-core.xml", "classpath:spring-db.xml"})
//@Transactional
public class BaseDaoTest {

    private Logger logger = LoggerFactory.getLogger(BaseDaoTest.class);

    @Autowired
    BaseDao baseDao;


    @Test
    public void testSelectRecChannel() {

        try {

            PermissionHelper.ignorePermissionThisTime();
            Car car = new Car();
            car.setId(1);
            List cars = baseDao.selectEntities(car);
            System.out.println(cars.size());
//            baseDao.updateEntity(employees);
//            baseDao.insertEntity(departments);
//            baseDao.insertEntity(employees);
        } catch (Exception e) {
            logger.error("query error!", e);
        }
    }
}