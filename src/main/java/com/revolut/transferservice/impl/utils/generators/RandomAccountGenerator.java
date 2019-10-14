

package com.revolut.transferservice.impl.utils.generators;

import org.apache.commons.lang3.StringUtils;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.parties.Party;
import com.revolut.transferservice.impl.ContextImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

class RandomAccountGenerator extends AbstractGenerator {

    private final int accountsPerClient;
    private final List<Long> partyIds;

    RandomAccountGenerator(final ContextImpl context, final List<Long> partyIds, final int accountsPerClient) {
        super(context, "accounts");
        Objects.requireNonNull(partyIds, "Ids list cannot be null");
        // TODO validate accountsPerClient
        this.partyIds = partyIds;
        this.accountsPerClient = accountsPerClient;
    }

    @Override
    List<Future<?>> doGenerate(final ExecutorService threadPool) {
        final List<Future<?>> futures = new ArrayList<>(partyIds.size() * accountsPerClient);
        for (Long partyId : partyIds) {
            Runnable runnableTask = () -> this.generateAccount(partyId);
            futures.add(threadPool.submit(runnableTask));
        }
        return futures;
    }

    private void generateAccount(Long partyId) {
        final AccountsRepository accountsRepository = context.getAccountsRepository();
        final Party pt = context.getPartyRepository().getById(partyId);
        if (pt.isValid()) {
            for (int i = 0; i < accountsPerClient; ++i) {
                final int idx = counter.incrementAndGet();
                final Account a = accountsRepository.addPassiveAccount(BaseCurrency.getDefault(), generateNumber(idx), pt);
                ids.add(a.getId());
            }
        } else {
            logger.error("Party with id = {} not found", partyId);
        }
    }

    private static String generateNumber(final int idx) {
        return "4080281010" + StringUtils.leftPad(String.valueOf(idx), 10, '0');
    }
}
