package com.refactoringhabit.host.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HostOptionInfoDto {

    private String name;
    private int quantity;
    private int price;
}
