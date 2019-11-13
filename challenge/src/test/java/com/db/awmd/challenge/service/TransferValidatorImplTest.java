package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.NotEnoughFundsException;
import com.db.awmd.challenge.exception.TransferBetweenSameAccountException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;


/**
 * The Class TransferValidatorImplTest.
 */
public class TransferValidatorImplTest {

    /** The transfer validator. */
    private TransferValidator transferValidator;
    
    /** The Constant ID1. */
    public static final String ID1="Id-1";
    
    /** The Constant ID2. */
    public static final String ID2="Id-2";

    /**
     * Sets the up.
     */
    @Before
    public void setUp() {
        transferValidator = new TransferValidatorImpl();
    }

    /**
     * Validate should throw exception when account from not found.
     *
     * @throws Exception the exception
     */
    @Test
    public void validate_should_throwException_when_accountFromNotFound() throws Exception {
        final Account accountTo = new Account(ID2);
        final Transfer transfer = new Transfer(ID1, accountTo.getAccountId(), new BigDecimal("2.00"));

        try {
            transferValidator.validate(null, new Account(ID2), transfer);
            fail("Account with Id-1 should not be found");
        } catch (AccountNotFoundException ace) {
            assertThat(ace.getMessage()).isEqualTo("Account Id-1 not found.");
        }
    }

    /**
     * Validate should throw exception when account to not found.
     *
     * @throws Exception the exception
     */
    @Test
    public void validate_should_throwException_when_accountToNotFound() throws Exception {
        final Account accountFrom = new Account(ID1);
        final Transfer transfer = new Transfer(ID1, "Id-5342", new BigDecimal("2.00"));

        try {
            transferValidator.validate(accountFrom, null, transfer);
            fail("Account with Id-5342 should not be found");
        } catch (AccountNotFoundException ace) {
            assertThat(ace.getMessage()).isEqualTo("Account Id-5342 not found.");
        }
    }

    /**
     * Validate should throw exception when not enough funds.
     *
     * @throws Exception the exception
     */
    @Test
    public void validate_should_throwException_when_NotEnoughFunds() throws Exception {
        final Account accountFrom = new Account(ID1);
        final Account accountTo = new Account(ID2);
        final Transfer transfer = new Transfer(ID1, ID2, new BigDecimal("2.00"));

        try {
            transferValidator.validate(accountFrom, accountTo, transfer);
            fail("Not enough funds");
        } catch (NotEnoughFundsException nbe) {
            assertThat(nbe.getMessage()).isEqualTo("Not enough funds on account Id-1 balance=0");
        }
    }

    /**
     * Validate should throw exception when transfer between same account.
     *
     * @throws Exception the exception
     */
    @Test
    public void validate_should_throwException_when_transferBetweenSameAccount() throws Exception {
        final Account accountFrom = new Account(ID1, new BigDecimal("20.00"));
        final Account accountTo = new Account(ID1);
        final Transfer transfer = new Transfer(ID1, ID1, new BigDecimal("2.00"));

        try {
            transferValidator.validate(accountFrom, accountTo, transfer);
            fail("Same account transfer");
        } catch (TransferBetweenSameAccountException sae) {
            assertThat(sae.getMessage()).isEqualTo("Transfer to self not permitted.");
        }
    }

    /**
     * Validate should allow valid transfer between accounts.
     *
     * @throws Exception the exception
     */
    @Test
    public void validate_should_allowValidTransferBetweenAccounts() throws Exception {
        final Account accountFrom = new Account(ID1, new BigDecimal("20.00"));
        final Account accountTo = new Account(ID2);
        final Transfer transfer = new Transfer(ID1, ID2, new BigDecimal("2.00"));

        transferValidator.validate(accountFrom, accountTo, transfer);
    }

}