package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;

/**
 * The Interface NotificationService.
 */
public interface NotificationService {

  /**
   * Notify about transfer.
   *
   * @param account the account
   * @param transferDescription the transfer description
   */
  void notifyAboutTransfer(Account account, String transferDescription);
}
