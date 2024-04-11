package com.myboard.board.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myboard.board.domain.FreeBoardPost;
import com.myboard.board.domain.MemberBoardPost;
import com.myboard.board.service.MemberBoardService;
import com.myboard.board.util.Message;
import com.myboard.board.util.Pager;
import com.myboard.board.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/board/member")
@RequiredArgsConstructor
public class MemberBoardController {
    
    private final MemberBoardService memberBoardService;
    
    @GetMapping("/list")
    public String getMemberBoard(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "7") int size,
            @RequestParam(defaultValue = "5") int bsize,
            @RequestParam(defaultValue = "") String field,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {
        
        List<MemberBoardPost> memberBoardList = memberBoardService.getMemberBoardList(page, size, bsize, field, keyword);
        int totalPost = memberBoardService.getTotalMemberBoardList(page, field, keyword);
        
        model.addAttribute("memberBoardList", memberBoardList);
        model.addAttribute("totalPost", totalPost);
        
        Pager pager = new Pager(page, size, bsize, totalPost);
        model.addAttribute("pager", pager);
        model.addAttribute("size", size);
        model.addAttribute("bszie", bsize);
        model.addAttribute("filed", field);
        model.addAttribute("keyword", keyword);
        
        return "thymeleaf/board/member/member-list";
    }
    
    @GetMapping("/detail")
    public String getMemberBoardDetail(@RequestParam(required = true) int idx,
    		Model model) {
    	
    	MemberBoardPost memberBoardPost = memberBoardService.getMemberBoardDetail(idx);
    	
    	model.addAttribute("memberBoardPost", memberBoardPost);
    	
    	return "thymeleaf/board/member/member-detail";
    }    
    
    @GetMapping("/write-form")
    public String getMemberBoardWriteForm() {
        
        return "thymeleaf/board/member/member-write-form";
    }        
    
    @PostMapping("/write")
    public String writeMemberBoard(MemberBoardPost memberBoardPost, Model model) {
    	boolean result = false;
        Message message = new Message();

    	// 접근 방지
        SecurityUtil securityUtil = new SecurityUtil();
        boolean auth = securityUtil.validateUsername(memberBoardPost.getUsername());
        
        if (!auth) {
            message.setMessage("잘못된 접근입니다.");
            message.setHref("/board/member/list");
        	
            return "thymeleaf/common/message";    	
        }
        

        result = memberBoardService.writeMemberBoard(memberBoardPost);
        if (result) {
            message.setMessage("글 작성이 완료되었습니다.");
        } else {
            message.setMessage("에러가 발생했습니다.");
        }
        
        message.setHref("/board/member/list");
        model.addAttribute("message", message);
        
        return "thymeleaf/common/message";
    }
    
    @PostMapping("/delete")
    public String deleteMemberBoard(MemberBoardPost memberBoardPost,
    		Model model) {
    	boolean result = false;
        Message message = new Message();
    	
    	// 접근 방지
        SecurityUtil securityUtil = new SecurityUtil();
        boolean auth = securityUtil.validateUsername(memberBoardPost.getUsername());
        
        if (!auth) {
            message.setMessage("잘못된 접근입니다.");
            message.setHref("/board/member/list");
        	
            return "thymeleaf/common/message";    
            
        }
        
    	result = memberBoardService.deleteMemberBoard(memberBoardPost);
    	
        if (result) {
            message.setMessage("삭제가 완료되었습니다.");
        } else {
            message.setMessage("에러가 발생했습니다.");
        }
        
        message.setHref("/board/member/list");
        model.addAttribute("message", message);
        
        return "thymeleaf/common/message";    	
    }
    
    @PostMapping("/edit-form")
    public String getMemberBoardEditForm(@RequestParam(required = false) int idx,
    		@RequestParam(required = false) String username, Model model) {
    	// 접근 방지
        SecurityUtil securityUtil = new SecurityUtil();
        boolean auth = securityUtil.validateUsername(username);
        
        if (!auth) {
            Message message = new Message();
            message.setMessage("잘못된 접근입니다.");
            message.setHref("/board/member/list");
        	
            return "thymeleaf/common/message";    	
        }
        
        MemberBoardPost memberBoardPost = memberBoardService.getMemberBoardDetail(idx);
        
    	model.addAttribute("memberBoardPost", memberBoardPost);
    	
        return "thymeleaf/board/member/member-edit-form";
    }      
    
    @PostMapping("/edit")
    public String editMemberBoard(MemberBoardPost memberBoardPost, Model model) {
    	// 접근 방지
        SecurityUtil securityUtil = new SecurityUtil();
        boolean auth = securityUtil.validateUsername(memberBoardPost.getUsername());
        
        if (!auth) {
            Message message = new Message();
            message.setMessage("잘못된 접근입니다.");
            message.setHref("/board/member/list");
        	
            return "thymeleaf/common/message";    	
        }
        
    	boolean result = memberBoardService.editMemberBaord(memberBoardPost);

    	Message message = new Message();

        if (result) {
            message.setMessage("글 수정이 완료되었습니다.");
        } else {
            message.setMessage("에러가 발생했습니다.");
        }
        
        message.setHref("/board/member/list");
        model.addAttribute("message", message);
        return "thymeleaf/common/message";
        
    }    
}
