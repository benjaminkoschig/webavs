package ch.globaz.pegasus.process.statistiquesOFAS.step2;

import java.util.Map;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.business.models.step.SimpleStep;
import ch.globaz.pegasus.business.services.process.statistiquesofas.StatistiquesOFASService;

public class PCProcessStatistiquesOFASStep2 implements JadeProcessStepInterface {

    public void abort(SimpleStep step, Map<Enum<?>, String> map) {
        // TODO Auto-generated method stub
    }

    public void after(SimpleStep step, Map<Enum<?>, String> map) {
        // TODO Auto-generated method stub
    }

    public void before(SimpleStep step, Map<Enum<?>, String> map) {
        // TODO Auto-generated method stub
    }

    public void checker(SimpleStep step, Map<Enum<?>, String> map) {
        // TODO Auto-generated method stub
    }

    public String customHtml(SimpleStep step, Map<Enum<?>, String> map) {
        String action = "";
        // if (JadeStepStateEnum.VALIDATE.equals(step.getCsState())) {
        action = "<a  data-g-download='docType:xls,parametres:" + step.getIdExecutionProcess() + ","
                + "serviceClassName:" + StatistiquesOFASService.class.getName() + ","
                + "serviceMethodName:genereateFileStatOFAS,docName:StatistiquesOFAS'>Générer Statistique</a>";
        // }
        return action;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return null;
    }

    public void validate(SimpleStep step, Map<Enum<?>, String> map) {
        // TODO Auto-generated method stub
    }

}
