package com.myboard.board.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.myboard.board.domain.ThymeBoardPost;
import com.myboard.board.service.ThymeBoardService;
import com.myboard.board.util.Pager;

@Controller
@RequestMapping("/thyme-board")
public class ThymeBoardWebController {
	
	private final ThymeBoardService thymeBoardService;
	
	public ThymeBoardWebController(ThymeBoardService thymeBoardService) {
		this.thymeBoardService = thymeBoardService;
	}
	
	@GetMapping("/list")
	public String getListPage(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "5") int bsize,
			@RequestParam(defaultValue = "") String field,
			@RequestParam(defaultValue = "") String keyword,
			Model model) {
		
		List<ThymeBoardPost> thymeBoardPostList = thymeBoardService.getThymeBoardPostList(page, size, bsize, field, keyword);
		int totalPost = thymeBoardService.getTotalThymeBoardPost(page, field, keyword);
		
		model.addAttribute("thymeBoardList", thymeBoardPostList);
		model.addAttribute("totalPost", totalPost);
		
		Pager pager = new Pager(page, size, bsize, totalPost);
		model.addAttribute("pager", pager);
		model.addAttribute("size", size);
		model.addAttribute("bszie", bsize);
		model.addAttribute("filed", field);
		model.addAttribute("keyword", keyword);
		
		return "thymeleaf/thyme-board/list";
	}
	
	@GetMapping("/write-form")
	public String getWriteForm() {
		
		return "thymeleaf/thyme-board/write-form";
	}
	
	@GetMapping("/edit-form")
	public String getEditForm(@RequestParam int idx,
			Model model) {
		
		model.addAttribute("idx", idx);
		return "thymeleaf/thyme-board/edit-form";
	}
	
	@GetMapping
	public String getThymeBoardPost(@RequestParam int idx,
			Model model) {
		ThymeBoardPost thymeBoardPost = thymeBoardService.getThymeBoardPostDetail(idx);
		
		model.addAttribute("thymeBoardPost", thymeBoardPost);
		
		return "thymeleaf/thyme-board/post-detail";
	}
	
	
	@PostMapping
	public String writeThymeBoardPost(ThymeBoardPost thymeBoardPost) {
		boolean result = false;
		
		result = thymeBoardService.writeThymeBoardPost(thymeBoardPost);
		if (result) {
			return "redirect:/thyme-board/list";
		} else {
			return "redirect:/thyme-board/error";			
		}
	}
	
	@PutMapping
	public String editThymeBoardPost(ThymeBoardPost thymeBoardPost) {
		boolean result = false;
		result = thymeBoardService.editThymeBoardPost(thymeBoardPost);
		
		if (result) {
			return "redirect:/thyme-board/list";
		} else {
			return "redirect:/thyme-board/error";			
		}		
	}
	
	@DeleteMapping
	public String delThymeBoardPost(int idx, String password) {
		boolean result = false;
		
		result = thymeBoardService.delThymeBoardPost(idx, password);
		
		if (result) {
			return "redirect:/thyme-board/list";
		} else {
			return "redirect:/thyme-board/error";			
		}		
	}	
	
	@GetMapping("/error")
	public String getErrorPage() {
		
		return "thymeleaf/thyme-board/error";
	}
}
