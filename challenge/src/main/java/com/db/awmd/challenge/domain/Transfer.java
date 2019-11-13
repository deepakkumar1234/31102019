package com.db.awmd.challenge.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Data
public class Transfer {

    /** The account from id. */
    @NotNull
    @NotEmpty
    private String accountFromId;

    /** The account to id. */
    @NotNull
    @NotEmpty
    private String accountToId;

    /** The amount. */
    @NotNull
    @Min(value = 1, message = "Transfer amount must be positive.")
    private BigDecimal amount;

    /**
     * Instantiates a new transfer.
     *
     * @param accountFromId the account from id
     * @param accountToId the account to id
     * @param amount the amount
     */
    @JsonCreator
    public Transfer(@JsonProperty("accountFromId") String accountFromId,
                    @JsonProperty("accountToId") String accountToId,
                    @JsonProperty("amount") BigDecimal amount){
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
    }

}
