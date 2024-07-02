package com.refactoringhabit.category.dto;

import java.io.Serializable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CategoryMiddleResponseDto implements Serializable {

    private String altId;
    private String name;
}
