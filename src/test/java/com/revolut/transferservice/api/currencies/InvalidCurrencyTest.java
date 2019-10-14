

package com.revolut.transferservice.api.currencies;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.currencies.Currency;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidCurrencyTest {

    @Test
    void invalidCurrency() {
        final Currency cc = BaseCurrency.getInvalid();
        assertNotNull(cc);

        assertFalse(cc.isValid());
        assertTrue(cc.isNotValid());

        assertEquals(BaseCurrency.getInvalid(), cc);
        assertEquals(-1, cc.hashCode());

        assertEquals("", cc.getISOCode());
    }
}