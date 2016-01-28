package ch.globaz.pegasus.process.adaptation.stepAdaptationRente;

import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import ch.globaz.jade.process.business.bean.JadeProcessStep;
import ch.globaz.jade.process.business.interfaceProcess.entity.JadeProcessEntityInterface;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepBeforable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepEnable;
import ch.globaz.jade.process.business.interfaceProcess.step.JadeProcessStepInterface;
import ch.globaz.pegasus.business.models.process.adaptation.RenteAdapationDemande;
import ch.globaz.pegasus.business.models.process.adaptation.RenteToUpdate;
import ch.globaz.pegasus.business.models.process.adaptation.RenteToUpdateSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.process.adaptation.PCProcessAdapationEnum;

public class PCProcessAdaptationStep implements JadeProcessStepInterface, JadeProcessStepEnable,
        JadeProcessStepBeforable {

    private Map<Enum<?>, String> properties = null;
    private Map<String, List<RenteAdapationDemande>> rentesDemandeCentral;
    private Map<String, RenteToUpdate> renteToUpdate;

    @Override
    public void before(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        properties = map;
        rentesDemandeCentral = PegasusServiceLocator.getRenteAdapationDemandeService().findByIdProcess(
                step.getIdExecutionProcess());
        renteToUpdate = findRenteToUpdate();
    }

    private List<String> createListIdDfh() {
        List<String> list = new ArrayList<String>();
        for (Entry<String, List<RenteAdapationDemande>> entry : rentesDemandeCentral.entrySet()) {
            for (RenteAdapationDemande rente : entry.getValue()) {
                if (!JadeStringUtil.isBlankOrZero(rente.getSimpleRenteAdaptation().getIdDonneeFinanciereHeaderOld())) {
                    list.add(rente.getSimpleRenteAdaptation().getIdDonneeFinanciereHeaderOld());
                }
            }
        }
        return list;
    }

    private Map<String, RenteToUpdate> findRenteToUpdate() throws JadePersistenceException, JadeApplicationException {
        List<String> listIdDFH = createListIdDfh();
        List<RenteToUpdate> list = PersistenceUtil.searchByLot(listIdDFH,
                new PersistenceUtil.SearchLotExecutor<RenteToUpdate>() {
                    @Override
                    public JadeAbstractSearchModel execute(List<String> ids) throws JadeApplicationException,
                            JadePersistenceException {
                        RenteToUpdateSearch search = new RenteToUpdateSearch();
                        search.setInIdDonneeFinanciere(ids);
                        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
                        return JadePersistenceManager.search(search);
                    }
                }, 3000);

        Map<String, RenteToUpdate> mapRente = new HashMap<String, RenteToUpdate>();
        for (final RenteToUpdate rente : list) {
            String key = rente.getSimpleDonneeFinanciereHeader().getId();
            mapRente.put(key, rente);

        }

        return mapRente;
    }

    @Override
    public JadeProcessEntityInterface getEntityHandler() {
        return new PCProcessAdaptationEntityHandler(rentesDemandeCentral, renteToUpdate);
    }

    @Override
    public Boolean isEnabled(JadeProcessStep step, Map<Enum<?>, String> map) throws JadeApplicationException,
            JadePersistenceException {
        if ("true".equalsIgnoreCase(map.get(PCProcessAdapationEnum.HAS_ADAPTATION_DES_RENTE))) {
            return true;
        } else {
            return false;
        }
    }

}
