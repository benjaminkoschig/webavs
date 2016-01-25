package ch.globaz.pegasus.business.services.transfertDossier;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.print.server.JadePrintDocumentContainer;
import globaz.jade.publish.document.JadePublishDocumentInfo;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;

public interface ITransfertDossierBuilder {

    public JadePrintDocumentContainer build(JadePublishDocumentInfo pubInfo) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException, Exception;

    public void loadParameters(Map<String, String> parameters, List<String> annexes, List<String> copies);

}
