package com.refactoringhabit.auth.controller;

import com.refactoringhabit.auth.domain.service.AuthService;
import com.refactoringhabit.auth.dto.FindEmailRequestDto;
import com.refactoringhabit.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2/auth")
public class AuthRestController {

    private final AuthService authService;

    @PostMapping("/find-email")
    public ApiResponse<String> findEmail(@RequestBody FindEmailRequestDto findEmailRequestDto) {
        return ApiResponse.ok(authService.findEmail(findEmailRequestDto));
    }
}
