package com.myboard.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myboard.board.domain.MemberBoardPost;
import com.myboard.board.service.MemberBoardService;
import com.myboard.board.util.Pager;

@Controller
@RequestMapping("/board/member")
public class MemberBoardController {
    
    private final MemberBoardService memberBoardService;
    
    public MemberBoardController(MemberBoardService memberBoardService) {
        this.memberBoardService = memberBoardService;
    }
    
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
    public String getFreeBoardDetail(@RequestParam(required = true) int idx,
    		Model model) {
    	
    	MemberBoardPost memberBoardDetail = memberBoardService.getMemberBoardDetail(idx);
    	
    	model.addAttribute("memberBoardDetail", memberBoardDetail);
    	
    	return "thymeleaf/board/member/member-detail";
    }    
}
