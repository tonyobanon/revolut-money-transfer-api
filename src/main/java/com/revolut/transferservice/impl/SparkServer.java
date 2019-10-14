
package com.revolut.transferservice.impl;

import java.util.NoSuchElementException;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Bank;
import com.revolut.transferservice.api.Context;
import com.revolut.transferservice.api.Identifiable;
import com.revolut.transferservice.api.Repository;
import com.revolut.transferservice.api.accounts.AccountsRepository;
import com.revolut.transferservice.api.classes.AccountPayload;
import com.revolut.transferservice.api.classes.PartyPayload;
import com.revolut.transferservice.api.classes.TransactionPayload;
import com.revolut.transferservice.api.currencies.BaseCurrency;
import com.revolut.transferservice.api.parties.Party;
import com.revolut.transferservice.api.parties.PartyRepository;
import com.revolut.transferservice.api.transactions.Transaction;
import com.revolut.transferservice.api.transactions.TransactionRepository;
import com.revolut.transferservice.impl.classes.PaginationParams;
import com.revolut.transferservice.impl.utils.JsonUtils;

import spark.Request;
import spark.Response;
import spark.Spark;

final class SparkServer {

	private static final String WITHOUT_DATA = "do_not_generate_data";
	private static final Logger logger = LoggerFactory.getLogger(SparkServer.class);

	static void start() {
		final String[] args = { WITHOUT_DATA };
		SparkServer.main(args);
		Spark.awaitInitialization();
	}

	static void startWithData() {
		SparkServer.main(null);
		Spark.awaitInitialization();
	}

	static void stop() {
		Spark.stop();
		Spark.awaitStop();
	}

	public static void main(String[] args) {
		Bank.setInstance(BankImpl.getInstance());
		generateData(args);

		// TODO API versioning
		// TODO POST\PUT should return object that they've created or changed
		Spark.port(9999);
		Spark.threadPool(10);
		Spark.after((req, res) -> res.type("application/json"));

		initPartyRoutes();
		initAccountRoutes();
		initTransactionRoutes();
		initExceptionsHandling();
	}

	private static void initPartyRoutes() {
		// http://localhost:9999/parties?limit=10
		// http://localhost:9999/parties?page=2&limit=20
		Spark.get("/parties", (req, res) -> {
			
			final Repository<Party> repository = Bank.getInstance().getContext().getPartyRepository();
			final PaginationParams pgParams = PaginationParams.from(req);
			return JsonUtils.make().toJson(repository.getAll(pgParams));
		});

		Spark.put("/parties", (req, res) -> {
		
			final PartyPayload payload = JsonUtils.make().fromJson(req.body(), PartyPayload.class);
			
			final PartyRepository repository = Bank.getInstance().getContext().getPartyRepository();
			Party party = null;

			switch (payload.getType()) {
			case LEGAL_PERSON:
				party = repository.addLegalPerson(payload.getTaxIdentificationNumber(), payload.getName());
				break;
			case PRIVATE_PERSON:
			default:
				party = repository.addPrivatePerson(payload.getTaxIdentificationNumber(), payload.getName(),
						payload.getLastName());
				break;
			}
			res.status(HttpServletResponse.SC_CREATED);
			res.header("Location", "/parties/" + party.getId());
			return JsonUtils.make().toJson(party);
		});

		// http://localhost:9999/parties/1
		Spark.get("/parties/:id", (req, res) -> {
			final Repository<Party> repository = Bank.getInstance().getContext().getPartyRepository();
			return JsonUtils.make().toJson(findById(Party.class, repository, req));
		});

		// http://localhost:9999/parties/1/accounts
		Spark.get("/parties/:id/accounts", (req, res) -> {
			final Repository<Party> repository = Bank.getInstance().getContext().getPartyRepository();
			final Party party = findById(Party.class, repository, req);
			final AccountsRepository accountsRepository = Bank.getInstance().getContext().getAccountsRepository();
			return JsonUtils.make().toJson(accountsRepository.getByHolder(party));
		});
	}

	private static void initAccountRoutes() {
		// http://localhost:9999/accounts?limit=10
		Spark.get("/accounts", (req, res) -> {
			final Repository<Account> repository = Bank.getInstance().getContext().getAccountsRepository();
			final PaginationParams pgParams = PaginationParams.from(req);
			return JsonUtils.make().toJson(repository.getAll(pgParams));
		});

		Spark.put("/accounts", (req, res) -> {
			
			System.out.println("HFHFH: " + req.body());
			
			final AccountPayload payload = JsonUtils.make().fromJson(req.body(), AccountPayload.class);

			Context ctx = Bank.getInstance().getContext();
			final AccountsRepository repository = ctx.getAccountsRepository();
			
			System.out.println("HFHFH: " + payload.getPartyId());

			final Party party = ctx.getPartyRepository().getById(payload.getPartyId());
			
			System.out.println("HFHFH: " + party);

			Account account = payload.isActive()
					? repository.addActiveAccount(BaseCurrency.valueOf(payload.getCurrency()), payload.getNumber(),
							party, payload.getBalance())
					: repository.addPassiveAccount(BaseCurrency.valueOf(payload.getCurrency()), payload.getNumber(),
							party);
					
			res.status(HttpServletResponse.SC_CREATED);
			res.header("Location", "/accounts/" + account.getId());
			return JsonUtils.make().toJson(account);
			
		});

		// http://localhost:9999/accounts/1
		Spark.get("/accounts/:id", (req, res) -> {
			final Repository<Account> repository = Bank.getInstance().getContext().getAccountsRepository();
			return JsonUtils.make().toJson(findById(Account.class, repository, req));
		});

		// http://localhost:9999/accounts/1/transactions?limit=100
		Spark.get("/accounts/:id/transactions", (req, res) -> {
			final Repository<Account> repository = Bank.getInstance().getContext().getAccountsRepository();
			final Account account = findById(Account.class, repository, req);
			final TransactionRepository transactionRepository = Bank.getInstance().getContext()
					.getTransactionRepository();
			final PaginationParams pgParams = PaginationParams.from(req);
			return JsonUtils.make().toJson(transactionRepository.getByAccount(account, pgParams));
		});
	}

	private static void initTransactionRoutes() {
		// http://localhost:9999/transactions?limit=100
		Spark.get("/transactions", (req, res) -> {
			final Repository<Transaction> repository = Bank.getInstance().getContext().getTransactionRepository();
			final PaginationParams pgParams = PaginationParams.from(req);
			return JsonUtils.make().toJson(repository.getAll(pgParams));
		});

		// http://localhost:9999/transactions/1
		Spark.get("/transactions/:id", (req, res) -> {
			final Repository<Transaction> repository = Bank.getInstance().getContext().getTransactionRepository();
			return JsonUtils.make().toJson(findById(Transaction.class, repository, req));
		});

		// http://localhost:9999/transactions
		Spark.post("/transactions", (req, res) -> {
			final TransactionPayload payload = JsonUtils.make().fromJson(req.body(), TransactionPayload.class);
			final Transaction trn = Bank.getInstance().transfer(payload);
			res.status(HttpServletResponse.SC_CREATED);
			res.header("Location", "/transactions/" + trn.getId());
			return JsonUtils.make().toJson(trn);
		});
	}

	private static void initExceptionsHandling() {
		Spark.exception(IllegalArgumentException.class,
				(e, req, res) -> fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

		Spark.exception(NullPointerException.class,
				(e, req, res) -> fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

		Spark.exception(NumberFormatException.class,
				(e, req, res) -> fillErrorInfo(res, e, HttpServletResponse.SC_BAD_REQUEST));

		Spark.exception(NoSuchElementException.class,
				(e, req, res) -> fillErrorInfo(res, e, HttpServletResponse.SC_NOT_FOUND));
	}

	private static void generateData(String[] args) {
		if (args != null && args.length > 0 && WITHOUT_DATA.equals(args[0].toLowerCase())) {
			return;
		}

		try {
			((BankImpl) Bank.getInstance()).generateData();
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage(), e);
		}
	}

	private static void fillErrorInfo(Response res, Exception err, int errCode) {
		res.type("application/json");
		res.status(errCode);
		res.body(JsonUtils.toJson(err, errCode));
	}

	private static <T extends Identifiable> T findById(Class<T> type, Repository<T> repository, Request req) {
		final Long id = getId(req);
		final T t = repository.getById(id);
		if (t.isNotValid()) {
			throw new NoSuchElementException(String.format("%s with id %d is not found", type.getSimpleName(), id));
		}
		return t;
	}

	private static Long getId(Request req) {
		return Long.valueOf(req.params("id"), 10);
	}
}
