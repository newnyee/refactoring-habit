package com.refactoringhabit.common.enums;

public enum UrlMappings {

    PREFIX_API("/api/v2"),
    VIEW_HOME("/"),
    VIEW_LOGIN("/login"),
    VIEW_HOST_JOIN("/host/join");

    private final String url;

    UrlMappings(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
