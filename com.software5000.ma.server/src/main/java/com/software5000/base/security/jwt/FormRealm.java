package com.software5000.base.security.jwt;

import com.software5000.base.security.PasswordEncryption;
import com.software5000.base.security.UserDefaultZimpl;
import com.software5000.base.security.UserRepositoryZimpl;
import com.zscp.master.util.ValidUtil;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import javax.annotation.Resource;

public class FormRealm extends AuthorizingRealm {

    @Resource
    private UserRepositoryZimpl userRepository;

    @Override
    public boolean supports(AuthenticationToken token) {
        return token != null && token instanceof UserDefaultZimpl;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) {
        UserDefaultZimpl upToken = (UserDefaultZimpl) token;
        if (!ValidUtil.isAnyEmpty(upToken.getUserId(), upToken.getPassword())) {
            return null;
        }

        UserDefaultZimpl user = userRepository.findByUserId(upToken);
        if (user != null) {
            SimpleAccount account = new SimpleAccount(user, user.getCredentials(), getName());
            account.addRole(user.getRoles());
            return account;
        }

        return null;
    }

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return new SimpleAuthorizationInfo(UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils().getRoles());
    }

    @Override
    protected void assertCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) throws AuthenticationException {
        if (!PasswordEncryption.checkPasswd(token.getCredentials().toString(), info.getCredentials().toString())) {
            String msg = "Submitted credentials for token [" + token + "] did not match the expected credentials.";
            throw new IncorrectCredentialsException(msg);
        }
    }
}
