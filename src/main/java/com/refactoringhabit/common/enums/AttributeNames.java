package com.refactoringhabit.common.enums;

public enum AttributeNames {
    
    ALT_ID("altId"),
    REDIRECT_URL("redirectURL"),
    MEMBER_INFO("memberInfo");

    private final String name;

    AttributeNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
