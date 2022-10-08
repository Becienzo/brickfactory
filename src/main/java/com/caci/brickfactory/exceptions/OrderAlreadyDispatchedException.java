package com.caci.brickfactory.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST) // return 400
public class OrderAlreadyDispatchedException extends RuntimeException {
    public OrderAlreadyDispatchedException(String message) {
        super(message);
    }
}
