package com.refactoringhabit.common.enums;

import static com.refactoringhabit.common.enums.UriMappings.*;

import java.util.Arrays;
import java.util.List;

public enum UriAccessLevel {
    NULL_SESSION_ONLY_URI(Arrays.asList(VIEW_JOIN, VIEW_FIND_MEMBER, VIEW_LOGIN)),
    PUBLIC_URI(Arrays.asList(VIEW_CATEGORY, VIEW_PRODUCT, VIEW_SEARCH_LIST));

    private final List<UriMappings> uriMappingsList;

    UriAccessLevel(List<UriMappings> list) {
        this.uriMappingsList = list;
    }

    public List<UriMappings> getUriMappingsList() {
        return uriMappingsList;
    }
}
