package com.myboard.board.util;

import lombok.Data;

@Data
public class Pager {
	private int page; // current page
	private int size; // rows per page
 	private int bsize; // pages per block
 
 	private int rows; // total elements
 	private int totalPage; // total pages
 	private int totalBlock; // total blocks
 
 	private int block; // current block
 	private int bsPage; // current block start page
 	private int bePage; // current block end page
 
 	private int prevPage;
 	private int nextPage;
 	
 	public Pager(int page, int size, int bsize, int rows) {
 
 		this.page = page;
 		this.size = size;
 		this.bsize = bsize;
 		this.rows = rows;
        
 		// 총 페이지 개수(totalPage) 는 전체 row 개수를 페이지 당 나타낼 row 개수(size)로 나누어 소수점 올림 처리하여 추출
 		// ex 265 row / 15 size = 18 page
 		totalPage = (int) Math.ceil((double) this.rows / this.size);
 
 		// 총 블럭 개수는(totalBlock)는 전체 페이지 개수를 블럭 당 나타낼 page 개수 (bsize)로 나누어 소수점 올림 처리하여 추출
 		totalBlock = (int) Math.ceil((double) totalPage / this.bsize);
 
 		//(현재 몇 번째 블럭인지) 블럭 넘버(block)는 bsize 에서 현재 페이지 넘버를 나누어 올림 처리 추출
 		//ex) 2/5 -> 1번째 블럭, 6/5 -> 2번째 블럭
 		block = (int) Math.ceil((double) this.page / this.bsize);
 
 		//현재 블럭의 끝 페이지는 bsize 에서 현재 블럭 넘버를 곱하여 추출
 		bePage = block * this.bsize;
 
 		//현재 블럭의 시작 페이지는 블럭 끝 페이지에서 bsize + 1을 뺄셈하여 추출
 		bsPage = bePage - this.bsize + 1;
 		
 		//단, 블럭 끝 페이지가 가장 끝 페이지를 넘지 않도록 (bsPage 계산이 끝난 후에) 		
 		if (bePage>totalPage) bePage = totalPage; 

 		prevPage = bsPage - 1;
 		nextPage = bePage + 1;
 	}
 }
