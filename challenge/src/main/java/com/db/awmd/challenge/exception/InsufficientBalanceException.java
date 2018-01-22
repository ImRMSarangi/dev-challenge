package com.db.awmd.challenge.exception;

public class InsufficientBalanceException extends RuntimeException {

	public InsufficientBalanceException(String accountId) {
		super("Account Id " + accountId + " has insufficient balance.");
	}
}
