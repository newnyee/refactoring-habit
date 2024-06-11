package com.refactoringhabit.auth.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.refactoringhabit.auth.domain.service.AuthService;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.auth.dto.SignInRequestDto;
import com.refactoringhabit.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthRestController {

    private final AuthService authService;

    @GetMapping("/check-email")
    public ApiResponse<Boolean> checkEmail(@RequestParam("email") String email) {
        return ApiResponse.ok(authService.emailCheck(email));
    }

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
    public ApiResponse<String> signIn(
        HttpServletResponse response, @RequestBody SignInRequestDto signInRequestDto)
        throws JsonProcessingException {
        authService.authenticationAndCreateSession(response, signInRequestDto);
        return ApiResponse.noContent();
    }

    @PostMapping("/tokens")
    public ApiResponse<String> reissueToken(
        HttpServletRequest request, HttpServletResponse response,
        @RequestAttribute("memberAltId") String memberAltId) throws JsonProcessingException {
        authService.reissueSession(request, response, memberAltId);
        return ApiResponse.noContent();
    }

    @PostMapping("/sign-out")
    public ApiResponse<String> signOut(HttpServletRequest request, HttpServletResponse response) {
        authService.removeSession(request, response);
        return ApiResponse.noContent();
    }
}
