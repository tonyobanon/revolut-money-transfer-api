

package com.revolut.transferservice.impl.accounts;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.parties.AbstractParty;
import com.revolut.transferservice.impl.accounts.AbstractAccount;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidAccountTest {

    @Test
    void invalidAccount() {
        final Account a = AbstractAccount.getInvalid();
        assertNotNull(a);

        assertFalse(a.isValid());
        assertTrue(a.isNotValid());
        assertEquals(Long.valueOf(-1), a.getId());

        assertEquals(AbstractAccount.getInvalid(), a);
        assertEquals(-1, a.hashCode());

        assertEquals("", a.getNumber());
        assertEquals(BigDecimal.valueOf(0), a.getBalance());
        assertEquals(BaseCurrency.getInvalid(), a.getCurrency());
        assertEquals(AbstractParty.getInvalid(), a.getHolder());
        assertFalse(a.isActive());
    }

    @Test
    void toStringImpl() {
        final Account a = AbstractAccount.getInvalid();
        assertNotNull(a);
        assertTrue(a.toString().startsWith("InvalidAccount{"));
    }
}