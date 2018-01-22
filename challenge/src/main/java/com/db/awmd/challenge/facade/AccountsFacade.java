package com.db.awmd.challenge.facade;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.db.awmd.challenge.service.AccountsService;

@Component
public class AccountsFacade {

	private final AccountsService accountsService;

	public AccountsFacade(AccountsService accountsService) {
		this.accountsService = accountsService;
	}

	public BigDecimal getAccountBalance(String accountId) {
		return this.accountsService.getAccountBalance(accountId);
	}

	public void addBalance(String accountToId, BigDecimal amount) {
		this.accountsService.addBalance(accountToId, amount);
	}

	public void deductBalance(String accountFromId, BigDecimal amount) {
		this.accountsService.deductBalance(accountFromId, amount);
	}

}
