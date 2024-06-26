package com.refactoringhabit.host.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class HostInfoRequestDto {

    private String nickName;

    private String phone;

    private String email;

    private String introduction;

    private String accountNumber;

    private String bank;

    private String accountHolder;

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
