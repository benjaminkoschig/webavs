package globaz.pegasus.vb.creancier;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCDroitHandler;
import globaz.pegasus.utils.PCUserHelper;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import ch.globaz.pegasus.business.constantes.IPCCreancier;
import ch.globaz.pegasus.business.domaine.ListTotal;
import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.droit.DroitException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pmtmensuel.PmtMensuelException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.Creancier;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordee;
import ch.globaz.pegasus.business.models.creancier.SimpleCreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.dettecomptatcompense.SimpleDetteComptatCompense;
import ch.globaz.pegasus.business.models.droit.Droit;
import ch.globaz.pegasus.business.models.droit.DroitSearch;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.PCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppointSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.pcaccordee.CalculPrestationsVerses;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;

public class PCCreanceAccordeeViewBean extends BJadePersistentObjectViewBean {

    public String getIdDecision() {
        return idDecision;
    }

    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    private CreanceAccordee creanceAccordee = null;
    private CreanceAccordeeSearch creanceAccordeeSearch = null;
    private CreancierSearch creancierSearch = null;
    private String idDemandePc = null;
    private String idDecision = null;
    private List<SimpleCreanceAccordee> listCreanceAccordees = null;
    private Map<String, String> mapMontantRetro = null;
    private Map<String, String> mapMontantRetroBrut = null;
    private Map<String, SimpleJoursAppoint> mapJoursAppoints = null;
    private HashMap<String, String> montantDejaRembourse = null;
    private BigDecimal montantDette = null;
    private PCAccordeeSearch pcAccordeeSearch = null;

    private BigDecimal totalVerse = null;

    private static final String BR = "<br />";
    private static final String UNION = " - ";
    private static final String SPACE = " ";

    public PCCreanceAccordeeViewBean() {
        super();
        creanceAccordee = new CreanceAccordee();
        pcAccordeeSearch = new PCAccordeeSearch();
        creancierSearch = new CreancierSearch();
        creanceAccordeeSearch = new CreanceAccordeeSearch();
    }

    /**
     * @param creanceAccordee la créance accordée
     */
    public PCCreanceAccordeeViewBean(CreanceAccordee creanceAccordee) {
        super();
        this.creanceAccordee = creanceAccordee;
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void add() throws CreancierException, JadeApplicationServiceNotAvailableException, JadePersistenceException {
        save();
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void delete() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
    }

    private Droit findCurrentDroit() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        DroitSearch search = new DroitSearch();
        Droit droit = null;
        search.setWhereKey(DroitSearch.CURRENT_VERSION);
        search.setForIdDemandePc(idDemandePc);
        search = PegasusServiceLocator.getDroitService().searchDroit(search);
        if (search.getSize() == 1) {
            droit = (Droit) search.getSearchResults()[0];
        } else {
            throw new DroitException("can't retrieve droit. " + search.getSize()
                    + " element(s) was found for the idDemande(" + idDemandePc + ")");
        }

        return droit;
    }

    private BigDecimal findMontantDette() throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {
        Droit droit = findCurrentDroit();
        ListTotal<SimpleDetteComptatCompense> listTotal = PegasusServiceLocator.getDetteComptatCompenseService()
                .findListTotalCompense(droit.getSimpleVersionDroit().getId(), droit.getSimpleDroit().getId());
        return listTotal.getTotal();
    }

    public String formateDescriptionPcAccordee(PCAccordee pcAccordee, BSession session) {

        StringBuilder description = new StringBuilder(PCUserHelper.getDetailAssure(session,
                pcAccordee.getPersonneEtendue()));
        description.append(BR);
        description.append(pcAccordee.getSimplePCAccordee().getDateDebut());
        description.append(UNION);
        description.append(pcAccordee.getSimplePCAccordee().getDateFin());

        if (mapJoursAppoints.containsKey(pcAccordee.getId())) {

            description.append(BR);

            description.append(BSessionUtil.getSessionFromThreadContext()
                    .getLabel("JSP_PC_DECOMPTE_L_AJOUT_COMPLEMENT"));
            description.append(SPACE);
            description.append(pcAccordee.getFirstJoursAppoint().getDateEntreHome().substring(3));
            description.append(SPACE).append(" (").append(pcAccordee.getFirstJoursAppoint().getNbrJoursAppoint());
            description.append(SPACE);
            description.append(BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECOMPTE_L_JOUR"));
            description.append(SPACE);
            description.append(pcAccordee.getFirstJoursAppoint().getMontantJournalier()).append(")");

        }

        return description.toString();
    }

    public SimpleCreanceAccordee getCreanceAccordeeByIdCreancierAndIdPcAccordee(String idCreancier, String idPcAccordee) {
        CreanceAccordee creanceAccordee = null;
        SimpleCreanceAccordee simpleCreanceAccordee = new SimpleCreanceAccordee();

        for (JadeAbstractModel model : creanceAccordeeSearch.getSearchResults()) {
            creanceAccordee = (CreanceAccordee) model;
            if (creanceAccordee.getSimpleCreanceAccordee().getIdCreancier().equals(idCreancier)
                    && creanceAccordee.getSimpleCreanceAccordee().getIdPCAccordee().equals(idPcAccordee)) {

                simpleCreanceAccordee = creanceAccordee.getSimpleCreanceAccordee();
            }
        }
        return simpleCreanceAccordee;
    }

    public SimpleVersionDroit getCurrentVersionedDroit() {
        return ((PCAccordee) pcAccordeeSearch.getSearchResults()[0]).getSimpleVersionDroit();
    }

    public String getDescriptionCreancier(Creancier creancier, BSession objSession) {
        String desc = "";
        if (JadeStringUtil.isEmpty(creancier.getSimpleTiers().getId())
                || "0".equals(creancier.getSimpleTiers().getId())) {
            desc = objSession.getCodeLibelle(IPCCreancier.CS_TYPE_CREANCE_IMPOT);
        } else {
            desc = creancier.getSimpleTiers().getDesignation1() + " " + creancier.getSimpleTiers().getDesignation2();
        }
        return desc;
    }

    @Override
    public String getId() {
        return creanceAccordee.getId();
    }

    /**
     * @return the idDemandePc
     */
    public String getIdDemandePc() {
        return idDemandePc;
    }

    public String getIdDroit() {
        return getCurrentVersionedDroit().getIdDroit();
    }

    /**
     * @return the listCreanceAccordees
     */
    public List<SimpleCreanceAccordee> getListCreanceAccordees() {
        return listCreanceAccordees;
    }

    public List<Creancier> getListCreancier() {
        List<Creancier> list = new ArrayList<Creancier>();
        for (JadeAbstractModel model : creancierSearch.getSearchResults()) {
            list.add((Creancier) model);
        }
        return list;
    }

    public List<PCAccordee> getListPcAccordee() throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        List<PCAccordee> list = new ArrayList<PCAccordee>();

        for (JadeAbstractModel model : pcAccordeeSearch.getSearchResults()) {

            list.add((PCAccordee) model);
        }

        return list;
    }

    public String getMontantADispo() throws NumberFormatException, PCAccordeeException, PmtMensuelException,
            JadeApplicationServiceNotAvailableException, DecisionException, JadePersistenceException {
        BigDecimal sommeADispo = new BigDecimal(0);

        for (String montant : mapMontantRetroBrut.values()) {
            sommeADispo = sommeADispo.add(new BigDecimal(montant));// Integer.parseInt(montant);
        }
        for (SimpleJoursAppoint jAppoint : mapJoursAppoints.values()) {
            sommeADispo = sommeADispo.add(new BigDecimal(jAppoint.getMontantTotal()));// Integer.parseInt(montant);
        }
        sommeADispo = sommeADispo.subtract(totalVerse);

        return (sommeADispo).subtract(montantDette).toString();
    }

    public String getMontantDejaRembourseByIdCreancier(String idCreancier) {
        String montant = montantDejaRembourse.get(idCreancier);
        return (JadeStringUtil.isEmpty(montant) || (montant == null)) ? "0" : montant;
    }

    public String getMontantDette() throws JadeApplicationServiceNotAvailableException, JadeApplicationException,
            JadePersistenceException {
        return new FWCurrency(montantDette.toString()).toStringFormat();
    }

    /**
     * Retourne le montant retroactif brut, sans déduction aucune
     * 
     * @param idPca
     *            l'id de la PCA
     * @return String
     */
    public String getMontantRetroBrutByIdPCA(String idPca) {

        String mntPca = mapMontantRetroBrut.get(idPca);

        BigDecimal montant = new BigDecimal(mntPca);

        if (mapJoursAppoints.containsKey(idPca)) {
            montant = montant.add(new BigDecimal(mapJoursAppoints.get(idPca).getMontantTotal()));
        }

        return montant.toString();
    }

    public String getMontantRetroByIdPCA(String idPca) {
        String mnt = mapMontantRetro.get(idPca);
        /*
         * if (Float.valueOf(mnt) < 0) { mnt = "0"; }
         */
        return mnt;
    }

    public String getNoVersionDroit() {
        return getCurrentVersionedDroit().getNoVersion();
    }

    public String getRequerantDetail() throws DroitException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        return PCDroitHandler.getInfoHtmlRequerantByIdDemande(idDemandePc);
    }

    /**
     * Retourne l'objet session
     * 
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (creanceAccordee != null) && !creanceAccordee.isNew() ? new BSpy(creanceAccordee.getSpy()) : new BSpy(
                getSession());
    }

    public String getTotalDejaVerse() {
        return new FWCurrency("-" + totalVerse.toString()).toStringFormat();

    }

    private void initCreanceAccodeeSearch() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        creanceAccordeeSearch.setForIdDemande(idDemandePc);
        creanceAccordeeSearch = PegasusServiceLocator.getCreanceAccordeeService().search(creanceAccordeeSearch);
    }

    private void initListCreancier() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        creancierSearch.setForIdDemande(idDemandePc);
        creancierSearch = PegasusServiceLocator.getCreancierService().search(creancierSearch);

    }

    private void initMontantDejaRembourse() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        HashMap<String, String> montants = new HashMap<String, String>();
        if (pcAccordeeSearch.getSize() > 0) {
            String version = getNoVersionDroit();
            for (JadeAbstractModel model : creanceAccordeeSearch.getSearchResults()) {
                CreanceAccordee creanceAccordee = (CreanceAccordee) model;
                if (/* JadeStringUtil.isEmpty(version) || */!version.equals(creanceAccordee.getSimpleVersionDroit()
                        .getNoVersion())) {
                    if (montants.get(creanceAccordee.getSimpleCreanceAccordee().getIdCreancier()) != null) {
                        FWCurrency mnt = new FWCurrency(creanceAccordee.getSimpleCreanceAccordee().getMontant());

                        mnt.add(montants.get(creanceAccordee.getSimpleCreanceAccordee().getIdCreancier()));

                        montants.put(creanceAccordee.getSimpleCreanceAccordee().getIdCreancier(), mnt.toString());
                    } else {
                        montants.put(creanceAccordee.getSimpleCreanceAccordee().getIdCreancier(), creanceAccordee
                                .getSimpleCreanceAccordee().getMontant());
                    }
                }
            }
        }
        montantDejaRembourse = montants;
    }

    /**
     * Retourne le montant déjé versé
     * 
     * @return
     * @throws PCAccordeeException
     * @throws PmtMensuelException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     * @throws JadePersistenceException
     */
    private void initMontantTotalDejaVerse() throws PCAccordeeException, PmtMensuelException,
            JadeApplicationServiceNotAvailableException, DecisionException, JadePersistenceException {
        CalculPrestationsVerses montantVerse = new CalculPrestationsVerses(getNoVersionDroit(), idDemandePc);
        totalVerse = montantVerse.getMontantVerse();

    }

    private void initpcAccordeeSearch() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        pcAccordeeSearch.setWhereKey(PCAccordeeSearch.FOR_CURRENT_VERSIONED_WITHOUT_COPIE);
        pcAccordeeSearch.setOrderKey(PCAccordeeSearch.ORDER_BY_DATE_DEBUT);
        pcAccordeeSearch.setForIdDemande(idDemandePc);
        pcAccordeeSearch = PegasusServiceLocator.getPCAccordeeService().search(pcAccordeeSearch);

    }

    /**
     * @throws Exception
     */
    @Override
    public void retrieve() throws Exception {
        if (JadeStringUtil.isBlankOrZero(idDemandePc)) {
            throw new PCAccordeeException("Unable to find the pca with any id demande");
        }

        initCreanceAccodeeSearch();
        initListCreancier();
        initpcAccordeeSearch();
        initJoursAppoint();
        initMontantDejaRembourse();
        initMontantTotalDejaVerse();
        mapMontantRetro = PegasusServiceLocator.getPCAccordeeService().getMapMontantRetroActif(idDemandePc,
                getNoVersionDroit());
        mapMontantRetroBrut = PegasusServiceLocator.getPCAccordeeService().getMapMontantRetroActifBrut(idDemandePc,
                getNoVersionDroit());
        montantDette = findMontantDette();
    }

    private List<String> getListIdPcas(List<PCAccordee> listePca) {
        List<String> idsPca = new ArrayList<String>();

        for (PCAccordee pca : listePca) {
            idsPca.add(pca.getId());
        }
        return idsPca;
    }

    /**
     * Initialisation des jours d'appoints
     * Création d'une map de jours d'appoint
     * Ajout des jours d'appoint dans la pca
     * 
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void initJoursAppoint() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        List<PCAccordee> listePca = getListPcAccordee();

        mapJoursAppoints = new HashMap<String, SimpleJoursAppoint>();

        if (getListIdPcas(listePca).size() != 0) {
            List<SimpleJoursAppoint> listeJoursAppoint = new ArrayList<SimpleJoursAppoint>();

            SimpleJoursAppointSearch search = new SimpleJoursAppointSearch();
            search.setInIdsPca(getListIdPcas(listePca));
            search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);
            search = PegasusImplServiceLocator.getSimpleJoursAppointService().search(search);
            listeJoursAppoint = PersistenceUtil.typeSearch(search, search.whichModelClass());

            for (SimpleJoursAppoint joursAppoint : listeJoursAppoint) {

                String idPca = joursAppoint.getIdPCAccordee();

                for (PCAccordee pca : listePca) {
                    if (pca.getId().equals(idPca)) {
                        pca.addJoursAppoint(joursAppoint);
                        mapJoursAppoints.put(idPca, joursAppoint);
                        break;
                    }
                }
            }
        }

    }

    private void save() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        CreanceAccordee creanceAccordee = null;

        SimpleCreanceAccordeeSearch simpleCreanceAccordeeSearch = new SimpleCreanceAccordeeSearch();
        for (SimpleCreanceAccordee scAccordee : listCreanceAccordees) {
            creanceAccordee = new CreanceAccordee();
            creanceAccordee.setSimpleCreanceAccordee(scAccordee);
            if (JadeStringUtil.isEmpty(creanceAccordee.getId())) {
                PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);

            } else {
                simpleCreanceAccordeeSearch.setForIdCreaanceAccordee(scAccordee.getId());

                PegasusServiceLocator.getCreanceAccordeeService().deleteWithSearchModele(simpleCreanceAccordeeSearch);

                PegasusServiceLocator.getCreanceAccordeeService().create(creanceAccordee);

            }

        }

    }

    /**
     * @param idCreancier
     */
    @Override
    public void setId(String id) {
    }

    /**
     * @param idDemandePc
     *            the idDemandePc to set
     */
    public void setIdDemandePc(String idDemandePc) {
        this.idDemandePc = idDemandePc;
    }

    /**
     * @param listCreanceAccordees
     *            the listCreanceAccordees to set
     */
    public void setListCreanceAccordees(List<SimpleCreanceAccordee> listCreanceAccordees) {
        this.listCreanceAccordees = listCreanceAccordees;
    }

    /**
     * @throws CreancierException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    @Override
    public void update() throws CreancierException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        save();
    }

}
