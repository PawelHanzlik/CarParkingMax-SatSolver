package com.example.sp.maxsat.Exceptions.Classes;

import com.example.sp.maxsat.Exceptions.Messages.ExceptionMessages;

public class NoSuchUserException extends RuntimeException{
    private static final long serialVersionUID = 2645315523262426567L;

    public NoSuchUserException() {
        super(ExceptionMessages.NO_SUCH_USER.getErrorMessage());
    }
}
