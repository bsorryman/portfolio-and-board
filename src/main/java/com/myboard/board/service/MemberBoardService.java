package com.myboard.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.myboard.board.domain.MemberBoardPost;
import com.myboard.board.repository.MemberBoardMapper;

@Service
public class MemberBoardService {
    private final MemberBoardMapper memberBoardMapper;
    
    public MemberBoardService(MemberBoardMapper memberBoardMapper ) {
        this.memberBoardMapper = memberBoardMapper;
    }
    
    
    public List<MemberBoardPost> getMemberBoardList(int page, int size, int bsize, String field, String keyword) {
        List<MemberBoardPost> thymeBoardPostList = memberBoardMapper.selectMemberBoardList(page, size, bsize, field, keyword);     
        
        return thymeBoardPostList;
    }

    public int getTotalMemberBoardList(int page, String field, String keyword) {
        int totalPost = memberBoardMapper.selectTotalMemberBoard(page, field, keyword);
        
        return totalPost;
    }
    
    public MemberBoardPost getMemberBoardDetail(int idx) {
    	MemberBoardPost memberBoardPost = memberBoardMapper.selectMemberBoardDetail(idx);
    	memberBoardMapper.updateHits(idx);
    	
    	return memberBoardPost;
    }    
    
    public boolean writeMemberBoard(MemberBoardPost memberBoardPost) {
    	boolean result = false;
        
    	int resultInt = memberBoardMapper.insertMemberBoard(memberBoardPost);
    	
    	if (resultInt == 1) {
    		result = true;
    	}
    	
    	return result;
    }
    
    public boolean deleteMemberBoard(MemberBoardPost memberBoardPost) {
    	boolean result = false;
        
    	int resultInt = memberBoardMapper.deleteMemberBoard(memberBoardPost);
    	
    	if (resultInt == 1) {
    		result = true;
    	}
    	
    	return result;    	
    }
    
    public boolean editMemberBaord(MemberBoardPost memberBoardPost) {
    	boolean result = false;
    	
    	int resultInt = memberBoardMapper.updateMemberBoard(memberBoardPost);
    	
    	if (resultInt == 1) {
    		result = true;
    	}
    	
    	return result;    
    }

}
