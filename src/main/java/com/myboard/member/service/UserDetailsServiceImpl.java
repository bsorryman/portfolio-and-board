package com.myboard.member.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myboard.member.domain.Member;
import com.myboard.member.repository.MemberMapper;

/**
 * Spring Security의 UserDetailsService 인터페이스를 구현한 클래스.
 * @author jspark
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private final MemberMapper memberMapper;
    
    public UserDetailsServiceImpl(MemberMapper memberMapper) {
        this.memberMapper = memberMapper;
    }
    
    /**
     * username(ID)를 통해 사용자 정보 객체를 불러오고, UserDetails(User)에 매핑하여 반환하는 함수
     * SecurityConfig에 설정된 formLogin에 의해 자동으로 서비스가 호출되고, 
     * 사용자가 입력한 Id, Password와 대조되어 검증하고, 일치하다면 인증이 완료되어 접근을 허락한다.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String memberId = username;
        Member user = memberMapper.selectBymemberId(memberId);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return User.withUsername(user.getMemberId())
            .password(user.getMemberPwd())
            .roles(user.getMemberRole())
            .build();
    }
    
}
