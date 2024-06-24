package com.refactoringhabit.host.dto;

import lombok.Builder;

@Builder
public record HostInfoResponseDto(

     String altId,
     String nickName,

     String phone,

     String profileImage,

     String email,

     String introduction,

     String accountNumber,

     String bank,

     String accountHolder) {

}
