package com.refactoringhabit.host.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class HostProductInfoDto {

    private String categoryMiddleAltId;
    private String productName;
    private String zipCode;
    private String address1;
    private String address2;
    private String extraAddress;
    private String tagGender;
    private String tagAge;
    private String tagWith;
    private String closedAt;
    private String type;
    private String description;
    private List<HostOptionInfoDto> optionInfoList;
}
