package com.elmika.tsp.application;

/**
 * Thrown when configuration cannot be loaded (file missing or malformed).
 * Surfaces misconfiguration instead of silently falling back to defaults.
 */
public class ConfigLoadException extends RuntimeException {

    public ConfigLoadException(String message) {
        super(message);
    }

    public ConfigLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
