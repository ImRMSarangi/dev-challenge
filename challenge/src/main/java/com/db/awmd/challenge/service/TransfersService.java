package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.TransferFieldsNullException;
import com.db.awmd.challenge.exception.AccountNotFoundException;
import com.db.awmd.challenge.exception.FromToAccountSameException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;

@Service
public class TransfersService {

	private final AccountsService accountsService;
	private final NotificationService notificationService;

	@Autowired
	public TransfersService(AccountsService accountsService, EmailNotificationService notificationService) {
		this.accountsService = accountsService;
		this.notificationService = notificationService;
	}

	public void transferMoney(Transfer transfer) throws RuntimeException {
		String accountFromId = transfer.getAccountFromId();
		String accountToId = transfer.getAccountToId();
		BigDecimal amount = transfer.getAmount();
		
		if(accountFromId == null || ("").equals(accountFromId)) {
			throw new TransferFieldsNullException("From Account");
		}
		
		if(accountToId == null || ("").equals(accountToId)) {
			throw new TransferFieldsNullException("To Account");
		}
		
		if(amount.compareTo(new BigDecimal(0)) == 0) {
			throw new TransferFieldsNullException("Amount");
		}
		
		if(accountFromId.equals(accountToId)) {
			throw new FromToAccountSameException();
		}
		
		Account accountFrom = accountsService.getAccount(accountFromId);
		Account accountTo = accountsService.getAccount(accountToId);
		
		if(null == accountFrom) {
			throw new AccountNotFoundException(accountFromId);
		}
		
		if(null == accountTo) {
			throw new AccountNotFoundException(accountToId);
		}
		
		BigDecimal remBalAccFrom = accountFrom.getBalance().subtract(amount);

		if (remBalAccFrom.compareTo(BigDecimal.ZERO) < 0) {
			throw new InsufficientBalanceException(accountFromId);
		} else {
			accountFrom.setBalance(remBalAccFrom);
			accountTo.setBalance(accountTo.getBalance().add(amount));
			
			this.accountsService.updateAccount(accountFrom);
			this.accountsService.updateAccount(accountTo);
			
			this.notificationService.notifyAboutTransfer(accountFrom, "An amount of " + amount
					+ " has been debited from your account for a transfer made to the account id " + accountToId + ".");
			this.notificationService.notifyAboutTransfer(accountTo, "An amount of " + amount
					+ " has been credited to your account from the account id " + accountFromId + ".");
		}
	}

}
