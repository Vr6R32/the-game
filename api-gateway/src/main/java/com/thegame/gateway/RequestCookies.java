package com.thegame.gateway;

import org.springframework.http.HttpCookie;
import org.springframework.util.MultiValueMap;

public record RequestCookies(String accessToken, String refreshToken) {

    public static RequestCookies extractCookiesFromRequest(MultiValueMap<String, HttpCookie> cookies) {
        String accessToken = null;
        String refreshToken = null;

        if (cookies != null) {
            HttpCookie accessTokenCookie = cookies.getFirst("accessToken");
            HttpCookie refreshTokenCookie = cookies.getFirst("refreshToken");

            if (accessTokenCookie != null) {
                accessToken = accessTokenCookie.getValue();
            }
            if (refreshTokenCookie != null) {
                refreshToken = refreshTokenCookie.getValue();
            }
        }

        return new RequestCookies(accessToken, refreshToken);
    }
}
