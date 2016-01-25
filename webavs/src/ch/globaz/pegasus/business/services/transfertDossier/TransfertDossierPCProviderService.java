package ch.globaz.pegasus.business.services.transfertDossier;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.decision.DecisionSuppression;
import ch.globaz.pegasus.business.models.transfert.SimpleTransfertDossierSuppression;
import ch.globaz.pegasus.business.models.transfert.TransfertDossierSuppression;

public interface TransfertDossierPCProviderService extends JadeApplicationService {

    void checkProcessArguments(String idDernierDomicileLegal, String idNouvelleCaisse, String[] copies)
            throws TransfertDossierException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException;

    SimpleTransfertDossierSuppression createTransfertDossierSuppression(DecisionSuppression decisionSuppression,
            String idNouvelleCaisse, String csMotifTransfert) throws TransfertDossierException,
            JadePersistenceException;

    void genereDocument(TransfertDossierSuppression transfert, DecisionSuppression decision, String gestMail)
            throws TransfertDossierException;

    TransfertDossierSuppression readTransfertByIdDecisionHeader(String idDecisionHeader)
            throws TransfertDossierException, JadePersistenceException;

}
