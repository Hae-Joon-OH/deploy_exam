package com.nomadlab.boot01.security;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Log4j2
@Service
//@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    // 인증 처리를 위한 코드 UserDetailService라는 인터페이스의 구현체라 볼 수 있음.

    private PasswordEncoder passwordEncoder;

    public CustomUserDetailService() {
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername: " + username);

        // User라는 객체를 사용해서 아이디와 패스워드를 생성해서 권한 확인
        // 근데 아래 코드만 작성하면 passwordencoder가 없어서 에러 발생
        // CustomSecurityConfig에 passwordencoder 코드 추가
        UserDetails userDetails = User.builder()
                .username("user1")
                // .password("1111")
                .password(passwordEncoder.encode("1111")) //  패스워드 인코딩 필요
                .authorities("ROLE_USER")
                .build();
        // 이렇게 하고 프로젝트 실행해서 로그인 하면 index.html 파일이 있기 때문에 '/' 경로로 자동 이동
        return userDetails;
    }

}
