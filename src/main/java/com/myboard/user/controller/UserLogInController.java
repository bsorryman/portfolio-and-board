package com.myboard.user.controller;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myboard.board.util.AesUtil;
import com.myboard.user.dto.UserDTO;
import com.myboard.user.service.UserService;

@Controller
@RequestMapping("/login")
public class UserLogInController {
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    
    public UserLogInController(
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
     * 로그인 페이지
     */
    @GetMapping
    public String getLogInPage(HttpServletRequest request) {
        
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        
        return "thymeleaf/user/login";
    }
    
    @GetMapping("/old")
    public String getNewLogInPage(HttpServletRequest request) {
        
        String uri = request.getHeader("Referer");
        if (uri != null && !uri.contains("/login")) {
            request.getSession().setAttribute("prevPage", uri);
        }
        
        return "thymeleaf/member/login";
    }    
    
    /**
     * 구글 소셜 로그인은 Security에 설정된 formLogin이 아닌 수동으로 로그인을 처리한다.
     * (id, password가 form 파라미터로 전달되지 않기 때문)
     * 구글 로그인 성공 시 전달되는 credential(jwt)를 디코딩하여 수동으로 User에 매핑하고
     * 수동으로 인증 토큰을 생성하여 검증한다. 
     * @param credential
     * @return
     */
    @PostMapping("/google")
    public String getLogInPage(HttpServletRequest request, HttpServletResponse response,
    		String credential) {
    	
        try {
            // google 계정 credential(jwt 토큰) 디코딩
            Map<String, Object> payloadMap = new HashMap<String, Object>();

            payloadMap = userService.decodePayloadInJwt(credential);
                
            String googleEmail = (String)payloadMap.get("email");
            String googleSub = (String)payloadMap.get("sub");
            
            // sub(password로 사용할 필드) 암호화
            String encodedGoogleSub = passwordEncoder.encode(googleSub);
            System.out.println("encode: " + encodedGoogleSub);
            // User 객체 매핑
            UserDTO userDTO = new UserDTO();

            userDTO.setUsername(googleEmail);
            userDTO.setPassword(encodedGoogleSub);
            userDTO.setSns("google");     
            
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
                
//                Cookie cookie = new Cookie("userInfo", URLEncoder.encode(encryptedUsername, "UTF-8"));
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
                /*
                 * 구글에서 이미 로그인된 구글 계정으로 소셜 로그인을 시도하는 것이므로
                 * result가 false(id는 맞지만 password는 틀린)일 경우는 사실상 없다.
                 * 소셜 로그인의 실패는 모두 UsernameNotFountException에서 걸러진다.
                 */
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
