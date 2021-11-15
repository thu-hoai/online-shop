package com.example.onlineshop.enums;

import lombok.Getter;

@Getter
public enum ExceptionCode {
    // Bad request
    BAD_REQUEST("400"),
    USER_NOT_FOUND("400"),
    OUT_OF_STOCK_ORDER_ITEM("400"),
    ERROR_SYNTAX_BAD_REQUEST("400"),
    EXISTENT_USER("400"),
    // Unauthorized
    ERROR_INVALID_CREDENTIAL("401"),
    ORDER_ITEM_NOT_FOUND("404"),
    ORDER_NOT_FOUND("400"),
    PRODUCT_NOT_FOUND("400"),
    ERROR_ELEMENT_NOT_FOUND("404"),

    // Access denied
    ERROR_ACCESS_DENIED("403");

    private final String statusCode;

    ExceptionCode(String statusCode) {
        this.statusCode = statusCode;
    }
}
