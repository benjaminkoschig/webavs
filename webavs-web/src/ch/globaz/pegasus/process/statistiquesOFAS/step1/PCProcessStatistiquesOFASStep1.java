package ch.globaz.pegasus.process.statistiquesOFAS.step1;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.enumProcess.JadeProcessEntityStateEnum;
import ch.globaz.jade.process.business.enumProcess.JadeProcessStepStateEnum;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepAfterable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepHtmlCutomable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.business.models.entity.SimpleEntitySearch;
import ch.globaz.jade.process.utils.JadeProcessCommonUtils;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.services.process.statistiquesofas.StatistiquesOFASService;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;

public class PCProcessStatistiquesOFASStep1 implements JadeProcessStepInterface, JadeProcessStepHtmlCutomable,
        JadeProcessStepBeforable, JadeProcessStepAfterable {

    private DonneesHorsDroitsProvider containerGlobal = null;
    private final List<Integer> listCheck = new ArrayList<Integer>();

    @Override
    public void after(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        SimpleEntitySearch searchWarning = new SimpleEntitySearch();
        searchWarning.setForCsEtat(JadeProcessEntityStateEnum.WARNING);
        searchWarning.setForIdExecuteProcess(step.getIdExecutionProcess());

        // int countWarning = JadeProcessServiceLocator.getSimpleEntiteService().count(searchWarning);
        // float pourcent = (countWarning / Integer.valueOf(step.getNbEntite())) * 100;
        int nbEntities = (Integer.valueOf(step.getNbEntite()));

        float pourcent = new BigDecimal(listCheck.size())
                .divide(new BigDecimal(nbEntities), 4, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(100))
                .floatValue();

        JadeProcessCommonUtils.addWarning("Nb cas ne passant pas le test: " + listCheck.size());
        JadeProcessCommonUtils.addWarning("Pourcentage de cas ne passant pas le test: " + pourcent);
        if ((100 - pourcent) < 98) {
            JadeProcessCommonUtils.addWarning("La statistique n'est pas validée!");
        }
    }

    @Override
    public void before(JadeProcessStep arg0, Map<Enum<?>, String> arg1) throws JadeApplicationException,
            JadePersistenceException {
        Collections.synchronizedCollection(listCheck);
        loadContainerGlobalCalcul();
    }

    @Override
    public String customHtml(JadeProcessStep step, Map<Enum<?>, String> map) {
        String action = "";
        if (!JadeProcessStepStateEnum.INIT.equals(step.getCsState())) {
            action = "<a  data-g-download='docType:txt,parametres:" + step.getIdExecutionProcess() + ","
                    + "serviceClassName:" + StatistiquesOFASService.class.getName() + ","
                    + "serviceMethodName:genereateFileStatOFAS,docName:StatistiquesOFAS'>Générer Statistiques</a>";
        }
        return action;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessStatistiqueOFASEntityHandler(containerGlobal, listCheck);

    }

    private void loadContainerGlobalCalcul() throws JadePersistenceException, CalculException,
            JadeApplicationServiceNotAvailableException, MonnaieEtrangereException {

        if (containerGlobal == null) {
            containerGlobal = DonneesHorsDroitsProvider.getInstance();
            containerGlobal.init();
        }
    }

}
