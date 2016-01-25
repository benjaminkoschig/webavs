package ch.globaz.perseus.business.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.DecisionForTypeDocument;
import ch.globaz.perseus.business.models.decision.DecisionForTypeDocumentSearch;

public interface DecisionForTypeDocumentService extends JadeApplicationService {

    public int count(DecisionForTypeDocumentSearch search) throws DecisionException, JadePersistenceException;

    public DecisionForTypeDocument create(DecisionForTypeDocument decisionForTypeDocument)
            throws JadePersistenceException, DecisionException;

    public DecisionForTypeDocument delete(DecisionForTypeDocument decisionForTypeDocument)
            throws JadePersistenceException, DecisionException;

    public DecisionForTypeDocument read(String idDecision) throws JadePersistenceException, DecisionException;

    public DecisionForTypeDocumentSearch search(DecisionForTypeDocumentSearch searchModel)
            throws JadePersistenceException, DecisionException;

    public DecisionForTypeDocument update(DecisionForTypeDocument DecisionForTypeDocument)
            throws JadePersistenceException, DecisionException;
}
