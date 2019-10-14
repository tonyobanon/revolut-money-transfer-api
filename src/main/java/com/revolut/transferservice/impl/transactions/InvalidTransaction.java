

package com.revolut.transferservice.impl.transactions;

import java.math.BigDecimal;

import com.revolut.transferservice.api.Identifiable;
import com.revolut.transferservice.impl.accounts.AccountImpl;

final class InvalidTransaction extends MoneyTransaction {

    private InvalidTransaction() {
        super(Identifiable.INVALID_ID, AccountImpl.getInvalid(), AccountImpl.getInvalid(), BigDecimal.ZERO);
    }

    @Override
    public int hashCode() {
        return (int) Identifiable.INVALID_ID;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        return (obj instanceof InvalidTransaction);
    }

    @Override
    public final boolean run() {
        return false;
    }

    private static class LazyHolder {
        private static final InvalidTransaction INSTANCE = new InvalidTransaction();
    }

    static InvalidTransaction getInstance() {
        return LazyHolder.INSTANCE;
    }
}
