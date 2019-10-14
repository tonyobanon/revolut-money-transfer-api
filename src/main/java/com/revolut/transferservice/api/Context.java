package com.revolut.transferservice.api;

import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.parties.PartyRepository;
import com.revolut.transferservice.api.transactions.TransactionRepository;

public interface Context {

	PartyRepository getPartyRepository();

	AccountsRepository getAccountsRepository();

	TransactionRepository getTransactionRepository();

}