package com.refactoringhabit.member.dto;

import lombok.Builder;

@Builder
public record MemberInfoResponseDto(
    String altId,
    String email,
    String nickName,
    String phone,
    String birth,
    String gender,
    String profileImage) {

}
