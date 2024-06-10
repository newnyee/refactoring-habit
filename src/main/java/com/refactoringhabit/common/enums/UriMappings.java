package com.refactoringhabit.common.enums;

public enum UriMappings {

    PREFIX_API("/api/v2"),
    VIEW_HOME("/"),
    VIEW_JOIN("/join"),
    VIEW_FIND_MEMBER("/find-member"),
    VIEW_LOGIN("/login"),
    VIEW_CATEGORY("/category"),
    VIEW_PRODUCT("/product"),
    VIEW_SEARCH_LIST("/search"),
    VIEW_HOST_JOIN("/host/join");

    private final String uri;

    UriMappings(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
