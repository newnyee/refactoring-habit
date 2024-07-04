package com.refactoringhabit.common.response;

import lombok.Builder;

@Builder
public record HostInfoDto(
    String hostAltId,
    String nickName,
    String profileImage
) {

}
