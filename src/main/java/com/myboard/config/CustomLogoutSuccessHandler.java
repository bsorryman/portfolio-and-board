package com.myboard.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

/**
 * 로그아웃 성공 시 쿠키를 파기하는 핸들러
 * @author jspark
 *
 */
@Component
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {
	@Value("${domain}")
	private String domain;
	
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        // 쿠키 설정
        Cookie userInfoCookie = new Cookie("bs_us_c", null);
        userInfoCookie.setMaxAge(0);
        userInfoCookie.setPath("/");
        userInfoCookie.setDomain(domain);
        response.addCookie(userInfoCookie); 
        response.sendRedirect("/"); // 로그아웃 성공 시 이동할 페이지 설정
    }

}
