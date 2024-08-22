package com.myboard.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.myboard.user.service.UserDetailsServiceImpl;
import com.myboard.util.AesUtil;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CustomFilter extends OncePerRequestFilter {
    
    private final UserDetailsServiceImpl userDetailsService;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        // 인증된 사용자인지 확인
        try {
            Authentication existingAuth = SecurityContextHolder.getContext().getAuthentication();
            
            if (existingAuth == null || !existingAuth.isAuthenticated()) {
                // 사용자 쿠키 추출
                Cookie[] cookies = request.getCookies();
                String userInfo = "";
                if (cookies != null) {
                    for (Cookie cookie : cookies) {
                        if (cookie.getName().equals("bs_us_c")) {
                             userInfo = cookie.getValue();
                        }
                    }
                }
                
                // 사용자 쿠키 값이 있다면
                if (!(userInfo.isEmpty())) {
                    AesUtil aesUtil = new AesUtil();
                    String decryptedUserinfo = aesUtil.decrypt(userInfo);
                    
                    // 인증
                    UserDetails userDetails = userDetailsService.loadUserByUsername(decryptedUserinfo);
                    
                    if (userDetails != null) {
                        // 비밀번호 없이 인증
                        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }                
                } 
                
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        filterChain.doFilter(request, response);
    }

}
