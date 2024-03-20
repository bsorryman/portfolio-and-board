package com.myboard.board.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.myboard.board.domain.MemberBoardPost;
import com.myboard.board.domain.ThymeBoardPost;

@Mapper
public interface MemberBoardMapper {
    int selectTotalMemberBoard(@Param("page") int page, @Param("field") String field,
            @Param("keyword") String keyword);
    
    List<MemberBoardPost> selectMemberBoardList(@Param("page") int page, @Param("size") int size,
            @Param("bsize") int bsize,
            @Param("field") String field,
            @Param("keyword") String keyword);

    int insertMemberBoard(MemberBoardPost memberBoardPost);

    ThymeBoardPost selectMemberBoardDetail(int idx);

    int deleteMemberBoard(@Param("idx") int idx, @Param("password") String password);

    int updateMemberBoard(MemberBoardPost memberBoardPost);

    //for edit
    int selectMemberBoardByPassword(@Param("idx") int idx, @Param("password") String password);

    void updateHits(@Param("idx") int idx);
}
