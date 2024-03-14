package com.myboard.member.domain;

import lombok.Data;

@Data
public class Member {
    private int idx;
    private String memberId;
    private String memberPwd;
    private String memberName;
    private String memberEmail;
    private String memberRole;
    private String gSocialYn;
    private String createdAt;
}
