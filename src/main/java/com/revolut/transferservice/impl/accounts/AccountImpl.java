

package com.revolut.transferservice.impl.accounts;

import java.math.BigDecimal;

import com.revolut.transferservice.api.currencies.Currency;
import com.revolut.transferservice.api.parties.Party;

public final class AccountImpl extends AbstractAccount {

    AccountImpl(Long id, Currency currency,
                           String number, Party holder, boolean active, BigDecimal balance) {
        super(id, currency, number, holder, active, balance);
        validateNumber(number);
    }

    AccountImpl(Long id, Currency currency,
                           String number, Party holder, boolean active) {
        this(id, currency, number, holder, active, BigDecimal.ZERO);
    }

    private static void validateNumber(String number) {
        if (number.length() != 20) {
            throw new IllegalArgumentException("Account number must contain 20 characters");
        }
    }
}
