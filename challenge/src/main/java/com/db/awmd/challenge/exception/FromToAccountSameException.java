package com.db.awmd.challenge.exception;

public class FromToAccountSameException extends RuntimeException {

	public FromToAccountSameException() {
		super("From and To account cannot be same.");
	}
}
