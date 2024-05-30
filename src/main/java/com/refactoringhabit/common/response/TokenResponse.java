package com.refactoringhabit.common.response;

import lombok.Builder;

@Builder
public record TokenResponse(
    String accessToken,
    String expiredTimeForAccessToken,
    String refreshToken,
    String expiredTimeForRefreshToken) {
}