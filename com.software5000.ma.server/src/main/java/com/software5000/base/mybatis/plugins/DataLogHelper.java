package com.software5000.base.mybatis.plugins;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;

/**
 * @Author Chenchen
 * @Created 2019/06/22
 */
@Intercepts({
        @Signature(type = ResultSetHandler.class, method = "handleResultSets", args = {Statement.class}),
        @Signature(type = Executor.class, method = "update", args = {MappedStatement.class,Object.class})
})
public class DataLogHelper implements Interceptor {

    private static final Log log = LogFactory.getLog(DataLogHelper.class);

    private static Map<String, Object> ucos = new HashMap<>();
    private static Map<String, DataLog> dataLogMap = new HashMap<>();


    private Properties props = null;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return processIntercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        if (null != properties && !properties.isEmpty()) {
            props = properties;
        }
    }

    private Object processIntercept(Invocation invocation) throws Exception {
        String interceptMethod = invocation.getMethod().getName();

        if("handleResultSets".equals(interceptMethod)){
            // 记录缓存用于对比差异记录，保留uco
            // 获取到当前的Statement
            Statement stmt =  (Statement) invocation.getArgs()[0];
            // 通过Statement获得当前结果集
            ResultSet resultSet = stmt.getResultSet();
            List<Object> resultList = new ArrayList<Object>();
            if(resultSet != null && resultSet.next()) {
                resultList.add(resultSet.getString("carNumber"));
                System.out.println(resultSet);
            }
        }else if("update".equals(interceptMethod)){
            //这里是更新或者插入方法，用于获取uco，生成数据日志

        }

        return invocation.proceed();
    }
}

