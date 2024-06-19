package com.refactoringhabit.member.controller;

import com.refactoringhabit.member.domain.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/join")
    public String join() {
        return "/pages/member/member-join";
    }

    @GetMapping("/my-page")
    public String myPage() {
        return "/pages/member/mypage";
    }

    @GetMapping("/my-page/info")
    public String updateProfile(@RequestAttribute("memberAltId") String memberAltId, Model model) {
        model.addAttribute("updateInfoDto", memberService.getMemberInfo(memberAltId));
        return "/pages/member/member-info-edit";
    }
}
