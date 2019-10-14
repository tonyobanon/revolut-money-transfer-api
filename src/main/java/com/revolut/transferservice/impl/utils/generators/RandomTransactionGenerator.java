

package com.revolut.transferservice.impl.utils.generators;

import org.apache.commons.lang3.tuple.Pair;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.transactions.Transaction;
import com.revolut.transferservice.impl.ContextImpl;
import com.revolut.transferservice.impl.utils.TransactionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class RandomTransactionGenerator extends AbstractGenerator {

    private final int trnCount;
    private final List<Long> accountIds;
    private final boolean runImmediately;

    RandomTransactionGenerator(final ContextImpl context, final List<Long> accountIds,
                               final boolean runImmediately, final int threadPoolSize, int trnCount) {
        super(context, "clients transactions", threadPoolSize);
        Objects.requireNonNull(accountIds, "Ids list cannot be null");
        // TODO validate trnCount
        this.accountIds = accountIds;
        this.runImmediately = runImmediately;
        this.trnCount = trnCount;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(trnCount);
        for (int i = 0; i < trnCount; ++i) {
            futures.add(threadPool.submit(this::generateTransaction));
        }
        return futures;
    }

    private void generateTransaction() {
        final AccountsRepository accountsRepository = context.getAccountsRepository();
        final Pair<Long, Long> randomIds = TransactionUtils.getRandomAccountIds(accountIds);
        final Account debit = accountsRepository.getById(randomIds.getLeft());
        if (debit.isValid()) {
            final Account credit = accountsRepository.getById(randomIds.getRight());
            if (credit.isValid()) {
                // TODO move to params
                final BigDecimal amount = TransactionUtils.generateAmount(5_000, 100_000);
                final Transaction transaction = context.getTransactionRepository().add(debit, credit, amount);
                if (runImmediately) {
                    transaction.run();
                }
                ids.add(transaction.getId());
            } else {
                logger.error("Credit account with id = {} not found", randomIds.getRight());
            }
        } else {
            logger.error("Debit account with id = {} not found", randomIds.getLeft());
        }
    }
}
