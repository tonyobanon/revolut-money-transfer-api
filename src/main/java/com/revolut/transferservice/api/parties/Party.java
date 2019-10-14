

package com.revolut.transferservice.api.parties;

import com.revolut.transferservice.api.Identifiable;
import com.revolut.transferservice.api.PartyType;

public interface Party extends Identifiable {

    String getName();

    boolean isPrivatePerson();

    boolean isLegalPerson();

    PartyType getPartyType();

    String getTaxIdentificationNumber();
}
