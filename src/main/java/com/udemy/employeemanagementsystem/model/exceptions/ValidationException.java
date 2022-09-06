package com.udemy.employeemanagementsystem.model.exceptions;

import java.util.HashMap;
import java.util.Map;

public class ValidationException extends RuntimeException {

    Map<String, String> errors = new HashMap<>();
    // one form can have multiple errors

    public ValidationException(String msg) {
        super(msg);
    }

    public Map<String, String> getErrors() {
        return errors;
    }

    public void addError(String field, String errorMsg) {
        errors.put(field, errorMsg);
    }
}
