package com.refactoringhabit.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberJoinRequestDto {

    private String email;
    private String password;
    private String nickName;
    private String phone;
    private String birth;
    private String gender;
    private String profileImage;

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}
