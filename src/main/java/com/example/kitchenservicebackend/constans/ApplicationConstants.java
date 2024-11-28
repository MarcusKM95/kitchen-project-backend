package com.example.kitchenservicebackend.constans;

public class ApplicationConstants {

    // Generelle beskeder
    public static final String ERROR_MSG = "An unknown error occurred.";
    public static final String SUCCESS_MSG = "Request successfully processed.";

    // JWT-relaterede konstanter
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";

    // Rolle-relaterede konstanter
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    // Admin-sektion beskeder
    public static final String ADMIN_ACCESS_DENIED = "Access denied: Admin privileges required.";
    public static final String TOKEN_INVALID = "Invalid authentication token.";
    public static final String TOKEN_EXPIRED = "Authentication token has expired.";
}
