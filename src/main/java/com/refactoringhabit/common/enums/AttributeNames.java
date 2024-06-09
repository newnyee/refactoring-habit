package com.refactoringhabit.common.enums;

public enum AttributeNames {

    MEMBER_ALT_ID("memberAltId"),
    REDIRECT_URL("redirectURL"),
    MEMBER_INFO("memberInfo"),
    SESSION_COOKIE_NAME("SESSION");

    private final String name;

    AttributeNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
