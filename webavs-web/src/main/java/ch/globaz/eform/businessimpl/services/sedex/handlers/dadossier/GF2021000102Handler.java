package ch.globaz.eform.businessimpl.services.sedex.handlers.dadossier;

import ch.globaz.eform.businessimpl.services.sedex.handlers.GFDaDossierHandler;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.HeaderType;
import eform.ch.eahv_iv.xmlns.eahv_iv_2021_000102._3.Message;
import globaz.jade.service.exception.JadeApplicationRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GF2021000102Handler extends GFDaDossierHandler {
    @Override
    public void extractData() throws RuntimeException {
        if(message != null) {
            try {
                Message messageToTreat = (Message) message;
                HeaderType header = messageToTreat.getHeader();
                if (header != null) {
                    initModel(header.getMessageId(), header.getOurBusinessReferenceId(), header.getMessageDate().toGregorianCalendar().toZonedDateTime().toLocalDate());
                    initCaisseData(header.getSenderId());
                }
                setBeneficiaireData(messageToTreat.getContent() != null ? messageToTreat.getContent().getInsuredPerson() : null);
            } catch (ClassCastException e) {
                LOG.error("GF2021000102Handler#extractData - HandlerErreur de type de message.", e);
                throw new JadeApplicationRuntimeException(e);
            }
        }
    }
}
