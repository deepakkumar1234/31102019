package com.db.awmd.challenge.web;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import com.db.awmd.challenge.exception.NotEnoughFundsException;
import com.db.awmd.challenge.exception.TransferBetweenSameAccountException;
import com.db.awmd.challenge.service.AccountsService;

import javax.validation.Valid;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The Class AccountsController.
 */
@RestController
@RequestMapping("/v1/accounts")

/** The Constant log. */
@Slf4j
public class AccountsController {

    /** The accounts service. */
    private final AccountsService accountsService;

    /**
     * Instantiates a new accounts controller.
     *
     * @param accountsService the accounts service
     */
    @Autowired
    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    /**
     * Creates the account.
     *
     * @param account the account
     * @return the response entity
     */
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> createAccount(@RequestBody @Valid Account account) {
        log.info("Creating account {}", account);

        try {
            this.accountsService.createAccount(account);
        } catch (DuplicateAccountIdException daie) {
            return new ResponseEntity<>(daie.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Gets the account.
     *
     * @param accountId the account id
     * @return the account
     */
    @GetMapping(path = "/{accountId}")
    public Account getAccount(@PathVariable String accountId) {
        log.info("Retrieving account for id {}", accountId);
        return this.accountsService.getAccount(accountId);
    }

    /**
     * Make transfer.
     *
     * @param transfer the transfer
     * @return the response entity
     */
    @PutMapping(path = "/transfer", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> makeTransfer(@RequestBody @Valid Transfer transfer) {
        log.info("Making transfer {}", transfer);

        try {
            this.accountsService.makeTransfer(transfer);
        } catch (AccountNotFoundException ane) {
            return new ResponseEntity<>(ane.getMessage(), HttpStatus.NOT_FOUND);
        } catch (NotEnoughFundsException nbe) {
            return new ResponseEntity<>(nbe.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        } catch (TransferBetweenSameAccountException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


}
