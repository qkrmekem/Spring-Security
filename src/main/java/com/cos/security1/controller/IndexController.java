package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {

    @GetMapping({"", "/"})
    public String index() {
        // 머스테치 기본폴더 src/main/resources
        // 뷰리졸버 설정 : templates (prefix), .mustache (suffix) 생략가능!!
        return "index"; // src/main/resources/tamplates/index.mustache
    }

    @GetMapping("/user")
    @ResponseBody
    public String user() {
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
    @GetMapping("/login")
    @ResponseBody
    public String login() {
        return "login";
    }

    @GetMapping("/join")
    @ResponseBody
    public String join() {
        return "join";
    }

    @GetMapping("/joinProc")
    @ResponseBody
    public String joinProc() {
        return "회원가입 완료됨!";
    }

}
