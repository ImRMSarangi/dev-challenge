package com.db.awmd.challenge.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.InsufficientBalanceException;
import com.db.awmd.challenge.facade.AccountsFacade;

@Service
public class TransfersService {

	private final AccountsFacade accountsFacade;

	@Autowired
	public TransfersService(AccountsFacade accountsFacade) {
		this.accountsFacade = accountsFacade;
	}

	public void transferMoney(Transfer transfer) throws InsufficientBalanceException {
		String accountFromId = transfer.getAccountFromId();
		String accountToId = transfer.getAccountToId();
		BigDecimal amount = transfer.getAmount();
		BigDecimal remainingBalance = this.accountsFacade.getAccountBalance(accountFromId)
				.subtract(amount);
		if(remainingBalance.compareTo(BigDecimal.ZERO) < 0) {
			throw new InsufficientBalanceException(accountFromId);
		} else {
			this.accountsFacade.deductBalance(accountFromId, transfer.getAmount());
			this.accountsFacade.addBalance(accountToId, transfer.getAmount());
		}
	}

}
