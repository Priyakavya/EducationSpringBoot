package com.education.api.exception;

/**
 * Thrown when the request is well-formed but breaks a domain rule - e.g.
 * deleting a teacher who still owns courses, or grading an exam that belongs
 * to a different course than the enrolment. Mapped to HTTP 409 Conflict.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
