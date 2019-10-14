package com.revolut.transferservice.api.classes;

import java.math.BigDecimal;

public class AccountPayload {

	private final Long partyId;
	private final String number; 
	private final String currency;
	private final BigDecimal balance;
	private final boolean active;
	
	public AccountPayload(Long partyId, String number, String currency, BigDecimal balance, boolean active) {
		super();
		this.partyId = partyId;
		this.number = number;
		this.currency = currency;
		this.balance = balance;
		this.active = active;
	}

	public Long getPartyId() {
		return partyId;
	}
	
	public String getNumber() {
		return number;
	}

	public String getCurrency() {
		return currency;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public boolean isActive() {
		return active;
	}
}
