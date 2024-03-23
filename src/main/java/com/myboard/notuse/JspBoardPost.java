package com.myboard.notuse;

import lombok.Data;

@Data
public class JspBoardPost {
	
	private int idx;
	private String title; 
	private String writer;
	private String password;
	private String contents;
	private String createdAt;
	private int hits;
	
	public static int seekOffset(int page, int size) {
		if (page > 0) {
			return (page - 1) * size;
		}
 		return 0;
 	}
}
