package com.education.api.exception;

/**
 * Thrown when a lookup by id finds nothing. Mapped to HTTP 404 by
 * GlobalExceptionHandler.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String resourceName;
    private final Object identifier;

    public ResourceNotFoundException(String resourceName, Object identifier) {
        super(resourceName + " not found with id: " + identifier);
        this.resourceName = resourceName;
        this.identifier = identifier;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        this.resourceName = null;
        this.identifier = null;
    }

    public String getResourceName() {
        return resourceName;
    }

    public Object getIdentifier() {
        return identifier;
    }
}
