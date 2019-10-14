

package com.revolut.transferservice.impl.transactions;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.classes.PagedResult;
import com.revolut.transferservice.api.transactions.Transaction;
import com.revolut.transferservice.api.transactions.TransactionRepository;
import com.revolut.transferservice.impl.classes.PagedResultImpl;

public final class DefaultTransactionRepository implements TransactionRepository {

    private final AtomicLong counter = new AtomicLong(0L);
    private final ConcurrentMap<Long, Transaction> transactions = new ConcurrentHashMap<>();

    @Override
    public Transaction getById(Long id) {
        return transactions.getOrDefault(id, null);
    }

    @Override
    public Transaction getInvalid() {
        return MoneyTransaction.getInvalid();
    }

    @Override
    public int size() {
        return transactions.size();
    }

    @Override
    public Transaction add(Account debit, Account credit, BigDecimal amount) {
        final Transaction transaction = MoneyTransaction.make(counter.incrementAndGet(), debit, credit, amount);
        transactions.putIfAbsent(transaction.getId(), transaction);
        return transaction;
    }

    @Override
    public PagedResult<Transaction> getAll(int pageNumber, int recordsPerPage) {
        return PagedResultImpl.from(pageNumber, recordsPerPage, transactions);
    }

    @Override
    public PagedResult<Transaction> getByAccount(Account account, int pageNumber, int recordsPerPage) {
        Objects.requireNonNull(account, "Account cannot be null");
        Predicate<Transaction> predicate = t -> t.getDebit().equals(account) || t.getCredit().equals(account);
        return PagedResultImpl.from(pageNumber, recordsPerPage, transactions, predicate);
    }
}
