

package com.revolut.transferservice.api.transactions;

import java.math.BigDecimal;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Identifiable;

public interface Transaction extends Identifiable {

    Account getDebit();

    Account getCredit();

    BigDecimal getAmount();

    TransactionState getState();

    boolean run();
}
