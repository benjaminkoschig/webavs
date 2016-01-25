package ch.globaz.perseus.business.services.models.retenue;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueDemandePCFAccordeeDecisionException;
import ch.globaz.perseus.business.models.retenue.RetenueDemandePCFAccordeeDecisionSearchModel;

public interface RetenueDemandePCFAccordeeDecisionService extends JadeApplicationService {

    /**
     * Permet de chercher des RetenueDemandePCFAccordeeDecision selon un mod�le de crit�res.
     * 
     * @param searchModel
     *            Le mod�le de crit�res
     * @return Le mod�le de crit�re avec les r�sultats
     * @throws JadePersistenceException
     *             Lev�e en cas de probl�me dans la couche de persistence
     * @throws RetenueDemandePCFAccordeeDecisionException
     *             Lev�e en cas de probl�me m�tier dans l'ex�cution du service
     */
    public RetenueDemandePCFAccordeeDecisionSearchModel search(RetenueDemandePCFAccordeeDecisionSearchModel searchModel)
            throws JadePersistenceException, RetenueDemandePCFAccordeeDecisionException;

}
