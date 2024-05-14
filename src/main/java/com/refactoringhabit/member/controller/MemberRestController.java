package com.refactoringhabit.member.controller;

import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v2/members")
public class MemberRestController {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String joinApi(
        @RequestPart(value = "memberInfo") MemberJoinRequestDto memberJoinRequestDto,
        @RequestPart(value = "profileImgFile", required = false) MultipartFile multipartFile
    ) {
        log.debug("memberJoinRequestDto = {}", memberJoinRequestDto);
        return "ok";
    }
}
