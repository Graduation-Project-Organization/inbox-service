package com.docflow.inbox_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MessageNotFound extends RuntimeException {
    public MessageNotFound(String messageId) {
        super(String.format("Message with id %s is not found", messageId));
    }
}