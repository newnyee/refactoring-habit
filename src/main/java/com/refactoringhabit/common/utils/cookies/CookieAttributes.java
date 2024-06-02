package com.refactoringhabit.common.utils.cookies;

public class CookieAttributes {

    public static final String COOKIE_PATH = "/";
    public static final String SET_COOKIE = "set-cookie";
    public static final String REFRESH_TOKEN_COOKIE_NAME = "refreshToken";
    public static final long SESSION_EXPIRE_TIME_MAX = 30 * 24 * 60 * 60L; // 30일
    public static final long SESSION_EXPIRE_TIME_ZERO = 0L;

    private CookieAttributes() {}
}
