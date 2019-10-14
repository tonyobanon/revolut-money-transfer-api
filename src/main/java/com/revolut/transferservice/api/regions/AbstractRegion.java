package com.revolut.transferservice.api.regions;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Region;
import com.revolut.transferservice.api.parties.Party;

public abstract class AbstractRegion implements Region {

	@Override
	public final void enforceRegionalPolicies(Party party, Account account) {

		// Ensure that the account has the required minimum starting balance

		if (minimimStartingBalance().compareTo(account.getBalance()) == 1) {
			throw new RegionalPolicyException(account.getId(),
					"The minimum balance should be at least " + minimimStartingBalance());
		}

		// Ensure that the tax id is valid

		if (!isValidTaxIdentificationId(party.getTaxIdentificationNumber())) {
			throw new RegionalPolicyException(account.getId(),
					"The Tax ID is invalid: " + party.getTaxIdentificationNumber());
		}

		if ((!allowsPrivateAccounts()) && party.isPrivatePerson()) {
			throw new RegionalPolicyException(account.getId(), "PrivateAccounts are not allowed at this time");
		}

		if ((!allowsLegalAccounts()) && party.isLegalPerson()) {
			throw new RegionalPolicyException(account.getId(), "LegalAccounts are not allowed at this time");
		}
	}

}
