package com.bemtivi.bemtivi.exceptions;

import com.bemtivi.bemtivi.exceptions.enums.RuntimeErrorEnum;
import lombok.Getter;

@Getter
public class InternalErrorException extends RuntimeException {
    private final RuntimeErrorEnum error;

    public InternalErrorException(RuntimeErrorEnum error) {
        super(error.getMessage());
        this.error = error;
    }
}
