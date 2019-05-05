package com.software5000.base.security;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.software5000.base.entity.BaseEntity;
import com.software5000.ma.entity.User;
import com.zscp.master.util.ClassUtil;
import com.zscp.master.util.ValidUtil;
import org.apache.shiro.SecurityUtils;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by wujin on 2016/12/22.
 */
public class UserDefaultUtil {

    static Log log = LogFactory.getLog(UserDefaultUtil.class);

    public static Integer getUserId() {
        Integer userId = null;
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            UserDefaultZimpl user = UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils();
            userId = user.getBusinessUser().getId();
        }
        return userId;
    }

    public static Integer getBusinessId() {
        Integer businessId = null;
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            UserDefaultZimpl user = UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils();
            businessId = user.getRealEntity().getId();
        }
        return businessId;
    }

    public static UserDefaultZimpl getUser() {
        UserDefaultZimpl user = null;
        if (SecurityUtils.getSubject().getPrincipal() != null) {
            user = (UserDefaultZimpl) SecurityUtils.getSubject().getPrincipal();
        }
        return user;
    }

    /**
     * 更新内存中用户的数据
     *
     * @param entity
     */
    public static void updateUser(BaseEntity entity) {
        //判断是否更新密码，如果是更新密码需更新UserDefaultZimpl中的password
        if (entity instanceof User) {
            User user = (User) entity;
            if (!ValidUtil.isEmpty(user.getPwd())) {
                getUser().setPassword(user.getPwd());
            }
        }
        //遍历出值为非空的属性进行UserDefaultZimpl中的entity更新
        for (String fieldName : ClassUtil.getColumnNames(entity.getClass(), false, null)) {
            Object value = ClassUtil.getValueByField(entity, fieldName);
            if (value != null && !ValidUtil.isEmpty(value)) {
                try {
                    ClassUtil.setValueByField(getUser().getRealEntity(), fieldName, value);
                } catch (IllegalAccessException|InvocationTargetException|NoSuchMethodException|NoSuchFieldException e) {
                    log.error("更新内存用户失败",e);
                }
            }
        }
    }

}
