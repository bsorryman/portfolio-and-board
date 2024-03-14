package com.myboard.member.service;

import java.util.Base64;
import java.util.Map;

import org.springframework.boot.json.BasicJsonParser;
import org.springframework.boot.json.JsonParser;
import org.springframework.stereotype.Service;

import com.myboard.member.domain.Member;
import com.myboard.member.repository.MemberMapper;

@Service
public class MemberService {
    private final MemberMapper memberMapper;
    
    public MemberService(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }
    
    /**
     * 회원가입 시 받은 Member 정보를 DB에 insert하는 함수
     * 
     * @param member
     * @return
     */
    public boolean registerMember(Member member) {
        boolean result = false;
        result = memberMapper.insertMember(member);
        
        return result;
    }
    
    /**
     * JWT를 파싱하고 payload 부분만 디코딩하여 반환하는 함수.
     * @param token
     * @return
     */
    public Map<String, Object> decodePayloadInJwt(String token) {
        String splitedToken[] = token.split("\\.");
        String header = splitedToken[0];
        String payload = splitedToken[1];
        String signature = splitedToken[2];
        
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String decodedPayloadString = new String(decoder.decode(payload));
        JsonParser jsonParser = new BasicJsonParser();
        Map<String, Object> payloadMap = jsonParser.parseMap(decodedPayloadString);
        
        return payloadMap;
    }
}
