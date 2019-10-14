

package com.revolut.transferservice.impl.parties;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.PartyType;
import com.revolut.transferservice.api.parties.Party;
import com.revolut.transferservice.impl.parties.PrivatePersonImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PrivatePersonTest {

    @Test
    void attributes() {
        final Party party = new PrivatePersonImpl(15L, "123456789012", "test", "best");
        assertNotNull(party);
        assertEquals(Long.valueOf(15L), party.getId());
        assertTrue(party.isValid());
        assertFalse(party.isNotValid());
        assertFalse(party.isLegalPerson());
        assertTrue(party.isPrivatePerson());
        assertEquals(PartyType.PRIVATE_PERSON, party.getPartyType());
        assertEquals("test best", party.getName());
        assertEquals("123456789012", party.getTaxIdentificationNumber());
    }
}