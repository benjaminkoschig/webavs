package globaz.pegasus.vb.pcaccordee;

import globaz.globall.db.BIPersistentObject;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.vb.BJadePersistentObjectListViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.domaine.dossier.IdDossier;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class PCPcAccordeeListViewBean extends BJadePersistentObjectListViewBean {

    private Map<String, SimpleAllocationNoel> allocationNoels = new HashMap<String, SimpleAllocationNoel>();
    private Map<IdDossier, List<ListPCAccordee>> currentPcas = new HashMap<IdDossier, List<ListPCAccordee>>();

    private String idDemande = null;
    private String idDossier = null;
    private String idDroit = null;
    private String idVersionDroit;
    private String noVersion = null;
    private ListPCAccordeeSearch pcAccordeesSearch = null;
    private String whereKey = null;

    public PCPcAccordeeListViewBean() {
        super();
        pcAccordeesSearch = new ListPCAccordeeSearch();

    }

    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    public String getGedLabel() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_GED_LINK_LABEL");
    }

    @Override
    public void find() throws Exception {
        if (JadeStringUtil.isNull(pcAccordeesSearch.getForCacherHistorique())) {
            pcAccordeesSearch.setForCacherHistorique("false");
        } else {
            pcAccordeesSearch.setForCacherHistorique("true");
        }
        if ((whereKey != null) && !("".equals(whereKey))) {
            pcAccordeesSearch.setWhereKey(whereKey);
            pcAccordeesSearch.setForIdDemande(idDemande);
            if (idDossier != null && !idDossier.isEmpty()) {
                pcAccordeesSearch.setForIdDossier(idDossier);
            }
            pcAccordeesSearch.setWhereKey("forVersionnedPca");
        } else {
            pcAccordeesSearch.setWhereKey("");
        }

        if (idVersionDroit != null && !idVersionDroit.isEmpty()) {
            SimpleVersionDroit simpleVersionDroit = PegasusImplServiceLocator.getSimpleVersionDroitService().read(
                    idVersionDroit);
            idDroit = simpleVersionDroit.getIdDroit();
            noVersion = simpleVersionDroit.getNoVersion();
        }

        // Si on vient du droit, idDroit et noVesrion setter
        if (!JadeStringUtil.isBlank(idDroit) && !JadeStringUtil.isBlank(noVersion)) {
            pcAccordeesSearch.setWhereKey("");
            pcAccordeesSearch.setForIdDroit(idDroit);
            pcAccordeesSearch.setForNoVersion(noVersion);
            pcAccordeesSearch.setForCacherHistorique("false");
        }
        pcAccordeesSearch = PegasusServiceLocator.getPCAccordeeService().searchForList(pcAccordeesSearch);

        filtrePcCourante();

        if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()) {
            Map<String, SimpleAllocationNoel> map = findAllocationNoelAndCreateMapByIdPca();
            allocationNoels = map;
        }
    }

    /**
     * Stocke dans une liste les pca courantes pour le dossier, cà.d:
     * - la/les pca qui n'a pas de date de fin pour un droit
     * - la/les pca qui a la date de début la plus récenet
     * 
     */
    private void filtrePcCourante() {

        List<ListPCAccordee> pcas = PersistenceUtil.typeSearch(pcAccordeesSearch, ListPCAccordee.class);

        for (ListPCAccordee pca : pcas) {

            IdDossier entiteCourante = new IdDossier(pca.getSimpleDemande().getIdDossier());

            // Si pas de pc courantes dans la map pour la version du droit
            if (!currentPcas.keySet().contains(entiteCourante)) {
                List<ListPCAccordee> pcasForVersionDroit = new ArrayList<ListPCAccordee>();
                pca.defineAsCourante();
                pcasForVersionDroit.add(pca);
                currentPcas.put(entiteCourante, pcasForVersionDroit);

            } else {
                // on recupere l'entite déjà aprésente, il ne peut y en avoir qu'une
                ListPCAccordee currentPcaForVersion = currentPcas.get(entiteCourante).get(0);

                // si date de debut egales, on ajoute la deuxieme entite
                if (currentPcaForVersion.getSimplePCAccordee().getDateDebut()
                        .equals(pca.getSimplePCAccordee().getDateDebut())) {
                    pca.defineAsCourante();
                    currentPcas.get(entiteCourante).add(pca);
                }
            }
        }

    }

    private List<SimpleAllocationNoel> findAllocationDeNoel(ListPCAccordeeSearch pcAccordeesSearch)
            throws JadePersistenceException, PCAccordeeException, AllocationDeNoelException,
            JadeApplicationServiceNotAvailableException {
        List<String> ids = new ArrayList<String>();
        List<SimpleAllocationNoel> list = new ArrayList<SimpleAllocationNoel>();
        for (JadeAbstractModel model : pcAccordeesSearch.getSearchResults()) {
            ids.add(model.getId());
        }
        if (ids.size() > 0) {
            SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
            search.setInIdsPcAccordee(ids);
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = PegasusServiceLocator.getSimpleAllocationDeNoelService().search(search);
            list = PersistenceUtil.typeSearch(search, search.whichModelClass());
        }
        return list;
    }

    private Map<String, SimpleAllocationNoel> findAllocationNoelAndCreateMapByIdPca() throws JadePersistenceException,
            PCAccordeeException, AllocationDeNoelException, JadeApplicationServiceNotAvailableException {
        List<SimpleAllocationNoel> list = findAllocationDeNoel(pcAccordeesSearch);
        Map<String, SimpleAllocationNoel> map = new HashMap<String, SimpleAllocationNoel>();
        for (SimpleAllocationNoel simpleAllocationNoel : list) {
            map.put(simpleAllocationNoel.getIdPCAccordee(), simpleAllocationNoel);
        }
        return map;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.vb.BJadePersistentObjectListViewBean#getEntity(int)
     */
    @Override
    public BIPersistentObject get(int idx) {
        return idx < pcAccordeesSearch.getSize() ? new PCPcAccordeeViewBean(
                (ListPCAccordee) pcAccordeesSearch.getSearchResults()[idx]) : new PCPcAccordeeViewBean();
    }

    /**
     * @return the idDemandePc
     */
    public String getIdDemande() {
        return idDemande;
    }

    /**
     * @return the idDossier
     */
    public String getIdDossier() {
        return idDossier;
    }

    public String getIdDroit() {
        return idDroit;
    }

    @Override
    protected JadeAbstractSearchModel getManagerModel() {
        return pcAccordeesSearch;
    }

    public String getNoVersion() {
        return noVersion;
    }

    /**
     * @return the pcAccordeesSearch
     */
    public ListPCAccordeeSearch getPcAccordeesSearch() {
        return pcAccordeesSearch;
    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * @return the whereKey
     */
    public String getWhereKey() {
        return whereKey;
    }

    public boolean hasAllocationNoel(ListPCAccordee pca) {
        return allocationNoels.containsKey(pca.getId());
    }

    /**
     * @param idDemandePc
     *            the idDemandePc to set
     */
    public void setIdDemande(String idDemande) {
        this.idDemande = idDemande;
    }

    /**
     * @param idDossier
     *            the idDossier to set
     */
    public void setIdDossier(String idDossier) {
        this.idDossier = idDossier;
    }

    public void setIdDroit(String idDroit) {
        this.idDroit = idDroit;
    }

    public void setNoVersion(String noVersion) {
        this.noVersion = noVersion;
    }

    /**
     * @param pcAccordeesSearch
     *            the pcAccordeesSearch to set
     */
    public void setPcAccordeesSearch(ListPCAccordeeSearch pcAccordeesSearch) {
        this.pcAccordeesSearch = pcAccordeesSearch;
    }

    /**
     * @param whereKey
     *            the whereKey to set
     */
    public void setWhereKey(String whereKey) {
        this.whereKey = whereKey;
    }

}
