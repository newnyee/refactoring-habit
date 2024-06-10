package com.refactoringhabit.common.response;

import lombok.Builder;

@Builder
public record MemberInfoDto(
    String type,
    String profileImage,
    String nickName) {

}
