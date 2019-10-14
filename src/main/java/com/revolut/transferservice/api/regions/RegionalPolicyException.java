package com.revolut.transferservice.api.regions;

public class RegionalPolicyException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final Long accountId;
	
	public RegionalPolicyException(Long accountId, String message) {
		super(message);
		this.accountId = accountId;
	}
	
	public Long getAccountId() {
		return accountId;
	}
}
