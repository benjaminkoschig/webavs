package ch.globaz.perseus.process.traitementAdaptation.impressionDesDecisions;

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
import ch.globaz.perseus.process.traitementAdaptation.PFProcessTraitementAdaptationEnum;

public class PFProcessTraitementAdaptationImpressionEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityDataFind<PFProcessTraitementAdaptationEnum> {

    private Map<PFProcessTraitementAdaptationEnum, String> mapSavedValue = new HashMap<PFProcessTraitementAdaptationEnum, String>();
    private List<Decision> listDecisions;

    public PFProcessTraitementAdaptationImpressionEntityHandler(List<Decision> listDecisions) {
        this.listDecisions = listDecisions;
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        if (mapSavedValue.containsKey(PFProcessTraitementAdaptationEnum.DECISION_IMPRIMER)) {
            Decision decision;
            decision = PerseusServiceLocator.getDecisionService().read(
                    mapSavedValue.get(PFProcessTraitementAdaptationEnum.DECISION_IMPRIMER));
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
    public void setData(Map<PFProcessTraitementAdaptationEnum, String> hashMap) {
        mapSavedValue = hashMap;
    }

}
