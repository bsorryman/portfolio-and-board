package com.myboard.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.Cookie;

public class EncryptedCookieUtil {
    
    public static Cookie createEncryptedCookie(String cookieName, String cookieValue, String server, int age) {
        AesUtil aesUtil = new AesUtil();
        String encryptedUsername = aesUtil.encrypt(cookieValue);
  
        Cookie cookie = new Cookie("userinfo", encryptedUsername);
        cookie.setMaxAge(age);
        
        // Set Cookie domain (with regex)
        String domain = "";
        Pattern pattern = Pattern.compile("/([^/:]+)");
        Matcher matcher = pattern.matcher(server);
        if (matcher.find()) {
            domain = matcher.group(1);
        }
        
        cookie.setDomain(domain);
        cookie.setPath("/");
        
        return cookie;
    }
}
