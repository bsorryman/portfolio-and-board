package com.myboard.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public void filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
        // 어떤 요청이든 '인증'이 필요하다.
        .anyRequest().authenticated()
        .and()
            // 단, 로그인 기능은 인증없이 허용
            .formLogin()
            .defaultSuccessUrl("/")
            .permitAll()
        .and()
            // 단, 로그아웃 기능은 인증없이 허용
            .logout()
            .permitAll();
    }
}
