

package com.revolut.transferservice.impl.parties;

import com.revolut.transferservice.api.parties.PrivatePerson;

class PrivatePersonImpl extends PrivatePerson {

    PrivatePersonImpl(Long id, String taxIdentificationNumber, String firstName, String lastName) {
        super(id, taxIdentificationNumber, firstName, lastName);
        validateTaxIdentificationNumber(taxIdentificationNumber);
    }

    private static void validateTaxIdentificationNumber(String taxIdentificationNumber) {
        if (taxIdentificationNumber.length() != 12) {
            throw new IllegalArgumentException("Tax identification number must contain 12 characters");
        }
    }
}
