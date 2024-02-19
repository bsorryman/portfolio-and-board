package com.myboard.api.free.domain;

import lombok.Data;

@Data
public class FreePost {
	
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
