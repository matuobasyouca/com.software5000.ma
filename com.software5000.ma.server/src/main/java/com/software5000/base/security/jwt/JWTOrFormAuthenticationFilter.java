package com.software5000.base.security.jwt;

import com.alibaba.fastjson.JSON;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import com.software5000.base.Constant;
import com.software5000.base.entity.ReturnResult;
import com.software5000.base.security.UserDefaultZimpl;
import com.zscp.master.util.ValidUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Set;

//@Component
public final class JWTOrFormAuthenticationFilter extends AuthenticatingFilter {
    protected Log log = LogFactory.getLog(JWTOrFormAuthenticationFilter.class);

    public static final String USER_ID = "userId";
    public static final String PASSWORD = "password";
    public static final String USER_TYPE = "userType";

    public static final String AUTHORIZATION_HEADER = "Authorization";

    public JWTOrFormAuthenticationFilter() {
        setLoginUrl(DEFAULT_LOGIN_URL);
    }

    @Override
    public void setLoginUrl(String loginUrl) {
        String previous = getLoginUrl();
        if (previous != null) {
            this.appliedPaths.remove(previous);
        }
        super.setLoginUrl(loginUrl);
        this.appliedPaths.put(getLoginUrl(), null);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        boolean loggedIn = false;

        // 如果关掉浏览器后，重新访问链接cookie还在，则直接用jwt信息尝试登录。
        if (isLoggedAttempt(request, response) && !SecurityUtils.getSubject().isAuthenticated()) {
            //存在jwt信息，并且未登录
            loggedIn = executeLogin(request, response);
        }

        if (!loggedIn) {
            onUnauthorized(request, response);
        }

        return loggedIn;
    }

    private void onUnauthorized(ServletRequest request, ServletResponse response) throws IOException {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String uriString = httpRequest.getRequestURI().toLowerCase();
        if (uriString.endsWith(".html")) {
            if (isLoggedAttempt(request, response) && SecurityUtils.getSubject().isAuthenticated()) { // 已经登录需要跳转值默认页面
                String userType = UserDefaultZimpl.getUserDefaultZimplFromSecurityUtils().getUserType();

                if (userType.equals(Constant.UserType.CUSTOMER)) {
                    WebUtils.issueRedirect(request, response, Constant.getCodeByName(Constant.SecuritySetting.CUSTOMER_DEAFULT_URL).getCodeValue());
                } else if (userType.equals(Constant.UserType.MERCHANT)) {
                    WebUtils.issueRedirect(request, response, Constant.getCodeByName(Constant.SecuritySetting.MERCHANT_DEAFULT_URL).getCodeValue());
                } else if (userType.equals(Constant.UserType.OPERATOR)) {
                    WebUtils.issueRedirect(request, response, Constant.getCodeByName(Constant.SecuritySetting.OPERATOR_DEAFULT_URL).getCodeValue());
                } else if (userType.equals(Constant.UserType.ZSOPERATOR)) {
                    WebUtils.issueRedirect(request, response, Constant.getCodeByName(Constant.SecuritySetting.ZSOPERATOR_DEAFULT_URL).getCodeValue());
                }
            } else { // 未登录则需要登录
                WebUtils.saveRequest(request);
                if (uriString.contains("/merchant")) { // 商户和操作员同一个界面登录
                    WebUtils.issueRedirect(request, response, Constant.getCodeByName(Constant.SecuritySetting.MERCHANT_REDIRECT_URL).getCodeValue());
                } else if (uriString.contains("/customer/")) {
                    WebUtils.issueRedirect(request, response, Constant.getCodeByName(Constant.SecuritySetting.CUSTOMER_REDIRECT_URL).getCodeValue());
                } else if (uriString.contains("/operator/")) {
                    WebUtils.issueRedirect(request, response, Constant.getCodeByName(Constant.SecuritySetting.ZSOPERATOR_REDIRECT_URL).getCodeValue());
                }

            }
        } else {
            HttpServletResponse httpResponse = WebUtils.toHttp(response);
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().println(JSON.toJSON(ReturnResult.buildResult(Constant.StateCode.MERCHANT_NOT_AUTHENTICATION.codeName)));
        }
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        Subject subject = getSubject(request, response);
        String[] rolesArray = (String[]) mappedValue;

        if (rolesArray == null || rolesArray.length == 0) {
            //no roles specified, so nothing to check - allow access.
            return subject.isAuthenticated();
        }
        Set<String> roles = CollectionUtils.asSet(rolesArray);
        for(String r : roles){
            if(subject.hasRole(r)) return true;
        }
        return false;
    }


    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws IOException {

        if (isLoginRequest(request, response)) {

            InputStream is = request.getInputStream();
            DataInputStream input = new DataInputStream(is);
            String json = input.readUTF();
            if (json != null && !json.isEmpty()) {

                JSON.parseObject(json).get(USER_ID);
                String username = JSON.parseObject(json).get(USER_ID).toString();
                String password = JSON.parseObject(json).get(PASSWORD).toString();
                String usertype = JSON.parseObject(json).get(USER_TYPE).toString();
                return new UserDefaultZimpl(username, password, usertype);
            }
        }

        if (isLoggedAttempt(request, response)) {
            String jwtToken = getAuthzHeader(request);
            if (jwtToken != null) {
                return createToken(jwtToken,request);
            }
        }

        return null;
    }

    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpResponse = WebUtils.toHttp(response);
        httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        return false;
    }

    protected boolean isLoggedAttempt(ServletRequest request, ServletResponse response) {
        String authzHeader = getAuthzHeader(request);
        return authzHeader != null;
    }

    protected String getAuthzHeader(ServletRequest request) {
        HttpServletRequest httpRequest = WebUtils.toHttp(request);
        String jwtToken = httpRequest.getHeader(AUTHORIZATION_HEADER);
        if (jwtToken == null) {
            // get token from cookies
            try {
                for (Cookie c : httpRequest.getCookies()) {
                    if ("ztoken".equals(c.getName())) {
                        jwtToken = c.getValue();
                    }
                }
            } catch (Exception e) {
                if(!(e instanceof NullPointerException))
                    log.info("get ztoken from cookie error!", e);
            }
        }
        return jwtToken;
    }

    public UserDefaultZimpl createToken(String token,ServletRequest request) {
        try {
            JWSObject jwsObject = JWSObject.parse(token);
            String decrypted = jwsObject.getPayload().toString();


            String userId = JSON.parseObject(decrypted).get("sub").toString();
            OhterWebToken ohterWebToken = checkCanAllowByOtherSys(request);
            if(ohterWebToken != null) {
                if (Constant.VisitWebType.API.codeName.equals(ohterWebToken.getTokenType())){
                    return new UserDefaultZimpl("api", token);
                }
            }
            return new UserDefaultZimpl(userId, token);

        } catch (ParseException ex) {
            throw new AuthenticationException(ex);
        }

    }

    //其他系统调用本系统情况
    private OhterWebToken checkCanAllowByOtherSys(ServletRequest request){
        String authzHeader = getAuthzHeader(request);
        if(ValidUtil.isNotEmpty(authzHeader)){
            try {
                String[] issArray = {Constant.VisitWebIss.CrmIss.codeName,Constant.VisitWebIss.EmktIss.codeName};
                JWT parse = JWTParser.parse(authzHeader);
                Object iss = parse.getJWTClaimsSet().getClaims().get("iss");
                if(iss != null && Arrays.asList(issArray).contains(iss.toString().toLowerCase())){//表示其他系统
                    //1.其他系统的看是否是接口，是接口就通过（实现其他系统通过前端直接调用本系统接口）,如果是页面直接登录
                    if(WebUtils.toHttp(request).getRequestURI().toLowerCase().endsWith(".html")){
                        return new OhterWebToken(Constant.VisitWebType.HTML.codeName,iss.toString());
                    }else{
                        return new OhterWebToken(Constant.VisitWebType.API.codeName,iss.toString());
                    }
                }
            } catch (ParseException e) {
                log.error("JWTParser.parse ERROR",e);
            }
        }

        return null;
    }


    class OhterWebToken{

        public OhterWebToken(String tokenType, String iss) {
            this.tokenType = tokenType;
            this.iss = iss;
        }

        private String tokenType;
        private String iss;

        public String getTokenType() {
            return tokenType;
        }

        public void setTokenType(String tokenType) {
            this.tokenType = tokenType;
        }

        public String getIss() {
            return iss;
        }

        public void setIss(String iss) {
            this.iss = iss;
        }
    }

}
