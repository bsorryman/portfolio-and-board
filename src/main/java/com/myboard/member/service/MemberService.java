package com.myboard.member.service;

import org.springframework.stereotype.Service;

import com.myboard.member.domain.Member;
import com.myboard.member.repository.MemberMapper;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }
    
    public boolean registerMember(Member member) {
        boolean result = false;
        result = memberMapper.insertMember(member);
        
        return result;
    }
}
