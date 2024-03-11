package com.myboard.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.myboard.board.util.Message;
import com.myboard.member.domain.Member;
import com.myboard.member.service.MemberService;

@Controller
@RequestMapping("/signup")
public class MemberSignUpController {
    private final MemberService memberService;
    
    public MemberSignUpController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    @GetMapping("/popup")
    public String getSignupPage() {
        
        return "thymeleaf/member/signup-popup";
    }
    
    @GetMapping
    public String getSignupRedirectPage(String credential) {
        
        return "thymeleaf/member/signup-redirect";
    }
    
    @PostMapping
    public String registerMember(Member member, Model model) {
        boolean result = false;
        
        // 알림 팝업 후 화면 전환이 필요
        Message message = new Message();
        
        try {
            result = memberService.registerMember(member);
            if (result) {
                message.setMessage("회원가입이 완료되었습니다.");
                message.setHref("/thyme-board/list");
                
            } else {
                message.setMessage("회원가입이 실패했습니다.");
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
    public String registerGoogleMember(String credential, Model model) {
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
        String googleName = (String)payloadMap.get("name");
        
        Member member = new Member();
        member.setMemberId(googleEmail);
        member.setMemberPwd(googleSub);
        member.setMemberEmail(googleEmail);
        member.setMemberName(googleName);
        member.setGSocialYn("y");
        
        try {
            memberService.registerMember(member);
            
            message.setMessage("구글 소셜 회원가입이 완료되었습니다.");
            message.setHref("/thyme-board/list");   
            
        } catch (Exception e) {
            e.printStackTrace();
            
            message.setMessage("알 수 없는 에러가 발생했습니다.");
            message.setHref("/thyme-board/list");               
        } 
        
        model.addAttribute("message", message);

        return "thymeleaf/common/message";
    }
    
}
