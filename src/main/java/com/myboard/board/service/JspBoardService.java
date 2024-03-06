package com.myboard.board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myboard.board.domain.JspBoardPost;
import com.myboard.board.repository.JspBoardListMapper;

@Service
public class JspBoardService {
	@Autowired
	JspBoardListMapper jspBoardListMapper;
	
	public List<JspBoardPost> getJspBoardPostList(int page, int size, int bsize, String field, String keyword) {
		List<JspBoardPost> jspBoardPostList = jspBoardListMapper.selectJspBoardPostList(page, size, bsize, field, keyword);		
		
		return jspBoardPostList;
	}
	
	public int getTotalJspBoardPost(int page, String field, String keyword) {
		int totalPost = jspBoardListMapper.selectTotalJspBoardPost(page, field, keyword);
		
		return totalPost;
	}

	public boolean writeJspBoardPost(JspBoardPost jspBoardPost) {
		boolean result = false;
		try {
			if (jspBoardListMapper.insertJspBoardPost(jspBoardPost)==1) {
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

	public JspBoardPost getJspBoardPostDetail(int idx) {
		JspBoardPost jspBoardPost = new JspBoardPost();
		
		try {
			jspBoardPost = jspBoardListMapper.selectJspBoardPost(idx);
			jspBoardListMapper.updateHitsOfJspBoardPost(idx);
		} catch (Exception e) {
			e.printStackTrace();
			jspBoardPost = null;
		}
		
		return jspBoardPost;
	}

	public boolean delJspBoardPost(int idx, String password) {
		boolean result = false;
		try {
			if (jspBoardListMapper.deleteJspBoardPost(idx, password)==1) {
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

	public boolean editJspBoardPost(JspBoardPost jspBoardPost) {
		boolean result = false;
		try {
			if (jspBoardListMapper.updateJspBoardPost(jspBoardPost)==1) {
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