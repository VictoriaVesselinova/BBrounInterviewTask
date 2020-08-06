package com.bbraun.accountmanager.exception;

public enum AccountErrors {

	EMPTY_ID("Id is null"), ACCOUNT_NOT_EXIST("Account with id = %d does not exist");

	public final String value;

	private AccountErrors(String value) {
		this.value = value;
	}

}
