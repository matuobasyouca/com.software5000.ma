package com.zscp.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MockServletContextWebContextLoader.class, locations = {"classpath:spring-core.xml", "classpath:spring-db.xml", "classpath:spring-db-read.xml", "classpath:spring-mvc.xml"})
public class BaseDaoTest {

    @Test
    public void testNewStudent() {
    }

}