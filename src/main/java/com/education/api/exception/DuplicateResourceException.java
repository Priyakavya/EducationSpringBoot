package com.education.api.exception;

/**
 * Thrown when a unique field would collide - a duplicate email, or a student
 * enrolled twice in the same course. Mapped to HTTP 409 Conflict.
 */
public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String message) {
        super(message);
    }

    public DuplicateResourceException(String resourceName, String field, Object value) {
        super(resourceName + " already exists with " + field + ": " + value);
    }
}
