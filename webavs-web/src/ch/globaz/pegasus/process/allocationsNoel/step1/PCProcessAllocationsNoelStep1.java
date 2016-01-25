package ch.globaz.pegasus.process.allocationsNoel.step1;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.context.exception.JadeNoBusinessLogSessionError;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.prestation.enums.codeprestation.PRCodePrestationPC;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepHtmlCutomable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInfoCurrentStep;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.jade.process.business.models.entity.SimpleEntity;
import ch.globaz.jade.process.businessimpl.models.JadeProcessExecut;
import ch.globaz.pegasus.business.constantes.IPCVariableMetier;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.models.process.allocationsNoel.CoupleSepareParLaMaladie;
import ch.globaz.pegasus.business.models.process.allocationsNoel.CoupleSepareParLaMaladieSearch;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulation;
import ch.globaz.pegasus.business.models.process.allocationsNoel.PCAccordeePopulationSearch;
import ch.globaz.pegasus.business.models.variablemetier.VariableMetier;
import ch.globaz.pegasus.business.models.variablemetier.VariableMetierSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.process.allocationsNoel.PCProcessAllocationsNoelEnum;

public class PCProcessAllocationsNoelStep1 implements JadeProcessStepInterface, JadeProcessStepBeforable,
        JadeProcessStepHtmlCutomable, JadeProcessStepInfoCurrentStep {

    private String anneeAllocation = null;
    private List<String> idsPca = null;
    private List<String> listCodePrestAllocationNoel = Collections.synchronizedList(new ArrayList<String>());
    private Map<String, SimpleAllocationNoel> mapAllocationNoelForDelete;
    private Map<String, Boolean> mapIsCoupleSeparer;
    private Map<String, PCAccordeePopulation> mapPca;
    private float montantAllocationNoel = 0f;

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        anneeAllocation = map.get(PCProcessAllocationsNoelEnum.ANNEE_ALLOCATION_NOEL);
        mapPca = findPcasAndGroupById();
        mapAllocationNoelForDelete = findAllocationNoelToDelete();
        montantAllocationNoel = findValeurMontantAllocation();

        mapIsCoupleSeparer = createMapSeparerParLaMaladie();
        for (PRCodePrestationPC code : PRCodePrestationPC.getCodePrestationAllocationNoel()) {
            listCodePrestAllocationNoel.add(code.getCodePrestationAsString());
        }
    }

    public void checker(JadeProcessStep step, Map<Enum<?>, String> map) {
        String date = map.get(PCProcessAllocationsNoelEnum.ANNEE_ALLOCATION_NOEL);
        if (JadeStringUtil.isEmpty(date)) {
            JadeThread.logError("", "pegasus.process.adaptation.dateAllocationNoel.mandatory");
        }
    }

    private Map<String, Boolean> createMapSeparerParLaMaladie() throws JadePersistenceException,
            JadeApplicationException {
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        Map<String, Integer> mapTemp = new HashMap<String, Integer>();

        List<String> idsVersionDroit = new ArrayList<String>();
        for (Entry<String, PCAccordeePopulation> entry : mapPca.entrySet()) {
            String key = entry.getValue().getSimplePCAccordee().getIdVersionDroit();
            if (!idsVersionDroit.contains(key)) {
                idsVersionDroit.add(key);
                mapTemp.put(key, 1);
            } else {
                mapTemp.put(key, mapTemp.get(key) + 1);
            }
            if (mapTemp.get(key) > 2) {
                throw new AllocationDeNoelException("More than tow PCAs found with this idDemande:" + key);
            }
        }
        List<CoupleSepareParLaMaladie> list = findCoupleSepareParLaMaladie(idsVersionDroit);

        for (CoupleSepareParLaMaladie coupleSepareParLaMladie : list) {
            String key = coupleSepareParLaMladie.getIdDemande();
            map.put(key, true);
        }
        return map;
    }

    @Override
    public String customHtml(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        String action = "";
        action = " <a data-g-download='docType:xls,parametres:" + step.getIdExecutionProcess() + ","
                + "serviceClassName:ch.globaz.pegasus.business.services.doc.excel.ListeDeControleService,"
                + "serviceMethodName:createListeControleAllocationNoel,docName:listeDeControle'>&nbsp;</a>";
        return action;
    }

    private Map<String, SimpleAllocationNoel> findAllocationNoelToDelete() throws JadePersistenceException,
            JadeApplicationException {
        List<SimpleAllocationNoel> pcas = PersistenceUtil.searchByLot(idsPca,
                new PersistenceUtil.SearchLotExecutor<SimpleAllocationNoel>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

                        search.setInIdsPcAccordee(ids);
                        if (ids.size() > 0) {
                            search = PegasusServiceLocator.getSimpleAllocationDeNoelService().search(search);
                        }
                        return search;
                    }
                }, 2000);
        Map<String, SimpleAllocationNoel> map = new ConcurrentHashMap<String, SimpleAllocationNoel>(pcas.size());
        for (SimpleAllocationNoel allocationNoel : pcas) {
            map.put(allocationNoel.getIdPCAccordee(), allocationNoel);
        }
        return map;
    }

    private List<CoupleSepareParLaMaladie> findCoupleSepareParLaMaladie(List<String> idsVersionDroit)
            throws JadePersistenceException, JadeApplicationException {

        List<CoupleSepareParLaMaladie> list = PersistenceUtil.searchByLot(idsVersionDroit,
                new PersistenceUtil.SearchLotExecutor<CoupleSepareParLaMaladie>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        CoupleSepareParLaMaladieSearch search = new CoupleSepareParLaMaladieSearch();
                        search.setInIdVersionDroit(ids);
                        search.setForDateFin("12." + anneeAllocation);
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        if (ids.size() > 0) {
                            return JadePersistenceManager.search(search);
                        } else {
                            return search;
                        }
                    }
                }, 2000);
        return list;
    }

    private Map<String, PCAccordeePopulation> findPcasAndGroupById() throws JadePersistenceException,
            JadeApplicationException {
        List<PCAccordeePopulation> pcas = PersistenceUtil.searchByLot(idsPca,
                new PersistenceUtil.SearchLotExecutor<PCAccordeePopulation>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        PCAccordeePopulationSearch search = new PCAccordeePopulationSearch();
                        search.setForInIdsPca(ids);
                        search.setForDateFin("12." + anneeAllocation);
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        return JadePersistenceManager.search(search);
                    }
                }, 2000);
        Map<String, PCAccordeePopulation> map = new ConcurrentHashMap<String, PCAccordeePopulation>(pcas.size());
        for (PCAccordeePopulation pca : pcas) {
            map.put(pca.getId(), pca);
        }
        return map;
    }

    private float findValeurMontantAllocation() throws JadePersistenceException, VariableMetierException,
            JadeApplicationServiceNotAvailableException, JadeNoBusinessLogSessionError {
        VariableMetierSearch vmSearch = new VariableMetierSearch();
        vmSearch.setForCsTypeVariableMetier(IPCVariableMetier.CS_ALLOCATION_NOEL);
        vmSearch.setWhereKey("withDateValable");
        vmSearch.setForLangue(BSessionUtil.getSessionFromThreadContext().getIdLangue());
        vmSearch = PegasusServiceLocator.getVariableMetierService().search(vmSearch);
        if (vmSearch.getSize() < 1) {
            JadeThread.logError("", "Aucune variable métier trouvée");
        } else if (vmSearch.getSize() == 1) {
            VariableMetier var = (((VariableMetier) vmSearch.getSearchResults()[0]));
            return Float.valueOf(var.getSimpleVariableMetier().getMontant());
        } else {
            JadeThread.logError("", "Plusieurs variables métier trouvées");
        }
        return 0;
    }

    /**
     * Retourne le handler servant à manipuler les entités
     */
    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAllocationsNoelEntityHandler(montantAllocationNoel, anneeAllocation, mapPca,
                mapAllocationNoelForDelete, listCodePrestAllocationNoel, mapIsCoupleSeparer);
    }

    @Override
    public void setInfosCurrentStep(JadeProcessExecut jadeInfo) {
        List<String> ids = new ArrayList<String>();
        if (jadeInfo.getSimpleEntiteSearch() != null) {
            for (JadeAbstractModel model : jadeInfo.getSimpleEntiteSearch().getSearchResults()) {
                ids.add(((SimpleEntity) model).getIdRef());
            }
        }
        idsPca = ids;
    }
}
