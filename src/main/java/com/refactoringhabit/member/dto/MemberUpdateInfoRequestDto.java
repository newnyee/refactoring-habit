package com.refactoringhabit.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberUpdateInfoRequestDto {

    private String nickName;
    private String gender;
    private String phone;
    private String birth;
    private String password;
}
