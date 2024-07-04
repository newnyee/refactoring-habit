package com.refactoringhabit.category.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryLargeResponseDto implements Serializable {

    private String name;
    private String engName;
    private List<CategoryMiddleResponseDto> categoryMiddleList;
}
