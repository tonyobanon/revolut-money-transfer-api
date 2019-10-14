

package com.revolut.transferservice.impl.parties;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicLong;

import com.revolut.transferservice.api.classes.PagedResult;
import com.revolut.transferservice.api.parties.AbstractParty;
import com.revolut.transferservice.api.parties.LegalPerson;
import com.revolut.transferservice.api.parties.Party;
import com.revolut.transferservice.api.parties.PartyRepository;
import com.revolut.transferservice.api.parties.PrivatePerson;
import com.revolut.transferservice.impl.classes.PagedResultImpl;

public final class DefaultPartyRepository implements PartyRepository {

    private final AtomicLong counter = new AtomicLong(0L);
    private final ConcurrentMap<Long, Party> parties = new ConcurrentHashMap<>();
    private final Long ourBankId;

    public DefaultPartyRepository() {
        final Party ourBank = addLegalPerson("7703408188", "Revolut LLC");
        ourBankId = ourBank.getId();
    }

    @Override
    public LegalPerson addLegalPerson(String taxIdentificationNumber, String name) {
        final LegalPerson legalPerson = new LegalPersonImpl(counter.incrementAndGet(), taxIdentificationNumber, name);
        parties.putIfAbsent(legalPerson.getId(), legalPerson);
        return legalPerson;
    }

    @Override
    public PrivatePerson addPrivatePerson(String taxIdentificationNumber, String firstName, String lastName) {
        final PrivatePerson privatePerson = new PrivatePersonImpl(counter.incrementAndGet(), taxIdentificationNumber, firstName, lastName);
        parties.putIfAbsent(privatePerson.getId(), privatePerson);
        return privatePerson;
    }

    @Override
    public Party getById(Long id) {
        return parties.getOrDefault(id, getInvalid());
    }

    @Override
    public Party getOurBank() {
        return getById(ourBankId);
    }

    @Override
    public PagedResult<Party> getAll(int pageNumber, int recordsPerPage) {
        return PagedResultImpl.from(pageNumber, recordsPerPage, parties);
    }

    @Override
    public Party getInvalid() {
        return AbstractParty.getInvalid();
    }

    @Override
    public int size() {
        return parties.size();
    }
}
