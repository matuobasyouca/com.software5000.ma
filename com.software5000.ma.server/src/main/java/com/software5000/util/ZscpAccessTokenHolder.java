package com.software5000.util;

import com.riversoft.weixin.common.AccessToken;
import com.riversoft.weixin.common.AccessTokenHolder;
import com.software5000.base.Constant;

/**
 * Created by Simo on 2017/7/1.
 */
public class ZscpAccessTokenHolder extends AccessTokenHolder {

    private AccessToken accessToken;

    public ZscpAccessTokenHolder() {
        super(null,null,null);
    }

    public ZscpAccessTokenHolder(String tokenUrl, String clientId, String clientSecret) {
        super(tokenUrl, clientId, clientSecret);
    }

    @Override
    public AccessToken getAccessToken() {
        if (accessToken == null || accessToken.expired()) {
            this.setTokenUrl(Constant.emktUrl + "/open/wx/selectAccessToken");
            String content = fetchAccessToken();
            AccessToken accessToken = AccessToken.fromJson(content);
            this.accessToken = accessToken;
        }
        return this.accessToken;
    }

    @Override
    public void refreshToken() {
        if (accessToken == null || accessToken.expired()) {
            this.setTokenUrl(Constant.emktUrl + "/open/wx/selectNewAccessToken");
            String content = fetchAccessToken();
            AccessToken accessToken = AccessToken.fromJson(content);
            this.accessToken = accessToken;
        }
    }

    @Override
    public void expireToken() {
        accessToken.setExpiresIn(-30);//强制设置为无效
    }
}
