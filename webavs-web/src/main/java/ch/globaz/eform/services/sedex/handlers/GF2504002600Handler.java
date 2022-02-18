package ch.globaz.eform.services.sedex.handlers;

import eform.ch.eahv_iv.xmlns.eahv_iv_2504_002600._1.HeaderType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2504_002600._1.Message;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GF2504002600Handler extends GFFormHandler {
    @Override
    protected void extractData() throws RuntimeException {
        try {
            Message messageToTreat = (Message) message;
            HeaderType header = messageToTreat.getHeader();
            if (header != null) {
                initModel(header.getMessageId(), header.getSubject(), header.getMessageDate().toGregorianCalendar().toZonedDateTime().toLocalDate());
            }
            setBeneficiaireData(messageToTreat.getContent() != null ? messageToTreat.getContent().getInsuredPerson() : null);
        } catch (ClassCastException e) {
            LOG.error("Erreur de type de message.", e);
            throw new JadeApplicationRuntimeException(e);
        }
    }
}
