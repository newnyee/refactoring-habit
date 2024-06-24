package com.refactoringhabit.common.enums;

public enum AttributeNames {

    MEMBER_ALT_ID("memberAltId"),
    HOST_ALT_ID("hostAltId"),
    REDIRECT_URL("redirectURL"),
    MEMBER_INFO("memberInfo"),
    HOST_INFO("hostInfo"),
    SESSION_COOKIE_NAME("SESSION");

    private final String name;

    AttributeNames(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
