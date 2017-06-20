package ch.globaz.pegasus.process.adapationHome.executeCalcule;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepHtmlCutomable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationHomeStep implements JadeProcessStepInterface, JadeProcessStepHtmlCutomable,
        JadeProcessStepBeforable {

    private DonneesHorsDroitsProvider containerGlobal = null;

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
        String action = "";
        // if (JadeStepStateEnum.VALIDATE.equals(step.getCsState())) {
        action = " <a data-g-download='docType:xls,parametres:" + step.getIdExecutionProcess() + ","
                + "serviceClassName:ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService,"
                + "serviceMethodName:createListeControleProcessAdaptation,docName:listeDeControle'>&nbsp;</a>";
        // }
        return action;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationHomeEntityHandler(containerGlobal);
    }

    private void loadContainerGlobalCalcul() throws JadePersistenceException, CalculException,
            JadeApplicationServiceNotAvailableException, MonnaieEtrangereException, PmtMensuelException {

        if (containerGlobal == null) {
            containerGlobal = DonneesHorsDroitsProvider.getInstance();
            containerGlobal.init();
        }
    }
}
