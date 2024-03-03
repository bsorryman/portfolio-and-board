package com.myboard.board.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.myboard.board.domain.ThymeBoardPost;

@Mapper
public interface ThymeBoardListMapper {
	int selectTotalThymeBoardPost(@Param("page") int page, @Param("field") String field,
			@Param("keyword") String keyword);
	
	List<ThymeBoardPost> selectThymeBoardPostList(@Param("page") int page, @Param("size") int size,
			@Param("bsize") int bsize,
			@Param("field") String field,
			@Param("keyword") String keyword);

	int insertThymeBoardPost(ThymeBoardPost thymeBoardPost);

	ThymeBoardPost selectThymeBoardPost(int idx);

	int deleteThymeBoardPost(@Param("idx") int idx, @Param("password") String password);

	int updateThymeBoardPost(ThymeBoardPost thymeBoardPost);

	//for edit
	int selectThymeBoardPostByPassword(@Param("idx") int idx, @Param("password") String password);

	void updateHitsOfThymeBoardPost(@Param("idx") int idx);
}
