package ch.globaz.eform.businessimpl.services.sedex.handlers;

import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.HeaderType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000101._3.Message;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GF2021000101Handler extends GFFormHandler {
    @Override
    protected void extractData() throws RuntimeException {
        try {
            Message messageToTreat = (Message) message;
            HeaderType header = messageToTreat.getHeader();
            if (header != null) {
                initModel(header.getMessageId(), header.getMessageType(), header.getSubject(), header.getMessageDate().toGregorianCalendar().toZonedDateTime().toLocalDate());
            }
            setBeneficiaireData(messageToTreat.getContent() != null ? messageToTreat.getContent().getInsuredPerson() : null);
        } catch (ClassCastException e) {
            LOG.error("GF2501001800#extractData - HandlerErreur de type de message.", e);
            throw new JadeApplicationRuntimeException(e);
        }
    }
}
