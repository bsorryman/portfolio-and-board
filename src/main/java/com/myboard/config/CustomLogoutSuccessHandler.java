package com.myboard.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * 로그아웃 성공 시 쿠키를 파기하는 핸들러
 * @author jspark
 *
 */
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 쿠키 설정
        Cookie userInfoCookie = new Cookie("userInfo", null);
        userInfoCookie.setMaxAge(0);
        userInfoCookie.setPath("/");
        response.addCookie(userInfoCookie); 
        
        response.sendRedirect("/login?logout=true"); // 로그아웃 성공 시 이동할 페이지 설정
    }

}
