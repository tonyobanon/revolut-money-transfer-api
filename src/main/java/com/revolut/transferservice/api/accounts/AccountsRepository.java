

package com.revolut.transferservice.api.accounts;

import java.math.BigDecimal;
import java.util.Collection;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Repository;
import com.revolut.transferservice.api.currencies.Currency;
import com.revolut.transferservice.api.parties.Party;

public interface AccountsRepository extends Repository<Account> {
	
	Account getOurBankMainAccount();

    Account addActiveAccount(Currency currency, String number, Party holder, BigDecimal balance);
    
    Account addPassiveAccount(Currency currency, String number, Party holder);

    Collection<Account> getByHolder(Party holder);
}
