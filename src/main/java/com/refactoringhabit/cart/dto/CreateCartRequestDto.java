package com.refactoringhabit.cart.dto;

import java.util.List;
import lombok.Getter;

@Getter
public class CreateCartRequestDto {

    private String productAltId;
    private List<ChooseOptionInfoDto> chooseOptionInfoDtos;
    private Boolean shouldDeleteCart;
}
