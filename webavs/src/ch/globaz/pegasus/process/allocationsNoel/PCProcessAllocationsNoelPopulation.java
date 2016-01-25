package ch.globaz.pegasus.process.allocationsNoel;

import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.annotation.BusinessKey;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationInterface;
import ch.globaz.jade.process.business.interfaceProcess.population.JadeProcessPopulationNeedProperties;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCPCAccordee;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulation;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulationSearch;

public class PCProcessAllocationsNoelPopulation implements JadeProcessPopulationInterface,
        JadeProcessPopulationNeedProperties<PCProcessAllocationsNoelEnum> {

    private List<JadeProcessEntity> entites = null;
    private Map<PCProcessAllocationsNoelEnum, String> properties = null;

    public PCProcessAllocationsNoelPopulation() {
        entites = new ArrayList<JadeProcessEntity>();
    }

    private void createList(JadeAbstractSearchModel searchModel) {

        for (JadeAbstractModel model : searchModel.getSearchResults()) {
            PCAccordeePopulation accordeePopulation = (PCAccordeePopulation) model;
            JadeProcessEntity entite = new JadeProcessEntity();
            entite.setIdRef(model.getId());
            entite.setDescription(accordeePopulation.getPersonneEtendue().getTiers().getDesignation1() + " "
                    + accordeePopulation.getPersonneEtendue().getTiers().getDesignation2() + " "
                    + accordeePopulation.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel());
            entites.add(entite);
        }
    }

    @Override
    @BusinessKey(unique = true, messageKey = "Il ne peut créer qu'un process par année ")
    public String getBusinessKey() {
        return properties.get(PCProcessAllocationsNoelEnum.ANNEE_ALLOCATION_NOEL);
    }

    @Override
    public Class<PCProcessAllocationsNoelEnum> getEnumForProperties() {
        return PCProcessAllocationsNoelEnum.class;
    }

    @Override
    public String getParametersForUrl(JadeProcessEntity entity) throws JadePersistenceException,
            JadeApplicationException {
        return "idPca=" + entity.getIdRef();
    }

    @Override
    public List<JadeProcessEntity> searchPopulation() throws JadePersistenceException, JadeApplicationException {

        PCAccordeePopulationSearch search = new PCAccordeePopulationSearch();
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        // search.setDefinedSearchSize(1);
        search.setForCsEtat(IPCPCAccordee.CS_ETAT_PCA_VALIDE);
        search.setForDateFin("12." + properties.get(PCProcessAllocationsNoelEnum.ANNEE_ALLOCATION_NOEL));
        search.setForDateDebut("12." + properties.get(PCProcessAllocationsNoelEnum.ANNEE_ALLOCATION_NOEL));
        // recherche pour le plan de calcul NON en refus
        search.setForCsEtatPlanCalcul(IPCValeursPlanCalcul.STATUS_REFUS);
        search.setIsPCAccordeeSupprime("2");
        search.setForCsEtatVersionDroit(IPCDroits.CS_VALIDE);
        search.setForCsEtatDemande(IPCDemandes.CS_OCTROYE);
        search = (PCAccordeePopulationSearch) JadePersistenceManager.search(search);
        createList(search);
        return entites;

    }

    @Override
    public void setProperties(Map<PCProcessAllocationsNoelEnum, String> hashMap) {
        properties = hashMap;
    }

}
