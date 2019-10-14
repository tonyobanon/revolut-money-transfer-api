

package com.revolut.transferservice.impl;

import com.revolut.transferservice.api.Context;
import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.parties.PartyRepository;
import com.revolut.transferservice.api.transactions.TransactionRepository;
import com.revolut.transferservice.impl.accounts.DefaultAccountsRepository;
import com.revolut.transferservice.impl.parties.DefaultPartyRepository;
import com.revolut.transferservice.impl.transactions.DefaultTransactionRepository;

import lombok.Getter;

@Getter
public class ContextImpl implements Context {

    private final PartyRepository partyRepository;
    private final AccountsRepository accountsRepository;
    private final TransactionRepository transactionRepository;

    private ContextImpl(PartyRepository partyRepository,
                   AccountsRepository accountsRepository,
                   TransactionRepository transactionRepository) {
        this.partyRepository = partyRepository;
        this.accountsRepository = accountsRepository;
        this.transactionRepository = transactionRepository;
    }
    
    @Override
	public PartyRepository getPartyRepository() {
		return partyRepository;
	}

	@Override
	public AccountsRepository getAccountsRepository() {
		return accountsRepository;
	}

	@Override
	public TransactionRepository getTransactionRepository() {
		return transactionRepository;
	}

	public static ContextImpl create() {
        final PartyRepository partyRepository = new DefaultPartyRepository();
        final AccountsRepository accountsRepository = new DefaultAccountsRepository(partyRepository);
        final TransactionRepository transactionRepository = new DefaultTransactionRepository();
        return new ContextImpl(partyRepository, accountsRepository, transactionRepository);
    }
}
