package com.refactoringhabit.auth.controller;

import com.refactoringhabit.auth.domain.service.AuthService;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.auth.dto.SignInRequestDto;
import com.refactoringhabit.auth.dto.SignInResponseDto;
import com.refactoringhabit.common.response.ApiResponse;
import com.refactoringhabit.common.response.TokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/find-email")
    public ApiResponse<String> findEmail(@RequestBody FindEmailRequestDto findEmailRequestDto) {
        return ApiResponse.ok(authService.findEmail(findEmailRequestDto));
    }

    @PostMapping("/reset-password")
    public ApiResponse<String> resetPassword(@RequestBody String email) {
        authService.resetPassword(email);
        return ApiResponse.noContent();
    }

    @PostMapping("/sign-in")
    public ApiResponse<SignInResponseDto> signIn(
        HttpServletResponse response, @RequestBody SignInRequestDto signInRequestDto) {
        return ApiResponse
            .ok(authService.authenticationAndCreateToken(response, signInRequestDto));
    }

    @GetMapping("/verify-token")
    public ApiResponse<String> verifyToken() {
        return ApiResponse.noContent();
    }

    @PostMapping("/tokens")
    public ApiResponse<TokenResponse> reissueToken(
        HttpServletRequest request,
        HttpServletResponse response,
        @RequestAttribute("altId") String altId) {

        return ApiResponse.ok(authService.reissueToken(request, response, altId));
    }
}
