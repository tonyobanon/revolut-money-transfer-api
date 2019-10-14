

package com.revolut.transferservice.api;

public interface Validatable {

    boolean isValid();

    default boolean isNotValid() {
        return !isValid();
    }
}
