package com.software5000.base.mybatis.plugins;

import com.software5000.base.security.UserDefaultUtil;
import com.software5000.base.security.UserDefaultZimpl;
import com.zscp.master.util.ValidUtil;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.operators.conditional.AndExpression;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SubSelect;
import net.sf.jsqlparser.statement.update.Update;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.apache.shiro.ShiroException;

import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * Created by cc on 2016/12/22.
 */
@Intercepts({@Signature(type = Executor.class, method = "update", args = {MappedStatement.class, Object.class}),
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PermissionHelper implements Interceptor {

    private static final Log log = LogFactory.getLog(PermissionHelper.class);

    private static final ThreadLocal<Long> ignorePermission = new ThreadLocal<Long>();

    private static int MAPPED_STATEMENT_INDEX = 0;
    private static int PARAMETER_INDEX = 1;

    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        if (ignorePermission.get() == null) {
            processIntercept(invocation);
        } else {
            ignorePermission.remove();
        }
        return invocation.proceed();
    }

    private void processIntercept(Invocation invocation) {
        try {
            // 获取当前用户角色
//            UserDefaultZimpl principal = new UserDefaultZimpl();
//            principal.setUserType(Constant.UserType.MERCHANT);
//            BaseEntity baseEntity = new BaseEntity();
//            baseEntity.setId(61);
//            principal.setRealEntity(baseEntity);
            UserDefaultZimpl principal = UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils();
            List<PermissionRule> rules = PermissionRule.getAllRules();
            if (rules == null || rules.size() == 0 || principal == null || principal.getRoles() == null || principal.getRoles().size() == 0) {
                return; // 没有规则就退出
            }

            MappedStatement ms = (MappedStatement) invocation.getArgs()[MAPPED_STATEMENT_INDEX];
            Object parameter = invocation.getArgs()[PARAMETER_INDEX];
            BoundSql boundSql = ms.getBoundSql(parameter);
            String sql = boundSql.getSql().trim();

            String SqlCmdTyep = ms.getSqlCommandType().name();
            if ("update".equalsIgnoreCase(SqlCmdTyep)) {
                sql = processUpdateSql(sql, rules, principal);
            } else if ("delete".equalsIgnoreCase(SqlCmdTyep)) {
                sql = processDeleteSql(sql, rules, principal);
            } else if ("select".equalsIgnoreCase(SqlCmdTyep)) {
                sql = processSelectSql(sql, rules, principal);
            }

            BoundSql newBoundSql = new BoundSql(ms.getConfiguration(),
                    sql, boundSql.getParameterMappings(), boundSql
                    .getParameterObject());
            for (ParameterMapping mapping : boundSql.getParameterMappings()) {
                String prop = mapping.getProperty();
                if (boundSql.hasAdditionalParameter(prop)) {
                    newBoundSql.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
                }
            }
            MappedStatement newMs = copyFromMappedStatement(ms,
                    new BoundSqlSqlSource(newBoundSql));
            invocation.getArgs()[MAPPED_STATEMENT_INDEX] = newMs;

        } catch (ShiroException se) {
            log.info("当前Shiro框架中没有有效主题。");
        } catch (Exception e) {
            log.error("处理Sql出错！", e);
        }
    }

    private String processSelectSql(String sql, List<PermissionRule> rules, UserDefaultZimpl principal) {
        try {
            String replaceSql = null;
            Select select = (Select) CCJSqlParserUtil.parse(sql);
            PlainSelect selectBody = (PlainSelect) select.getSelectBody();
            String mainTable = null;
            if (selectBody.getFromItem() instanceof Table) {
                mainTable = ((Table) selectBody.getFromItem()).getName().replace("`", "");
            } else if (selectBody.getFromItem() instanceof SubSelect) {
                replaceSql = processSelectSql(((SubSelect) selectBody.getFromItem()).getSelectBody().toString(), rules, principal);
            }
            if (!ValidUtil.isEmpty(replaceSql)) {
                sql = sql.replace(((SubSelect) selectBody.getFromItem()).getSelectBody().toString(), replaceSql);
            }
            String mainTableAlias = mainTable;
            try {
                mainTableAlias = selectBody.getFromItem().getAlias().getName();
            } catch (Exception e) {
                log.debug("当前sql中， " + mainTable + " 没有设置别名");
            }


            String condExpr = null;
            PermissionRule realRuls = null;
            for (PermissionRule rule :
                    rules) {
                for (Object roleStr :
                        principal.getRoles()) {
                    if (rule.getRoles().indexOf("," + roleStr + ",") != -1) {
                        if (rule.getFromEntity().indexOf("," + mainTable + ",") != -1) {
                            // 若主表匹配规则主体，则直接使用本规则
                            realRuls = rule;

                            condExpr = rule.getExps().replace("{uid}", UserDefaultUtil.getUserId().toString()).replace("{bid}", UserDefaultUtil.getBusinessId().toString()).replace("{me}", mainTable).replace("{me.a}", mainTableAlias);
                            if (selectBody.getWhere() == null) {
                                selectBody.setWhere(CCJSqlParserUtil.parseCondExpression(condExpr));
                            } else {
                                AndExpression and = new AndExpression(selectBody.getWhere(), CCJSqlParserUtil.parseCondExpression(condExpr));
                                selectBody.setWhere(and);
                            }
                        }

                        try {
                            String joinTable = null;
                            String joinTableAlias = null;
                            for (Join j :
                                    selectBody.getJoins()) {
                                if (rule.getFromEntity().indexOf("," + ((Table) j.getRightItem()).getName() + ",") != -1) {
                                    // 当主表不能匹配时，匹配所有join，使用符合条件的第一个表的规则。
                                    realRuls = rule;
                                    joinTable = ((Table) j.getRightItem()).getName();
                                    joinTableAlias = j.getRightItem().getAlias().getName();

                                    condExpr = rule.getExps().replace("{uid}", UserDefaultUtil.getUserId().toString()).replace("{bid}", UserDefaultUtil.getBusinessId().toString()).replace("{me}", joinTable).replace("{me.a}", joinTableAlias);
                                    if (j.getOnExpression() == null) {
                                        j.setOnExpression(CCJSqlParserUtil.parseCondExpression(condExpr));
                                    } else {
                                        AndExpression and = new AndExpression(j.getOnExpression(), CCJSqlParserUtil.parseCondExpression(condExpr));
                                        j.setOnExpression(and);
                                    }
                                }
                            }
                        } catch (Exception e) {
                            log.debug("当前sql没有join的部分！");
                        }
                    }
                }
            }
            if (realRuls == null) return sql; // 没有合适规则直接退出。

            if (sql.indexOf("limit ?,?") != -1 && select.toString().indexOf("LIMIT ? OFFSET ?") != -1) {
                sql = select.toString().replace("LIMIT ? OFFSET ?", "limit ?,?");
            } else {
                sql = select.toString();
            }

        } catch (JSQLParserException e) {
            log.error("change sql error .", e);
        }
        return sql;
    }

    private String processUpdateSql(String sql, List<PermissionRule> rules, UserDefaultZimpl principal) {
        try {
            Update stm = (Update) CCJSqlParserUtil.parse(sql);
            String mainTable = stm.getTables().get(0).getName().replace("`", "");
            String mainTableAlias = mainTable;
            try {
                mainTableAlias = stm.getTables().get(0).getAlias().getName();
            } catch (Exception e) {
                log.debug("当前sql中， " + mainTable + " 没有设置别名");
            }

            PermissionRule realRuls = null;
            lv1:
            for (PermissionRule rule :
                    rules) {
                for (Object roleStr :
                        principal.getRoles()) {
                    if (rule.getRoles().indexOf("," + roleStr + ",") != -1) {
                        try {
                            for (Table t : stm.getTables()) {
                                if (rule.getFromEntity().indexOf("," + t.getName() + ",") != -1) {
                                    // 匹配主表
                                    realRuls = rule;
                                    mainTable = t.getName();
                                    mainTableAlias = t.getAlias().getName();
                                    break lv1;
                                }
                            }
                            for (Join j :
                                    stm.getJoins()) {
                                if (rule.getFromEntity().indexOf("," + ((Table) j.getRightItem()) + ",") != -1) {
                                    // 当主表不能匹配时，匹配所有join，使用符合条件的第一个表的规则。
                                    realRuls = rule;
                                    mainTable = ((Table) j.getRightItem()).getName();
                                    mainTableAlias = j.getRightItem().getAlias().getName();
                                    break lv1;
                                }
                            }
                        } catch (Exception e) {
                            log.debug("当前sql没有join的部分！");
                        }

                    }
                }
            }
            if (realRuls == null) return sql; // 没有合适规则直接退出。

            String condExpr = realRuls.getExps().replace("{uid}", UserDefaultUtil.getUserId().toString()).replace("{bid}", UserDefaultUtil.getBusinessId().toString()).replace("{me}", mainTable).replace("{me.a}", mainTableAlias);
            if (stm.getWhere() == null) {
                stm.setWhere(CCJSqlParserUtil.parseCondExpression(condExpr));
            } else {
                AndExpression and = new AndExpression(stm.getWhere(), CCJSqlParserUtil.parseCondExpression(condExpr));
                stm.setWhere(and);
            }

            sql = stm.toString();
        } catch (JSQLParserException e) {
            log.error("change sql error .", e);
        }
        return sql;
    }

    private String processDeleteSql(String sql, List<PermissionRule> rules, UserDefaultZimpl principal) {
        try {
            Delete stm = (Delete) CCJSqlParserUtil.parse(sql);
            String mainTable = stm.getTable().getName().replace("`", "");
            String mainTableAlias = mainTable;
            try {
                mainTableAlias = stm.getTable().getAlias().getName();
            } catch (Exception e) {
                log.debug("当前sql中， " + mainTable + " 没有设置别名");
            }

            PermissionRule realRuls = null;
            lv1:
            for (PermissionRule rule :
                    rules) {
                for (Object roleStr :
                        principal.getRoles()) {
                    if (rule.getRoles().indexOf("," + roleStr + ",") != -1) {
                        try {
                            if (rule.getFromEntity().indexOf("," + stm.getTable().getName() + ",") != -1) {
                                // 当主表不能匹配时，匹配所有join，使用符合条件的第一个表的规则。
                                realRuls = rule;
                                mainTable = stm.getTable().getName();
                                mainTableAlias = stm.getTable().getAlias().getName();
                                break lv1;
                            }
                        } catch (Exception e) {
                            log.debug("当前sql没有join的部分！");
                        }

                    }
                }
            }
            if (realRuls == null) return sql; // 没有合适规则直接退出。

            String condExpr = realRuls.getExps().replace("{uid}", UserDefaultUtil.getUserId().toString()).replace("{bid}", UserDefaultUtil.getBusinessId().toString()).replace("{me}", mainTable).replace("{me.a}", mainTableAlias);
            if (stm.getWhere() == null) {
                stm.setWhere(CCJSqlParserUtil.parseCondExpression(condExpr));
            } else {
                AndExpression and = new AndExpression(stm.getWhere(), CCJSqlParserUtil.parseCondExpression(condExpr));
                stm.setWhere(and);
            }

            sql = stm.toString();
        } catch (JSQLParserException e) {
            log.error("change sql error .", e);
        }
        return sql;
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms,
                                                    SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms
                .getConfiguration(), ms.getId(), newSqlSource, ms
                .getSqlCommandType());

        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        try {
            builder.keyProperty(ms.getKeyProperties()[0]);
        } catch (Exception e) {
            builder.keyProperty(null);
        }

        // setStatementTimeout()
        builder.timeout(ms.getTimeout());

        // setStatementResultMap()
        builder.parameterMap(ms.getParameterMap());

        // setStatementResultMap()
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());

        // setStatementCache()
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    public static void ignorePermissionThisTime() {
        ignorePermission.set(new Date().getTime());
    }

    @Override
    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    @Override
    public void setProperties(Properties properties) {
        //nothing
    }

    public static class BoundSqlSqlSource implements SqlSource {
        BoundSql boundSql;

        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }
}
