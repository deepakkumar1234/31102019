package com.db.awmd.challenge;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.service.AccountsService;
import java.math.BigDecimal;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

/**
 * The Class AccountsControllerTest.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class AccountsControllerTest {

  /** The mock mvc. */
  private MockMvc mockMvc;

  /** The accounts service. */
  @Autowired
  private AccountsService accountsService;

  /** The web application context. */
  @Autowired
  private WebApplicationContext webApplicationContext;

  /**
   * Prepare mock mvc.
   */
  @Before
  public void prepareMockMvc() {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

    // Reset the existing accounts before each test.
    accountsService.getAccountsRepository().clearAccounts();
  }

  /**
   * Creates the account.
   *
   * @throws Exception the exception
   */
  @Test
  public void createAccount() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-123\",\"balance\":1000}").andExpect(status().isCreated());

    Account account = accountsService.getAccount("Id-123");
    assertThat(account.getAccountId()).isEqualTo("Id-123");
    assertThat(account.getBalance()).isEqualByComparingTo("1000");
  }

  /**
   * Creates the duplicate account.
   *
   * @throws Exception the exception
   */
  @Test
  public void createDuplicateAccount() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-123\",\"balance\":1000}").andExpect(status().isCreated());

    createAccountWithContent("{\"accountId\":\"Id-123\",\"balance\":1000}").andExpect(status().isBadRequest());
  }

  /**
   * Creates the account no account id.
   *
   * @throws Exception the exception
   */
  @Test
  public void createAccountNoAccountId() throws Exception {
    createAccountWithContent("{\"balance\":1000}")
            .andExpect(status().isBadRequest());
  }

  /**
   * Creates the account no balance.
   *
   * @throws Exception the exception
   */
  @Test
  public void createAccountNoBalance() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-123\"}").andExpect(status().isBadRequest());
  }

  /**
   * Creates the account no body.
   *
   * @throws Exception the exception
   */
  @Test
  public void createAccountNoBody() throws Exception {
    this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  /**
   * Creates the account negative balance.
   *
   * @throws Exception the exception
   */
  @Test
  public void createAccountNegativeBalance() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-123\",\"balance\":-1000}")
            .andExpect(status().isBadRequest());
  }

  /**
   * Creates the account empty account id.
   *
   * @throws Exception the exception
   */
  @Test
  public void createAccountEmptyAccountId() throws Exception {
    createAccountWithContent("{\"accountId\":\"\",\"balance\":1000}").andExpect(status().isBadRequest());
  }

  /**
   * Creates the account with content.
   *
   * @param content the content
   * @return the result actions
   * @throws Exception the exception
   */
  private ResultActions createAccountWithContent(final String content) throws Exception {
    return this.mockMvc.perform(post("/v1/accounts").contentType(MediaType.APPLICATION_JSON)
            .content(content));
  }

  /**
   * Gets the account.
   *
   * @return the account
   * @throws Exception the exception
   */
  @Test
  public void getAccount() throws Exception {
    String uniqueAccountId = "Id-" + System.currentTimeMillis();
    Account account = new Account(uniqueAccountId, new BigDecimal("123.45"));
    this.accountsService.createAccount(account);
    verifyAccountBalance(uniqueAccountId, new BigDecimal("123.45"));
  }

  /**
   * Verify account balance.
   *
   * @param accountId the account id
   * @param balance the balance
   * @throws Exception the exception
   */
  private void verifyAccountBalance(final String accountId, final BigDecimal balance) throws Exception {
    this.mockMvc.perform(get("/v1/accounts/" + accountId))
            .andExpect(status().isOk())
            .andExpect(
                    content().string("{\"accountId\":\"" + accountId + "\",\"balance\":"+balance+"}"));
  }

  /**
   * Make transfer account from not found.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferAccountFromNotFound() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-2\",\"balance\":1000}").andExpect(status().isCreated());

    makeTransferWithContent("{\"accountFromId\":\"Id-1\",\"accountToId\":\"Id-2\",\"amount\":1000}")
            .andExpect(status().isNotFound());
  }

  /**
   * Make transfer account to not found.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferAccountToNotFound() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-1\",\"balance\":1000.20}").andExpect(status().isCreated());

    makeTransferWithContent("{\"accountFromId\":\"Id-1\",\"accountToId\":\"Id-2\",\"amount\":1000}")
            .andExpect(status().isNotFound());
  }

  /**
   * Make transfer with content.
   *
   * @param content the content
   * @return the result actions
   * @throws Exception the exception
   */
  private ResultActions makeTransferWithContent(String content) throws Exception {
    return this.mockMvc.perform(
            put("/v1/accounts/transfer")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(content));
  }

  /**
   * Make transfer same account.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferSameAccount() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-1\",\"balance\":1000}").andExpect(status().isCreated());
    makeTransferWithContent("{\"accountFromId\":\"Id-1\",\"accountToId\":\"Id-1\",\"amount\":1000}")
            .andExpect(status().isBadRequest());
  }

  /**
   * Make transfer overdraft.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferOverdraft() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-1\",\"balance\":20.50}").andExpect(status().isCreated());
    createAccountWithContent("{\"accountId\":\"Id-2\",\"balance\":1000}").andExpect(status().isCreated());

    makeTransferWithContent("{\"accountFromId\":\"Id-1\",\"accountToId\":\"Id-2\",\"amount\":21}")
            .andExpect(status().isUnprocessableEntity());
  }

  /**
   * Make transfer negative amount.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferNegativeAmount() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-1\",\"balance\":100.50}").andExpect(status().isCreated());
    createAccountWithContent("{\"accountId\":\"Id-2\",\"balance\":1000.50}").andExpect(status().isCreated());

    makeTransferWithContent("{\"accountFromId\":\"Id-1\",\"accountToId\":\"Id-2\",\"amount\":-50}")
            .andExpect(status().isBadRequest());
  }

  /**
   * Make transfer empty body.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferEmptyBody() throws Exception {
    makeTransferWithContent("{}")
            .andExpect(status().isBadRequest());
  }

  /**
   * Make transfer no body.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferNoBody() throws Exception {
    this.mockMvc.perform(put("/v1/accounts/transfer").contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
  }

  /**
   * Make transfer zero balance after transfer.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferZeroBalanceAfterTransfer() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-1\",\"balance\":2000.20}").andExpect(status().isCreated());
    createAccountWithContent("{\"accountId\":\"Id-2\",\"balance\":100}").andExpect(status().isCreated());

    makeTransferWithContent("{\"accountFromId\":\"Id-1\",\"accountToId\":\"Id-2\",\"amount\":2000.20}")
            .andExpect(status().isOk());

    verifyAccountBalance("Id-1", new BigDecimal("0.00"));
    verifyAccountBalance("Id-2", new BigDecimal("2100.20"));
  }

  /**
   * Make transfer between accounts positive balance after transfer.
   *
   * @throws Exception the exception
   */
  @Test
  public void makeTransferBetweenAccountsPositiveBalanceAfterTransfer() throws Exception {
    createAccountWithContent("{\"accountId\":\"Id-1\",\"balance\":5100.21}").andExpect(status().isCreated());
    createAccountWithContent("{\"accountId\":\"Id-2\",\"balance\":6000}").andExpect(status().isCreated());

    makeTransferWithContent("{\"accountFromId\":\"Id-1\",\"accountToId\":\"Id-2\",\"amount\":5000}")
            .andExpect(status().isOk());

    verifyAccountBalance("Id-1", new BigDecimal("100.21"));
    verifyAccountBalance("Id-2", new BigDecimal("11000"));
  }










}
