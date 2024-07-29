package com.refactoringhabit.category.dto;

import java.io.Serializable;
import java.util.List;

public record CategoryLargeResponseDto (
    String name,
    String engName,
    String image,
    List<CategoryMiddleResponseDto> categoryMiddleList) implements Serializable {
}
