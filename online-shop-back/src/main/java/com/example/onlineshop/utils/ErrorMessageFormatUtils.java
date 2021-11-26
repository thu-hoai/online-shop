package com.example.onlineshop.utils;

import com.example.onlineshop.enums.ExceptionCode;

import java.util.ResourceBundle;

public class ErrorMessageFormatUtils {

    private ErrorMessageFormatUtils() {}


    public static final ResourceBundle resourceBundle = ResourceBundle.getBundle("messageErrorResouce");

    public static String getErrorMessage(ExceptionCode messageKey, String arg) {
        return String.format(resourceBundle.getString(messageKey.toString()), arg);
    }

}
