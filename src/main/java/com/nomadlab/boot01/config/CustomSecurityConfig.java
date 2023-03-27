package com.nomadlab.boot01.config;

import com.nomadlab.boot01.security.CustomUserDetailService;
import com.nomadlab.boot01.security.handler.CustomSocialLoginSuccessHandler;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import javax.sql.DataSource;

@Log4j2
@Configuration
@RequiredArgsConstructor
// 권한 체크를 위한 어노태이션
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class CustomSecurityConfig {

    // 주입 필요
    private final DataSource dataSource;
    private final CustomUserDetailService userDetailService;

    // 메서드 생성
    @Bean
    public PersistentTokenRepository persistentTokenRepository() {
        JdbcTokenRepositoryImpl repo = new JdbcTokenRepositoryImpl();
        repo.setDataSource(dataSource);
        return repo;
    }

    // 이렇게 하면 접속시 로그인 화면으로 자동 넘어가고
    // 아이디는 user 비밀번호는 아래 로그에 Using generated security password: 1f969541-73b8-4c95-84c0-44461eba902c 이걸로 로그인하면 됨.
    // 근데 이렇게 안하고 그냥 로그인 없이 보고 싶다면 아래 코드 추가 하면 됨.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        log.info("-------------------configure-------------------");

        // 커스텀 로그인 페이지
        http.formLogin().loginPage("/member/login");

        // csrf 토큰이 아직 발급 받지 않았기 때문에 로그인 하면 에러가 떠서
        // 그냥 disable()로 해서 CSRF 토큰 비활성화 시켜서 그냥 로그인 가능하게 함.
        http.csrf().disable();


        http.rememberMe()
                .key("12345678")
                .tokenRepository(persistentTokenRepository())
                .userDetailsService(userDetailService)
                .tokenValiditySeconds(60*60*24*30);

//        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler()); // 403

        http.oauth2Login().loginPage("/member/login").successHandler(authenticationSuccessHandler());
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

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new CustomSocialLoginSuccessHandler(passwordEncoder());
    }

}
