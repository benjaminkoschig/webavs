package ch.globaz.pegasus.process.adaptationPrimeAssuranceMaladie.executeCalcule;

import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepHtmlCutomable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.process.adaptation.PCAdaptationUtils;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;

import java.util.Map;

public class PCProcessImportationPrimeAssuranceMaladieStep implements JadeProcessStepInterface, JadeProcessStepHtmlCutomable,
        JadeProcessStepBeforable, JadeProcessStepInfoCurrentStep {

    private DonneesHorsDroitsProvider containerGlobal = null;
    private JadeProcessExecut infoProcess;

    @Override
    public void before(JadeProcessStep arg0, Map<Enum<?>, String> arg1) throws JadeApplicationException,
            JadePersistenceException {
        loadContainerGlobalCalcul();
    }

    public void checker(JadeProcessStep step, Map<Enum<?>, String> map) {
        String date = map.get(PCProcessAdapationEnum.DATE_ADAPTATION);
        if (JadeStringUtil.isEmpty(date)) {
            JadeThread.logError("", "pegasus.process.adaptation.dateAdaptation.mandatory");
        }
    }

    @Override
    public String customHtml(JadeProcessStep step, Map<Enum<?>, String> map) {
        StringBuilder action = new StringBuilder();
        if (step.getKeyStep().equals(infoProcess.getCurrentStep().getKeyStep())) {
            action.append(PCAdaptationUtils.createHtmlForButtonList(step));
        }
        return action.toString();
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessImportationPrimeAssuranceMaladieEntityHandler(containerGlobal);
    }

    private void loadContainerGlobalCalcul() throws JadePersistenceException, CalculException,
            JadeApplicationServiceNotAvailableException, MonnaieEtrangereException, PmtMensuelException {

        if (containerGlobal == null) {
            containerGlobal = DonneesHorsDroitsProvider.getInstance();
            containerGlobal.init();
        }
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut infoProcess) {
        this.infoProcess = infoProcess;
    }
}
