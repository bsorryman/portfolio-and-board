package com.myboard.api.free.controller;

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

import com.myboard.api.free.domain.FreePost;
import com.myboard.api.free.service.FreeService;
import com.myboard.api.util.Pager;

@RestController
@RequestMapping("/api/v1.0/free")
public class FreeController {

	@Autowired
	FreeService freeService;
	
	@GetMapping("/list")
	public Map<String, Object> getFreePostList(
			@RequestParam(defaultValue = "1", required = false) int page,
			@RequestParam(defaultValue = "10", required = false) int size,
			@RequestParam(defaultValue = "5", required = false) int bsize,
			@RequestParam(defaultValue = "", required = false) String field,
			@RequestParam(defaultValue = "", required = false) String keyword) {
		
		Map<String, Object> resultMap = new HashMap<String, Object>();

		List<FreePost> freePostList = freeService.getFreePostList(page, size, bsize, field, keyword);
		int totalPost = freeService.getTotalFreePost(page, field, keyword);
		
		Pager pager = new Pager(page, size, bsize, totalPost);

		resultMap.put("freePostList", freePostList);
		resultMap.put("totalPost", totalPost);
		resultMap.put("pager", pager);
		
		return resultMap;
	}
	
	@GetMapping
	public FreePost getFreePostDetail(@RequestParam int idx) {
		FreePost freePost = freeService.getFreePostDetail(idx);
		return freePost;
	}
	
	@PostMapping
	public boolean writeFreePost(FreePost freePost) {
		boolean result = false;
		
		result = freeService.writeFreePost(freePost);

		return result;
	}
	
	@PutMapping
	public boolean editFreePost(FreePost freePost) {
		boolean result = false;
		result = freeService.editFreePost(freePost);
		
		return result;
	}
	
	@DeleteMapping
	public boolean delFreePost(int idx, String password) {
		boolean result = false;
		
		result = freeService.delFreePost(idx, password);
		
		return result;
	}
	
	
}
