

package com.revolut.transferservice.api;

import com.revolut.transferservice.api.classes.Pageable;

public interface Repository<T> extends Pageable<T> {

    int size();

    T getById(Long id);

    T getInvalid();
}
