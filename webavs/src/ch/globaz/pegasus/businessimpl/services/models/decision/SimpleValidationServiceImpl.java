package ch.globaz.pegasus.businessimpl.services.models.decision;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import java.util.List;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecision;
import ch.globaz.pegasus.business.models.decision.SimpleValidationDecisionSearch;
import ch.globaz.pegasus.business.services.models.decision.SimpleValidationService;
import ch.globaz.pegasus.businessimpl.services.PegasusAbstractServiceImpl;

public class SimpleValidationServiceImpl extends PegasusAbstractServiceImpl implements SimpleValidationService {

    @Override
    public int count(SimpleValidationDecisionSearch search) throws DecisionException, JadePersistenceException {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public SimpleValidationDecision create(SimpleValidationDecision validation) throws JadePersistenceException {
        return (SimpleValidationDecision) JadePersistenceManager.add(validation);
    }

    @Override
    public void delete(List<String> idsDecisionHeader) throws DecisionException, JadePersistenceException {
        if (idsDecisionHeader == null) {
            throw new DecisionException("Unable to delete idsDecisionHeader, the model passed is null!");
        }
        SimpleValidationDecisionSearch searchModel = new SimpleValidationDecisionSearch();
        searchModel.setForInIdDecisionHeader(idsDecisionHeader);
        JadePersistenceManager.delete(searchModel);
    }

    @Override
    public SimpleValidationDecision delete(SimpleValidationDecision validationDecision) throws DecisionException,
            JadePersistenceException {
        if (validationDecision == null) {
            throw new DecisionException("Unbale to delete simpleValidation, the model passed is null!");
        }
        return (SimpleValidationDecision) JadePersistenceManager.delete(validationDecision);
    }

    @Override
    public int delete(SimpleValidationDecisionSearch validationDecisionSearch) throws DecisionException,
            JadePersistenceException {
        if (validationDecisionSearch == null) {
            throw new DecisionException("Unbale to delete simpleValidation, the model passed is null!");
        }
        return JadePersistenceManager.delete(validationDecisionSearch);
    }

    @Override
    public SimpleValidationDecision read(String idValidationDecision) throws JadePersistenceException,
            DecisionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SimpleValidationDecisionSearch search(SimpleValidationDecisionSearch validationDecisionSearch)
            throws JadePersistenceException, DecisionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SimpleValidationDecision update(SimpleValidationDecision decisionValidation)
            throws JadePersistenceException, DecisionException {
        // TODO Auto-generated method stub
        return null;
    }

}
