package com.myboard.user.repository;

import org.apache.ibatis.annotations.Mapper;

import com.myboard.user.dto.UserDTO;

@Mapper
public interface UserMapper {
    
    boolean insertUser(UserDTO userDTO);
    UserDTO selectUserByUsername(String username);

}
