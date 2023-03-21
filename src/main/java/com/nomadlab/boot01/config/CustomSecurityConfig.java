package com.nomadlab.boot01.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Log4j2
@Configuration
@RequiredArgsConstructor
// 권한 체크를 위한 어노태이션
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {
    // 이렇게 하면 접속시 로그인 화면으로 자동 넘어가고
    // 아이디는 user 비밀번호는 아래 로그에 Using generated security password: 1f969541-73b8-4c95-84c0-44461eba902c 이걸로 로그인하면 됨.
    // 근데 이렇게 안하고 그냥 로그인 없이 보고 싶다면 아래 코드 추가 하면 됨.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("-------------------confiture-------------------");

        http.formLogin().loginPage("/member/login");

        return http.build();
    }

    // css나 js 파일같은 정적 파일에는 시큐리티를 적용할 필요가 없으므로
    // 아래 코드 추가
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        log.info("--------------------web configure--------------------");

        return (web -> web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations()));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
