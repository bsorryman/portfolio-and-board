package com.myboard.board.domain;

import lombok.Data;

@Data
public class MemberBoardPost {
    private int idx;
    private String title; 
    private String username;
    private String nickname;
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
