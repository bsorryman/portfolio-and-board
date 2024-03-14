package com.myboard.member.repository;

import org.apache.ibatis.annotations.Mapper;

import com.myboard.member.domain.Member;

@Mapper
public interface MemberMapper {
    
    boolean insertMember(Member member);
    Member selectBymemberId(String memberId);

}
