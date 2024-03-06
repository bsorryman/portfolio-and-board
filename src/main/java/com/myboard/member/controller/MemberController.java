package com.myboard.member.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
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
    
    @GetMapping("/popup")
    public String getSignupPage() {
        
        return "thymeleaf/signup-popup";
    }
    
    @GetMapping("/redirect")
    public String getSignupRedirectPage() {
        
        return "thymeleaf/signup-redirect";
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
    
    @PostMapping("/google-login")
    @ResponseBody
    public String registerGoogleMember(HttpServletRequest request) {
        
        boolean result = false;
        
        System.out.println(request.getHeader("alg"));
        /*
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
        */    
        
        return "<script>"
                + "alert('구글 회원가입이 완료되었습니다.')"
                + "window.location.herf='/thyme-board/list'"
                + "</script>";
    }
    
}
