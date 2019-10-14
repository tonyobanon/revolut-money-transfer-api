

package com.revolut.transferservice.impl.parties;

import com.revolut.transferservice.api.parties.LegalPerson;

final class LegalPersonImpl extends LegalPerson {

    LegalPersonImpl(Long id, String taxIdentificationNumber, String name) {
        super(id, taxIdentificationNumber, name);
        validateTaxIdentificationNumber(taxIdentificationNumber);
    }

    private static void validateTaxIdentificationNumber(String taxIdentificationNumber) {
        if (taxIdentificationNumber.length() != 10) {
            throw new IllegalArgumentException("Tax identification number must contain 10 characters");
        }
    }
}
