

package com.revolut.transferservice.api;

import java.math.BigDecimal;

import lombok.ToString;

@ToString
public class InvalidBalanceException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	private final BigDecimal expected;
    private final BigDecimal actual;

    public InvalidBalanceException(BigDecimal expected, BigDecimal actual) {
        this.expected = expected;
        this.actual = actual;
    }

	public BigDecimal getExpected() {
		return expected;
	}

	public BigDecimal getActual() {
		return actual;
	}
}
