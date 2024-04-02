package com.myboard.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.myboard.util.EncryptedCookieUtil;
import com.myboard.util.AesUtil;
/**
 * 로컬 계정 로그인 성공시 쿠키를 등록하는 핸들러
 * (구글 소셜 로그인은 수동으로)
 * @author jspark
 *
 */
@Component
public class CustomLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${server}")
    private String server;
    
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
    
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws ServletException, IOException {
        
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName(); 
        
        Cookie encryptedCookie = 
                EncryptedCookieUtil.createEncryptedCookie("userinfo", username, server, 60 * 60 * 24 * 365);
        
        response.addCookie(encryptedCookie);
        
        // 에러 세션 제거
        clearSession(request);

        SavedRequest savedRequest = requestCache.getRequest(request, response);

        /**
         * prevPage 값이 있다면 사용자가 직접 URL 입력을 통해 접근한 경우이므로
         * 세션의 해당 속성을 삭제한다.
         */
        String prevPage = (String) request.getSession().getAttribute("prevPage");
//        if (prevPage != null) {
//            request.getSession().removeAttribute("prevPage");
//        }

        String uri = "/";

        /**
         * savedRequest가 있다면 접근 권한이 없는 URL에 접근하여 login 페이지로 들어온 것이므로
         * 접근하려 했던 URL을 저장해 놓는다.
         */
        if (savedRequest != null) {
            uri = savedRequest.getRedirectUrl();
            System.out.println("접근권한X: " + uri);
        } else if (prevPage != null && !prevPage.equals("")) {
            // 회원가입에서 넘어온 경우 메인페이지로 보낸다.
            if (prevPage.contains("/signup")) {
                uri = "/";
            } else {
                uri = prevPage;
                System.out.println("prevPage: " + prevPage);
            }
        }
        
        setDefaultTargetUrl("/");
        redirectStrategy.sendRedirect(request, response, uri);

    }
    
    /**
     * 에러 세션 제거 함수
     * @param request
     */
    private void clearSession(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }    
}
