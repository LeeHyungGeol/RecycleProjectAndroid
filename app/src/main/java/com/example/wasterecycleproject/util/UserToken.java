package com.example.wasterecycleproject.util;

public class UserToken {

    private static String token = "";

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        UserToken.token = token;
    }
}
