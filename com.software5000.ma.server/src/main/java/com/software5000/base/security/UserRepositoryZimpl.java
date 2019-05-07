package com.software5000.base.security;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.software5000.base.BaseDao;
import com.software5000.base.Constant;
import com.software5000.base.mybatis.plugins.PermissionHelper;
import com.software5000.base.security.jwt.MACVerifierExtended;
import com.software5000.base.security.jwt.TokenResponse;
import com.software5000.ma.entity.Business;
import com.software5000.ma.entity.BusinessUser;
import com.software5000.ma.entity.Operator;
import com.software5000.ma.entity.User;
import com.zscp.master.util.DateUtils;
import com.zscp.master.util.ValidUtil;
import com.zscp.master.util.encrypt.MD5Builder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class UserRepositoryZimpl {
    private Log log = LogFactory.getLog(UserRepositoryZimpl.class);

    private byte[] sharedKey;
    /**
     * 登录成功需要保存的jwt用户信息，以后可以改为存在redis中。
     */
    private final Map<Object, UserDefaultZimpl> jwtUserStore = new HashMap<>();

    private String issUsertype;

    @Resource
    private BaseDao baseDao;

//    @Resource
//    private SmsService smsService;

    /**
     * findByUserId is used by FormRealm to get the user's information using the field userId
     */
    public UserDefaultZimpl findByUserId(UserDefaultZimpl userToken) {
        UserDefaultZimpl user = getUserFromDb(userToken);
        if (user != null) {
            jwtUserStore.put(user.getUserId(), user);
        }
        return user;
    }

    /**
     * findById is used by JWTRealm to get the user's information from the sub field in the JWT
     */
    public UserDefaultZimpl findById(UserDefaultZimpl userToken) {
        UserDefaultZimpl userDefault = jwtUserStore.get(userToken.getUserId());
        try {
            SignedJWT signed = SignedJWT.parse(userToken.getToken());
            JWTClaimsSet claimsSet = signed.getJWTClaimsSet();

            // 内存中的过期时间 必须是 yyyy-MM-dd HH:mm:ss  ,过期熔断机制
            Date lastDate = null;
            try {
                lastDate = DateUtils.parse(Constant.getCodeByName(Constant.SecuritySetting.MERCHANT_EXPIRED_TIME).getCodeValue(), "yyyy-MM-dd HH:mm:ss");
            } catch (Exception e) {
                lastDate = DateUtils.parse("2017-01-01 00:00:00", DateUtils.DATE_FULL_STR);
            }
            if (claimsSet.getIssueTime().getTime() < lastDate.getTime()) {
                return null;
            }


            if (ValidUtil.isEmpty(userDefault)) {
                userToken.setUserType(claimsSet.getIssuer().replace(this.getIssuer() + ".", ""));
                userDefault = getUserFromDb(userToken);
                if (userDefault.getRealEntity().getUpdateTime().getTime() > claimsSet.getIssueTime().getTime()) {
                    return null;
                }
                jwtUserStore.put(userDefault.getUserId(), userDefault);
            }
        } catch (Exception e) {
            return userDefault;
        }

        return userDefault;
    }

    private UserDefaultZimpl getUserFromDb(UserDefaultZimpl userToken) {
        UserDefaultZimpl user = null;
        if (Constant.UserType.MERCHANT.equals(userToken.getUserType())) {
            BusinessUser b = new BusinessUser();
            Business business = new Business();
            b.setUserName(userToken.getUserId());
            b.setState(Constant.BusinessUserState.ON_WORK.codeName);
            try {
                PermissionHelper.ignorePermissionThisTime();
                b = baseDao.selectSingleEntity(b);
                business.setId(b.getBusinessId());
                business = baseDao.selectSingleEntity(business);
                user = new UserDefaultZimpl(b.getUserName(), b.getPwd(), b.getMercharType(), business, b);
            } catch (Exception e) {
                log.error("select MERCHANT user error! ", e);
            }
        } else if (Constant.UserType.CUSTOMER.equals(userToken.getUserType())) {
            User u = new User();
            u.setMobile(userToken.getUserId());
            try {
                u = baseDao.selectEntity(u).get(0);
                //判断是手机号验证码登陆还是手机号密码登陆
                if (userToken.getLoginType().equals(Constant.LoginType.CUSTOMER_MOBILE_CODE_LOGIN)) {
//                    SmsRecord smsRecord = smsService.getSmsRecord(u.getMobile(), SmsService.SmsRequestType.USER_USE_MOBILE_LOGIN);
//                    if (smsRecord != null) {
//                        String loginCodePwd = PasswordEncryption.toPasswd(smsRecord.getExpand());
//                        user = new UserDefaultZimpl(u.getMobile(), loginCodePwd, Constant.UserType.CUSTOMER, u);
//                    }
                } else if (userToken.getLoginType().equals(Constant.LoginType.CUSTOMER_MOBILE_PASSWORD_LOGIN)) {
                    user = new UserDefaultZimpl(u.getMobile(), u.getPwd(), Constant.UserType.CUSTOMER, u);
                } else if (userToken.getLoginType().equals(Constant.LoginType.CUSTOMER_WXOPENID_LOGIN)) {
                    user = new UserDefaultZimpl(u.getMobile(), PasswordEncryption.toPasswd(u.getWxOpenId()), Constant.UserType.CUSTOMER, u);
                }
//                user = new UserDefaultZimpl(u.getMobile(), u.get.getPwd(), Constant.UserType.MERCHANT, b);
            } catch (SQLException e) {
                log.error("select CUSTOMER user error! ", e);
            }

        } else if (Constant.UserType.ZSOPERATOR.equals(userToken.getUserType())) {
            try {
                Operator operator = new Operator();
                operator.setUserName(userToken.getUserId());
                operator.setState(0);
                operator = baseDao.selectSingleEntity(operator);
                user = new UserDefaultZimpl(operator.getUserName(), operator.getPassword(), userToken.getUserType(), operator);
            } catch (Exception e) {
                log.error("select CUSTOMER user error! ", e);
            }

        }
        return user;
    }

    public String getIssuer() {
        return "com.zscp.ma";
    }

    public long getExpirationDate() {
        try {
            return 1000 * 60 * 60 * Integer.valueOf(Constant.getCodeByName(Constant.SecuritySetting.MERCHANT_EXPIRED_TIME).getCodeValue());
        } catch (Exception e) {
            return 1000 * 60 * 60 * 24 * 30L; // 30天过期
        }
    }

    public byte[] getSharedKey() {
        return MD5Builder.getMD5Str(Constant.getStringCodeValueByName(Constant.SysConfig.JWT_SHARED_KEY)).getBytes();
    }

    public TokenResponse createToken(UserDefaultZimpl user) {
        TokenResponse response = new TokenResponse();
        response.setUser(user);

        String token = null;
        try {
            JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();

            builder.issuer(getIssuer() + "." + user.getUserType());
            builder.subject(user.getUserId());
            builder.issueTime(new Date());
            builder.notBeforeTime(new Date());
            builder.expirationTime(new Date(new Date().getTime() + getExpirationDate()));
            builder.jwtID(UUID.randomUUID().toString());

            JWTClaimsSet claimsSet = builder.build();
            JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

            Payload payload = new Payload(claimsSet.toJSONObject());

            JWSObject jwsObject = new JWSObject(header, payload);

            JWSSigner signer = new MACSigner(getSharedKey());
            jwsObject.sign(signer);
            token = jwsObject.serialize();
        } catch (JOSEException ex) {
            return null;
        }

        response.setToken(token);
        return response;
    }

    public boolean validateToken(String token) {

        try {
            SignedJWT signed = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifierExtended(getSharedKey(), signed.getJWTClaimsSet());
            return signed.verify(verifier);
        } catch (Exception ex) {
            return false;
        }

    }

}
