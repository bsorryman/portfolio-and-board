package com.myboard.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myboard.board.domain.FreeBoardPost;
import com.myboard.board.repository.FreeBoardMapper;
import com.myboard.board.util.AesUtil;

@Service
public class FreeBoardService {
    private final FreeBoardMapper freeBoardMapper;
    private final AesUtil aesUtil = new AesUtil();

    public FreeBoardService(FreeBoardMapper freeBoardMapper ) {
        this.freeBoardMapper = freeBoardMapper;
    }
    
    
    public List<FreeBoardPost> getFreeBoardList(int page, int size, int bsize, String field, String keyword) {
        List<FreeBoardPost> thymeBoardPostList = freeBoardMapper.selectFreeBoardList(page, size, bsize, field, keyword);     
        
        return thymeBoardPostList;
    }

    public int getTotalFreeBoardList(int page, String field, String keyword) {
        int totalPost = freeBoardMapper.selectTotalFreeBoard(page, field, keyword);
        
        return totalPost;
    }
    
    public FreeBoardPost getFreeBoardPost(int idx) {
    	FreeBoardPost freeBoardPost = freeBoardMapper.selectFreeBoardPost(idx);
    	freeBoardMapper.updateHits(idx);
    	
    	return freeBoardPost;
    }

    public boolean writeFreeBoard(FreeBoardPost freeBoardPost) {
        boolean result = false;
        int resultInt= freeBoardMapper.insertFreeBoard(freeBoardPost); 
        
        if (resultInt == 1) {
            result = true;
        }
        
        return result;
    }

	public boolean editFreeBaord(FreeBoardPost freeBoardPost) {
        boolean result = false;
        int resultInt= freeBoardMapper.updateFreeBoard(freeBoardPost); 
        
        if (resultInt == 1) {
            result = true;
        }
        
        return result;
	}
	
	public boolean checkPassword(int idx, String password) {
		boolean result = false;
		
    	FreeBoardPost realPost = freeBoardMapper.selectFreeBoardPost(idx);
    	String realPassword = aesUtil.decrypt(realPost.getPassword());
    	
		//int resultInt = freeBoardMapper.checkPassword(idx, password);
        
		if (password.equals(realPassword)) {
            result = true;
        }
		
		return result;
	}
	
	public boolean deleteFreeBoard(FreeBoardPost freeBoardPost) {
		boolean result = false;
		
		int resultInt = freeBoardMapper.deleteFreeBoard(freeBoardPost);

		if (resultInt == 1) {
            result = true;
        }
        
        return result;		
	}

}
