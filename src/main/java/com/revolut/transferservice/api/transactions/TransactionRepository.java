

package com.revolut.transferservice.api.transactions;

import java.math.BigDecimal;

import com.revolut.transferservice.api.Account;
import com.revolut.transferservice.api.Repository;
import com.revolut.transferservice.api.classes.PagedResult;
import com.revolut.transferservice.api.classes.Pagination;

public interface TransactionRepository extends Repository<Transaction> {

    Transaction add(Account debit, Account credit, BigDecimal amount);

    PagedResult<Transaction> getByAccount(Account account, int pageNumber, int recordsPerPage);

    default PagedResult<Transaction> getByAccount(Account account, Pagination pagination) {
        return getByAccount(account, pagination.getPageNumber(), pagination.getRecordsPerPage());
    }
}
