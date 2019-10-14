

package com.revolut.transferservice.api.parties;

import com.revolut.transferservice.api.Repository;

public interface PartyRepository extends Repository<Party> {

    Party addLegalPerson(String taxIdentificationNumber, String name);

    Party addPrivatePerson(String taxIdentificationNumber, String firstName, String lastName);

    Party getOurBank();
}
