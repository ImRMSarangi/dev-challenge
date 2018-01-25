package com.db.awmd.challenge.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.db.awmd.challenge.domain.Account;
import com.db.awmd.challenge.domain.Transfer;
import com.db.awmd.challenge.exception.TransferFieldsNullException;
import com.db.awmd.challenge.exception.FromToAccountSameException;
import com.db.awmd.challenge.exception.InsufficientBalanceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TransfersServiceTest {

	
	@Autowired
	private AccountsService accountsService;
	
	@Autowired
	private TransfersService transfersService;
	
	@Autowired
	private NotificationService notificationService;
	
	@Before
	public void createAccounts() {
		accountsService.getAccountsRepository().clearAccounts();
		
		Account accountFrom = new Account("Id-123", new BigDecimal(1000));
		Account accountTo = new Account("Id-124", new BigDecimal(2000));
		
		accountsService.createAccount(accountFrom);
		accountsService.createAccount(accountTo);

	}
	
	@Test
	public void transferMoney() {
		Transfer transfer = new Transfer("Id-123", "Id-124", new BigDecimal(500));
		
		this.transfersService.transferMoney(transfer);
		
		Account accountFrom = this.accountsService.getAccount("Id-123");
		Account accountTo = this.accountsService.getAccount("Id-124");
		
		assertThat(accountFrom.getBalance()).isEqualByComparingTo(new BigDecimal(500));
		assertThat(accountTo.getBalance()).isEqualByComparingTo(new BigDecimal(2500));
	}
	
	@Test
	public void transferMoneyNoAccountFromId() {
		Transfer transfer = new Transfer();
		transfer.setAccountToId("Id-124");
		transfer.setAmount(new BigDecimal(500));
		
		try {
			this.transfersService.transferMoney(transfer);
		} catch(TransferFieldsNullException aine) {
			assertThat(aine.getMessage()).isEqualTo("From Account field cann't be blank.");
		}
		
	}
	
	@Test
	public void transferMoneyNoAccountToId() {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId("Id-123");
		transfer.setAmount(new BigDecimal(500));
		
		try {
			this.transfersService.transferMoney(transfer);
		} catch(TransferFieldsNullException aine) {
			assertThat(aine.getMessage()).isEqualTo("To Account field cann't be blank.");
		}
		
	}
	
	@Test
	public void transferMoneyNoAmount() {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId("Id-123");
		transfer.setAccountToId("Id-124");
		
		try {
			this.transfersService.transferMoney(transfer);
		} catch(TransferFieldsNullException aine) {
			assertThat(aine.getMessage()).isEqualTo("Amount field cann't be blank.");
		}
		
	}
	
	@Test
	public void transferMoneySameFromToAccountId() {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId("Id-123");
		transfer.setAccountToId("Id-123");
		transfer.setAmount(new BigDecimal(500));
		
		try {
			this.transfersService.transferMoney(transfer);
		} catch(FromToAccountSameException ftase) {
			assertThat(ftase.getMessage()).isEqualTo("From and To account cannot be same.");
		}
		
	}
	
	@Test
	public void transferMoneyInsufficientBalance() {
		Transfer transfer = new Transfer();
		transfer.setAccountFromId("Id-123");
		transfer.setAccountToId("Id-124");
		transfer.setAmount(new BigDecimal(5000));
		
		try {
			this.transfersService.transferMoney(transfer);
		} catch(InsufficientBalanceException ibe) {
			assertThat(ibe.getMessage()).isEqualTo("Account Id Id-123 has insufficient balance.");
		}
		
	}
}
