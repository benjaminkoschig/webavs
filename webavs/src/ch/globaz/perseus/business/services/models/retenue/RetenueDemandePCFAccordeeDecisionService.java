package ch.globaz.perseus.business.services.models.retenue;

import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.JadeApplicationService;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueDemandePCFAccordeeDecisionException;
import ch.globaz.perseus.business.models.retenue.RetenueDemandePCFAccordeeDecisionSearchModel;

public interface RetenueDemandePCFAccordeeDecisionService extends JadeApplicationService {

    /**
     * Permet de chercher des RetenueDemandePCFAccordeeDecision selon un modèle de critères.
     * 
     * @param searchModel
     *            Le modèle de critères
     * @return Le modèle de critère avec les résultats
     * @throws JadePersistenceException
     *             Levée en cas de problème dans la couche de persistence
     * @throws RetenueDemandePCFAccordeeDecisionException
     *             Levée en cas de problème métier dans l'exécution du service
     */
    public RetenueDemandePCFAccordeeDecisionSearchModel search(RetenueDemandePCFAccordeeDecisionSearchModel searchModel)
            throws JadePersistenceException, RetenueDemandePCFAccordeeDecisionException;

}
