package ch.globaz.pegasus.process.allocationsNoel.step1;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.corvus.business.models.rentesaccordees.SimpleInformationsComptabilite;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordees;
import ch.globaz.corvus.business.models.rentesaccordees.SimplePrestationsAccordeesSearch;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.jade.process.business.bean.JadeProcessEntity;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityPropertySavable;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.models.pcaccordee.AllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulation;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.process.allocationsNoel.PCProcessAllocationsNoelEnum;

public class PCProcessAllocationsNoelEntityHandler implements JadeProcessEntityInterface,
        JadeProcessEntityPropertySavable<PCProcessAllocationsNoelEnum> {

    public final static String CS_TYPE_COURRIER = "508001";
    public final static String CS_TYPE_DOMICILE = "508008";
    public final static String MOIS_ALLOCATION_NOEL = "12";

    private String anneeAllocation = null;
    String id = null;
    private List<String> listCodePrestAllocationNoel = new ArrayList<String>();
    private Map<PCProcessAllocationsNoelEnum, String> map = null;
    private Map<String, SimpleAllocationNoel> mapAllocationNoelForDelete;
    private Map<String, Boolean> mapIsCoupleSeparer;
    private Map<String, PCAccordeePopulation> mapPca;
    private float montantAllocation = 0;

    public PCProcessAllocationsNoelEntityHandler(float montantForfatitAllocation, String anneeAllocation,
            Map<String, PCAccordeePopulation> mapPca, Map<String, SimpleAllocationNoel> mapAllocationNoelForDelete,
            List<String> listCodePrestAllocationNoel, Map<String, Boolean> mapIsCoupleSeparer) {
        map = new HashMap<PCProcessAllocationsNoelEnum, String>();
        this.mapAllocationNoelForDelete = mapAllocationNoelForDelete;
        montantAllocation = montantForfatitAllocation;
        this.anneeAllocation = anneeAllocation;
        this.listCodePrestAllocationNoel = new ArrayList<String>();
        this.mapPca = mapPca;
        this.mapIsCoupleSeparer = mapIsCoupleSeparer;
        this.listCodePrestAllocationNoel = listCodePrestAllocationNoel;
    }

    private void deleteSimplePrestationAndSimpleInfoCompta(SimplePrestationsAccordees simplePrestation)
            throws JadePersistenceException, JadeApplicationException {
        SimpleInformationsComptabilite simpleInformationsComptabilite = CorvusServiceLocator
                .getSimpleInformationsComptabiliteService().read(simplePrestation.getIdInfoCompta());
        if ((simpleInformationsComptabilite.getId() != null) && (simpleInformationsComptabilite.getSpy() != null)) {
            CorvusServiceLocator.getSimpleInformationsComptabiliteService().delete(simpleInformationsComptabilite);
        }
        PegasusImplServiceLocator.getSimplePrestatioAccordeeService().delete(simplePrestation);
    }

    @Override
    public Map<PCProcessAllocationsNoelEnum, String> getValueToSave() {
        return map;
    }

    /**
     * Effectue la recherche et la suppression des prestation de Noël qui auraient pu éventuellement être créées lors
     * d'un précédent run
     */
    public void rollBack(SimpleAllocationNoel simpleAllocationNoel) throws JadePersistenceException,
            JadeApplicationException {
        // Suppression de la rente du tiers bénéficiaire
        if ((simpleAllocationNoel != null) && !simpleAllocationNoel.isNew()) {
            SimplePrestationsAccordeesSearch prestationsAccordeesSearch = search(simpleAllocationNoel);
            if (prestationsAccordeesSearch.getSize() == 0) {
                throw new AllocationDeNoelException("Any one prestation was founded with this idAllocationNoel: "
                        + simpleAllocationNoel.getId() + " ");
            }
            for (JadeAbstractModel abstractModel : prestationsAccordeesSearch.getSearchResults()) {
                deleteSimplePrestationAndSimpleInfoCompta((SimplePrestationsAccordees) abstractModel);
            }
            PegasusServiceLocator.getSimpleAllocationDeNoelService().delete(simpleAllocationNoel);
        }
    }

    @Override
    public void run() throws JadeApplicationException, JadePersistenceException {
        PCAccordeePopulation accordeePopulation = mapPca.get(id);
        if (accordeePopulation == null) {
            throw new AllocationDeNoelException("Impossible de récupérer la PCAccordeePopulation avec l'id : " + id);
        }

        SimpleAllocationNoel simpleAllocationNoel = mapAllocationNoelForDelete.get(id);
        Boolean isCoupleSeparer = mapIsCoupleSeparer.containsKey(accordeePopulation.getSimpleDroit().getIdDemandePC());

        String idAdressePaiementPostaleCreer = null;
        if (simpleAllocationNoel != null) {
            // on sauve l'id de l'ardresse créer car si on relance le process on perd cette info
            idAdressePaiementPostaleCreer = simpleAllocationNoel.getIdAdressePaiementPostaleCreer();
        }

        // Si besoin, suppression des prestation créées lors d'un précédent run du process
        rollBack(simpleAllocationNoel);

        // Création des prestation 'allocation de Noël'
        AllocationNoel allocationNoel = PegasusImplServiceLocator.getAllocationDeNoelService().create(
                accordeePopulation, montantAllocation, anneeAllocation, idAdressePaiementPostaleCreer, isCoupleSeparer);

    }

    private SimplePrestationsAccordeesSearch search(SimpleAllocationNoel simpleAllocationNoel)
            throws JadeApplicationServiceNotAvailableException, JadePersistenceException, JadeApplicationException {
        SimplePrestationsAccordeesSearch searchModel = new SimplePrestationsAccordeesSearch();
        List<String> ids = new ArrayList<String>();
        ids.add(simpleAllocationNoel.getIdPrestationAccordee());
        if (!JadeStringUtil.isBlankOrZero(simpleAllocationNoel.getIdPrestationAccordeeConjoint())) {
            ids.add(simpleAllocationNoel.getIdPrestationAccordeeConjoint());
        }
        if (ids.size() > 0) {
            searchModel.setForInIdPrestation(ids);
            searchModel.setWhereKey(SimplePrestationsAccordeesSearch.GROUPE_ID_PRESTATION_WHERE_KEY);
            return PegasusImplServiceLocator.getSimplePrestatioAccordeeService().find(searchModel);
        } else {
            return searchModel;
        }
    }

    @Override
    public void setCurrentEntity(JadeProcessEntity entity) {
        id = entity.getIdRef();
    }

}
