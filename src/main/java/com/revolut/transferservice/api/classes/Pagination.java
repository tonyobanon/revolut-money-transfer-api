

package com.revolut.transferservice.api.classes;

import com.revolut.transferservice.api.Validatable;

public interface Pagination extends Validatable {

    int getRecordsPerPage();

    int getPageNumber();
}
