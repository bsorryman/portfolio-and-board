package com.myboard.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myboard.board.domain.FreeBoardPost;
import com.myboard.board.service.FreeBoardService;
import com.myboard.board.util.Pager;

@Controller
@RequestMapping("/board/free")
public class FreeBoardController {
    
    private final FreeBoardService freeBoardService;
    
    public FreeBoardController(FreeBoardService freeBoardService) {
        this.freeBoardService = freeBoardService;
    }
    
    @GetMapping("/list")
    public String getFreeBoard(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "5") int bsize,
            @RequestParam(defaultValue = "") String field,
            @RequestParam(defaultValue = "") String keyword,
            Model model) {
        
        List<FreeBoardPost> freeBoardList = freeBoardService.getFreeBoardList(page, size, bsize, field, keyword);
        int totalPost = freeBoardService.getTotalFreeBoardList(page, field, keyword);
        
        model.addAttribute("freeBoardList", freeBoardList);
        model.addAttribute("totalPost", totalPost);
        
        Pager pager = new Pager(page, size, bsize, totalPost);
        model.addAttribute("pager", pager);
        model.addAttribute("size", size);
        model.addAttribute("bszie", bsize);
        model.addAttribute("filed", field);
        model.addAttribute("keyword", keyword);
        
        return "thymeleaf/board/free/free-list";
    }
    
}
