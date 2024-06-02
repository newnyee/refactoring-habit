package com.refactoringhabit.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthController {

    @GetMapping("/find-member")
    public String findMember() {
        return "/pages/member/find-member";
    }

    @GetMapping("/find-member/id")
    public String findId() {
        return "/pages/member/find-id";
    }

    @GetMapping("/find-member/password")
    public String findPassword() {
        return "/pages/member/find-password";
    }

    @GetMapping("/login")
    public String login() {
        return "/pages/member/member-login";
    }
}
