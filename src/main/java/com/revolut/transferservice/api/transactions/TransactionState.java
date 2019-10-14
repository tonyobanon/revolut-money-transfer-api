

package com.revolut.transferservice.api.transactions;

public enum TransactionState {

    NEW,
    INSUFFICIENT_FUNDS,
    COMPLETED,
    CONCURRENCY_ERROR,
    RESTARTED
}
