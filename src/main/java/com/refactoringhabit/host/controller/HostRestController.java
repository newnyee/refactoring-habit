package com.refactoringhabit.host.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.host.domain.service.HostService;
import com.refactoringhabit.host.dto.HostJoinRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/api/v2/hosts")
@RequiredArgsConstructor
public class HostRestController {

    private final HostService hostService;

    @PostMapping
    public ApiResponse<String> hostJoinApi(
        @RequestAttribute("memberAltId") String memberAltId,
        @RequestPart(value = "hostInfo") HostJoinRequestDto hostJoinRequestDto,
        @RequestPart(value = "profileImgFile", required = false) MultipartFile multipartFile) {
        log.debug("member alt id = {}", memberAltId);
        log.debug("host join request dto = {}", hostJoinRequestDto.getNickName());
        log.debug("multipart file = {}", multipartFile);

        hostService.hostJoin(memberAltId, hostJoinRequestDto, multipartFile);
        return ApiResponse.created();
    }

    @GetMapping("/check-nick-name")
    public ApiResponse<Boolean> hostNickNameCheckApi(@RequestParam("nickName") String nickName) {
        return ApiResponse.ok(hostService.nickNameCheck(nickName));
    }
}
