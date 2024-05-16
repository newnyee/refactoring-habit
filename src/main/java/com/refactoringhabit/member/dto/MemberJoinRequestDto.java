package com.refactoringhabit.member.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

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
    private MultipartFile profileImgFile;

    public void setProfileImgFile(MultipartFile profileImgFile) {
        this.profileImgFile = profileImgFile;
    }
}
