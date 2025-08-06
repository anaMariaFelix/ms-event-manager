package com.anamariafelix.ms_event_manager.exception;

public class EmailUniqueViolationException extends RuntimeException {

    public EmailUniqueViolationException(String msg) {
        super(msg);
    }
}
