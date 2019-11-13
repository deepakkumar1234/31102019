package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AccountUpdate;
import com.db.awmd.challenge.exception.DuplicateAccountIdException;

import java.math.BigDecimal;
import java.util.List;

/**
 * The Interface AccountsRepository.
 */
public interface AccountsRepository {

  /**
   * Creates the account.
   *
   * @param account the account
   * @throws DuplicateAccountIdException the duplicate account id exception
   */
  void createAccount(Account account) throws DuplicateAccountIdException;

  /**
   * Gets the account.
   *
   * @param accountId the account id
   * @return the account
   */
  Account getAccount(String accountId);

  /**
   * Clear accounts.
   */
  void clearAccounts();

  /**
   * Update accounts batch.
   *
   * @param accountUpdates the account updates
   * @return true, if successful
   */
  boolean updateAccountsBatch(List<AccountUpdate> accountUpdates);

}
