package com.myboard.api.free.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.myboard.api.free.domain.FreePost;
import com.myboard.api.free.repository.FreeListMapper;

@Service
public class FreeService {
	@Autowired
	FreeListMapper freeListMapper;
	
	public List<FreePost> getFreePostList(int page, int size, int bsize, String field, String keyword) {
		List<FreePost> freePostList = freeListMapper.selectFreePostList(page, size, bsize, field, keyword);		
		
		return freePostList;
	}
	
	public int getTotalFreePost(int page, String field, String keyword) {
		int totalPost = freeListMapper.selectTotalFreePost(page, field, keyword);
		
		return totalPost;
	}

	public boolean writeFreePost(FreePost freePost) {
		boolean result = false;
		try {
			if (freeListMapper.insertFreePost(freePost)==1) {
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

	public FreePost getFreePostDetail(int idx) {
		FreePost freePost = new FreePost();
		
		try {
			freePost = freeListMapper.selectFreePost(idx);
			freeListMapper.updateHitsOfFreePost(idx);
		} catch (Exception e) {
			e.printStackTrace();
			freePost = null;
		}
		
		return freePost;
	}

	public boolean delFreePost(int idx, String password) {
		boolean result = false;
		try {
			if (freeListMapper.deleteFreePost(idx, password)==1) {
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

	public boolean editFreePost(FreePost freePost) {
		boolean result = false;
		try {
			if (freeListMapper.updateFreePost(freePost)==1) {
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