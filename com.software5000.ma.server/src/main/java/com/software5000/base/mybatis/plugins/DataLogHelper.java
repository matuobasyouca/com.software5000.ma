package com.software5000.base.mybatis.plugins;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.Properties;

/**
 * Created by cc on 2016/12/22.
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class DataLogHelper implements Interceptor {

    private static final Log log = LogFactory.getLog(DataLogHelper.class);

    private Properties props = null;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        return processIntercept(invocation);
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof StatementHandler) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        if (null != properties && !properties.isEmpty()){
            props = properties;
        }
    }

    private Object processIntercept(Invocation invocation) throws InvocationTargetException, IllegalAccessException {
        String interceptMethod = invocation.getMethod().getName();

        StatementHandler handler = (StatementHandler) PluginUtil.processTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(handler);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCmdType = ms.getSqlCommandType();
        if (sqlCmdType != SqlCommandType.UPDATE && sqlCmdType != SqlCommandType.INSERT) {
            return invocation.proceed();
        }


        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        //获取原始sql
        String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
        log.debug("==> originalSql: " + originalSql);
        //追加参数
        if (sqlCmdType == SqlCommandType.UPDATE ) {
        } else if (sqlCmdType == SqlCommandType.INSERT ) {
        }
        return invocation.proceed();
    }
}

