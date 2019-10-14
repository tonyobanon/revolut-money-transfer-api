package com.revolut.transferservice.api.regions;

import java.math.BigDecimal;

public class USRegion extends AbstractRegion {

	public static final String ISO_CODE = "USA";
	
	@Override
	public BigDecimal minimimStartingBalance() {
		return BigDecimal.ZERO;
	}

	@Override
	public Boolean isValidTaxIdentificationId(String taxIdentification) {
		return true;
	}

	@Override
	public Boolean allowsPrivateAccounts() {
		return true;
	}

	@Override
	public Boolean allowsLegalAccounts() {
		return true;
	}
}
