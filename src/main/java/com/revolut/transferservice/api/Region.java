package com.revolut.transferservice.api;

import java.math.BigDecimal;

import com.revolut.transferservice.api.parties.Party;

public interface Region {

	void enforceRegionalPolicies(Party party, Account account);
	
	BigDecimal minimimStartingBalance();
	
	Boolean isValidTaxIdentificationId(String taxIdentification);
	
	Boolean allowsPrivateAccounts();

	Boolean allowsLegalAccounts();
	
}
