package com.cos.security1.config;

import com.cos.security1.config.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // 스프링 시큐리티 필터가 스프링 필터체인에 등록이 됨
// securedEnabled : @Secured 활성화 / prePostEnabled : @preAuthorize, @PostAuthorize 활성화
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled = true)
public class SecurityConfig{

//1. 코드받기(인증), 2.엑세스토큰 받기(권한),
// 3.사용자 정보를 가져오고, 4. 정보를 토대로 회원가입을 자동으로 진행시키기도 함.
// 4-2(이메일, 전화번호, 이름, 아이디) + 쇼핑몰일 경우 집주소, 백화점의 경우 vip여부 정보가 필요

    // 패스워드를 암호화 시켜주는 객체
    @Bean
    public BCryptPasswordEncoder encodePwd() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    private PrincipalOauth2UserService principalOauth2UserService;
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated() // user 페이지에는 권한 필요
                .antMatchers("/manager/**").access("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // manager페이지에 필요한 권한
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')") // admin페이지에 필요한 권한
                .anyRequest().permitAll() // 모든 요청은 권한 허가
                // 추가
                .and()
                .formLogin()
                .loginPage("/loginForm")
                // 로그인 관련 추가
                // "/login"으로 접근을 하면 security가 낚아채서 대신 로그인을 처리해준다
                // 따라서 따로 컨트롤러에 login메소드를 만들지 않아도 된다
                .loginProcessingUrl("/login")
                // 로그인 성공시 이동할 페이지
                .defaultSuccessUrl("/")
                // oauth2관련 설정
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
                // 코드 추가
                .userInfoEndpoint()
                .userService(principalOauth2UserService)
        ; // 구글로그인 성공 이후에 후처리가 필요.  Tip oauth는 코드를 받는게 아니라, (엑세스토큰 + 사용자프로필 정보를 한번에 받음)

        return http.build();
    }


}
