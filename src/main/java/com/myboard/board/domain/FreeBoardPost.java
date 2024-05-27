package com.myboard.board.domain;

import lombok.Data;

@Data
public class FreeBoardPost {
    private int idx;
    private String title; 
    private String writer;
    private String password;
    private String contents;
    private char deleteYn;
    private String createdAt;
    private int hits;
}
