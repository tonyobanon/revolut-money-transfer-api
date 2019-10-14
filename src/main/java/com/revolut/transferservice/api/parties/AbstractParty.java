

package com.revolut.transferservice.api.parties;

import lombok.Getter;

import java.util.Objects;

import com.revolut.transferservice.api.PartyType;

public abstract class AbstractParty implements Party {

    @Getter
    private final Long id;

    @Getter
    private final PartyType partyType;

    @Getter
    private final String taxIdentificationNumber;

    AbstractParty(Long id, PartyType partyType, String taxIdentificationNumber) {
        Objects.requireNonNull(id, "Id cannot be null");
        Objects.requireNonNull(partyType, "PartyType cannot be null");
        Objects.requireNonNull(taxIdentificationNumber, "Tax identification number cannot be null");

        this.id = id;
        this.partyType = partyType;
        this.taxIdentificationNumber = taxIdentificationNumber;
    }
    
    @Override
    public Long getId() {
    	return id;
    }
    
    @Override
    public PartyType getPartyType() {
    	return partyType;
    }
    
    public String getTaxIdentificationNumber() {
		return taxIdentificationNumber;
	}

    public final boolean isPrivatePerson() {
        return PartyType.PRIVATE_PERSON == partyType;
    }

    public final boolean isLegalPerson() {
        return PartyType.LEGAL_PERSON == partyType;
    }

    @Override
    public final String toString() {
        return String.format("Party{%s, type=%s, tax identification number=%s, id=%d}",
                getName(), getPartyType(), getTaxIdentificationNumber(), getId());
    }

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    public static Party getInvalid() {
        return InvalidParty.getInstance();
    }
}
