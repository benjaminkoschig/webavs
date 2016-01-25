package ch.globaz.pegasus.process.adaptation.step2;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.pegasus.utils.ChrysaorUtil;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepHtmlCutomable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.business.models.externalmodule.ExternalJobActionSource;
import ch.globaz.pegasus.business.models.externalmodule.jsonparameters.AdaptationParameter;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCAdaptationUtils;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepHtmlCutomable,
        JadeProcessStepInfoCurrentStep, JadeProcessStepAfterable {

    public static final String KEY_STEP = "6";
    private JadeProcessExecut infoProcess;

    @Override
    public String customHtml(JadeProcessStep step, Map<Enum<?>, String> map) {
        String action = "";
        if (step.getKeyStep().equals(infoProcess.getCurrentStep().getKeyStep())) {
            action = PCAdaptationUtils.createHtmlForButtonList(step);
        }

        return action;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationEntityHandler();
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut infoProcess) {
        this.infoProcess = infoProcess;
    }

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        if (ChrysaorUtil.isChrysaorEnabled()) {
            try {
                PegasusServiceLocator.getChrysaorService().sendJobFor(ExternalJobActionSource.ADAPTATION,
                        new AdaptationParameter(infoProcess.getSimpleExecutionProcess().getIdExecutionProcess()));
            } catch (Exception e) {
                JadeLogger.info(this,
                        " [Chrysaor] The job submitting for chrysaor throw an exception: " + e.getMessage());
            }
        }

    }

}
