package com.coforge.training.softbank.auth.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NEFTTransactionDTO {

	private String fromAccount;
    private String toAccount;
    private BigDecimal amount;
    private String transactionType;
    private String remarks;
    private LocalDateTime transactionDate;
    private BigDecimal fromAccountBalanceAfterTransaction;
    private BigDecimal toAccountBalanceAfterTransaction;
}
