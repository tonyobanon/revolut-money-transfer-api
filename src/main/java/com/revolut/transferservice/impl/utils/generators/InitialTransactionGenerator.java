

package com.revolut.transferservice.impl.utils.generators;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.transactions.Transaction;
import com.revolut.transferservice.impl.ContextImpl;
import com.revolut.transferservice.impl.utils.TransactionUtils;

class InitialTransactionGenerator extends AbstractGenerator {

    private final List<Long> accountIds;
    private final boolean runImmediately;

    InitialTransactionGenerator(ContextImpl context, List<Long> accountIds, boolean runImmediately) {
        super(context, "initial transactions");
        Objects.requireNonNull(accountIds, "Ids list cannot be null");
        this.accountIds = accountIds;
        this.runImmediately = runImmediately;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(accountIds.size());
        for (Long accountId : accountIds) {
            Runnable runnableTask = () -> this.generateInitialTransaction(accountId);
            futures.add(threadPool.submit(runnableTask));
        }
        return futures;
    }

    private void generateInitialTransaction(final Long creditAccountId) {
        final AccountsRepository accountsRepository = context.getAccountsRepository();
        final Account debit = accountsRepository.getOurBankMainAccount();
        final Account credit = accountsRepository.getById(creditAccountId);
        if (credit.isValid()) {
            // TODO move to params
            final BigDecimal amount = TransactionUtils.generateAmount(500_000, 1000_000);
            final Transaction transaction = context.getTransactionRepository().add(debit, credit, amount);
            if (runImmediately) {
                transaction.run();
            }
            ids.add(transaction.getId());
        } else {
            logger.error("Credit account with id = {} not found", creditAccountId);
        }
    }
}
