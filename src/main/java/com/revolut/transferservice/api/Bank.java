package com.revolut.transferservice.api;

import com.revolut.transferservice.api.classes.TransactionPayload;
import com.revolut.transferservice.api.transactions.Transaction;

public abstract class Bank {
	
	private static Bank INSTANCE;
	
	public static Bank getInstance() {
		return INSTANCE;
	}

	public static void setInstance(Bank instance) {
		if (INSTANCE == null) {
			INSTANCE = instance;
		}
	}

	public abstract Context getContext();

	public abstract Transaction transfer(TransactionPayload payload);

}