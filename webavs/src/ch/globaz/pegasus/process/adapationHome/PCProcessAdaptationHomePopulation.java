package ch.globaz.pegasus.process.adapationHome;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeDroit;
import ch.globaz.pegasus.business.models.habitat.TaxeJournaliereHomeDroitSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationHomePopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<PCProcessAdapationEnum> {

    private Map<PCProcessAdapationEnum, String> properties = null;

    public PCProcessAdaptationHomePopulation() {

    }

    public void checker(Map<Enum<?>, String> map) {
        String date = map.get(PCProcessAdapationEnum.DATE_ADAPTATION);
        if (JadeStringUtil.isEmpty(date)) {
            JadeThread.logError("", "pegasus.process.adaptation.dateAdaptation.mandatory");
        }
    }

    private List<JadeProcessEntity> createArray(TaxeJournaliereHomeDroitSearch search) {
        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();
        Set<String> setIdDemande = new LinkedHashSet<String>();
        for (JadeAbstractModel model : search.getSearchResults()) {
            TaxeJournaliereHomeDroit homeDroit = (TaxeJournaliereHomeDroit) model;
            JadeProcessEntity entite = new JadeProcessEntity();
            if (!setIdDemande.contains(entite.getIdRef())) {
                entite.setIdRef(homeDroit.getVersionDroit().getDemande().getId());

                entite.setDescription(homeDroit.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                        .getPersonneEtendue().getPersonneEtendue().getNumAvsActuel()
                        + " "
                        + homeDroit.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                                .getPersonneEtendue().getTiers().getDesignation1()
                        + " "
                        + homeDroit.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                                .getPersonneEtendue().getTiers().getDesignation2());
                entite.setValue1(homeDroit.getVersionDroit().getDemande().getSimpleDemande().getIdGestionnaire());
                entites.add(entite);
                setIdDemande.add(entite.getIdRef());
            }
        }
        return entites;
    }

    @Override
    public String getBusinessKey() {
        return null;
    }

    @Override
    public Class<PCProcessAdapationEnum> getEnumForProperties() {
        return PCProcessAdapationEnum.class;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        Demande demande;
        String parameters = "";

        demande = PegasusServiceLocator.getDemandeService().read(entity.getIdRef());
        parameters = "idDemandePc=" + demande.getSimpleDemande().getIdDemande() + "&idDossier="
                + demande.getDossier().getDossier().getIdDossier();

        return parameters;
    }

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {

        TaxeJournaliereHomeDroitSearch search = new TaxeJournaliereHomeDroitSearch();
        search.setForIdHome(properties.get(PCProcessAdapationEnum.ID_HOME));
        search.setForDate(properties.get(PCProcessAdapationEnum.DATE_ADAPTATION));
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setWhereKey(TaxeJournaliereHomeDroitSearch.WITH_VERSIONED);
        search = PegasusServiceLocator.getDroitService().search(search);
        return createArray(search);

    }

    @Override
    public void setProperties(Map<PCProcessAdapationEnum, String> hashMap) {
        properties = hashMap;
    }

}
