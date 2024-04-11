package com.myboard.user.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myboard.board.util.Message;
import com.myboard.user.dto.UserDTO;
import com.myboard.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/signup")
@RequiredArgsConstructor
public class UserSignUpController {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    
    /*
     * 회원가입 페이지
     */
    @GetMapping
    public String getSignupPage() {
        
        return "thymeleaf/user/signup";
    }
    
    /**
     * 회원가입 처리 컨트롤러.
     * 비밀번호는 암호화하여 처리한다.
     * @param userDTO
     * @param model
     * @return
     */
    @PostMapping
    public String registerUser(UserDTO userDTO, Model model) {
        boolean result = false;
        
        // 알림 팝업 후 화면 전환에 필요
        Message message = new Message();
        String encodedPassword = passwordEncoder.encode(userDTO.getPassword()); //암호화
        userDTO.setPassword(encodedPassword); //암호화된 비밀번호로 재설정
        
        // 회원가입 처리
        try {
            result = userService.registerUser(userDTO);
            if (result) {
                message.setMessage("회원가입이 완료되었습니다.");
                message.setHref("/login");
                
            } else {
                message.setMessage("회원가입이 실패했습니다.");
                message.setHref("/");
            }
        } catch (Exception e) {
            e.printStackTrace();
            
            message.setMessage("알 수 없는 에러가 발생했습니다.");
            message.setHref("/");
            
        }
        
        model.addAttribute("message", message);
        
        return "thymeleaf/common/message";

    }
    
    /**
     * 구글 소셜 회원가입 처리 컨트롤러.
     * Credential 토큰을 복호화하여 payload에서 사용자 정보를 가져와 가입시킨다. 
     * 비밀번호에 쓰일 필드는 암호화한다.
     * @param credential
     * @param model
     * @return
     */
    @PostMapping("/google")
    public String registerGoogleUser(String credential, Model model) {
        Map<String, Object> payloadMap = new HashMap<String, Object>();
        
        // 알림 팝업 후 화면 전환이 필요
        Message message = new Message();

        if (credential != null) {
            // payload 복호화
            payloadMap = userService.decodePayloadInJwt(credential);
            
        } else {
            message.setMessage("잘못된 접근입니다.");
            message.setHref("/");   
        }
        
        // payload의 사용자 정보
        String googleEmail = (String)payloadMap.get("email");
        String googleSub = (String)payloadMap.get("sub");
        String googleName = (String)payloadMap.get("name");
        String encodedGoogleSub = passwordEncoder.encode(googleSub); //암호화
        String googlePicture = (String)payloadMap.get("picture");

        // User 객체에 매핑
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(googleEmail);
        userDTO.setPassword(encodedGoogleSub);
        userDTO.setNickname(googleName);
        userDTO.setEmail(googleEmail);
        userDTO.setPicture(googlePicture);
        userDTO.setRole("user");
        userDTO.setSns("google");
        
        // 회원가입 처리
        try {
            userService.registerUser(userDTO);
            
            message.setMessage("구글 소셜 회원가입이 완료되었습니다.");
            message.setHref("/login");   
            
        } catch (Exception e) {
            e.printStackTrace();
            
            message.setMessage("알 수 없는 에러가 발생했습니다.");
            message.setHref("/");               
        } 
        
        model.addAttribute("message", message);

        return "thymeleaf/common/message";
    }
    
    @ResponseBody
    @PostMapping("/id-check")
    public boolean idCheck(@RequestParam(required = true) String username) {
    	boolean result = false;
    	
    	result = userService.idCheck(username);
    	
    	return result;
    }
    
}
