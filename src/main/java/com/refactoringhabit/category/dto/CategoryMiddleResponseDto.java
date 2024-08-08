package com.refactoringhabit.category.dto;

import java.io.Serializable;

public record CategoryMiddleResponseDto(
    String altId,
    String name,
    String engName) implements Serializable {
}
