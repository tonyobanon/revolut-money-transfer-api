

package com.revolut.transferservice.api.currencies;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.currencies.Currency;

import static org.junit.jupiter.api.Assertions.*;

class BaseCurrencyTest {

    @Test
    void valueOf() {
        final Currency rub = BaseCurrency.valueOf("GBP");
        assertNotNull(rub);
        assertTrue(rub.isValid());
        assertFalse(rub.isNotValid());
        assertEquals("GBP", rub.getISOCode());
        assertEquals(BaseCurrency.valueOf("GBP"), rub);
        assertSame(BaseCurrency.valueOf("GBP"), rub);

        final Currency usd = BaseCurrency.valueOf("USD");
        assertNotNull(usd);
        assertTrue(usd.isValid());
        assertFalse(usd.isNotValid());
        assertEquals("USD", usd.getISOCode());
        assertEquals(BaseCurrency.valueOf("USD"), usd);
        assertSame(BaseCurrency.valueOf("USD"), usd);

        assertNotEquals(usd, rub);
    }

    @Test
    void defaultCurrency() {
        final Currency def = BaseCurrency.getDefault();
        assertNotNull(def);
        assertTrue(def.isValid());
        assertEquals("GBP", def.getISOCode());
    }
}