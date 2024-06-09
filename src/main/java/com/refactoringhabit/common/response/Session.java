package com.refactoringhabit.common.response;

import lombok.Builder;

@Builder
public record Session(String accessToken, String refreshToken) {
}