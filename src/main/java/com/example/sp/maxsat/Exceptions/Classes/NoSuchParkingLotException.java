package com.example.sp.maxsat.Exceptions.Classes;

import com.example.sp.maxsat.Exceptions.Messages.ExceptionMessages;

public class NoSuchParkingLotException extends RuntimeException{
    private static final long serialVersionUID = 2645315523262426567L;

    public NoSuchParkingLotException() {
        super(ExceptionMessages.NO_SUCH_PARKING_LOT.getErrorMessage());
    }
}
