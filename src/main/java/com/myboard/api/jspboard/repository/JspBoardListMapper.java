package com.myboard.api.jspboard.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.myboard.api.jspboard.domain.JspBoardPost;

@Mapper
public interface JspBoardListMapper {
	int selectTotalJspBoardPost(@Param("page") int page, @Param("field") String field,
			@Param("keyword") String keyword);
	
	List<JspBoardPost> selectJspBoardPostList(@Param("page") int page, @Param("size") int size,
			@Param("bsize") int bsize,
			@Param("field") String field,
			@Param("keyword") String keyword);

	int insertJspBoardPost(JspBoardPost jspBoardPost);

	JspBoardPost selectJspBoardPost(int idx);

	int deleteJspBoardPost(@Param("idx") int idx, @Param("password") String password);

	int updateJspBoardPost(JspBoardPost jspBoardPost);

	//for edit
	int selectJspBoardPostByPassword(@Param("idx") int idx, @Param("password") String password);

	void updateHitsOfJspBoardPost(@Param("idx") int idx);
	
}
