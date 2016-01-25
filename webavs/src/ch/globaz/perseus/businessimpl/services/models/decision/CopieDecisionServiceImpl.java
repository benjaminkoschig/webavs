package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.CopieDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.SimpleCopieDecision;
import ch.globaz.perseus.business.models.decision.SimpleCopieDecisionSearchModel;
import ch.globaz.perseus.business.services.models.decision.CopieDecisionService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class CopieDecisionServiceImpl extends PerseusAbstractServiceImpl implements CopieDecisionService {

    @Override
    public int count(CopieDecisionSearchModel search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count CopieDecision, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public CopieDecision create(CopieDecision copieDecision) throws JadePersistenceException, DecisionException {
        if (copieDecision == null) {
            throw new DecisionException("Unable to create CopieDecision, the given model is null!");
        }

        try {
            SimpleCopieDecision simpleCopieDecision = copieDecision.getSimpleCopieDecision();
            simpleCopieDecision = PerseusImplServiceLocator.getSimpleCopieDecisionService().create(simpleCopieDecision);
            copieDecision.setSimpleCopieDecision(simpleCopieDecision);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return copieDecision;
    }

    @Override
    public CopieDecision delete(CopieDecision copieDecision) throws JadePersistenceException, DecisionException {
        if (copieDecision == null) {
            throw new DecisionException("Unable to delete a CopieDecision, the model passed is null!");
        }

        try {
            copieDecision.setSimpleCopieDecision(PerseusImplServiceLocator.getSimpleCopieDecisionService().delete(
                    copieDecision.getSimpleCopieDecision()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return copieDecision;
    }

    @Override
    public int delete(SimpleCopieDecisionSearchModel searchModel) throws JadePersistenceException, DecisionException {
        if (searchModel == null) {
            throw new DecisionException("Unable to delete a CopieDecision, the searchModel passed is null!");
        }
        return JadePersistenceManager.delete(searchModel);
    }

    @Override
    public void deleteByLots(JadeAbstractModel[] lotCopies) throws JadePersistenceException, DecisionException {
        if ((lotCopies == null) || (lotCopies.length == 0)) {
            throw new DecisionException("Unable to delete lot copies, the list is null or empty!");
        }
        // Suppression des annexes du lot
        for (JadeAbstractModel copie : lotCopies) {
            SimpleCopieDecision copiesDecision = ((CopieDecision) copie).getSimpleCopieDecision();
            JadePersistenceManager.delete(copiesDecision);
        }

    }

    @Override
    public CopieDecision read(String idCopieDecision) throws JadePersistenceException, DecisionException {
        if (JadeStringUtil.isEmpty(idCopieDecision)) {
            throw new DecisionException("Unable to read a CopieDecision, the id passed is null!");
        }
        CopieDecision copieDecision = new CopieDecision();
        copieDecision.setId(idCopieDecision);

        return (CopieDecision) JadePersistenceManager.read(copieDecision);
    }

    @Override
    public CopieDecisionSearchModel search(CopieDecisionSearchModel searchModel) throws JadePersistenceException,
            DecisionException {
        if (searchModel == null) {
            throw new DecisionException("Unable to search a CopieDecision, the search model passed is null!");
        }

        return (CopieDecisionSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public CopieDecision update(CopieDecision copieDecision) throws JadePersistenceException, DecisionException {
        if (copieDecision == null) {
            throw new DecisionException("Unable to update CopieDecision, the given model is null!");
        }

        try {
            SimpleCopieDecision simpleCopieDecision = copieDecision.getSimpleCopieDecision();
            simpleCopieDecision = PerseusImplServiceLocator.getSimpleCopieDecisionService().update(simpleCopieDecision);
            copieDecision.setSimpleCopieDecision(simpleCopieDecision);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return copieDecision;
    }

}
