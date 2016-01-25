package ch.globaz.perseus.businessimpl.services.models.decision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import ch.globaz.perseus.business.exceptions.models.decision.DecisionException;
import ch.globaz.perseus.business.models.decision.AnnexeDecision;
import ch.globaz.perseus.business.models.decision.AnnexeDecisionSearchModel;
import ch.globaz.perseus.business.models.decision.SimpleAnnexeDecision;
import ch.globaz.perseus.business.models.decision.SimpleAnnexeDecisionSearchModel;
import ch.globaz.perseus.business.services.models.decision.AnnexeDecisionService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;

public class AnnexeDecisionServiceImpl extends PerseusAbstractServiceImpl implements AnnexeDecisionService {

    @Override
    public int count(AnnexeDecisionSearchModel search) throws DecisionException, JadePersistenceException {
        if (search == null) {
            throw new DecisionException("Unable to count AnnexeDecision, the search model passed is null!");
        }
        return JadePersistenceManager.count(search);
    }

    @Override
    public AnnexeDecision create(AnnexeDecision annexeDecision) throws JadePersistenceException, DecisionException {
        if (annexeDecision == null) {
            throw new DecisionException("Unable to create AnnexeDecision, the given model is null!");
        }

        try {
            SimpleAnnexeDecision simpleAnnexeDecision = annexeDecision.getSimpleAnnexeDecision();
            simpleAnnexeDecision = PerseusImplServiceLocator.getSimpleAnnexeDecisionService().create(
                    simpleAnnexeDecision);
            annexeDecision.setSimpleAnnexeDecision(simpleAnnexeDecision);

        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return annexeDecision;
    }

    @Override
    public AnnexeDecision delete(AnnexeDecision annexeDecision) throws JadePersistenceException, DecisionException {
        if (annexeDecision == null) {
            throw new DecisionException("Unable to delete a AnnexeDecision, the searchModel passed is null!");
        }

        try {
            annexeDecision.setSimpleAnnexeDecision(PerseusImplServiceLocator.getSimpleAnnexeDecisionService().delete(
                    annexeDecision.getSimpleAnnexeDecision()));
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return annexeDecision;
    }

    @Override
    public int delete(SimpleAnnexeDecisionSearchModel searchModel) throws JadePersistenceException, DecisionException {
        if (searchModel == null) {
            throw new DecisionException(
                    "Unable to delete a AnnexeDecisionSearchModel entities, the model passed is null!");
        }
        return JadePersistenceManager.delete(searchModel);
    }

    @Override
    public void deleteByLots(JadeAbstractModel[] lotAnnexes) throws DecisionException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        if ((lotAnnexes == null) || (lotAnnexes.length == 0)) {
            throw new DecisionException("Unable to delete lot annexe, the list is null or empty!");
        }
        // Suppression des annexes du lot
        for (JadeAbstractModel annexe : lotAnnexes) {
            ((AnnexeDecision) annexe).setSimpleAnnexeDecision(PerseusImplServiceLocator
                    .getSimpleAnnexeDecisionService().delete(((AnnexeDecision) annexe).getSimpleAnnexeDecision()));
        }
    }

    @Override
    public AnnexeDecision read(String idAnnexeDecision) throws JadePersistenceException, DecisionException {
        if (JadeStringUtil.isEmpty(idAnnexeDecision)) {
            throw new DecisionException("Unable to read a AnnexeDecision, the id passed is null!");
        }
        AnnexeDecision annexeDecision = new AnnexeDecision();
        annexeDecision.setId(idAnnexeDecision);

        return (AnnexeDecision) JadePersistenceManager.read(annexeDecision);
    }

    @Override
    public AnnexeDecisionSearchModel search(AnnexeDecisionSearchModel searchModel) throws JadePersistenceException,
            DecisionException {
        if (searchModel == null) {
            throw new DecisionException("Unable to search a AnnexeDecision, the search model passed is null!");
        }

        return (AnnexeDecisionSearchModel) JadePersistenceManager.search(searchModel);
    }

    @Override
    public AnnexeDecision update(AnnexeDecision annexeDecision) throws JadePersistenceException, DecisionException {
        if (annexeDecision == null) {
            throw new DecisionException("Unable to update AnnexeDecision, the given model is null!");
        }

        try {
            SimpleAnnexeDecision simpleAnnexeDecision = annexeDecision.getSimpleAnnexeDecision();
            simpleAnnexeDecision = PerseusImplServiceLocator.getSimpleAnnexeDecisionService().update(
                    simpleAnnexeDecision);
            annexeDecision.setSimpleAnnexeDecision(simpleAnnexeDecision);
        } catch (JadeApplicationServiceNotAvailableException e) {
            throw new DecisionException("Service not available - " + e.getMessage());
        }

        return annexeDecision;
    }

}
