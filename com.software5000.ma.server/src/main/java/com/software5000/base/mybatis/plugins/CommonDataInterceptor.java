package com.software5000.base.mybatis.plugins;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.expression.operators.relational.ExpressionList;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.expression.operators.relational.MultiExpressionList;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.insert.Insert;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.SqlCommandType;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * @author 605162215@qq.com
 * @date 2016-06-23
 */
@Intercepts({@Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})})
public class CommonDataInterceptor implements Interceptor {

    private static final Log log = LogFactory.getLog(CommonDataInterceptor.class);
    private static final SimpleDateFormat TIMESTAMP_FORMAT = new SimpleDateFormat("-yyyy-MM-dd HH:mm:ss.SSS-");
    private static final SimpleDateFormat TIMESTAMP_FORMAT2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private static final ThreadLocal<Long> ignoreData = new ThreadLocal<Long>();

    private Properties props = null;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (ignoreData.get() == null) {
            processIntercept(invocation);
        } else {
            ignoreData.remove();
        }
        return invocation.proceed();
    }

    public Object processIntercept(Invocation invocation) throws Throwable {
        String interceptMethod = invocation.getMethod().getName();
        if (!"prepare".equals(interceptMethod)) {
            return invocation.proceed();
        }

        StatementHandler handler = (StatementHandler) PluginUtil.processTarget(invocation.getTarget());
        MetaObject metaObject = SystemMetaObject.forObject(handler);
        MappedStatement ms = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
        SqlCommandType sqlCmdType = ms.getSqlCommandType();
        if (sqlCmdType != SqlCommandType.UPDATE && sqlCmdType != SqlCommandType.INSERT) {
            return invocation.proceed();
        }
        //获取配置参数
        String createDateColumn, updateDateColumn;

        createDateColumn = "createTime";
        updateDateColumn = "updateTime";


        BoundSql boundSql = (BoundSql) metaObject.getValue("delegate.boundSql");
        Object parameterObject = boundSql.getParameterObject();
        //获取原始sql
        String originalSql = (String) metaObject.getValue("delegate.boundSql.sql");
        log.debug("==> originalSql: " + originalSql);
        //追加参数
        String newSql = "";
        if (sqlCmdType == SqlCommandType.UPDATE && updateDateColumn.length() > 0) {
            newSql = changeSqlForDate(originalSql);
        } else if (sqlCmdType == SqlCommandType.INSERT && createDateColumn.length() > 0) {
            newSql = changeInsertData(originalSql);
        }
        //修改原始sql
        if (newSql.length() > 0) {
            log.debug("==> newSql after change create/update time : " + newSql);
            metaObject.setValue("delegate.boundSql.sql", newSql);
        }
        return invocation.proceed();
    }

    private String changeInsertData(String sqls) {

        int createTimeIndex = -1;
        try {
            Statement stmt = CCJSqlParserUtil.parse(sqls);
            Insert insert = (Insert) stmt;
            List<Column> columns = insert.getColumns();
            for (int ci = 0; ci < columns.size(); ci++) {
                if (columns.get(ci).getColumnName().equalsIgnoreCase("createTime")) {
                    createTimeIndex = ci;
                }
            }
            ItemsList itemList = insert.getItemsList();
            if (itemList instanceof ExpressionList) {
                if (createTimeIndex == -1) {
                    columns.add(new Column("createTime"));
                    ((ExpressionList) itemList).getExpressions().add(new StringValue(TIMESTAMP_FORMAT.format(new Date())));
                }else {
                    ((ExpressionList) itemList).getExpressions().set(createTimeIndex, new StringValue((TIMESTAMP_FORMAT.format(new Date()))));
//                    ((StringValue) ((ExpressionList) itemList).getExpressions().get(createTimeIndex)).setValue(TIMESTAMP_FORMAT2.format(new Date()));
                }
            }else if (itemList instanceof MultiExpressionList) {
                for (ExpressionList  el : ((MultiExpressionList)itemList).getExprList()
                        ) {
                    if (createTimeIndex == -1) {
                        columns.add(new Column("createTime"));
                        el.getExpressions().add(new StringValue(TIMESTAMP_FORMAT.format(new Date())));
                    }else {
                        el.getExpressions().set(createTimeIndex, new StringValue((TIMESTAMP_FORMAT.format(new Date()))));
                    }
                }
            }
            return insert.toString();
        } catch (JSQLParserException e) {
            log.error("change insert sql date error!", e);
        }


        return null;
    }

    private String changeSqlForDate(String sqls) {
        StringBuilder sb = new StringBuilder();
        // 可能是多个sql
        for (String sql : sqls.split(";")) {
            // 简单来，直接在where之前增加一个updateTime的时间设置
            if (sql.toLowerCase().indexOf(" set ") != -1)
                sql = sql.replaceAll("(?i) set ", " SET updateTime = '" + TIMESTAMP_FORMAT2.format(new Date()) + "',");
            else
                sql = sql + ",updateTime = '" + TIMESTAMP_FORMAT2.format(new Date()) + "'";
            sb.append(sql);
            if (sb.toString().length() > 0) {
                sb.append(";");
            }
        }

        return sb.toString();
    }

    private boolean contains(List<Column> columns, String columnName) {
        if (columns == null || columns.size() <= 0) {
            return false;
        }
        if (columnName == null || columnName.length() <= 0) {
            return false;
        }
        for (Column column : columns) {
            if (column.getColumnName().equalsIgnoreCase(columnName)) {
                return true;
            }
        }
        return false;
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
        if (null != properties && !properties.isEmpty()) props = properties;
    }

    public static void ignoreDataThisTime(){ignoreData.set(new Date().getTime());}

}
