package com.myboard.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myboard.user.dto.UserDTO;
import com.myboard.user.repository.UserMapper;

/**
 * Spring Security의 UserDetailsService 인터페이스를 구현한 클래스.
 * @author jspark
 *
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private final UserMapper userMapper;
    
    public UserDetailsServiceImpl(UserMapper userMapper) {
        this.userMapper = userMapper;
    }
    
    /**
     * username으로 UserDetails 값을 가져오는 함수.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userMapper.selectUserByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        /*
        // UserDetails 필드 매핑
        user.setUsername(user.getMemberId());
        user.setPassword(user.getMemberPwd());
        
        // UserDetails 권한은 타입을 구성하기 까다로워 Build 후 매핑한다. 
        UserDetails userDetails = User.withUsername(user.getMemberId())
                                      .roles(user.getMemberRole())
                                      .build();
        
        user.setAuthorities(userDetails.getAuthorities());
        */
        
        return user;
    }
}
