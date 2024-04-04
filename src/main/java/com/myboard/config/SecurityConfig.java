package com.myboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private final CustomFilter customFilter; 
    
    public SecurityConfig(CustomFilter customFilter) {
        this.customFilter = customFilter;
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .addFilterBefore(customFilter, BasicAuthenticationFilter.class) // 인증 전 필터
        .authorizeRequests() // 접근 관련
            .antMatchers("/assets/**", "/images/**",  // resources
            		"/portfolio/**", //portfolio page
                    "/login/**", "/signup/**",  // member
                    "/", // main 
                    "/jsp-board/**", "/api/**", // 구버전 게시판  
                    "/index", "/board/free/**" // 신버전 게시판
                    )
                .permitAll() // 접근 허용 URL
            .anyRequest().authenticated() // 나머지 접근은 인증 필요 
            .and()
        .formLogin() // 로그인 관련
            .loginPage("/login") // 로그인 URL
            .usernameParameter("username")
            .passwordParameter("password")
            .successHandler(new CustomLoginSuccessHandler()) // 로그인 성공시 핸들러
            .permitAll() //로그인 페이지에 대한 모든 접근 허용
            .failureUrl("/login?error=true") 
            .and()
        .logout() // 로그아웃 관련
            .logoutUrl("/logout") // 로그아웃 URL
            .logoutSuccessHandler(new CustomLogoutSuccessHandler())
            .permitAll()        
            .and()
        .csrf().disable(); // CSRF 보호 비활성화
        
        return http.build();
    }
    
    /**
     * 비밀번호 암호화 Bean
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }    
    
    /**
     * config의 formLogin을 사용하지 않고 수동 로그인(사용자 인증)을 위한 Bean
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication) throws AuthenticationException {
                return authentication;
            }
        };
    }
    
}
