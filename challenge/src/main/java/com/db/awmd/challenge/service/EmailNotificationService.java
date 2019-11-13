package com.db.awmd.challenge.service;

import com.db.awmd.challenge.domain.Account;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

/** The Constant log. */
@Slf4j
@Service
public class EmailNotificationService implements NotificationService {

  /* (non-Javadoc)
   * @see com.db.awmd.challenge.service.NotificationService#notifyAboutTransfer(com.db.awmd.challenge.domain.Account, java.lang.String)
   */
  @Override
  public void notifyAboutTransfer(Account account, String transferDescription) {
    //THIS METHOD USED TO NOTIFY VIA EMAIL OR SMS
    log
      .info("Sending notification to owner of {}: {}", account.getAccountId(), transferDescription);
  }

}
