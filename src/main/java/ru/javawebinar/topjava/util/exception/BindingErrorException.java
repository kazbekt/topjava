package ru.javawebinar.topjava.util.exception;

public class BindingErrorException extends RuntimeException {
    private final String[] errors;

    public BindingErrorException(String[] errors) {
        super("Validation failed: " + String.join(", ", errors));
        this.errors = errors;
    }

    public String[] getErrors() {
        return errors;
    }
}