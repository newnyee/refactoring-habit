package com.refactoringhabit.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FindEmailRequestDto {

    private String phone;
    private String birth;
}
