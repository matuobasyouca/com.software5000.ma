package com.software5000.base.security.jwt;

import com.software5000.base.security.UserDefaultZimpl;
import com.software5000.base.security.UserRepositoryZimpl;
import com.zscp.master.util.ValidUtil;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;
import java.util.Arrays;

public class JWTRealm extends AuthorizingRealm {

    @Resource
    private UserRepositoryZimpl userRepository;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UserDefaultZimpl;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UserDefaultZimpl upToken = (UserDefaultZimpl) token;
        if(ValidUtil.isEmpty(((UserDefaultZimpl) token).getToken())){
            return null;
        }

        UserDefaultZimpl user = userRepository.findById(upToken);

        if (user != null && userRepository.validateToken(upToken.getToken())) {
            SimpleAccount account = new SimpleAccount(user, "", getName());
            account.addRole(user.getRoles());
            return account;
        }

        /**
         * 当用户不存在，但是jwt验证通过时，用户名为api的，就是接口访问带的token，直接授权为api
         */
        if (user == null && userRepository.validateToken(upToken.getToken())
                && "api".equals(upToken.getUserId().toString().toLowerCase())) {
            UserDefaultZimpl userDefaultZimpl = new UserDefaultZimpl("api",upToken.getToken());
            userDefaultZimpl.setUserType("api");
            SimpleAccount account = new SimpleAccount(userDefaultZimpl, "", getName());
            account.addRole(Arrays.asList("api"));
            return account;
        }


        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo(UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils().getRoles());
    }

}
