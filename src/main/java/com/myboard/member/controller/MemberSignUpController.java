package com.myboard.member.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;
    
    public MemberSignUpController(MemberService memberService, 
            PasswordEncoder passwordEncoder) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
    }
    
    /*
     * 회원가입 페이지
     */
    @GetMapping
    public String getSignupPage() {
        
        return "thymeleaf/member/signup";
    }
    
    /**
     * 회원가입 처리 컨트롤러.
     * 비밀번호는 암호화하여 처리한다.
     * @param member
     * @param model
     * @return
     */
    @PostMapping
    public String registerMember(Member member, Model model) {
        boolean result = false;
        
        // 알림 팝업 후 화면 전환에 필요
        Message message = new Message();
        String encodedPassword = passwordEncoder.encode(member.getMemberPwd()); //암호화
        member.setMemberPwd(encodedPassword); //암호화된 비밀번호로 재설정
        
        // 회원가입 처리
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
    
    /**
     * 구글 소셜 회원가입 처리 컨트롤러.
     * Credential 토큰을 복호화하여 payload에서 사용자 정보를 가져와 가입시킨다. 
     * 비밀번호에 쓰일 필드는 암호화한다.
     * @param credential
     * @param model
     * @return
     */
    @PostMapping("/google")
    public String registerGoogleMember(String credential, Model model) {
        Map<String, Object> payloadMap = new HashMap<String, Object>();
        
        // 알림 팝업 후 화면 전환이 필요
        Message message = new Message();

        if (credential != null) {
            // payload 복호화
            payloadMap = memberService.decodePayloadInJwt(credential);
            
        } else {
            message.setMessage("잘못된 접근입니다.");
            message.setHref("/thyme-board/list");   
        }
        
        // payload의 사용자 정보
        String googleEmail = (String)payloadMap.get("email");
        String googleSub = (String)payloadMap.get("sub");
        String googleName = (String)payloadMap.get("name");
        String encodedGoogleSub = passwordEncoder.encode(googleSub); //암호화

        // Member 객체에 매핑
        Member member = new Member();
        member.setMemberId(googleEmail);
        member.setMemberPwd(encodedGoogleSub);
        member.setMemberName(googleName);
        member.setMemberEmail(googleEmail);
        member.setMemberRole("user");
        member.setGSocialYn("y");
        
        // 회원가입 처리
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
