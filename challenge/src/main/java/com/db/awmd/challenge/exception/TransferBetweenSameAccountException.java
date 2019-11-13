package com.db.awmd.challenge.exception;

/**
 * The Class TransferBetweenSameAccountException.
 */
public class TransferBetweenSameAccountException extends RuntimeException {

    /**
     * Instantiates a new transfer between same account exception.
     *
     * @param message the message
     */
    public TransferBetweenSameAccountException(String message){
        super(message);
    }

}
