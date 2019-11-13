package com.db.awmd.challenge.exception;

/**
 * The Class AccountNotFoundException.
 */
public class AccountNotFoundException extends RuntimeException {

    /**
     * Instantiates a new account not found exception.
     *
     * @param message the message
     */
    public AccountNotFoundException(String message) {
        super(message);
    }

}
