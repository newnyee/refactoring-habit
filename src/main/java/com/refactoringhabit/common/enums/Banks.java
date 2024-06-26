package com.refactoringhabit.common.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.refactoringhabit.host.domain.exception.NotFoundBankException;
import java.util.Arrays;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum Banks {

    NH("농협은행", "011"),
    KB("국민은행", "004"),
    WOORI("우리은행", "020"),
    SHINHAN("신한은행", "088"),
    IBK("기업은행", "003"),
    HANA("하나은행", "081"),
    KAKAO("카카오뱅크", "090");

    private final String name;
    private final String code;

    public static final List<Banks> BANK_LIST = Arrays.stream(Banks.values()).toList();

    public static Banks of(String code) {
        return BANK_LIST.stream()
            .filter(banks -> banks.code.equals(code))
            .findFirst()
            .orElseThrow(NotFoundBankException::new);
    }
}
