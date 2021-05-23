package com.example.sp.maxsat.Exceptions.Classes;

import com.example.sp.maxsat.Exceptions.Messages.ExceptionMessages;

public class NoSuchZoneException extends RuntimeException{
    private static final long serialVersionUID = 2645315523262426567L;

    public NoSuchZoneException() {
        super(ExceptionMessages.NO_SUCH_ZONE.getErrorMessage());
    }
}
