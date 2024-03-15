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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class CustomFilter extends OncePerRequestFilter {
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        // userInfo 쿠키 추출
        Cookie[] cookies = request.getCookies();
        String userInfo = "";
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userInfo")) {
                     userInfo = cookie.getValue();
                     System.out.println(userInfo);
                }
            }
        }

        if (userInfo.isEmpty() || userInfo == "") {
            // 쿠키가 없다면 pass
            filterChain.doFilter(request,response);
            return;
        } else {
            Authentication authenticationAlready = SecurityContextHolder.getContext().getAuthentication();

            if (authenticationAlready != null && authenticationAlready.isAuthenticated()) {
                // 이미 인증되어있다면 pass
                filterChain.doFilter(request,response);
                return;
            } else {
                // 쿠키가 있고 인증이 안되었다면 인증 후 넘긴다
                Authentication authentication = new UsernamePasswordAuthenticationToken(userInfo, null);
                
                try {
                    Authentication authenticated = authenticationManager.authenticate(authentication);
                    SecurityContextHolder.getContext().setAuthentication(authenticated);
                    
                    if (authenticated == null) {
                        System.out.println("authenticated == null");
                        filterChain.doFilter(request, response);  
                        return;
                    }
                    System.out.println("authenticated != null");
                    filterChain.doFilter(request, response);  
                    return;
                } catch (AuthenticationException e) {
                    
                    filterChain.doFilter(request, response);  
                    return;
                }    
            }
        }
        
    }

}
