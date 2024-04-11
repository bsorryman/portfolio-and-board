package com.myboard.util;

import javax.servlet.http.Cookie;

public class EncryptedCookieUtil {
    
    public static Cookie createEncryptedCookie(String cookieName, String cookieValue, String domain, int age) {
        AesUtil aesUtil = new AesUtil();
        String encryptedUsername = aesUtil.encrypt(cookieValue);
  
        Cookie cookie = new Cookie(cookieName, encryptedUsername);
        cookie.setMaxAge(age);
        cookie.setDomain(domain);
        cookie.setPath("/");
        
        return cookie;
    }
}
