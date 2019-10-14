

package com.revolut.transferservice.impl.accounts;

import java.math.BigDecimal;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Identifiable;
import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.parties.AbstractParty;

final class InvalidAccount extends AbstractAccount {

    private InvalidAccount() {
        super(Identifiable.INVALID_ID, BaseCurrency.getInvalid(), "", AbstractParty.getInvalid(), false, BigDecimal.ZERO);
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

        return (obj instanceof InvalidAccount);
    }

    @Override
    public String toString() {
        final String base = super.toString();
        return base.replace("Account{", "InvalidAccount{");
    }

    private static class LazyHolder {
        private static final InvalidAccount INSTANCE = new InvalidAccount();
    }

    static Account getInstance() {
        return LazyHolder.INSTANCE;
    }
}
