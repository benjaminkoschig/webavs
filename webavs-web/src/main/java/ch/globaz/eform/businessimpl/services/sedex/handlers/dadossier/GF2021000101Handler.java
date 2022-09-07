package ch.globaz.eform.businessimpl.services.sedex.handlers.dadossier;

import ch.globaz.eform.businessimpl.services.sedex.handlers.GFDaDossierHandler;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.Message;
import eform.ch.eahv_iv.xmlns.eahv_iv_common._4.NaturalPersonsOASIDIType;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;

@Slf4j
public class GF2021000101Handler extends GFDaDossierHandler {
    @Override
    protected String getMessageId() {
        return ((Message) message).getHeader().getMessageId();
    }

    @Override
    protected String getYourBusinessReferenceId() {
        return null;
    }

    @Override
    protected String getOurBusinessReferenceId() {
        return ((Message) message).getHeader().getOurBusinessReferenceId();
    }

    @Override
    protected LocalDate getMessageDate() {
        return ((Message) message).getHeader().getMessageDate().toGregorianCalendar().toZonedDateTime().toLocalDate();
    }

    @Override
    protected String getSenderId() {
        return ((Message) message).getHeader().getSenderId();
    }

    @Override
    protected NaturalPersonsOASIDIType getInsuredPerson() {
        return ((Message) message).getContent() != null ?((Message) message).getContent().getInsuredPerson() : null;
    }
}
