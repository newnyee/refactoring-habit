package com.refactoringhabit.member.controller;

import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.member.domain.service.MemberService;
import com.refactoringhabit.member.dto.MemberJoinRequestDto;
import com.refactoringhabit.member.dto.MemberUpdateInfoRequestDto;
import com.refactoringhabit.review.domain.service.ReviewService;
import com.refactoringhabit.review.dto.ReviewDetailListByMemberResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/members")
public class MemberRestController {

    private final MemberService memberService;
    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<String> joinApi(
        @RequestPart(value = "memberInfo") MemberJoinRequestDto memberJoinRequestDto,
        @RequestPart(value = "profileImgFile", required = false) MultipartFile multipartFile) {
        memberService.memberJoin(memberJoinRequestDto, multipartFile);
        return ApiResponse.created();
    }

    @PatchMapping("/{memberAltId}")
    public ApiResponse<String> updateInfoApi(
        @PathVariable("memberAltId") String memberAltId,
        @RequestPart(value = "updateInfo") MemberUpdateInfoRequestDto memberUpdateInfoRequestDto,
        @RequestPart(value = "profileImgFile", required = false) MultipartFile multipartFile) {
        memberService.memberUpdate(memberAltId, memberUpdateInfoRequestDto, multipartFile);
        return ApiResponse.noContent();
    }

    @GetMapping("/{memberAltId}/reviews")
    public ApiResponse<ReviewDetailListByMemberResponseDto> getReviewsByMemberApi(
        @PathVariable("memberAltId") String memberAltId, Pageable pageable) {
        return ApiResponse.ok(ReviewDetailListByMemberResponseDto.builder()
            .reviewDetailDtos(reviewService.getReviewDetailDtoListByMemberId(memberAltId, pageable))
            .reviewCount(reviewService.getReviewCountByMemberId(memberAltId))
            .build());
    }
}
