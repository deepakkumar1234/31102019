package com.db.awmd.challenge.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Instantiates a new account update.
 *
 * @param accountId the account id
 * @param amount the amount
 */
@Data
public class AccountUpdate {

    /** The account id. */
    private final String accountId;
    
    /** The amount. */
    private final BigDecimal amount;

}
