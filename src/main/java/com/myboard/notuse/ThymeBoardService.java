package com.myboard.notuse;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ThymeBoardService {
	private final ThymeBoardListMapper thymeBoardListMapper;
	
	public ThymeBoardService(ThymeBoardListMapper thymeBoardListMapper) {
		this.thymeBoardListMapper = thymeBoardListMapper;
	}
	
	public List<ThymeBoardPost> getThymeBoardPostList(int page, int size, int bsize, String field, String keyword) {
		List<ThymeBoardPost> thymeBoardPostList = thymeBoardListMapper.selectThymeBoardPostList(page, size, bsize, field, keyword);		
		
		return thymeBoardPostList;
	}
	
	public int getTotalThymeBoardPost(int page, String field, String keyword) {
		int totalPost = thymeBoardListMapper.selectTotalThymeBoardPost(page, field, keyword);
		
		return totalPost;
	}

	public boolean writeThymeBoardPost(ThymeBoardPost thymeBoardPost) {
		boolean result = false;
		try {
			if (thymeBoardListMapper.insertThymeBoardPost(thymeBoardPost)==1) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	public ThymeBoardPost getThymeBoardPostDetail(int idx) {
		ThymeBoardPost thymeBoardPost = new ThymeBoardPost();
		
		try {
			thymeBoardPost = thymeBoardListMapper.selectThymeBoardPost(idx);
			thymeBoardListMapper.updateHitsOfThymeBoardPost(idx);
		} catch (Exception e) {
			e.printStackTrace();
			thymeBoardPost = null;
		}
		
		return thymeBoardPost;
	}

	public boolean delThymeBoardPost(int idx, String password) {
		boolean result = false;
		try {
			if (thymeBoardListMapper.deleteThymeBoardPost(idx, password)==1) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}

	public boolean editThymeBoardPost(ThymeBoardPost thymeBoardPost) {
		boolean result = false;
		try {
			if (thymeBoardListMapper.updateThymeBoardPost(thymeBoardPost)==1) {
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		}
		
		return result;
	}
	
}
