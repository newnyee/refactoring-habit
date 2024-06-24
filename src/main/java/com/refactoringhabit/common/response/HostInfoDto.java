package com.refactoringhabit.common.response;

import lombok.Builder;

@Builder
public record HostInfoDto(
    String nickName,
    String profileImage
) {

}
