package com.refactoringhabit.auth.dto;

import com.refactoringhabit.common.response.TokenResponse;
import lombok.Builder;

@Builder
public record SignInResponseDto(
    TokenResponse tokenResponse,
    String nickName,
    String profileImage){

}
