package com.revolut.transferservice.api.classes;

import com.revolut.transferservice.api.PartyType;

public class PartyPayload {

	private final String taxIdentificationNumber;
	private final PartyType type;
	private final String name;
	private final String lastName;

	public PartyPayload(String taxIdentificationNumber, PartyType type, String name, String lastName) {
		this.taxIdentificationNumber = taxIdentificationNumber;
		this.type = type;
		this.name = name;
		this.lastName = lastName;
	}

	public String getTaxIdentificationNumber() {
		return taxIdentificationNumber;
	}

	public PartyType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getLastName() {
		return lastName;
	}

}
