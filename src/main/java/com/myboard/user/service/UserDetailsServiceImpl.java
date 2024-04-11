package com.myboard.user.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.myboard.user.dto.UserDTO;
import com.myboard.user.repository.UserMapper;

import lombok.RequiredArgsConstructor;

/**
 * Spring Security의 UserDetailsService 인터페이스를 구현한 클래스.
 * @author jspark
 *
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService{
    
    private final UserMapper userMapper;
    
    /**
     * username으로 UserDetails 값을 가져오는 함수.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDTO user = userMapper.selectUserByUsername(username);
        
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        
        return user;
    }
}
