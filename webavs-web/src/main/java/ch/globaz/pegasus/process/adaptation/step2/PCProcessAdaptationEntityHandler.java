package ch.globaz.pegasus.process.adaptation.step2;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityNeedProperties;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.models.decision.SimpleDecisionHeader;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PCProcessAdapationEnum>, JadeProcessEntityNeedProperties {

    private String id = null;
    private Map<PCProcessAdapationEnum, String> map = null;
    private Map<Enum<?>, String> properties = null;

    public PCProcessAdaptationEntityHandler() {
        map = new HashMap<PCProcessAdapationEnum, String>();
    }

    @Override
    public Map<PCProcessAdapationEnum, String> getValueToSave() {
        return map;
    }

    @Override
    public void run() {
        try {
            List<SimpleDecisionHeader> decisions = PegasusServiceLocator.getDecisionAdaptationService()
                    .preparerValiderDecision(id, properties.get(PCProcessAdapationEnum.DATE_DOCUMENT_IMPRESSION));
            map.put(PCProcessAdapationEnum.ID_DECISION_HEADER, decisions.get(0).getIdDecisionHeader());
            map.put(PCProcessAdapationEnum.ID_TIERS_AYANT_DROIT, decisions.get(0).getIdTiersBeneficiaire());
            if (decisions.size() > 1) {
                map.put(PCProcessAdapationEnum.ID_DECISION_HEADER_CONJOINT, decisions.get(1).getIdDecisionHeader());
                map.put(PCProcessAdapationEnum.ID_TIERS_CONJOINT, decisions.get(1).getIdTiersBeneficiaire());
            }

        } catch (JadeApplicationException e) {
            JadeProcessCommonUtils.addError(e);
        } catch (JadePersistenceException e) {
            JadeProcessCommonUtils.addError(e);
        }

    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        id = entity.getIdRef();
    }

    @Override
    public void setProperties(Map<Enum<?>, String> map) {
        properties = map;
    }

}
