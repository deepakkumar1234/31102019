package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AccountUpdate;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

/**
 * The Class AccountsRepositoryInMemory.
 */
@Repository
public class AccountsRepositoryInMemory implements AccountsRepository {

    /** The accounts. */
    private final Map<String, Account> accounts = new ConcurrentHashMap<>();

    /* (non-Javadoc)
     * @see com.db.awmd.challenge.repository.AccountsRepository#createAccount(com.db.awmd.challenge.domain.Account)
     */
    @Override
    public void createAccount(Account account) throws DuplicateAccountIdException {
        Account previousAccount = accounts.putIfAbsent(account.getAccountId(), account);
        if (previousAccount != null) {
            throw new DuplicateAccountIdException(
                    "Account id " + account.getAccountId() + " already exists!");
        }
    }

    /* (non-Javadoc)
     * @see com.db.awmd.challenge.repository.AccountsRepository#getAccount(java.lang.String)
     */
    @Override
    public Account getAccount(String accountId) {
        return accounts.get(accountId);
    }

    /* (non-Javadoc)
     * @see com.db.awmd.challenge.repository.AccountsRepository#clearAccounts()
     */
    @Override
    public void clearAccounts() {
        accounts.clear();
    }

    /* (non-Javadoc)
     * @see com.db.awmd.challenge.repository.AccountsRepository#updateAccountsBatch(java.util.List)
     */
    @Override
    public boolean updateAccountsBatch(List<AccountUpdate> accountUpdates) {
        accountUpdates
                .stream()
                .forEach(this::updateAccount);

        return true;
    }

    /**
     * Update account.
     *
     * @param accountUpdate the account update
     */
    private void updateAccount(final AccountUpdate accountUpdate) {
        final String accountId = accountUpdate.getAccountId();
        accounts.computeIfPresent(accountId, (key, account) -> {
            account.setBalance(account.getBalance().add(accountUpdate.getAmount()));
            return account;
        });
    }

}
