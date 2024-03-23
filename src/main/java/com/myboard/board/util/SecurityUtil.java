package com.myboard.board.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUtil {

	/**
	 * 파라미터로 받은 username과 현재 인증된 사용자의 username이 같은지 검증하는 함수
	 * 
	 * @param username
	 * @return
	 */
	public boolean validateUsername(String username) {

		boolean result = false;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String authUsername = "";
        
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                authUsername = userDetails.getUsername();
            }
        } else {
        	
            return result;    	
        }		
        
        if(username.equals(authUsername)) {
        	result = true;
        } 
        
        return result;
	}
}
