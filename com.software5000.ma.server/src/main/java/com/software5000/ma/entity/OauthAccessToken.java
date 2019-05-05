package com.software5000.ma.entity;

import java.util.Date;

/**
 * 微信授权
 */
public class OauthAccessToken {  
    // 获取到的凭证  
    private String token;  
    // 凭证有效时间，单位：秒  
    private int expiresIn;  
    //刷新凭证
    private String refreshToken;
    //openid
    private String openid;
    //scope
    private String scope;
    
    //获取时间
    private Date date;
    
    public String getToken() {  
        return token;  
    }  
  
    public void setToken(String token) {  
        this.token = token;  
    }  
  
    public int getExpiresIn() {  
        return expiresIn;  
    }  
  
    public void setExpiresIn(int expiresIn) {  
        this.expiresIn = expiresIn;  
    }

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getScope() {
		return scope;
	}

	public void setScope(String scope) {
		this.scope = scope;
	}  
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * 是否过期
	 * @return
	 */
	public boolean isExpired(){
		long diff = new Date().getTime() - date.getTime();
		return diff / 1000 > (expiresIn - 500);
	}
    
    
}  
