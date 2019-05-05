package com.software5000.base.security.jwt;


import com.software5000.base.security.UserDefaultZimpl;

public class TokenResponse {

    public TokenResponse() {
    }

    public TokenResponse(UserDefaultZimpl user, String token) {
        this.user = user;
        this.token = token;
    }

    private String token;

    private UserDefaultZimpl user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public UserDefaultZimpl getUser() {
        return user;
    }

    public void setUser(UserDefaultZimpl user) {
        this.user = user;
    }
}
