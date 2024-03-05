package com.myboard.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.myboard.member.domain.Member;
import com.myboard.member.service.MemberService;
import com.sun.jna.platform.win32.Wincon.INPUT_RECORD.Event;

@Controller
@RequestMapping("/signup")
public class MemberController {
    private final MemberService memberService;
    
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }
    
    @GetMapping
    public String getSignupPage() {
        
        return "thymeleaf/signup";
    }
    
    @PostMapping
    public String registerMember(Member member) {
        boolean result = false;
        
        try {
            result = memberService.registerMember(member);
            if (result) {
                return "redirect:/thyme-board/list";
            } else {
                return "redirect:/thyme-board/error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/thyme-board/error";
        }
        

        
    }
    
}
