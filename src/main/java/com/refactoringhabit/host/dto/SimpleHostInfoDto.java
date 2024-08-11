package com.refactoringhabit.host.dto;

public record SimpleHostInfoDto(
    String profileImage,
    String nickName,
    Long reviewCount,
    Long totalProductCount) {
}
