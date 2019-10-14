

package com.revolut.transferservice.impl.parties;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.PartyType;
import com.revolut.transferservice.api.parties.Party;
import com.revolut.transferservice.impl.parties.LegalPersonImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LegalPersonTest {

    @Test
    void attributes() {
        final Party party = new LegalPersonImpl(11L, "1234567890", "test");
        assertNotNull(party);
        assertEquals(Long.valueOf(11L), party.getId());
        assertTrue(party.isValid());
        assertFalse(party.isNotValid());
        assertTrue(party.isLegalPerson());
        assertFalse(party.isPrivatePerson());
        assertEquals(PartyType.LEGAL_PERSON, party.getPartyType());
        assertEquals("test", party.getName());
        assertEquals("1234567890", party.getTaxIdentificationNumber());
    }
}