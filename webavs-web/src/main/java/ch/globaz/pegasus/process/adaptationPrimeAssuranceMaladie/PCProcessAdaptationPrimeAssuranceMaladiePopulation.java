package ch.globaz.pegasus.process.adaptationPrimeAssuranceMaladie;

import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.models.demande.Demande;
import ch.globaz.pegasus.business.models.demande.DemandeSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;

import java.util.*;

public class PCProcessAdaptationPrimeAssuranceMaladiePopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<PCProcessAdapationEnum> {

    private Map<PCProcessAdapationEnum, String> properties = null;

    public PCProcessAdaptationPrimeAssuranceMaladiePopulation() {

    }

    public void checker(Map<Enum<?>, String> map) {
        String date = map.get(PCProcessAdapationEnum.DATE_ADAPTATION);
        if (JadeStringUtil.isEmpty(date)) {
            JadeThread.logError("", "pegasus.process.adaptation.dateAdaptation.mandatory");
        }
    }

    private List<JadeProcessEntity> createArray(DemandeSearch search) {
        List<JadeProcessEntity> entites = new ArrayList<JadeProcessEntity>();

        for (JadeAbstractModel model : search.getSearchResults()) {
            Demande demande = (Demande) model;
            JadeProcessEntity entite = new JadeProcessEntity();
            entite.setIdRef(model.getId());
            entite.setDescription(demande.getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                    .getNumAvsActuel()
                    + " "
                    + demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation1()
                    + " "
                    + demande.getDossier().getDemandePrestation().getPersonneEtendue().getTiers().getDesignation2());
            entite.setValue1(demande.getSimpleDemande().getIdGestionnaire());
            entites.add(entite);
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

        DemandeSearch search = new DemandeSearch();
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setOrderBy("nomPrenom");
        search.setForCsEtatDemande(IPCDemandes.CS_OCTROYE);
        search.setForNotCsEtatDemande(IPCDemandes.CS_ANNULE);
        search.setWhereKey(DemandeSearch.WITH_DEMANDE_DATE_FIN_NULL);
        PegasusServiceLocator.getDemandeService().search(search);
        return createArray(search);

    }

    @Override
    public void setProperties(Map<PCProcessAdapationEnum, String> hashMap) {
        properties = hashMap;
    }

}
