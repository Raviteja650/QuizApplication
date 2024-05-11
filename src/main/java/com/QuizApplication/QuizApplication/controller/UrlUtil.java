package com.QuizApplication.QuizApplication.controller;

import jakarta.servlet.http.HttpServletRequest;

public class UrlUtil {

    public static String getApplicationUrl(HttpServletRequest request)
    {
        String appUrl=request.getRequestURI().toString();
        return appUrl.replace(request.getServletPath(),"");
    }
}
