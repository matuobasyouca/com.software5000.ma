package com.software5000.base.mybatis.plugins;

import com.software5000.base.Constant;
import com.software5000.base.entity.SystemCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cc on 2016/12/24.<br>
 * SystemCode.codeType == PermissionRule
 */
public class PermissionRule {

    private static final Log log = LogFactory.getLog(PermissionRule.class);
    /**
     * codeName<br>
     * 适用角色列表<br>
     * 格式如: ,RoleA,RoleB,
     */
    private String roles;
    /**
     * codeValue<br>
     * 主实体，多表联合
     * 格式如: ,SystemCode,User,
     */
    private String fromEntity;
    /**
     * codeDesc<br>
     * 过滤表达式字段, <br>
     * <code>{uid}</code>会自动替换为当前用户的userId<br>
     * <code>{me}</code> main entity 主实体名称
     * <code>{me.a}</code> main entity alias 主实体别名
     * 格式如：
     * <ul>
     * <li>userId = {uid}</li>
     * <li>(userId = {uid} AND authType > 3)</li>
     * <li>((userId = {uid} AND authType) > 3 OR (dept in (select dept from depts where manager.id = {uid})))</li>
     * </ul>
     */
    private String exps;

    /**
     * codeShowName<br>
     * 规则说明
     */
    private String ruleComment;


    private static List<PermissionRule> allRule = null;
    private static long refreshTime = 0L;

    public static List getAllRules() {
        if (Constant.refreshTime() > refreshTime) {
            log.debug("尝试更新权限限制规则！");
            allRule = new ArrayList<>();
            refreshTime = Constant.refreshTime();
            for (SystemCode c :
                    Constant.getAllCodes()) {
                if ("PermissionRule".equals(c.getCodeType())) {
                    PermissionRule rule = new PermissionRule();
                    rule.setExps(c.getCodeDesc());
                    rule.setRoles(c.getCodeName());
                    rule.setFromEntity(c.getCodeValue());
                    rule.setRuleComment(c.getCodeShowName());
                    allRule.add(rule);
                }
            }
            log.debug("更新权限限制规则完成！");
        }
        return allRule;
    }


    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public String getRuleComment() {
        return ruleComment;
    }

    public void setRuleComment(String ruleComment) {
        this.ruleComment = ruleComment;
    }

    public String getFromEntity() {
        return fromEntity;
    }

    public void setFromEntity(String fromEntity) {
        this.fromEntity = fromEntity;
    }

    public String getExps() {
        return exps;
    }

    public void setExps(String exps) {
        this.exps = exps;
    }
}
