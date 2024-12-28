package com.coforge.training.softbank.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.coforge.training.softbank.auth.dto.NEFTTransactionDTO;

@FeignClient(name = "transaction-service", url = "http://localhost:8087")
public interface TransactionServiceFeignClient {

	@PostMapping("/transactions/deposit")
	String performDepositTransaction(@RequestBody NEFTTransactionDTO transactionDTO);

	@PostMapping("/transactions/withdrawal")
	String performWithdrawalTransaction(@RequestBody NEFTTransactionDTO transactionDTO);
}