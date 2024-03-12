package com.myboard.member.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myboard.board.util.Message;
import com.myboard.member.domain.Member;
import com.myboard.member.service.MemberService;

@Controller
@RequestMapping("/login")
public class MemberLogInController {
    private final MemberService memberService;
    
    public MemberLogInController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    @GetMapping
    public String getLogInPage(String credential) {
        
        return "thymeleaf/member/login";
    }
    
    @PostMapping
    public String logIn(Member member, Model model, HttpServletResponse response) {
        boolean result = false;
        
        // 알림 팝업 후 화면 전환이 필요
        Message message = new Message();
        
        try {
            result = memberService.logInMember(member);
            Cookie cookie = new Cookie("memberId", member.getMemberId());
            response.addCookie(cookie);
            if (result) {
                message.setMessage("로그인이 완료되었습니다.");
                message.setHref("/thyme-board/list");
                
            } else {
                message.setMessage("로그인이 실패했습니다.");
                message.setHref("/thyme-board/list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            
            message.setMessage("알 수 없는 에러가 발생했습니다.");
            message.setHref("/thyme-board/list");
            
        }
        
        model.addAttribute("message", message);
        
        return "thymeleaf/common/message";

    }
    
    @PostMapping("/google")
    public String registerGoogleMember(String credential, Model model, HttpServletResponse response) {
        Map<String, Object> payloadMap = new HashMap<String, Object>();
        
        // 알림 팝업 후 화면 전환이 필요
        Message message = new Message();
        
        if (credential != null) {
            payloadMap = memberService.decodePayloadInJwt(credential);
            
        } else {
            message.setMessage("잘못된 접근입니다.");
            message.setHref("/thyme-board/list");   
        }
        
        String googleEmail = (String)payloadMap.get("email");
        String googleSub = (String)payloadMap.get("sub");
        
        Member member = new Member();
        member.setMemberId(googleEmail);
        member.setMemberPwd(googleSub);
        member.setGSocialYn("y");
        
        boolean result = false;

        try {
            result = memberService.logInMember(member);
            Cookie cookie = new Cookie("member_id", member.getMemberId());
            cookie.setDomain("localhost");
            cookie.setPath("/");
            response.addCookie(cookie);
            System.out.println(cookie.getValue());
            if (result) {
                message.setMessage("로그인이 완료되었습니다.");
                message.setHref("/thyme-board/list");
                
            } else {
                message.setMessage("로그인이 실패했습니다.");
                message.setHref("/thyme-board/list");
            }
        } catch (Exception e) {
            e.printStackTrace();
            
            message.setMessage("알 수 없는 에러가 발생했습니다.");
            message.setHref("/thyme-board/list");               
        } 
        
        model.addAttribute("message", message);

        return "thymeleaf/common/message";
    }
    
}
