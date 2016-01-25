package ch.globaz.pegasus.process.adapationHome.validerDecision;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationHomeStep implements JadeProcessStepInterface {

    public void checker(JadeProcessStep step, Map<Enum<?>, String> map) {
        String date = map.get(PCProcessAdapationEnum.DATE_ADAPTATION);
        if (JadeStringUtil.isEmpty(date)) {
            JadeThread.logError("", "pegasus.process.adaptation.dateAdaptation.mandatory");
        }
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationHomeEntityHandler();
    }

}
