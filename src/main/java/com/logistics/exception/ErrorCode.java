package com.logistics.exception;

public enum ErrorCode {
    // Generic Errors (1000-1999)
    INTERNAL_SERVER_ERROR("ERR1000", "Internal Server Error"),
    VALIDATION_ERROR("ERR1001", "Validation Error"),
    RESOURCE_NOT_FOUND("ERR1002", "Resource Not Found"),
    INVALID_REQUEST("ERR1003", "Invalid Request"),
    
    // Authentication Errors (2000-2999)
    AUTHENTICATION_FAILED("ERR2000", "Authentication Failed"),
    INVALID_CREDENTIALS("ERR2001", "Invalid Credentials"),
    TOKEN_EXPIRED("ERR2002", "Token Expired"),
    ACCESS_DENIED("ERR2003", "Access Denied"),
    
    // Inventory Errors (3000-3999)
    INVENTORY_NOT_FOUND("ERR3000", "Inventory Item Not Found"),
    SKU_ALREADY_EXISTS("ERR3001", "SKU Already Exists"),
    INVALID_QUANTITY("ERR3002", "Invalid Quantity"),
    CONCURRENT_MODIFICATION("ERR3003", "Concurrent Modification"),
    
    // User Management Errors (4000-4999)
    USER_NOT_FOUND("ERR4000", "User Not Found"),
    EMAIL_ALREADY_EXISTS("ERR4001", "Email Already Exists"),
    INVALID_ROLE("ERR4002", "Invalid Role"),
    LAST_ADMIN_DELETE("ERR4003", "Cannot Delete Last Admin");

    private final String code;
    private final String defaultMessage;

    ErrorCode(String code, String defaultMessage) {
        this.code = code;
        this.defaultMessage = defaultMessage;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
} 