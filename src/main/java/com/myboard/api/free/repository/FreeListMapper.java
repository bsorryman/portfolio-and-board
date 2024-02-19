package com.myboard.api.free.repository;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.myboard.api.free.domain.FreePost;

@Mapper
public interface FreeListMapper {
	int selectTotalFreePost(@Param("page") int page, @Param("field") String field,
			@Param("keyword") String keyword);
	
	List<FreePost> selectFreePostList(@Param("page") int page, @Param("size") int size,
			@Param("bsize") int bsize,
			@Param("field") String field,
			@Param("keyword") String keyword);

	int insertFreePost(FreePost freePost);

	FreePost selectFreePost(int idx);

	int deleteFreePost(@Param("idx") int idx, @Param("password") String password);

	int updateFreePost(FreePost freePost);

	//for edit
	int selectFreePostByPassword(@Param("idx") int idx, @Param("password") String password);

	void updateHitsOfFreePost(@Param("idx") int idx);
	
}
