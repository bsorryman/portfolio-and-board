package com.myboard.google.controller;

import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.myboard.board.util.AesUtil;
import com.myboard.google.dto.GoogleInfResponse;
import com.myboard.google.dto.GoogleRequest;
import com.myboard.google.dto.GoogleResponse;
import com.myboard.user.dto.UserDTO;
import com.myboard.user.service.UserService;

@Controller
public class GoogleOAuthController {
    @Value("${google.client.id}")
    private String googleClientId;
    @Value("${google.client.pwd}")
    private String googleClientPwd;
    
    private final RequestCache requestCache = new HttpSessionRequestCache();
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    
    public GoogleOAuthController(
            UserService userService,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            UserDetailsService userDetailsService ) {
        
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }
    
    /**
     * 사용자가 구글 로그인을 할 수 있는 URL을 리턴하는 함수
     * @param type
     * @return
     */
    @PostMapping("/{type}/oauth2/google")
    @ResponseBody
    public String loginUrlGoogle(@PathVariable("type") String type){
        String reqUrl = "https://accounts.google.com/o/oauth2/v2/auth?client_id=" + googleClientId
                + "&redirect_uri=http://localhost:8080/"+type+"/oauth2/google&response_type=code&scope=email%20profile%20openid&access_type=offline&prompt=select_account";

        return reqUrl;
    }
    
    /**
     * 사용자가 구글 로그인 성공 시 authorization_code를 반환받는 함수
     * 
     * @param authCode
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/login/oauth2/google")
    public String loginGoogle(@RequestParam(value = "code") String authCode,
            HttpServletRequest request,
            HttpServletResponse response){
        try {
            RestTemplate restTemplate = new RestTemplate();
            GoogleRequest googleOAuthRequestParam = GoogleRequest
                    .builder()
                    .clientId(googleClientId)
                    .clientSecret(googleClientPwd)
                    .code(authCode)
                    .redirectUri("http://localhost:8080/login/oauth2/google")
                    .grantType("authorization_code").build();
            
            // Google Access Token 요청
            ResponseEntity<GoogleResponse> resultEntity = restTemplate.postForEntity("https://oauth2.googleapis.com/token",
                    googleOAuthRequestParam, GoogleResponse.class);
            
            String jwtToken=resultEntity.getBody().getId_token();
            
            Map<String, String> map=new HashMap<>();
            map.put("id_token",jwtToken);
            
            // 받은 Access Token으로 사용자 정보 요청
            ResponseEntity<GoogleInfResponse> resultEntity2 = 
                    restTemplate.postForEntity("https://oauth2.googleapis.com/tokeninfo", map, GoogleInfResponse.class);
            
            // 사용자 정보 매핑
            String googleEmail = resultEntity2.getBody().getEmail();  
            String googleSub = resultEntity2.getBody().getSub();
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
                
                // 쿠키 설정
                AesUtil aesUtil = new AesUtil();
                String encryptedUsername = aesUtil.encrypt(googleEmail);
                
                Cookie cookie = new Cookie("userInfo", encryptedUsername);
                cookie.setMaxAge(60 * 60 * 24 * 365); 
                cookie.setDomain("localhost");
                cookie.setPath("/");
                
                response.addCookie(cookie); 
                
                // 에러 세션 제거
                HttpSession session = request.getSession(false);
                if (session != null) {
                    session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
                }
                
                SavedRequest savedRequest = requestCache.getRequest(request, response);

                /**
                 * prevPage 값이 있다면 사용자가 직접 URL 입력을 통해 접근한 경우이므로
                 * 세션의 해당 속성을 삭제한다.
                 */
                String prevPage = (String) request.getSession().getAttribute("prevPage");
                if (prevPage != null) {
                    request.getSession().removeAttribute("prevPage");
                }

                String uri = "/";

                /**
                 * savedRequest가 있다면 접근 권한이 없는 URL에 접근하여 login 페이지로 들어온 것이므로
                 * 접근하려 했던 URL을 저장해 놓는다.
                 */
                if (savedRequest != null) {
                    uri = savedRequest.getRedirectUrl();
                } else if (prevPage != null && !prevPage.equals("")) {
                    // 회원가입에서 넘어온 경우 메인페이지로 보낸다.
                    if (prevPage.contains("/signup")) {
                        uri = "/";
                    } else {
                        uri = prevPage;
                    }
                }
                
                return "redirect:" + uri;
            } else {
                
                return "redirect:/login?error=true";
            }            
        } catch (UsernameNotFoundException e) {
            //회원등록되지 않은 구글 계정
            
            return "redirect:/login?error=true";
        } catch (Exception e) {
            e.printStackTrace();
            
            return "redirect:/login?error=true";
        }
        
    }  
}
