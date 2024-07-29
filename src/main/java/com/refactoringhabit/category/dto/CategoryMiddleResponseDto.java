package com.refactoringhabit.category.dto;

import java.io.Serializable;

public record CategoryMiddleResponseDto(
    String altId,
    String name) implements Serializable {
}
