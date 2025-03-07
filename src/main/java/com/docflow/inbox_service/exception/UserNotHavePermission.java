package com.docflow.inbox_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class UserNotHavePermission extends RuntimeException {
    public UserNotHavePermission(String message) {
        super(message);
    }
}
