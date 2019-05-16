package com.software5000.base;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.software5000.base.jsql.ConditionWrapper;
import com.software5000.util.ClassUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component("baseDao")
public class MyBaseDao extends BaseDao {

    protected Log log = LogFactory.getLog(MyBaseDao.class);

    private SqlSessionTemplate sqlSessionTemplate;

    public MyBaseDao() {
        // 在默认构造函数中设置 数据库是否蛇形， 数据库格式大小写， 通用忽略的字段名称
        this.initConfig(false, false, "");
    }

    @Resource
    public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
        if (this.sqlSessionTemplate == null || sqlSessionFactory != this.sqlSessionTemplate.getSqlSessionFactory()) {
            this.sqlSessionTemplate = this.createSqlSessionTemplate(sqlSessionFactory);
        }
    }

    protected SqlSessionTemplate createSqlSessionTemplate(SqlSessionFactory sqlSessionFactory) {
        return new SqlSessionTemplate(sqlSessionFactory);
    }

    @Override
    public SqlSession getSqlSession() {
        return this.sqlSessionTemplate;
    }



    public void deleteEntity(Object entity){
        this.deleteEntity(entity, "id");
    }
    public void updateEntity(Object entity) {
        this.updateEntity(entity, "id");
    }


    /**
     * 根据sql方法名称和条件，取回单个查询结果对象
     */
    public Object selectObject(String sqlName, Object obj)  {
        return getSqlSession().selectOne(sqlName, obj);

    }

    /**
     * 根据sql方法名称，取回单个查询结果对象
     */
    public Object selectObject(String sqlName) {
        return getSqlSession().selectOne(sqlName);
    }

    public List selectEntity(Object entity) {
        return this.selectEntities(entity);
    }

    public <T> T selectSingleEntity(T entity) {
        return (T) this.selectEntities(entity).get(0);
    }

    public <T> T selectEntityById(Object value, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            ClassUtil.setValueByField(t, "id", value);
            return (T) this.selectEntities(t).get(0);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }

    public <T> T selectSingleEntity(String fieldName, Object value, Class<T> tClass) {
        try {
            T t = tClass.newInstance();
            ClassUtil.setValueByField(t, fieldName, value);
            return (T) this.selectEntities(t).get(0);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
    }


    public PageInfo selectEntitiesByPage(String sqlName, Map paramMap, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(this.selectList(sqlName, paramMap));
    }

    public PageInfo selectEntitiesByPage(String sqlName, Map paramMap, int pageNum, int pageSize,String orderBy) {
        PageHelper.startPage(pageNum, pageSize,orderBy);
        return new PageInfo(this.selectList(sqlName, paramMap));
    }

    public PageInfo selectEntitiesByPage(int pageNum, int pageSize, Object entity, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(this.selectEntities(entity, orderBy));
    }

    public PageInfo selectEntitiesByPage(int pageNum, int pageSize, Object entity, ConditionWrapper conditionWrapper, String orderBy) {
        PageHelper.startPage(pageNum, pageSize);
        return new PageInfo(this.selectEntities(entity, conditionWrapper,orderBy));
    }
}
