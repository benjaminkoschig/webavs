package ch.globaz.pegasus.businessimpl.services.models.transfert;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.pegasus.business.exceptions.models.transfertdossier.TransfertDossierException;
import ch.globaz.pegasus.business.models.transfert.SimpleTransfertDossierSuppression;
import ch.globaz.pegasus.business.models.transfert.SimpleTransfertDossierSuppressionSearch;
import ch.globaz.pegasus.business.services.models.transfert.SimpleTransfertDossierSuppressionService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleTransfertDossierSuppressionServiceImpl extends PegasusAbstractServiceImpl implements
        SimpleTransfertDossierSuppressionService {

    @Override
    public int count(SimpleTransfertDossierSuppressionSearch search) throws TransfertDossierException,
            JadePersistenceException {
        if (search == null) {
            throw new TransfertDossierException(
                    "Unable to count simpleTransfertDossierSuppression, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public SimpleTransfertDossierSuppression create(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException {
        if (decision == null) {
            throw new TransfertDossierException(
                    "Unable to create simpleTransfertDossierSuppression, the model passed is null!");
        }
        return (SimpleTransfertDossierSuppression) JadePersistenceManager.add(decision);

    }

    @Override
    public SimpleTransfertDossierSuppression delete(SimpleTransfertDossierSuppression decision)
            throws TransfertDossierException, JadePersistenceException {
        if (decision == null) {
            throw new TransfertDossierException(
                    "Unable to delete simpleTransfertDossierSuppression, the model passed is null!");
        }
        return (SimpleTransfertDossierSuppression) JadePersistenceManager.delete(decision);
    }

    @Override
    public SimpleTransfertDossierSuppression read(String idDecision) throws JadePersistenceException,
            TransfertDossierException {
        if (idDecision == null) {
            throw new TransfertDossierException(
                    "Unable to read simpleTransfertDossierSuppression, the id passed is null!");
        }
        SimpleTransfertDossierSuppression simpleDecisionSuppression = new SimpleTransfertDossierSuppression();
        simpleDecisionSuppression.setId(idDecision);
        return (SimpleTransfertDossierSuppression) JadePersistenceManager.read(simpleDecisionSuppression);
    }

    @Override
    public SimpleTransfertDossierSuppressionSearch search(SimpleTransfertDossierSuppressionSearch decisionSearch)
            throws JadePersistenceException, TransfertDossierException {
        if (decisionSearch == null) {
            throw new TransfertDossierException(
                    "Unable to search simpleTransfertDossierSuppression, the search model passed is null!");
        }
        return (SimpleTransfertDossierSuppressionSearch) JadePersistenceManager.search(decisionSearch);
    }

    @Override
    public SimpleTransfertDossierSuppression update(SimpleTransfertDossierSuppression decision)
            throws JadePersistenceException, TransfertDossierException {
        if (decision == null) {
            throw new TransfertDossierException(
                    "Unable to update simpleTransfertDossierSuppression, the model passed is null!");
        }
        return (SimpleTransfertDossierSuppression) JadePersistenceManager.update(decision);
    }

}
