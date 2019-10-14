

package com.revolut.transferservice.impl.accounts;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.parties.PartyRepository;
import com.revolut.transferservice.impl.ContextImpl;
import com.revolut.transferservice.impl.accounts.AccountImpl;
import com.revolut.transferservice.impl.utils.JsonUtils;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountImplTest {

    private final PartyRepository repository = ContextImpl.create().getPartyRepository();


    @Test
    void getId() {
        final Account a = makeAccount(11L);
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(Long.valueOf(11L), a.getId());
    }

    @Test
    void getNumber() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals("30102810100000000001", a.getNumber());
    }

    @Test
    void getCurrency() {
        Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(BaseCurrency.getDefault(), a.getCurrency());

        a = new AccountImpl(1L, BaseCurrency.valueOf("USD"), "30102810100000000001", repository.getOurBank(), false);
        assertNotNull(a);
        assertTrue(a.isValid());
        assertFalse(a.isActive());
        assertEquals(BaseCurrency.valueOf("USD"), a.getCurrency());
    }

    @Test
    void getBalance() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(BigDecimal.valueOf(0L), a.getBalance());
    }

    @Test
    void getHolder() {
        final Account a = makeAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(repository.getOurBank(), a.getHolder());
    }

    @Test
    void debitAndCredit() {
        final Account a = makeAccount();
        assertFalse(a.debit(BigDecimal.TEN));
        assertTrue(a.credit(BigDecimal.TEN));
        assertEquals(BigDecimal.TEN, a.getBalance());
        assertTrue(a.debit(BigDecimal.ONE));
        assertEquals(BigDecimal.valueOf(9), a.getBalance());
    }

    @Test
    void toStringImpl() {
        final Account a = makeAccount();
        assertTrue(a.toString().startsWith("Account{"));
        assertEquals("Account{id=1, currency=BaseCurrency(isoCode=GBP, country=GBR), number=30102810100000000001, active=true, balance=0, holder=Party{Revolut LLC, type=LEGAL_PERSON, tax identification number=7703408188, id=1}}",
                a.toString());
    }

    @Test
    void lockShouldBeTransient() {
        final Account a = makeAccount();
        final String json = JsonUtils.make().toJson(a);
        assertNotNull(json);
        assertFalse(json.contains("lock"));
        assertEquals("{\n" +
                "  \"id\": 1,\n" +
                "  \"currency\": {\n" +
                "    \"isoCode\": \"GBP\",\n" +
                "    \"country\": \"GBR\"\n" +
                "  },\n" +
                "  \"number\": \"30102810100000000001\",\n" +
                "  \"holder\": {\n" +
                "    \"name\": \"Revolut LLC\",\n" +
                "    \"id\": 1,\n" +
                "    \"partyType\": \"LEGAL_PERSON\",\n" +
                "    \"taxIdentificationNumber\": \"7703408188\"\n" +
                "  },\n" +
                "  \"active\": true,\n" +
                "  \"balance\": 0\n" +
                "}", json);
    }

    private AccountImpl makeAccount() {
        return makeAccount(1L);
    }

    private AccountImpl makeAccount(Long id) {
        return new AccountImpl(id, BaseCurrency.getDefault(),"30102810100000000001", repository.getOurBank(), true);
    }
}