package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Security Session => Authentication => UserDetails
// 시큐리티 설정에서  .loginProcessingUrl("/login")을 설정해뒀으므로
// '/login' 요청이 오면 자동으로 UserDetailsService 타입으로 IoC되어있는 loadUserByUsername 메서드가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public PrincipalDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 중요!!!
    // 여기서 username과 loginForm.html의 username과 이름이 동일하게 username이어야 한다.
    // 중요!!
    // Security Session(Authentication(UserDetails))
    // Security Session(Authentication(UserDetails))
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            return new PrincipalDetails(user);
        }
        return null;
    }
}
