

package com.revolut.transferservice.api;

import java.math.BigDecimal;
import java.util.concurrent.locks.Lock;

import com.revolut.transferservice.api.currencies.Currency;
import com.revolut.transferservice.api.parties.Party;

public interface Account extends Identifiable {

    String getNumber();

    Currency getCurrency();

    BigDecimal getBalance();

    boolean debit(BigDecimal amount);

    boolean credit(BigDecimal amount);

    Party getHolder();

    boolean isActive();

    Lock writeLock();
}
