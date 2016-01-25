package ch.globaz.pegasus.process.adaptation.step1;

import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang.StringUtils;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.enumProcess.JadeProcessStepStateEnum;
import ch.globaz.jade.process.business.exceptions.EntiteException;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepHtmlCutomable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepValidable;
import ch.globaz.jade.process.business.models.entity.SimpleEntity;
import ch.globaz.jade.process.business.models.entity.SimpleEntitySearch;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.exceptions.models.calcul.CalculException;
import ch.globaz.pegasus.business.exceptions.models.monnaieetrangere.MonnaieEtrangereException;
import ch.globaz.pegasus.business.models.droit.VersionDroit;
import ch.globaz.pegasus.business.models.droit.VersionDroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.containercalcul.DonneesHorsDroitsProvider;
import ch.globaz.pegasus.process.adaptation.PCAdaptationUtils;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepHtmlCutomable,
        JadeProcessStepBeforable, JadeProcessStepValidable {
    // private VariableMetierProvider variableMetierProvider = null;

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
        if (!JadeProcessStepStateEnum.INIT.equals(step.getCsState())
                && !JadeProcessStepStateEnum.VALIDATE.equals(step.getCsState())) {
            action = PCAdaptationUtils.createHtmlForButtonList(step);
        }
        return action;
    }

    private List<VersionDroit> findDroitCalcule(List<String> ids) throws JadePersistenceException,
            JadeApplicationException {

        List<VersionDroit> listDroit = PersistenceUtil.searchByLot(ids,
                new PersistenceUtil.SearchLotExecutor<VersionDroit>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        VersionDroitSearch search = new VersionDroitSearch();
                        search.setForCsEtatDroit(IPCDroits.CS_CALCULE);
                        search.setInIdDemandes(ids);
                        search.setForCsModif(IPCDroits.CS_MOTIF_DROIT_ADAPTATION);
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        return PegasusServiceLocator.getDroitService().searchVersionDroit(search);
                    }
                }, 2000);
        return listDroit;
    }

    public List<SimpleEntity> findEntityStep(String idCurrentStep) throws EntiteException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimpleEntitySearch search = new SimpleEntitySearch();
        search.setForIdCurrentStep(idCurrentStep);
        search.setForIsManual(false);
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search = JadeProcessServiceLocator.getSimpleEntiteService().search(search);
        return PersistenceUtil.typeSearch(search, search.whichModelClass());
    }

    public List<String> findIdsRef(List<SimpleEntity> list) throws EntiteException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        List<String> ids = new ArrayList<String>();
        for (SimpleEntity entite : list) {
            ids.add(entite.getIdRef());
        }
        return ids;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationEntityHandler(containerGlobal);
    }

    public Map<String, List<VersionDroit>> groupVersionDroitByIdDemande(List<VersionDroit> listVersionDroit) {
        Map<String, List<VersionDroit>> mapDroit = JadeListUtil.groupBy(listVersionDroit,
                new JadeListUtil.Key<VersionDroit>() {
                    @Override
                    public String exec(VersionDroit e) {
                        return e.getDemande().getSimpleDemande().getIdDemande();
                    }
                });
        return mapDroit;
    }

    private void loadContainerGlobalCalcul() throws JadePersistenceException, CalculException,
            JadeApplicationServiceNotAvailableException, MonnaieEtrangereException {
        if (containerGlobal == null) {
            containerGlobal = DonneesHorsDroitsProvider.getInstance();
            containerGlobal.init();
        }
    }

    @Override
    public void validate(JadeProcessStep step, Map<? extends Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        List<String> entiteNonTraite = new ArrayList<String>();

        List<SimpleEntity> listEntities = findEntityStep(step.getIdStep());
        List<String> idsRefEntities = findIdsRef(listEntities);

        List<VersionDroit> listDroitCacule = findDroitCalcule(idsRefEntities);
        Map<String, List<VersionDroit>> mapDroitCalcule = groupVersionDroitByIdDemande(listDroitCacule);

        for (SimpleEntity entite : listEntities) {
            if (!mapDroitCalcule.containsKey(entite.getIdRef())) {
                entiteNonTraite.add(entite.getDescription());
            }
        }

        if (entiteNonTraite.size() > 0) {
            JadeThread.logError(null, "pegasus.process.adaptation.adaptation.validation.etatVersion",
                    new String[] { StringUtils.join(entiteNonTraite, "<br />") });
        }
    }
}
