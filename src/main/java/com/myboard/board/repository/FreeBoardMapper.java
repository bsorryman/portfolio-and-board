package com.myboard.board.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.myboard.board.domain.FreeBoardPost;

@Mapper
public interface FreeBoardMapper {
    int selectTotalFreeBoard(@Param("page") int page, @Param("field") String field,
            @Param("keyword") String keyword);
    
    List<FreeBoardPost> selectFreeBoardList(@Param("page") int page, @Param("size") int size,
            @Param("bsize") int bsize,
            @Param("field") String field,
            @Param("keyword") String keyword);

    int insertFreeBoard(FreeBoardPost freeBoardPost);

    FreeBoardPost selectFreeBoardDetail(int idx);

    int deleteFreeBoard(@Param("idx") int idx, @Param("password") String password);

    int updateFreeBoard(FreeBoardPost freeBoardPost);

    //for edit
    int selectFreeBoardByPassword(@Param("idx") int idx, @Param("password") String password);

    void updateHits(@Param("idx") int idx);
}
