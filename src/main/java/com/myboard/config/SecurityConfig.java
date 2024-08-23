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
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final CustomFilter customFilter; 
    private final SimpleUrlAuthenticationSuccessHandler customLoginSuccessHandler;
    private final LogoutSuccessHandler customLogoutSuccessHandler;
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
        .addFilterBefore(customFilter, BasicAuthenticationFilter.class) // 인증 전 필터
        .authorizeRequests() // 접근 관련
            .antMatchers("/assets/**", "/images/**", "/pdf/**", "/favicon.ico",  // resources
            		"/portfolio/**", //portfolio page
            		"/project/**",
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
            .successHandler(customLoginSuccessHandler) // 로그인 성공시 핸들러
            .permitAll() //로그인 페이지에 대한 모든 접근 허용
            .failureUrl("/login?type=error") 
            .and()
        .logout() // 로그아웃 관련
            //.logoutUrl("/logout") // 로그아웃 URL -> CSRF 설정 추가하면서 사용 불가 
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
            .logoutSuccessHandler(customLogoutSuccessHandler)
            .permitAll()        
            .and()
        .csrf().csrfTokenRepository(csrfTokenRepository()); 
        
        return http.build();
    }
    
    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository csrfTokenRepository = new HttpSessionCsrfTokenRepository();
        csrfTokenRepository.setHeaderName("X-CSRF-TOKEN");
        return csrfTokenRepository;
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
