package ch.globaz.perseus.businessimpl.services.models.retenue;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueDemandePCFAccordeeDecisionException;
import ch.globaz.perseus.business.models.retenue.RetenueDemandePCFAccordeeDecisionSearchModel;
import ch.globaz.perseus.business.services.models.retenue.RetenueDemandePCFAccordeeDecisionService;
import ch.globaz.perseus.businessimpl.services.PerseusAbstractServiceImpl;

public class RetenueDemandePCFAccordeeDecisionServiceImpl extends PerseusAbstractServiceImpl implements
        RetenueDemandePCFAccordeeDecisionService {

    @Override
    public RetenueDemandePCFAccordeeDecisionSearchModel search(RetenueDemandePCFAccordeeDecisionSearchModel searchModel)
            throws JadePersistenceException, RetenueDemandePCFAccordeeDecisionException {

        if (searchModel == null) {
            throw new RetenueDemandePCFAccordeeDecisionException(
                    "Impossible de chercher RetenueDemandePCFAccordeeDecision, le searchmodel passé est null");
        }
        return (RetenueDemandePCFAccordeeDecisionSearchModel) JadePersistenceManager.search(searchModel);
    }

}
