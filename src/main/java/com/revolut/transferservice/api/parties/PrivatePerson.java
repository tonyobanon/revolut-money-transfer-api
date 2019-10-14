

package com.revolut.transferservice.api.parties;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import com.revolut.transferservice.api.PartyType;

import java.util.Objects;

public abstract class PrivatePerson extends AbstractParty {

    private final String firstName;
    private final String lastName;

    protected PrivatePerson(Long id, String taxIdentificationNumber, String firstName, String lastName) {
        super(id, PartyType.PRIVATE_PERSON, taxIdentificationNumber);
        Objects.requireNonNull(firstName, "First name cannot be null");
        Objects.requireNonNull(lastName, "Last name cannot be null");
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Override
    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(41, 11)
                .append(firstName)
                .append(lastName)
                .append(getPartyType())
                .append(getTaxIdentificationNumber())
                .toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!(obj instanceof PrivatePerson)) {
            return false;
        }

        PrivatePerson other = (PrivatePerson) obj;
        return new EqualsBuilder()
                .append(firstName, other.firstName)
                .append(lastName, other.lastName)
                .append(getPartyType(), other.getPartyType())
                .append(getTaxIdentificationNumber(), other.getTaxIdentificationNumber())
                .isEquals();
    }
}
