package com.myboard.board.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.myboard.board.domain.JspBoardPost;
import com.myboard.board.service.JspBoardService;
import com.myboard.board.util.Pager;

@RestController
@RequestMapping("/api/v1.0/jsp-board")
public class JspBoardController {

	@Autowired
	JspBoardService jspBoardService;
	
	@GetMapping("/list")
	public Map<String, Object> getJspBoardPostList(
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size,
			@RequestParam(defaultValue = "5", required = false) int bsize,
			@RequestParam(defaultValue = "", required = false) String field,
			@RequestParam(defaultValue = "", required = false) String keyword) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<JspBoardPost> jspBoardPostList = jspBoardService.getJspBoardPostList(page, size, bsize, field, keyword);
		int totalPost = jspBoardService.getTotalJspBoardPost(page, field, keyword);
		
		Pager pager = new Pager(page, size, bsize, totalPost);

		resultMap.put("jspBoardPostList", jspBoardPostList);
		resultMap.put("totalPost", totalPost);
		resultMap.put("pager", pager);
		
		return resultMap;
	}
	
	@GetMapping
	public JspBoardPost getJspBoardPostDetail(@RequestParam int idx) {
		JspBoardPost jspBoardPost = jspBoardService.getJspBoardPostDetail(idx);
		return jspBoardPost;
	}
	
	@PostMapping
	public boolean writeJspBoardPost(JspBoardPost jspBoardPost) {
		boolean result = false;
		
		result = jspBoardService.writeJspBoardPost(jspBoardPost);

		return result;
	}
	
	@PutMapping
	public boolean editJspBoardPost(JspBoardPost jspBoardPost) {
		boolean result = false;
		result = jspBoardService.editJspBoardPost(jspBoardPost);
		
		return result;
	}
	
	@DeleteMapping
	public boolean delJspBoardPost(int idx, String password) {
		boolean result = false;
		
		result = jspBoardService.delJspBoardPost(idx, password);
		
		return result;
	}
	
	
}
