package com.myboard.web.jspboard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/jsp-board")
public class JspBoardWebController {
	
	@GetMapping("/list")
	public String getListPage(@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "5") int bsize,
			@RequestParam(defaultValue = "") String field,
			@RequestParam(defaultValue = "") String keyword,
			Model model) {
		
		model.addAttribute("page", page);
		model.addAttribute("size", size);
		model.addAttribute("bszie", bsize);
		model.addAttribute("filed", field);
		model.addAttribute("keyword", keyword);
		return "/jsp-board/list";
	}
	
	@GetMapping("/write")
	public String getWriteForm() {
		
		return "/jsp-board/write-form";
	}
	
	@GetMapping("/edit-form")
	public String getEditForm(@RequestParam int idx,
			Model model) {
		
		model.addAttribute("idx", idx);
		return "/jsp-board/edit-form";
	}
	
	@GetMapping
	public String getPostDetail(@RequestParam int idx,
			Model model) {
		
		model.addAttribute("idx", idx);
		return "/jsp-board/post-detail";
	}
	
}
