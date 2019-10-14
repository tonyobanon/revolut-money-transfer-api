

package com.revolut.transferservice.impl.accounts;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Context;
import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.parties.Party;
import com.revolut.transferservice.api.parties.PartyRepository;
import com.revolut.transferservice.impl.ContextImpl;
import com.revolut.transferservice.impl.accounts.DefaultAccountsRepository;

import java.math.BigDecimal;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AccountsRepositoryTest {

    @Test
    void getInitialBalance() {
        final DefaultAccountsRepository repository = (DefaultAccountsRepository) ContextImpl.create().getAccountsRepository();
        assertEquals(BigDecimal.valueOf(100_000_000.00d), repository.getInitialBalance());
    }

    @Test
    void getOurBankMainAccount() {
    	final DefaultAccountsRepository repository = (DefaultAccountsRepository) ContextImpl.create().getAccountsRepository();
        final Account a = repository.getOurBankMainAccount();
        assertNotNull(a);
        assertTrue(a.isValid());
        assertTrue(a.isActive());
        assertEquals(repository.getInitialBalance(), a.getBalance());
    }

    @Test
    void size() {
        final DefaultAccountsRepository repository = (DefaultAccountsRepository) ContextImpl.create().getAccountsRepository();
        assertEquals(1, repository.size());
        repository.addActiveAccount(BaseCurrency.getDefault(), "20202810100000012345", repository.getPartyRepository().getOurBank(), BigDecimal.ZERO);
        assertEquals(2, repository.size());
    }

    @Test
    void getByHolder() {
        final Context context = ContextImpl.create();
        final AccountsRepository repository = context.getAccountsRepository();
        final PartyRepository partyRepository = context.getPartyRepository();
        final Party party = partyRepository.addLegalPerson("1234567890", "test");

        Collection<Account> accounts = repository.getByHolder(party);
        assertNotNull(accounts);
        assertEquals(0, accounts.size());

        final Account clientAccount = repository.addPassiveAccount(BaseCurrency.getDefault(), "40702810001234567890", party);
        accounts = repository.getByHolder(party);
        assertEquals(1, accounts.size());
        assertEquals(clientAccount, accounts.iterator().next());

        accounts = repository.getByHolder(partyRepository.getOurBank());
        assertEquals(1, accounts.size());
        assertEquals(repository.getOurBankMainAccount(), accounts.iterator().next());
    }
}