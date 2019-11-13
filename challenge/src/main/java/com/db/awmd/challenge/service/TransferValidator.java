package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.NotEnoughFundsException;

/**
 * The Interface TransferValidator.
 */
interface TransferValidator {

    /**
     * Validate.
     *
     * @param accountFrom the account from
     * @param accountTo the account to
     * @param transfer the transfer
     * @throws AccountNotFoundException the account not found exception
     * @throws NotEnoughFundsException the not enough funds exception
     */
    void validate(final Account accountFrom, final Account accountTo, final Transfer transfer) throws AccountNotFoundException, NotEnoughFundsException;

}
