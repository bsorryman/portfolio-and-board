package com.myboard.google.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import com.myboard.google.dto.GoogleInfResponse;
import com.myboard.google.service.GoogleOAuthService;
import com.myboard.user.dto.UserDTO;
import com.myboard.user.service.UserService;
import com.myboard.util.EncryptedCookieUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class GoogleOAuthController {
    @Value("${google.client.id}")
    private String googleClientId;
    
    @Value("${google.client.secret}")
    private String googleClientSecret;
    
    @Value("${server}")
    private String server;

    @Value("${domain}")
    private String domain;
    
    private final RequestCache requestCache = new HttpSessionRequestCache();
    
    private final UserService userService;
    private final GoogleOAuthService googleOAuthService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    
    /**
     * 사용자가 구글 로그인을 할 수 있는 URL을 리턴하는 함수
     * @param type
     * @return
     */
    @GetMapping("/{type}/google/auth")
    public String loginUrlGoogle(@PathVariable("type") String type){
        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri="+server+"/"+type+"/google/access&response_type=code&scope=email%20profile%20openid&access_type=offline&prompt=select_account";

        return "redirect:" + reqUrl;
    }
    
    /**
     * authCode 값을 사용해서 구글 사용자 정보를 가져와 사용자 인증(로그인)하는 함수.
     * 
     * @param authCode - authorization_code
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/login/google/access")
    public String loginGoogle(@RequestParam(value = "code") String authCode,
            HttpServletRequest request,
            HttpServletResponse response){
        try {
            ResponseEntity<GoogleInfResponse> resultEntity = 
                    googleOAuthService.getGoogleUserInfo(authCode, "login", googleClientId, googleClientSecret, server);
            
            // 사용자 정보 매핑
            String googleEmail = resultEntity.getBody().getEmail();  
            String googleSub = resultEntity.getBody().getSub();
            // sub(password로 사용할 필드) 암호화
            String encodedGoogleSub = passwordEncoder.encode(googleSub);
            
            // User 객체 매핑
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(googleEmail);
            userDTO.setPassword(encodedGoogleSub);
            userDTO.setSns("google");     
            
            /**
             * Security 로그인 처리 (UserDetails)
             */
            // userName(google eamil)로 UserDetails 가져오기
            UserDetails userDetails = userDetailsService.loadUserByUsername(googleEmail);

            // 인증 토큰 생성
            UsernamePasswordAuthenticationToken token = 
                    new UsernamePasswordAuthenticationToken(userDetails, encodedGoogleSub, userDetails.getAuthorities());

            // 인증 
            authenticationManager.authenticate(token);
            boolean result = token.isAuthenticated();
            
            // 인증 성공 및 실패 
            if (result) {
                SecurityContextHolder.getContext().setAuthentication(token);
                
                Cookie encryptedCookie = 
                        EncryptedCookieUtil.createEncryptedCookie("bs_us_c", googleEmail, domain, 60 * 60 * 24 * 365);

                
                response.addCookie(encryptedCookie); 
                
                // 에러 세션 제거
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                }
                
                String prevPage = (String) request.getSession().getAttribute("prevPage");
//                if (prevPage != null) {
//                    request.getSession().removeAttribute("prevPage");
//                }

                String uri = "/";

                /**
                 * savedRequest가 있다면 접근 권한이 없는 URL에 접근하여 login 페이지로 들어온 것이므로
                 * 접근하려 했던 URL을 저장해 놓는다.
                 */
                SavedRequest savedRequest = requestCache.getRequest(request, response);
                
                if (savedRequest != null) {
                    uri = savedRequest.getRedirectUrl();
                } else if (prevPage != null && !prevPage.equals("")) {
                    // 회원가입에서 넘어온 경우 메인페이지로 보낸다.
                    if (prevPage.contains("/signup") || prevPage.contains("type=signup")) {
                        uri = "/";
                    } else {
                        uri = prevPage;

                    }
                }
                
                return "redirect:" + uri;
            } else {
                
                return "redirect:/login?type=no";
            }            
        } catch (UsernameNotFoundException e) {
            //회원등록되지 않은 구글 계정
            
            return "redirect:/login?type=no";
        } catch (Exception e) {
            e.printStackTrace();
            
            return "redirect:/login?type=error";
        }
        
    }  
    
    /**
     * authCode 값을 사용해서 구글 사용자 정보를 가져와 회원가입 하는 함수.
     * 
     * @param authCode - authorization_code
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/signup/google/access")
    public String signUpGoogle(@RequestParam(value = "code") String authCode,
            HttpServletRequest request,
            HttpServletResponse response){
        
        try {
            /*
             * Request for access token with authCode and get Google user information. 
             * (Google OAuth API)
             */
            ResponseEntity<GoogleInfResponse> resultEntity = 
                    googleOAuthService.getGoogleUserInfo(authCode, "signup", googleClientId, googleClientSecret, server);
            
            UserDTO userDTO = new UserDTO();
            userDTO.setUsername(resultEntity.getBody().getEmail());
            userDTO.setPassword(passwordEncoder.encode(resultEntity.getBody().getSub()));
            userDTO.setEmail(resultEntity.getBody().getEmail());
            userDTO.setNickname(resultEntity.getBody().getName());
            userDTO.setRole("user");
            userDTO.setSns("google");
            userDTO.setPicture(resultEntity.getBody().getPicture());
            
            if (userService.registerUser(userDTO)) {
                return "redirect:/portfolio?type=signup";
            } else {
                return "redirect:/";
            }
            
        } catch (Exception e) {
            e.printStackTrace();
            
            return "redirect:/";
        }
    }          
}
