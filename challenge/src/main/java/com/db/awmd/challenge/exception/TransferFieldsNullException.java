package com.db.awmd.challenge.exception;

public class TransferFieldsNullException extends RuntimeException {

	public TransferFieldsNullException(String accIdentifier) {
		super(accIdentifier + " field cann't be blank.");
	}
}
