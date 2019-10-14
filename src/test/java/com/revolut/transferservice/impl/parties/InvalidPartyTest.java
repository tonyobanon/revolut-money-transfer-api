

package com.revolut.transferservice.impl.parties;

import org.junit.jupiter.api.Test;

import com.revolut.transferservice.api.PartyType;
import com.revolut.transferservice.api.parties.AbstractParty;
import com.revolut.transferservice.api.parties.Party;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class InvalidPartyTest {

    @Test
    void invalidParty() {
        final Party pt = AbstractParty.getInvalid();
        assertNotNull(pt);

        assertFalse(pt.isValid());
        assertTrue(pt.isNotValid());
        assertEquals(Long.valueOf(-1), pt.getId());

        assertEquals(AbstractParty.getInvalid(), pt);
        assertEquals(-1, pt.hashCode());

        assertEquals("", pt.getName());
        assertEquals("", pt.getTaxIdentificationNumber());
        assertEquals(PartyType.LEGAL_PERSON, pt.getPartyType());
        assertTrue(pt.isLegalPerson());
        assertFalse(pt.isPrivatePerson());
    }
}