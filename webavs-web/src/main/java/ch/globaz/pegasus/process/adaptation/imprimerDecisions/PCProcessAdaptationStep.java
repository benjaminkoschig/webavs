package ch.globaz.pegasus.process.adaptation.imprimerDecisions;

import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSessionUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.common.util.CaisseInfoPropertiesWrapper;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepHtmlCutomable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.business.exceptions.models.process.AdaptationException;
import ch.globaz.pegasus.process.adaptation.PCAdaptationUtils;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepAfterable,
        JadeProcessStepInfoCurrentStep, JadeProcessStepBeforable, JadeProcessStepHtmlCutomable {

    public static final String KEY_STEP = "7";
    private List<CommunicationAdaptationElement> container = null;
    private JadeProcessExecut infoProcess;

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {

        // lancer process impression avec comme parametre l'entité
        PCImpressionAdaptationProcess process = new PCImpressionAdaptationProcess();
        process.setSession(BSessionUtil.getSessionFromThreadContext());
        process.setDateSurDocument(map.get(PCProcessAdapationEnum.DATE_DOCUMENT_IMPRESSION));
        process.setDonneesContainer(container);

        try {
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            throw new AdaptationException("Unable to start........");
        }
    }

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        container = new ArrayList<CommunicationAdaptationElement>();
    }

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
        return new PCProcessAdaptationEntityHandler(container, CaisseInfoPropertiesWrapper.getInstance());
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut infoProcess) {
        this.infoProcess = infoProcess;
    }

}
