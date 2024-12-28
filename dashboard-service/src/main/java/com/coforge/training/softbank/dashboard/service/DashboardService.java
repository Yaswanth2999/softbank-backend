package com.coforge.training.softbank.dashboard.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.coforge.training.softbank.dashboard.dto.TransactionSummaryDTO;
import com.coforge.training.softbank.dashboard.dto.UserDetailsDTO;
import com.coforge.training.softbank.dashboard.feign.AuthenticationFeignClient;
import com.coforge.training.softbank.dashboard.feign.TransactionFeignClient;


/**
* User   : Singuluri.Kumar
* Date   : 16-Dec-2024
* Time   : 10:09:39 pm
* Project:dashboard-service
**/

@Service
public class DashboardService {
 
	private final TransactionFeignClient transactionFeignClient;
	private final AuthenticationFeignClient authenticationFeignClient;
 
	public DashboardService(TransactionFeignClient transactionFeignClient,AuthenticationFeignClient authenticationFeignClient) {
		this.transactionFeignClient = transactionFeignClient;
		this.authenticationFeignClient=authenticationFeignClient;
 
	}
 
	public List<TransactionSummaryDTO> getTransactionHistoryCustom(String accountNo, String upiId, String startDate, String endDate) {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	    LocalDate start = LocalDate.parse(startDate, formatter);
	    LocalDate end = LocalDate.parse(endDate, formatter);

	    List<TransactionSummaryDTO> neftTransactions = transactionFeignClient.getNeftTransactionHistory(accountNo, startDate, endDate);
	    List<TransactionSummaryDTO> upiTransactions = transactionFeignClient.getUpiTransactionHistory(upiId, startDate, endDate);

	    // Filter transactions by date
	    neftTransactions = neftTransactions.stream()
	        .filter(transaction -> !transaction.getTransactionDate().toLocalDate().isBefore(start) && !transaction.getTransactionDate().toLocalDate().isAfter(end))
	        .collect(Collectors.toList());

	    upiTransactions = upiTransactions.stream()
	        .filter(transaction -> !transaction.getTransactionDate().toLocalDate().isBefore(start) && !transaction.getTransactionDate().toLocalDate().isAfter(end))
	        .collect(Collectors.toList());

	    neftTransactions.addAll(upiTransactions);
	    neftTransactions.sort(Comparator.comparing(TransactionSummaryDTO::getTransactionDate));

	    return neftTransactions;
	}
 
	public List<TransactionSummaryDTO> getTransactionHistory(String accountNo, String upiId, String period) {
		LocalDate endDate = LocalDate.now();
		LocalDate startDate;
 
		switch (period.toLowerCase()) {
		case "10days":
			startDate = endDate.minusDays(10);
			break;
		case "1month":
			startDate = endDate.minusMonths(1);
			break;
		case "3months":
			startDate = endDate.minusMonths(3);
			break;
		case "6months":
			startDate = endDate.minusMonths(6);
			break;
		case "1year":
			startDate = endDate.minusYears(1);
			break;
		default:
			throw new IllegalArgumentException("Invalid period specified.");
		}
 
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String formattedStartDate = startDate.format(formatter);
		String formattedEndDate = endDate.format(formatter);
 
		List<TransactionSummaryDTO> neftTransactions = transactionFeignClient.getNeftTransactionHistory(accountNo, formattedStartDate, formattedEndDate);
		List<TransactionSummaryDTO> upiTransactions = transactionFeignClient.getUpiTransactionHistory(upiId, formattedStartDate, formattedEndDate);
 
		neftTransactions.addAll(upiTransactions);
		neftTransactions.sort((t1, t2) -> t1.getTransactionDate().compareTo(t2.getTransactionDate()));
 
		return neftTransactions;
	}
 
	public UserDetailsDTO getUserDetails(String username) {
	    return authenticationFeignClient.getUserDetailsByUsername(username);
	}
	}