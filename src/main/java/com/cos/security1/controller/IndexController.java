package com.cos.security1.controller;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/test/login")
    // PrincipalDetails가 UserDetails을 상속받고 있기 때문에 UserDetails 대신에 PrincipalDetails를 사용할 수 있음
    public @ResponseBody String loginTest(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) { // DI(의존성주입)
        System.out.println("/test/login =========");
        // Authetication을 PrincipalDetails로 캐스팅하여 사용하는 방법
        // PrincipalDetails는 우리가 앞서 생성했던 config.auth.PrincipalDetails
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        System.out.println("principalDetails = " + principalDetails.getUser());

        // @AuthenticationPrincipal을 사용하는 방법
        // PrincipalDetails가 UserDetails를 상속받고 있기 때문에 UserDetails 대신에 PrincipalDetails를 사용할 수 있음
        System.out.println("userDetails = " + userDetails.getUser());
        return "세션 정보 확인하기";
    }

    @GetMapping("/test/oauth/login")
    @ResponseBody
    // PrincipalDetails가 UserDetails을 상속받고 있기 때문에 UserDetails 대신에 PrincipalDetails를 사용할 수 있음
    public String loginOAuthTest(Authentication authentication, @AuthenticationPrincipal OAuth2User oAuth) { // DI(의존성주입)
        System.out.println("/test/oauth/login =========");
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        System.out.println("authentication = " + oAuth2User.getAttributes());
        System.out.println("OAuth2User = " + oAuth.getAttributes());
        return "OAuth 세션 정보 확인하기";
    }

    public IndexController(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources
        // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) 생략가능!!
        return "index"; // src/main/resources/tamplates/index.mustache
    }

    //OAuth로그인을 해도 PrincipalDetails로 받을 수 있고
    //일반 로그인을 해도 PrincipalDetails로 받을 수 있다.
    @GetMapping("/user")
    @ResponseBody
    public String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        System.out.println("principalDetails = " + principalDetails.getUser());
        return "user";
    }

    @GetMapping("/manager")
    @ResponseBody
    public String manager() {
        return "manager";
    }

    @GetMapping("/admin")
    @ResponseBody
    public String admin() {
        return "admin";
    }

    // 스프링 시큐리티가 주소를 낚아채버림 - Security Config 파일 생성 후 작동 안함
    @GetMapping("/loginForm")
    public String loginForm() {
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm() {
        return "joinForm";
    }

    @PostMapping("/join")
    public String join(User user) {
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user); // 회원가입은 되지만 비밀번호가 1234등이 들어갈 수 있음
        // -> 이는 시큐리티로 로그인을 할 수 없는데, 이유는 패스워드가 암호화가 안되어 있기 때문임!!
        return "redirect:/loginForm";
    }

    @Secured("ROLE_ADMIN") // 특정 권한이 있는 사용자만 접근 가능!
    @GetMapping("/info")
    @ResponseBody
    public String info() {
        return "개인정보";
    }

    // 여러 권한을 걸때 편함
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // data 메서드가 실행되기 직전에 실행
    @GetMapping("/data")
    @ResponseBody
    public String data() {
        return "데이터정보";
    }
}
