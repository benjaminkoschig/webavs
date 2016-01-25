package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.DecisionForTypeDocument;
import ch.globaz.perseus.business.models.decision.DecisionForTypeDocumentSearch;
import ch.globaz.perseus.business.services.models.decision.DecisionForTypeDocumentService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class DecisionForTypeDocumentServiceImpl extends PerseusAbstractServiceImpl implements
        DecisionForTypeDocumentService {

    @Override
    public int count(DecisionForTypeDocumentSearch search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count DecisionForTypeDocument, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public DecisionForTypeDocument create(DecisionForTypeDocument decisionForTypeDocument)
            throws JadePersistenceException, DecisionException {
        if (decisionForTypeDocument == null) {
            throw new DecisionException("Unable to create decisionForTypeDocument, the given model is null!");
        }

        return decisionForTypeDocument;
    }

    @Override
    public DecisionForTypeDocument delete(DecisionForTypeDocument decisionForTypeDocument)
            throws JadePersistenceException, DecisionException {
        if (decisionForTypeDocument == null) {
            throw new DecisionException("Unable to delete a decisionForTypeDocument, the model passed is null!");
        }

        try {
            // Suppression de la decision
            decisionForTypeDocument.setSimpleDecision(PerseusImplServiceLocator.getSimpleDecisionService().delete(
                    decisionForTypeDocument.getSimpleDecision()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return decisionForTypeDocument;
    }

    @Override
    public DecisionForTypeDocument read(String idDecisionForTypeDocument) throws JadePersistenceException,
            DecisionException {
        if (JadeStringUtil.isEmpty(idDecisionForTypeDocument)) {
            throw new DecisionException("Unable to read a decision, the id passed is null!");
        }

        DecisionForTypeDocument decisionForTypeDocument = new DecisionForTypeDocument();
        decisionForTypeDocument.setId(idDecisionForTypeDocument);

        return (DecisionForTypeDocument) JadePersistenceManager.read(decisionForTypeDocument);
    }

    @Override
    public DecisionForTypeDocumentSearch search(DecisionForTypeDocumentSearch searchModel)
            throws JadePersistenceException, DecisionException {
        if (searchModel == null) {
            throw new DecisionException("Unable to search a decision, the search model passed is null!");
        }

        return (DecisionForTypeDocumentSearch) JadePersistenceManager.search(searchModel);
    }

    @Override
    public DecisionForTypeDocument update(DecisionForTypeDocument decisionForTypeDocument)
            throws JadePersistenceException, DecisionException {
        if (decisionForTypeDocument == null) {
            throw new DecisionException("Unable to update decisionForTypeDocument, the given model is null!");
        }

        return decisionForTypeDocument;
    }

}
