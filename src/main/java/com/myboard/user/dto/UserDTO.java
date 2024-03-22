package com.myboard.user.dto;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.Data;

@Data
public class UserDTO implements UserDetails{
    
    private static final long serialVersionUID = 1L;
    
    /*
     * DB tb_user 필드 
     */
    private int idx;
    private String nickname;
    private String email;
    private String picture;
    private String role;
    private String sns;
    private String createDt;
    private String updateDt;
    
    /**
     * UserDetials 필드
     */
    private String username;
    private String password;
    
    /**
     * DB에서 사용하지 않는 UserDetails 필드
     */
    private boolean accountNonLocked = true; // 계정 잠기지 않음
    private boolean accountNonExpired = true ; // 계정 만료 없음
    private boolean credentialsNonExpired = true ; // 비밀번호 만료 없음
    private boolean enabled = true; // 사용자 활성화 됨
    
    /**
     * UserDetails Getter
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        for(String role : role.split(",")){
            authorities.add(new SimpleGrantedAuthority(role));
        }
        
        return authorities;
    }
    
    @Override
    public String getPassword() {
        return password;
    }
    
    @Override
    public String getUsername() {
        return username;
    }
    
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }
    
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }
    
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
