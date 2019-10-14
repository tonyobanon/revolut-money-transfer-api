
package com.revolut.transferservice.impl.accounts;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Region;
import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.classes.PagedResult;
import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.currencies.Currency;
import com.revolut.transferservice.api.parties.Party;
import com.revolut.transferservice.api.parties.PartyRepository;
import com.revolut.transferservice.api.regions.UKRegion;
import com.revolut.transferservice.api.regions.USRegion;
import com.revolut.transferservice.impl.classes.PagedResultImpl;

public final class DefaultAccountsRepository implements AccountsRepository {

	private static final Logger logger = LoggerFactory.getLogger(DefaultAccountsRepository.class);
	private static final BigDecimal INITIAL_BALANCE = BigDecimal.valueOf(100_000_000.00d);

	private final AtomicLong counter;
	private final ConcurrentMap<Long, Account> accounts;
	private final PartyRepository partyRepository;
	private final Long ourBankAccountId;

	private static final ConcurrentMap<String, Region> regions = new ConcurrentHashMap<>();

	public DefaultAccountsRepository(PartyRepository partyRepository) {
		this.counter = new AtomicLong(0L);
		this.accounts = new ConcurrentHashMap<>();
		this.partyRepository = partyRepository;
		final Account ourBankAccount = this.addActiveAccount(BaseCurrency.getDefault(), "20202810100000010001",
				partyRepository.getOurBank(), INITIAL_BALANCE);
		this.ourBankAccountId = ourBankAccount.getId();
	}

	@Override
	public Account getOurBankMainAccount() {
		return getById(ourBankAccountId);
	}

	PartyRepository getPartyRepository() {
		return partyRepository;
	}

	BigDecimal getInitialBalance() {
		return INITIAL_BALANCE;
	}

	@Override
	public Account getById(Long id) {
		return accounts.getOrDefault(id, getInvalid());
	}

	@Override
	public Account getInvalid() {
		return AbstractAccount.getInvalid();
	}

	@Override
	public Account addPassiveAccount(Currency currency, String number, Party holder) {
		return addAccount(currency, number, holder, false, BigDecimal.ZERO);
	}

	@Override
	public Account addActiveAccount(Currency currency, String number, Party holder, BigDecimal balance) {
		return addAccount(currency, number, holder, true, balance);
	}

	private Account addAccount(Currency currency, String number, Party holder, Boolean active, BigDecimal balance) {

		// Create account
		final Account account = new AccountImpl(counter.incrementAndGet(), currency, number, holder, active, balance);

		// Enforce regional policies
		Region region = getRegion(currency.getCountry());

		region.enforceRegionalPolicies(holder, account);

		// Register account
		logger.debug("Creating account: " + account);
		accounts.putIfAbsent(account.getId(), account);
		return account;
	}

	@Override
	public int size() {
		return accounts.size();
	}

	@Override
	public PagedResult<Account> getAll(int pageNumber, int recordsPerPage) {
		return PagedResultImpl.from(pageNumber, recordsPerPage, accounts);
	}

	@Override
	public Collection<Account> getByHolder(Party holder) {
		return Collections.unmodifiableCollection(accounts.values().stream().filter(a -> a.getHolder().equals(holder))
				.sorted(Comparator.comparing(Account::getId)).collect(Collectors.toList()));
	}

	private static Region getRegion(String isoCode) {
		return regions.get(isoCode);
	}

	static {

		// Register available regions
		regions.put(USRegion.ISO_CODE, new USRegion());
		regions.put(UKRegion.ISO_CODE, new UKRegion());
	}
}
