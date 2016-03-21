package ch.globaz.pegasus.businessimpl.services.doc.excel.impl;

import globaz.globall.db.BSessionUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.client.util.JadeUUIDGenerator;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.i18n.JadeI18n;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.op.common.merge.IMergingContainer;
import globaz.op.excelml.model.document.ExcelmlWorkbook;
import globaz.webavs.common.CommonExcelmlContainer;
import java.util.List;
import java.util.Map;
import ch.globaz.jade.process.business.JadeProcessServiceLocator;
import ch.globaz.jade.process.business.enumProcess.JadeProcessEntityStateEnum;
import ch.globaz.jade.process.business.exceptions.ProprieteException;
import ch.globaz.pegasus.business.exceptions.doc.DocException;
import ch.globaz.pegasus.business.models.process.allocationsNoel.AllocationNoelEntity;
import ch.globaz.pegasus.business.models.process.allocationsNoel.AllocationNoelEntitySearch;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.process.allocationsNoel.PCProcessAllocationsNoelEnum;

public class ListeControleAllocationNoel extends PegasusAbstractExcelServiceImpl {
    public final static String MODEL_NAME = "listeDeControleAllocationNoel.xml";
    private String idExecutionProcess = null;
    private String outPutName = "listeDeControleAllocationNoel";

    public ExcelmlWorkbook createDoc(String idExecutionProcess) throws DocException {
        if (idExecutionProcess == null) {
            throw new DocException("Unable to execute createDoc, the idLot is null!");
        }
        setIdExecutionProcess(idExecutionProcess);
        return this.createDoc();
    }

    public String createDocAndSave(String idExecutionProcess) throws Exception {
        ExcelmlWorkbook wk = this.createDoc(idExecutionProcess);
        String nomDoc = outPutName + "_" + JadeUUIDGenerator.createStringUUID() + ".xml";
        return save(wk, nomDoc);
    }

    private String findAnneAllocation() throws ProprieteException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        Map<Enum<?>, String> properties = JadeProcessServiceLocator.getPropertiesService().findPropertiesProvided(
                idExecutionProcess);
        String annee = properties.get(PCProcessAllocationsNoelEnum.ANNEE_ALLOCATION_NOEL);
        return annee;
    }

    public String getIdExecutionProcess() {
        return idExecutionProcess;
    }

    public String getMessage(String messageId) {
        return JadeI18n.getInstance().getMessage(JadeThread.currentLanguage(), messageId);
    }

    @Override
    public String getModelName() {
        return ListeControleAllocationNoel.MODEL_NAME;
    }

    @Override
    public String getOutPutName() {
        return outPutName;
    }

    @Override
    public IMergingContainer loadResults() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {

        CommonExcelmlContainer container = new CommonExcelmlContainer();

        List<AllocationNoelEntity> listAllocations = searchAllocation();

        String annee = findAnneAllocation();

        for (AllocationNoelEntity allocationNoelEntity : listAllocations) {
            if (!JadeProcessEntityStateEnum.ERROR.equals(allocationNoelEntity.getCsEtat())) {
                // la ligne sans erreur
                container.put("description", allocationNoelEntity.getDescription());
                container.put("csEtat", allocationNoelEntity.getCsEtat().toLabel());
                container.put("manual", allocationNoelEntity.getIsManual().toString());
                container.put("montant", allocationNoelEntity.getMontantRequerant());
                container.put("montantconjoint", allocationNoelEntity.getMontantConjoint());
                container.put("nbr", allocationNoelEntity.getSimpleAllocationNoel().getNbrePersonnes());
                container
                        .put("genrepc",
                                BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                        allocationNoelEntity.getCsGenrePca()));
                container.put("dateAllocationNoel", annee);
                container.put("hasPaiementPostal", allocationNoelEntity.getSimpleAllocationNoel()
                        .getHasPaiementPostal() ? "Oui" : "Non");
                container.put("paiementPostalCreer", (JadeStringUtil.isBlankOrZero(allocationNoelEntity
                        .getSimpleAllocationNoel().getIdAdressePaiementPostaleCreer())) ? "Non" : "Oui");
                container.put("annee", allocationNoelEntity.getSimpleAllocationNoel().getAnneeAllocation());
            } else {
                // pour les entites en erreur
                container.put("description", allocationNoelEntity.getDescription());
                container.put("csEtat", allocationNoelEntity.getCsEtat().toLabel());
                container.put("manual", allocationNoelEntity.getIsManual().toString());
                container.put("montant", "");
                container.put("montantconjoint", "");
                container.put("nbr", "");
                container
                        .put("genrepc",
                                BSessionUtil.getSessionFromThreadContext().getCodeLibelle(
                                        allocationNoelEntity.getCsGenrePca()));
                container.put("dateAllocationNoel", "");
                container.put("hasPaiementPostal", "");
                container.put("paiementPostalCreer", "");
                container.put("annee", "");
            }
        }
        return container;
    }

    private List<AllocationNoelEntity> searchAllocation() throws JadePersistenceException {
        AllocationNoelEntitySearch search = new AllocationNoelEntitySearch();
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
        search.setForIdExecutionProcess(idExecutionProcess);
        List<AllocationNoelEntity> listAllocations = PersistenceUtil.search(search, search.whichModelClass());
        return listAllocations;
    }

    public void setIdExecutionProcess(String idExecutionProcess) {
        this.idExecutionProcess = idExecutionProcess;
    }
}
