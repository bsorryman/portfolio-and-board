package com.myboard.api;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.myboard.user.dto.UserDTO;
import com.myboard.user.service.UserService;

@Controller
public class UserApiController {
    @Autowired
    UserService userService;
    
    // API Example
    @RequestMapping(method = RequestMethod.GET, value = "/apis/")
    public ResponseEntity<List<UserDTO>> getUserList() {
        List<UserDTO> userList = new ArrayList<UserDTO>();

        try {
            //userList = userService.getUserList();
        } catch (Exception e) {
            return new ResponseEntity<List<UserDTO>>(userList, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
        return new ResponseEntity<List<UserDTO>>(userList, HttpStatus.OK);
        
    }
    
    
}
