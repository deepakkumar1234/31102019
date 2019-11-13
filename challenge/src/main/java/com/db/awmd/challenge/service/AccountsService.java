package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AccountUpdate;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.NotEnoughFundsException;
import com.db.awmd.challenge.exception.TransferBetweenSameAccountException;
import com.db.awmd.challenge.repository.AccountsRepository;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * The Class AccountsService.
 */
@Service
public class AccountsService {

    /**
     * Gets the accounts repository.
     *
     * @return the accounts repository
     */
    @Getter
    private final AccountsRepository accountsRepository;

    /** The notification service. */
    
    /**
     * Gets the notification service.
     *
     * @return the notification service
     */
    @Getter
    private final NotificationService notificationService;

    /** The transfer validator. */
    @Autowired
    private TransferValidator transferValidator;

    /**
     * Instantiates a new accounts service.
     *
     * @param accountsRepository the accounts repository
     * @param notificationService the notification service
     */
    @Autowired
    public AccountsService(AccountsRepository accountsRepository, NotificationService notificationService) {
        this.accountsRepository = accountsRepository;
        this.notificationService = notificationService;
    }

    /**
     * Creates the account.
     *
     * @param account the account
     */
    public void createAccount(Account account) {
        this.accountsRepository.createAccount(account);
    }

    /**
     * Gets the account.
     *
     * @param accountId the account id
     * @return the account
     */
    public Account getAccount(String accountId) {
        return this.accountsRepository.getAccount(accountId);
    }

    /**
     * Makes a transfer between two accounts for the balance specified by the {@link Transfer} object.
     *
     * @param transfer the transfer
     * @throws AccountNotFoundException When an account does not exist
     * @throws NotEnoughFundsException When there are not enough funds to complete the transfer
     * @throws TransferBetweenSameAccountException Transfer to self account is not permitted
     */
    public void makeTransfer(Transfer transfer) throws AccountNotFoundException, NotEnoughFundsException, TransferBetweenSameAccountException {

        final Account accountFrom = accountsRepository.getAccount(transfer.getAccountFromId());
        final Account accountTo = accountsRepository.getAccount(transfer.getAccountToId());
        final BigDecimal amount = transfer.getAmount();

        transferValidator.validate(accountFrom, accountTo, transfer);

        //ideally atomic operation in production
        boolean successful = accountsRepository.updateAccountsBatch(Arrays.asList(
                new AccountUpdate(accountFrom.getAccountId(), amount.negate()),
                new AccountUpdate(accountTo.getAccountId(), amount)
                ));

        if (successful){
            notificationService.notifyAboutTransfer(accountFrom, "The transfer to the account with ID " + accountTo.getAccountId() + " is now complete for the amount of " + transfer.getAmount() + ".");
            notificationService.notifyAboutTransfer(accountTo, "The account with ID + " + accountFrom.getAccountId() + " has transferred " + transfer.getAmount() + " into your account.");
        }
    }

}
