package ch.globaz.perseus.process.traitementAnnuel.impressionDesDecisions;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityDataFind;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.process.traitementAnnuel.PFProcessTraitementAnnuelEnum;

public class PFProcessTraitementAnnuelImpressionEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityDataFind<PFProcessTraitementAnnuelEnum> {

    private Map<PFProcessTraitementAnnuelEnum, String> mapSavedValue = new HashMap<PFProcessTraitementAnnuelEnum, String>();
    private List<Decision> listDecisions;

    public PFProcessTraitementAnnuelImpressionEntityHandler(List<Decision> listDecisions) {
        this.listDecisions = listDecisions;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        if (mapSavedValue.containsKey(PFProcessTraitementAnnuelEnum.DECISION_IMPRIMER)) {
            Decision decision;
            decision = PerseusServiceLocator.getDecisionService().read(
                    mapSavedValue.get(PFProcessTraitementAnnuelEnum.DECISION_IMPRIMER));
            listDecisions.add(decision);
        }

    }

    /**
     * Chargement des valeurs sauvegardés lors de la génération de la population
     */
    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {

    }

    /**
     * Chargement des valeurs sauvegardés lors du dernier step.
     */
    @Override
    public void setData(Map<PFProcessTraitementAnnuelEnum, String> hashMap) {
        mapSavedValue = hashMap;
    }

}
