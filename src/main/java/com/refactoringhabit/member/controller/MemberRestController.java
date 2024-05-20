package com.refactoringhabit.member.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.member.domain.service.MemberService;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
public class MemberRestController {

    private final MemberService memberService;

    @PostMapping
    public ApiResponse<String> joinApi(
        @RequestPart(value = "memberInfo") MemberJoinRequestDto memberJoinRequestDto,
        @RequestPart(value = "profileImgFile", required = false) MultipartFile multipartFile
    ) {
        memberService.memberJoin(memberJoinRequestDto, multipartFile);
        return ApiResponse.created();
    }

    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam("email") String email) {
        return ApiResponse.ok(memberService.emailCheck(email));
    }
}
