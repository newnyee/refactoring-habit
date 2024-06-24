package com.refactoringhabit.common.controller;

import static com.refactoringhabit.common.enums.Banks.BANK_LIST;

import com.refactoringhabit.common.enums.Banks;
import com.refactoringhabit.common.response.ApiResponse;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CommonRestController {

    @GetMapping("/api/v2/banks")
    public ApiResponse<List<Banks>> bankList() {
        return ApiResponse.ok(BANK_LIST);
    }
}
