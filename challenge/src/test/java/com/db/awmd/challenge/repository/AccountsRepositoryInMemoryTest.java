package com.db.awmd.challenge.repository;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.AccountUpdate;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * The Class AccountsRepositoryInMemoryTest.
 */
public class AccountsRepositoryInMemoryTest {

    /** The accounts repository. */
    private AccountsRepository accountsRepository;
    
    /** The Constant ID1. */
    public static final String ID1="Id-1";
    
    /** The Constant ID2. */
    public static final String ID2="Id-2";

    /**
     * Sets the up.
     */
    @Before
    public void setUp(){
        accountsRepository = new AccountsRepositoryInMemory();
    }

    /**
     * Update accounts batch should update all accounts.
     *
     * @throws Exception the exception
     */
    @Test
    public void updateAccountsBatch_should_updateAllAccounts() throws Exception {

        accountsRepository.createAccount(new Account(ID1, BigDecimal.ZERO));
        accountsRepository.createAccount(new Account(ID2, new BigDecimal("150.20")));

        List<AccountUpdate> accountUpdates = Arrays.asList(
                new AccountUpdate(ID1, BigDecimal.ZERO),
                new AccountUpdate(ID2, new BigDecimal("-50"))
        );

        accountsRepository.updateAccountsBatch(accountUpdates);
        assertBalance(ID1, BigDecimal.ZERO);
        assertBalance(ID2, new BigDecimal("100.20"));
    }

    /**
     * Assert balance.
     *
     * @param accountId the account id
     * @param balance the balance
     */
    private void assertBalance(String accountId, BigDecimal balance){
        assertThat(accountsRepository.getAccount(accountId).getBalance()).isEqualTo(balance);
    }

}