package com.db.awmd.challenge.exception;

public class AccountNotFoundException extends RuntimeException {
	
	public AccountNotFoundException(String accountId) {
		super("Account id " + accountId + " doesn't exist.");
	}
}
