package com.cos.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시켜줌
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어줌
// 즉 같은 session인데 security가 자체적으로 관리하는 공간에 저장된다(Security ContextHolder에 저장)
// Authentication 타입 객체로 저장 공간에 접근하자
// Authentication 안에는 User정보가 있어야 함
// User오브젝트 타입은 => UserDetails 타입 객체가 들어가야 함

import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// 의존성을 살펴보면
// Security Session => Authentication => UserDetails
// 여기는 UserDetails를 정의한 클래스
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user;//콤포지션

    public PrincipalDetails(User user) {
        this.user = user;
    }



    // 해당 User의 권한을 리턴하는 메서드!!!
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    // 계정이 만료되지 않았는지?
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정이 잠겼는가?
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 계정 암호 등이 안전한가?
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {

        //우리 사이트는 1년동안 회원 로그인을 안하면 휴면 계정으로 하기로 함!!!
        // 현재시간 - 로그인 시간 = 1년이상이면 휴면 계정으로 전환
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
