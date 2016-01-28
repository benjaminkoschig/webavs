package ch.globaz.pegasus.business.services.annonce.communicationocc;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.annonce.CommunicationOCC;

public interface CommunicationOCCBuilder {
    public JadePrintDocumentContainer build(List<CommunicationOCC> communications, JadePublishDocumentInfo pubInfos,
            String mailGest, String dateRapport) throws DecisionException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException, Exception;

}
