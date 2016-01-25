package globaz.pegasus.vb.pcaccordee;

import globaz.framework.util.FWCurrency;
import globaz.globall.db.BConstants;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.pegasus.utils.PCPCAccordeeHandler;
import globaz.prestation.tools.PRStringUtils;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordee;
import ch.globaz.pegasus.business.models.pcaccordee.ListPCAccordeeSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.PersonneDansPlanCalculSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoelSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;

public class PCPcAccordeeViewBean extends BJadePersistentObjectViewBean {

    private final static String ID_TO_DIPLAY_PCAL = "btnDisplayCal_";
    private ListPCAccordee pcAccordee = null;
    private ListPCAccordeeSearch pcAccordeeSearch = null;
    private PersonneDansPlanCalcul personneDansPlanCalcul = null;
    private PersonneDansPlanCalculSearch personneDansPlanCalculSearch = null;

    private SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();

    public PCPcAccordeeViewBean() {
        super();
        pcAccordee = new ListPCAccordee();
        personneDansPlanCalcul = new PersonneDansPlanCalcul(pcAccordee.getIdTiersBeneficiaire());
    }

    public PCPcAccordeeViewBean(ListPCAccordee pcAccordee) {
        super();
        this.pcAccordee = pcAccordee;
    }

    @Override
    public void add() throws PCAccordeeException {
        throw new PCAccordeeException("Can't create with this viewbean!");
    }

    /**
     * Methode retournant la balise span à appliquer sur les entités ayant un idPcaParent
     * 
     * @return chaine contenant le balisage span, vide si inutile
     */
    public String dealInfoPcaCopieFromPreviousVersion(int index) {

        // Si idPcaPernt diff de 0, pca copié
        if (!JadeStringUtil.isBlankOrZero(pcAccordee.getSimplePCAccordee().getIdPcaParent())) {
            String libelle = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_PCACCORDEE_INFO_COPIE") + "  ";
            Integer noVersionPrecedente = Integer.parseInt(getPcAccordee().getSimpleVersionDroit().getNoVersion()) - 1;
            libelle = PRStringUtils.replaceString(libelle, "{0}", noVersionPrecedente.toString());

            String bubblePosition = "";
            if (index == 0) {
                bubblePosition = "position:bottom,";
            }
            String spanBalise = "<span data-g-bubble='" + bubblePosition + "wantMarker:false,text:¦" + libelle
                    + "¦'><img src='/webavs/images/cp.png'/></span>";
            return spanBalise;
        } else {
            return "";
        }
    }

    @Override
    public void delete() throws PCAccordeeException {
        throw new PCAccordeeException("Can't delete with this viewbean!");
    }

    public String getDateDebut() {
        return pcAccordee.getSimplePCAccordee().getDateDebut();
    }

    public String getDateFin() {
        return pcAccordee.getSimplePCAccordee().getDateFin();
    }

    public String getGedLabel() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_GED_LINK_LABEL");
    }

    @Override
    public String getId() {
        return pcAccordee.getId();
    }

    public String getIdDemandePc() {
        return pcAccordee.getSimpleDemande().getIdDemande();
    }

    public String getIdDossier() {
        return getPcAccordee().getSimpleDossier().getIdDossier();
    }

    public String getIdDroit() {
        return pcAccordee.getSimpleDroit().getIdDroit();
    }

    public String getIdPlanCalculToDisplay() {

        String idPca = getPcAccordee().getSimplePCAccordee().getIdPCAccordee();

        if (!pcAccordee.getSimplePCAccordee().getIdPcaParent().equals(PersistenceUtil.PERSISTENCE_NO_VALUE)) {
            idPca = getPcAccordee().getSimplePCAccordee().getIdPcaParent();
        }

        StringBuilder idHtml = new StringBuilder(PCPcAccordeeViewBean.ID_TO_DIPLAY_PCAL);
        idHtml.append(idPca).append("_").append(pcAccordee.getSimplePrestationsAccordees().getIdTiersBeneficiaire());

        return idHtml.toString();
    }

    public String getIdTiersRequerant() {
        return pcAccordee.getPersonneEtendue().getTiers().getIdTiers();
    }

    /**
     * Définit si un plan de calcul est accessible selon les rêgles suivantes:</br>
     * 
     * Si le plan à la propriété isAccessible --> ok Tous les autres cas --> ko
     * 
     * Le plan de calcul affiché sera celui de la pca, OU celui de la pca parente pour le cas de copie de pca
     * 
     * @return si le plan de calcul est défini comme accessible
     * @throws PCAccordeeException
     */
    public boolean getIsPlanCAlculAccessible() throws PCAccordeeException {

        return PCPCAccordeeHandler.getIsPlanCalculAccessible(pcAccordee.getSimpleVersionDroit().getCsMotif(),
                pcAccordee.getSimplePlanDeCalcul().getIsPlanCalculAccessible());
    }

    public String getNoAvs() {
        return pcAccordee.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
    }

    public ListPCAccordee getPcAccordee() {
        return pcAccordee;
    }

    public ListPCAccordeeSearch getPcAccordeeSearch() throws Exception {
        searchChildren();
        return pcAccordeeSearch;
    }

    /**
     * Retourne l'état de la pca en fonction du résultat du plan de calcul PARTIEL, OCTROI, ou montant si octroyé
     * 
     * @return
     */
    public String getPCAResultState() {

        // si octroi
        if (pcAccordee.getSimplePlanDeCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {
            return new FWCurrency(pcAccordee.getSimplePlanDeCalcul().getMontantPCMensuelle()).toStringFormat();
        }
        // refus ou octroi partiel
        else {
            // octroi partiel
            if (pcAccordee.getSimplePlanDeCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
                return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECISION_OCTROI_PARTIEL");
            }
            // refus
            else {
                return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECISION_REFUS");
            }
        }
    }

    public PersonneEtendueComplexModel getPersonneBeneficiaire() {
        return pcAccordee.getPersonneEtendue();
    }

    public String getRetenueBlocageLibelle() {
        try {

            if (getPcAccordee().getSimplePrestationsAccordees().get_isPrestationBloquee()
                    .equalsIgnoreCase(BConstants.DB_BOOLEAN_TRUE.toString())
                    && getPcAccordee().getSimplePrestationsAccordees().getIsRetenues()
                    || (getPcAccordee().getIsRetenuesConjoint() && getPcAccordee().getIsPrestationBloqueeConjoint())) {
                return getSession().getApplication().getLabel("JSP_PC_PCACCORDEE_L_R_B",
                        getSession().getUserInfo().getLanguage());
            } else if (getPcAccordee().getSimplePrestationsAccordees().getIsRetenues()
                    || getPcAccordee().getIsRetenuesConjoint()) {
                return getSession().getApplication().getLabel("JSP_PC_PCACCORDEE_L_R",
                        getSession().getUserInfo().getLanguage());
            } else if (getPcAccordee().getSimplePrestationsAccordees().get_isPrestationBloquee()
                    .equalsIgnoreCase(BConstants.DB_BOOLEAN_TRUE.toString())
                    || getPcAccordee().getIsPrestationBloqueeConjoint()) {
                return getSession().getApplication().getLabel("JSP_PC_PCACCORDEE_L_B",
                        getSession().getUserInfo().getLanguage());
            } else {
                return "";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "";

        }

    }

    public BSession getSession() {
        return (BSession) getISession();
    }

    @Override
    public BSpy getSpy() {
        return (pcAccordee != null) && !pcAccordee.isNew() ? new BSpy(pcAccordee.getSpy()) : new BSpy(getSession());
    }

    public SimpleVersionDroit getVersionDroit() {
        return pcAccordee.getSimpleVersionDroit();
    }

    /**
     * Retourne true si la pcaccordée a des jours d'appoints
     * 
     * @return
     */
    public Boolean hasJoursAppoint() {
        return pcAccordee.getSimplePCAccordee().getHasJoursAppoint();
    }

    /**
     * Retourne l'état (enfant, ou pas) de la pca. Si il y aun parent, retourne true
     * 
     * @return true si enfant, false, sinon
     */
    public Boolean isPcaChildrenFromOtherPca() {
        return !JadeStringUtil.isBlankOrZero(pcAccordee.getSimplePCAccordee().getIdPcaParent());
    }

    @Override
    public void retrieve() throws Exception {
        pcAccordee = PegasusServiceLocator.getPCAccordeeService().read(pcAccordee.getId());
        if (useAllocationNoel()) {
            SimpleAllocationNoelSearch search = new SimpleAllocationNoelSearch();
            search.setForIdPcAccordee(pcAccordee.getId());
            search = PegasusServiceLocator.getSimpleAllocationDeNoelService().search(search);
            if (search.getSize() > 0) {
                if (search.getSize() > 1) {
                    throw new PCAccordeeException("Too many allocationNoel founded with this idPca:"
                            + pcAccordee.getId());
                }
                simpleAllocationNoel = (SimpleAllocationNoel) search.getSearchResults()[0];
            }
        }
    }

    public void searchChildren() throws Exception {

        // null : enfantDansCalcul non initialisé
        personneDansPlanCalculSearch = PegasusServiceLocator.getEnfantDansCalculService().search(
                personneDansPlanCalculSearch);
    }

    @Override
    public void setId(String newId) {
        pcAccordee.setId(newId);

    }

    public void setPcAccordee(ListPCAccordee pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    public void setPcAccordeeSearch(ListPCAccordeeSearch pcAccordeeSearch) {
        this.pcAccordeeSearch = pcAccordeeSearch;
    }

    /*
     * (non-Javadoc)
     * 
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws PCAccordeeException {
        throw new PCAccordeeException("Can't update with this viewbean!");
    }

    public boolean useAllocationNoel() throws PropertiesException {
        return EPCProperties.ALLOCATION_NOEL.getBooleanValue();
    }
}
