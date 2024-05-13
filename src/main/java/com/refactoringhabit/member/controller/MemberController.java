package com.refactoringhabit.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberController {

    @GetMapping("/join")
    public String join() {
        return "/pages/member/member-join";
    }
}
