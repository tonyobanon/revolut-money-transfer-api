

package com.revolut.transferservice.impl.transactions;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.impl.ContextImpl;
import com.revolut.transferservice.impl.transactions.MoneyTransaction;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyTransactionTest {

    @Test
    void constructorWithNulls() {
        final AccountsRepository repository = ContextImpl.create().getAccountsRepository();

        NullPointerException e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(null, null, null, null));
        assertEquals("Id cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, null, null, null));
        assertEquals("Debit account cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), null, null));
        assertEquals("Credit account cannot be null", e.getLocalizedMessage());

        e = assertThrows(NullPointerException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), null));
        assertEquals("Amount cannot be null", e.getLocalizedMessage());
    }

    @Test
    void constructorWithInvalidValues() {
        final AccountsRepository repository = ContextImpl.create().getAccountsRepository();

        IllegalArgumentException e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), BigDecimal.valueOf(-1)));
        assertEquals("Amount must be greater than zero", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), BigDecimal.valueOf(0)));
        assertEquals("Amount must be greater than zero", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getInvalid(), repository.getInvalid(), BigDecimal.ONE));
        assertEquals("Debit account must be valid", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getOurBankMainAccount(), repository.getInvalid(), BigDecimal.ONE));
        assertEquals("Credit account must be valid", e.getLocalizedMessage());

        e = assertThrows(IllegalArgumentException.class,
                () -> MoneyTransaction.make(1L, repository.getOurBankMainAccount(), repository.getOurBankMainAccount(), BigDecimal.ONE));
        assertEquals("Accounts must be different", e.getLocalizedMessage());
    }
}