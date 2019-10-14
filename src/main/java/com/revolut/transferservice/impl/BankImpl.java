
package com.revolut.transferservice.impl;

import lombok.Getter;

import java.util.NoSuchElementException;
import java.util.Objects;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Bank;
import com.revolut.transferservice.api.Context;
import com.revolut.transferservice.api.Repository;
import com.revolut.transferservice.api.classes.TransactionPayload;
import com.revolut.transferservice.api.transactions.Transaction;
import com.revolut.transferservice.impl.utils.generators.DataGenerator;

public final class BankImpl extends Bank {

    @Getter
    private final ContextImpl context;

    private BankImpl(ContextImpl context) {
        this.context = context;
    }

    void generateData() {
        DataGenerator.getInstance(context)
                .withPartiesCount(100)
                .withAccountsPerClient(2)
                .withClientTransactions(10_000)
                .generate();
    }

    @Override
	public Context getContext() {
		return context;
	}

    @Override
	public Transaction transfer(TransactionPayload payload) {
        Objects.requireNonNull(payload, "Transaction data cannot be null");

        final Repository<Account> accountRepository = context.getAccountsRepository();
        final Account debit = accountRepository.getById(payload.getDebitAccountId());
        validateAccount(debit, payload.getDebitAccountId());
        final Account credit = accountRepository.getById(payload.getCreditAccountId());
        validateAccount(credit, payload.getCreditAccountId());

        final Transaction trn = context.getTransactionRepository().add(debit, credit, payload.getAmount());
        trn.run();
        return trn;
    }

    private void validateAccount(Account account, Long id) {
        if (account.isNotValid()) {
            throw new NoSuchElementException(String.format("Account with id %d is not found", id));
        }
    }

    private static class LazyHolder {
        static final BankImpl INSTANCE = new BankImpl(ContextImpl.create());
    }

    public static BankImpl getInstance() {
        return LazyHolder.INSTANCE;
    }
}
