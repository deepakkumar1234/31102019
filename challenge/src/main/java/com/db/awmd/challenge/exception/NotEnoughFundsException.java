package com.db.awmd.challenge.exception;

/**
 * The Class NotEnoughFundsException.
 */
public class NotEnoughFundsException extends RuntimeException {

    /**
     * Instantiates a new not enough funds exception.
     *
     * @param message the message
     */
    public NotEnoughFundsException(String message){
        super(message);
    }
}
