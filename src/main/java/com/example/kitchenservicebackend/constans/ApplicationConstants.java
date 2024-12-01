package com.example.kitchenservicebackend.constans;

public class ApplicationConstants {

    // Generelle beskeder
    public static final String ERROR_MSG = "An unknown error occurred.";
    public static final String SUCCESS_MSG = "Request successfully processed.";

    // Login/Registrering
    public static final String LOGIN_SUCCESS = "Login successful.";
    public static final String LOGIN_FAILURE = "Invalid credentials, please try again.";
    public static final String REGISTRATION_SUCCESS = "User registered successfully.";
    public static final String REGISTRATION_FAILURE = "Registration failed, please try again.";

    // JWT-relaterede konstanter
    public static final String JWT_HEADER = "Authorization";
    public static final String JWT_PREFIX = "Bearer ";
    public static final long JWT_EXPIRATION_TIME = 86400000L; // 1 dag i millisekunder
    public static final String JWT_SECRET_KEY = "mySecretKeyForJWT"; // Skift til en mere sikker nøgle

    // Rolle-relaterede konstanter
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_USER = "ROLE_USER";

    // Admin-sektion beskeder
    public static final String ADMIN_ACCESS_DENIED = "Access denied: Admin privileges required.";
    public static final String TOKEN_INVALID = "Invalid authentication token.";
    public static final String TOKEN_EXPIRED = "Authentication token has expired.";

    // Logbeskeder
    public static final String LOG_ERROR_PROCESSING_REQUEST = "Error processing the request.";
    public static final String LOG_USER_AUTHENTICATION_FAILED = "User authentication failed.";
    public static final String LOG_INVALID_JWT_TOKEN = "Invalid JWT token provided.";
}
