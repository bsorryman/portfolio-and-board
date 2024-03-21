package com.myboard.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myboard.board.domain.FreeBoardPost;
import com.myboard.board.service.FreeBoardService;
import com.myboard.board.util.Message;
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
            @RequestParam(defaultValue = "7") int size,
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
    
    @GetMapping("/detail")
    public String getFreeBoardPost(@RequestParam(required = true) int idx,
    		Model model) {
    	
    	FreeBoardPost freeBoardPost = freeBoardService.getFreeBoardPost(idx);
    	
    	model.addAttribute("freeBoardPost", freeBoardPost);
    	
    	return "thymeleaf/board/free/free-detail";
    }
    
    @GetMapping("/write-form")
    public String getFreeBoardWriteForm() {
        
        return "thymeleaf/board/free/free-write-form";
    }    
    
    @PostMapping("/write")
    public String writeFreeBoard(FreeBoardPost freeBoardPost, Model model) {
        boolean result = freeBoardService.writeFreeBoard(freeBoardPost);
        
        Message message = new Message();

        if (result) {
            message.setMessage("글 작성이 완료되었습니다.");
        } else {
            message.setMessage("에러가 발생했습니다.");
        }
        
        message.setHref("/board/free/list");
        model.addAttribute("message", message);
        return "thymeleaf/common/message";
    }
    
}
