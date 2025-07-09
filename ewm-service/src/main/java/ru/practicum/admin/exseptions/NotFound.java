package ru.practicum.admin.exseptions;

public class NotFound extends RuntimeException {
    public NotFound(String message) {
        super(message);
    }
}
