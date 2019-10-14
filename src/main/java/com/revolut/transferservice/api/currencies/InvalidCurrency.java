

package com.revolut.transferservice.api.currencies;

import com.revolut.transferservice.api.Identifiable;

final class InvalidCurrency extends BaseCurrency {

    private InvalidCurrency() {
        super("", "");
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

        return (obj instanceof InvalidCurrency);
    }

    private static class LazyHolder {
        private static final InvalidCurrency INSTANCE = new InvalidCurrency();
    }

    static Currency getInstance() {
        return LazyHolder.INSTANCE;
    }
}
