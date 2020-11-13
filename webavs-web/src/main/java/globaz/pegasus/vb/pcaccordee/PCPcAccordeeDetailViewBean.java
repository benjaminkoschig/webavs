package globaz.pegasus.vb.pcaccordee;

import ch.globaz.common.properties.PropertiesException;
import ch.globaz.corvus.business.services.CorvusServiceLocator;
import ch.globaz.pegasus.business.constantes.*;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.models.droit.SimpleVersionDroit;
import ch.globaz.pegasus.business.models.pcaccordee.*;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pegasus.businessimpl.services.models.calcul.CalculComparatifServiceImpl;
import ch.globaz.pegasus.businessimpl.utils.PCproperties;
import ch.globaz.pegasus.businessimpl.utils.PersistenceUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.PegasusCalculUtil;
import ch.globaz.pegasus.businessimpl.utils.calcul.PeriodePCAccordee;
import ch.globaz.pegasus.businessimpl.utils.calcul.TupleDonneeRapport;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;
import globaz.corvus.db.rentesaccordees.REEnteteBlocage;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeBloquee;
import globaz.corvus.db.rentesaccordees.REPrestationAccordeeBloqueeManager;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeListUtil;
import globaz.jade.client.util.JadeListUtil.Key;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.common.Jade;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.persistence.model.JadeAbstractSearchModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;
import org.apache.commons.lang.NotImplementedException;

import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.*;

/**
 * ViewBean pour le traitement des pcAccordées (détail) <<<<<<< .working
 *
 * @author SCE - 19 oct. 2010 - Créaion initiales
 * @author SCE - 24 sept. 2013 - Modif 1.12, ajout gestion blocage débblocage ======= >>>>>>> .merge-right.r24112
 */
public class PCPcAccordeeDetailViewBean extends BJadePersistentObjectViewBean {

    private String dateLiberation = null;
    private REEnteteBlocage entete = null;
    private String ERROR_ADRESS_MESSAGE = null;
    private String idPlanCalcule = null;
    private Boolean isResultatNull = false;
    private ArrayList<SimplePlanDeCalcul> listePlanCalculs = null;
    private ArrayList<PlanDeCalculWitMembreFamille> listMembreFamille = null;

    private final String PC_STATUS_OCTROI = IPCValeursPlanCalcul.STATUS_OCTROI;
    private final String PC_STATUS_PARTIEL = IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL;
    private final String PC_STATUS_REFUS = IPCValeursPlanCalcul.STATUS_REFUS;

    private PCAccordee pcAccordee = null;
    private PlanDeCalculWitMembreFamilleSearch planDeCalculSearch = null;
    private SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();

    /**
     *
     */
    public PCPcAccordeeDetailViewBean() {
        super();
        pcAccordee = new PCAccordee();
        ERROR_ADRESS_MESSAGE = ((BSession) getISession()).getLabel("JSP_PC_ADRESSE_INTROUVABLE");
    }

    /**
     * @param pcAccordee
     */
    public PCPcAccordeeDetailViewBean(PCAccordee pcAccordee) {
        super();
        this.pcAccordee = pcAccordee;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
        // TODO Auto-generated method stub

    }

    public void bloquerPC() throws Exception {

        PCAccordee pca = pcAccordee;
        // recherche de pc pour la meme version de droit avec la meme date de debut (couple separer par la maladie)
        PCAccordeeSearch pcSearch = new PCAccordeeSearch();
        pcSearch.setForVersionDroit(pca.getSimpleVersionDroit().getIdVersionDroit());
        pcSearch.setForDateDebut(pca.getSimplePCAccordee().getDateDebut());
        pcSearch = PegasusServiceLocator.getPCAccordeeService().search(pcSearch);

        if ((pcSearch.getSearchResults().length > 2) || (pcSearch.getSearchResults().length == 0)) {
            throw new PCAccordeeException("The number of pc retrieved is not correct: "
                    + pcSearch.getSearchResults().length + "pc found![" + this.getClass().getName() + "]");
        }

        // on debloque la pca courante du bean
        bloquePca(pca);

        // on va parcourir les resultats, et updater celle qui a un id différent de celle du bean
        if (pcSearch.getSearchResults().length == 2) {

            for (JadeAbstractModel model : pcSearch.getSearchResults()) {
                PCAccordee pcaConjoint = (PCAccordee) model;
                if (!pca.getId().equals(pcaConjoint.getId())) {
                    bloquePca(pcaConjoint);
                }
            }
        }
    }

    private void updateAllocationNoelIfAvailable(PCAccordee pca, Boolean block) throws PCAccordeeException,
            AllocationDeNoelException, JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AllocationNoel allocNoel = PegasusServiceLocator.getAllocationDeNoelService().readAllocationNoelByIdPca(
                pca.getSimplePCAccordee().getId());

        // Si allocation de noel
        if (!allocNoel.isNew()) {
            allocNoel.getSimplePrestationsAccordees().setIsPrestationBloquee(block);

            PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                    allocNoel.getSimplePrestationsAccordees());

        }
    }

    private void bloquePca(PCAccordee pca) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        bloqueSinglePc(pca);
        updateAllocationNoelIfAvailable(pca, Boolean.TRUE);
    }

    private void bloqueSinglePc(PCAccordee pca) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        pca.getSimplePrestationsAccordees().set_isPrestationBloquee(BConstants.DB_BOOLEAN_TRUE.toString());
        pca.getSimplePrestationsAccordeesConjoint().set_isPrestationBloquee(BConstants.DB_BOOLEAN_TRUE.toString());
        pca.setSimplePrestationsAccordees(PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                pca.getSimplePrestationsAccordees()));
        pca.setSimplePrestationsAccordeesConjoint(PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                pca.getSimplePrestationsAccordeesConjoint()));
    }

    public void deBloquerPC() throws Exception {

        PCAccordee pca = pcAccordee;
        // recherche de pc pour la meme version de droit avec la meme date de debut (couple separer par la maladie)
        PCAccordeeSearch pcSearch = new PCAccordeeSearch();
        pcSearch.setForVersionDroit(pca.getSimpleVersionDroit().getIdVersionDroit());
        pcSearch.setForDateDebut(pca.getSimplePCAccordee().getDateDebut());
        pcSearch = PegasusServiceLocator.getPCAccordeeService().search(pcSearch);

        if ((pcSearch.getSearchResults().length > 2) || (pcSearch.getSearchResults().length == 0)) {
            throw new PCAccordeeException("The number of pc retrieved is not correct: "
                    + pcSearch.getSearchResults().length + "pc found![" + this.getClass().getName() + "]");
        }

        // on debloque la pca courante du bean
        debloquePca(pca);

        // on va parcourir les resultats, et updater celle qui a un id différent de celle du bean
        if (pcSearch.getSearchResults().length == 2) {

            for (JadeAbstractModel model : pcSearch.getSearchResults()) {
                PCAccordee pcaConjoint = (PCAccordee) model;
                if (!pca.getId().equals(pcaConjoint.getId())) {
                    debloquePca(pcaConjoint);
                }
            }
        }

    }

    private void debloquePca(PCAccordee pca) throws JadePersistenceException, JadeApplicationException {
        deBloqueSinglePc(pca);
        updateAllocationNoelIfAvailable(pca, Boolean.FALSE);
    }

    private void deBloqueSinglePc(PCAccordee pca) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        pca.getSimplePrestationsAccordees().set_isPrestationBloquee(BConstants.DB_BOOLEAN_FALSE.toString());
        pca.getSimplePrestationsAccordeesConjoint().set_isPrestationBloquee(BConstants.DB_BOOLEAN_FALSE.toString());
        pca.setSimplePrestationsAccordees(PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                pca.getSimplePrestationsAccordees()));
        pca.setSimplePrestationsAccordeesConjoint(PegasusImplServiceLocator.getSimplePrestatioAccordeeService().update(
                pca.getSimplePrestationsAccordeesConjoint()));
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
        throw new NotImplementedException();

    }

    private void fillListeMembreFamille() {

        listMembreFamille = new ArrayList<PlanDeCalculWitMembreFamille>();

        // recup premiere entite de la map
        Map.Entry<String, List<PlanDeCalculWitMembreFamille>> planCalculAvecMembreFamille = getOrderedMapByPlanCalcul()
                .entrySet().iterator().next();

        for (PlanDeCalculWitMembreFamille pcalMbr : planCalculAvecMembreFamille.getValue()) {
            listMembreFamille.add(pcalMbr);
        }
    }

    private void fillListePlanCalculs() {

        listePlanCalculs = new ArrayList<SimplePlanDeCalcul>();
        // liste temp
        ArrayList<SimplePlanDeCalcul> listePcalOctroi = new ArrayList<SimplePlanDeCalcul>();
        ArrayList<SimplePlanDeCalcul> listePcalRefusPartiel = new ArrayList<SimplePlanDeCalcul>();

        for (String idPCal : getOrderedMapByPlanCalcul().keySet()) {
            SimplePlanDeCalcul pcal = getOrderedMapByPlanCalcul().get(idPCal).get(0).getSimplePlanDeCalcul();
            // on ajoute uniquement les plans octroyé --> traitement de tri spécifique pour les refus et les octroi
            // partiels
            if (IPCValeursPlanCalcul.STATUS_OCTROI.equals(pcal.getEtatPC())) {
                listePcalOctroi.add(pcal);
            } else {
                listePcalRefusPartiel.add(pcal);
            }
        }
        // on ordre les pcal refus et partiel
        orderListePlanCalcul(listePcalRefusPartiel);
        // ajout à la fin de la liste
        listePcalOctroi.addAll(listePcalRefusPartiel);

        listePlanCalculs.addAll(listePcalOctroi);

    }

    public String getAction() {
        return IPCActions.ACTION_PCACCORDEES_LIST;
    }

    /**
     * Retourne l'adresse de courier du tiers
     *
     * @return String, adresse de courrier
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    private String getAdresseCourrier(String membre) throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        AdresseTiersDetail detailTiers = null;

        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {

            if (!JadeStringUtil.isIntegerEmpty(pcAccordee.getSimpleInformationsComptabilite().getIdTiersAdressePmt())) {
                detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                        pcAccordee.getSimpleInformationsComptabilite().getIdTiersAdressePmt(), Boolean.TRUE,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), null);
            }

        } else if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {

            if (!JadeStringUtil.isIntegerEmpty(pcAccordee.getSimpleInformationsComptabiliteConjoint()
                    .getIdTiersAdressePmt())) {
                detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(
                        pcAccordee.getSimpleInformationsComptabiliteConjoint().getIdTiersAdressePmt(), Boolean.TRUE,
                        IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(), null);
            }
        }

        String address = ((detailTiers != null) && (detailTiers.getAdresseFormate() != null)) ? detailTiers
                .getAdresseFormate() : ERROR_ADRESS_MESSAGE;
        return address.replace("\"", "&#34;").replace("'", "&#39;");
    }

    public String getAdresseCourrierConjoint() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        return getAdresseCourrier(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    public String getAdresseCourrierRequerant() throws JadeApplicationServiceNotAvailableException,
            JadePersistenceException, JadeApplicationException {
        return getAdresseCourrier(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
    }

    /**
     * Gestion ERREURS
     *
     * @return
     */
    public boolean getAutoShowErrorPopup() {
        return getSession().getAttribute(globaz.framework.servlets.FWServlet.OBJ_NO_JSP_POPUP) == null;
    }

    public String getConjointInfos() {
        return getTiersInfosAsString(IPCDroits.CS_ROLE_FAMILLE_CONJOINT);
    }

    public String getCreationAndUpdateInfos() throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {
        if (getCreationSpy() != null) {
            return getCreationSpy() + "Update: " + getLastModification();
        } else {
            return "Update: " + getLastModification();
        }
    }

    /**
     * Gestion ERREURS
     *
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    private String getCreationSpy() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        @SuppressWarnings("rawtypes")
        Class vbClass = this.getClass();
        java.lang.reflect.Method creationSpyMethod = null;
        String creationSpy = null;

        try {
            creationSpyMethod = vbClass.getMethod("getCreationSpy", new Class[0]);
            globaz.globall.db.BSpy creationSpyObject = (globaz.globall.db.BSpy) creationSpyMethod.invoke(this,
                    new Object[0]);
            if (creationSpyObject != null) {
                creationSpy = "Creation: " + creationSpyObject.getDate() + ", " + creationSpyObject.getTime() + " - "
                        + creationSpyObject.getUser() + " / ";

            }
        } catch (NoSuchMethodException nsme) {

        } catch (ClassCastException cce) {

        }

        return creationSpy;
    }

    public String getDateDebutBlocage() throws Exception {
        String dateDebut = "";
        REPrestationAccordeeBloqueeManager mgr2 = new REPrestationAccordeeBloqueeManager();
        mgr2.setSession(getSession());
        mgr2.setForIdEnteteBlocage(getPcAccordee().getSimplePrestationsAccordees().getIdEnteteBlocage());
        mgr2.find(BManager.SIZE_NOLIMIT);
        // boolean isBlocageEnCoursAnnee = false;
        for (int i = 0; i < mgr2.size(); i++) {
            REPrestationAccordeeBloquee pb = (REPrestationAccordeeBloquee) mgr2.getEntity(i);

            dateDebut = pb.getDateBlocage().substring(0, 6);
            break;

        }
        return dateDebut;

    }

    public String getDateLiberation() {
        return dateLiberation;
    }

    public boolean getDisplayJoursAppoints() throws PropertiesException {
        return PCproperties.getBoolean(EPCProperties.GESTION_JOURS_APPOINTS);
    }

    public REEnteteBlocage getEntete() throws Exception {
        REEnteteBlocage entete = new REEnteteBlocage();
        entete.setSession(getSession());
        entete.setIdEnteteBlocage(getPcAccordee().getSimplePrestationsAccordees().getIdEnteteBlocage());
        entete.retrieve();

        return entete;
    }

    public String getGedLabel() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_GED_LINK_LABEL");
    }

    /**
     * Recherche si une prestations est accordée au conjoint pour la même pca (couple a DOM2Rentes).
     *
     * @return boolean true si prestations pour conjoint aussi
     */
    public Boolean getHasPCAConjoint() {
        boolean test = !JadeStringUtil.isBlankOrZero(pcAccordee.getSimplePrestationsAccordeesConjoint().getId());
        return test;
        // return true;
    }

    /**
     * Gestion ERREURS
     *
     * @return
     */
    public boolean getHasViewBeanErrors() {

        return getMsgType().equals(globaz.framework.bean.FWViewBeanInterface.ERROR);
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return pcAccordee.getSimplePCAccordee().getId();
    }

    /**
     * Retourne l'id de la demande de la pcaccordee
     *
     * @return idPcaccorde, id de la demande de la pcaccordee
     */
    public String getIdDemandePc() {
        return pcAccordee.getSimpleDroit().getIdDemandePC();
    }

    public String getIdLangue() {

        return globaz.framework.controller.FWDefaultServletAction.getIdLangueIso(getSession());
    }

    public String getIdPlanCalcule() {
        return idPlanCalcule;
    }

    public String getIdTierConjoint() {
        return getPersonneEtendue(IPCDroits.CS_ROLE_FAMILLE_CONJOINT).getTiers().getIdTiers();
    }

    public String getIdTierRequerant() {
        return getPersonneEtendue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT).getTiers().getIdTiers();
    }

    public String getIdTiersBeneficiaire() {
        return pcAccordee.getSimplePrestationsAccordees().getIdTiersBeneficiaire();
    }

    public String getIdTiersRequerant() {
        return pcAccordee.getPersonneEtendue().getTiers().getIdTiers();
    }

    /**
     * Retourne l'état (enfant, ou pas) de la pca. Si il y aun parent, retourne true
     *
     * @return true si enfant, false, sinon
     */
    public Boolean getIsPcaChildrenFromOtherPca() {
        boolean ret = !JadeStringUtil.isBlankOrZero(pcAccordee.getSimplePCAccordee().getIdPcaParent());
        return ret;
    }

    /**
     * Definit si la pc est candidate à être bloqué ou débloqué PCValidéé ET DateDefin vide ET etatPlanCalculRetenu pas
     * vide ET etatPlanCalculRetenu pas en refus
     *
     * @return pc candidate au deblocage
     */
    public boolean getIsPcCandidatePourBlocageDeblocage() throws PCAccordeeException {

        String etatPcPlanCalculRetenu = null;// = this.pcAccordee.getPlanRetenu().getEtatPC();

        for (SimplePlanDeCalcul pcal : listePlanCalculs) {
            if (pcal.getIsPlanRetenu()) {
                etatPcPlanCalculRetenu = pcal.getEtatPC();
            }
        }

        return (IPCPCAccordee.CS_ETAT_PCA_VALIDE.equals(pcAccordee.getSimplePCAccordee().getCsEtatPC())
                && JadeStringUtil.isEmpty(pcAccordee.getSimplePCAccordee().getDateFin()) && (!JadeStringUtil
                .isBlank(etatPcPlanCalculRetenu) && !IPCValeursPlanCalcul.STATUS_REFUS
                .equalsIgnoreCase(etatPcPlanCalculRetenu)));
    }

    /**
     * Retourne un chaine correspondant à la description de la somme des jours d'appoint, si il y en a, ou un message
     * aucun JA
     *
     * @return chaine de caractère
     */
    public String getJoursAppoint() {

        String valRetour = null;

        SimpleJoursAppoint simpleJoursAppoint = pcAccordee.getFirstJoursAppoint();
        if ((simpleJoursAppoint != null) && !simpleJoursAppoint.isNew()) {
            float montantJour = Float.parseFloat(simpleJoursAppoint.getMontantJournalier());
            int nbJours = Integer.parseInt(simpleJoursAppoint.getNbrJoursAppoint());
            String libelle = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_D_PC_ACCORDEE_JA");

            float montant = montantJour * nbJours;
            valRetour = nbJours + " " + libelle + " " + new FWCurrency(montantJour).toStringFormat() + " = "
                    + new FWCurrency(montant).toStringFormat();
        } else {
            valRetour = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_D_PC_ACCORDEE_NOJA");
        }
        return valRetour;
    }

    /**
     * Gestion ERREURS
     *
     * @return
     */
    private String getLastModification() {
        if ((this != null) && (getSpy() != null)) {
            return "" + getSpy().getDate() + ", " + getSpy().getTime() + " - " + getSpy().getUser();
        } else {
            return "";
        }
    }

    public List<PlanDeCalculWitMembreFamille> getListeEnfant() {

        List<PlanDeCalculWitMembreFamille> listeEnfant = new ArrayList<PlanDeCalculWitMembreFamille>();

        for (PlanDeCalculWitMembreFamille membre : listMembreFamille) {
            if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(membre.getDroitMembreFamille().getSimpleDroitMembreFamille()
                    .getCsRoleFamillePC())) {
                listeEnfant.add(membre);
            }
        }

        return listeEnfant;
    }

    public List<PlanDeCalculWitMembreFamille> getListeEnfantsPlanCalcul(String idPcal) {

        List<PlanDeCalculWitMembreFamille> listeDeRetour = new ArrayList<PlanDeCalculWitMembreFamille>();

        for (PlanDeCalculWitMembreFamille plan : getOrderedMapByPlanCalcul().get(idPcal)) {
            if (plan.getDroitMembreFamille().getSimpleDroitMembreFamille().getCsRoleFamillePC()
                    .equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT)) {
                listeDeRetour.add(plan);
            }
        }

        return listeDeRetour;
    }

    public List<SimplePlanDeCalcul> getListePlanCalculs() {
        return listePlanCalculs;
    }

    public Map<String, List<PlanDeCalculWitMembreFamille>> getMapEnfants() {

        Map<String, List<PlanDeCalculWitMembreFamille>> mapEnfants = new HashMap<String, List<PlanDeCalculWitMembreFamille>>();

        for (String idPcal : getOrderedMapByPlanCalcul().keySet()) {

            ArrayList<PlanDeCalculWitMembreFamille> listeEnfant = new ArrayList<PlanDeCalculWitMembreFamille>();

            for (PlanDeCalculWitMembreFamille pcal : getOrderedMapByPlanCalcul().get(idPcal)) {
                if (IPCDroits.CS_ROLE_FAMILLE_ENFANT.equals(pcal.getDroitMembreFamille().getSimpleDroitMembreFamille()
                        .getCsRoleFamillePC())) {
                    listeEnfant.add(pcal);
                }
            }

            mapEnfants.put(idPcal, listeEnfant);
        }

        return mapEnfants;
    }

    public String getMontantAllocationNoel() {
        if (!simpleAllocationNoel.isNew()) {
            return new FWCurrency(new BigDecimal(simpleAllocationNoel.getMontantAllocation()).divide(
                    new BigDecimal(simpleAllocationNoel.getNbrePersonnes())).toString()).toStringFormat();
        }
        return null;
    }

    public String getMontantTotalAllocationNoel() {
        if (!simpleAllocationNoel.isNew()) {
            return new FWCurrency(simpleAllocationNoel.getMontantAllocation()).toStringFormat();
        }
        return null;
    }

    public String getNbPersonneInAllacationNoel() {
        if (!simpleAllocationNoel.isNew()) {
            return simpleAllocationNoel.getNbrePersonnes();
        }
        return null;
    }

    public String getNoAvsConjoint() {
        return pcAccordee.getPersonneEtendueConjoint().getPersonneEtendue().getNumAvsActuel();
    }

    public String getNoAvsRequerant() {
        return pcAccordee.getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
    }

    /**
     * Retourne une map trié par idPlan de calcul du modele de recherche
     *
     * @return
     */
    public Map<String, List<PlanDeCalculWitMembreFamille>> getOrderedMapByPlanCalcul() {

        // Liste typé du modèle de recherche
        List<PlanDeCalculWitMembreFamille> list = PersistenceUtil.typeSearch(planDeCalculSearch,
                planDeCalculSearch.whichModelClass());

        Map<String, List<PlanDeCalculWitMembreFamille>> planDeCalculParId = JadeListUtil.groupBy(list,
                new Key<PlanDeCalculWitMembreFamille>() {
                    @Override
                    public String exec(PlanDeCalculWitMembreFamille e) {
                        return e.getSimplePlanDeCalcul().getIdPlanDeCalcul();
                    }
                });

        return planDeCalculParId;
    }

    public String getParentHrefLink() {
        if (getIsPcaChildrenFromOtherPca()) {
            String detailLink = "pegasus?userAction=" + IPCActions.ACTION_PCACCORDEE_DETAIL + ".afficher&selectedId="
                    + pcAccordee.getSimplePCAccordee().getIdPcaParent() + "&idChild=" + pcAccordee.getId();
            return detailLink;
        } else {
            return "";
        }

    }

    /**
     * @return the pcAccordee
     */
    public PCAccordee getPcAccordee() {
        return pcAccordee;
    }

    /**
     * Retourne le modele complex de la personne
     *
     * @return personneComplexModel
     */
    private PersonneEtendueComplexModel getPersonneEtendue(String membre) {
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return pcAccordee.getPersonneEtendue();
        } else if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
            return pcAccordee.getPersonneEtendueConjoint();
        } else {
            return null;
        }

    }

    public String getRequerantInfos() {
        return getTiersInfosAsString(IPCDroits.CS_ROLE_FAMILLE_REQUERANT);
    }

    /**
     * Retourne l'objet session
     *
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /**
     * Retourne le modele simpleVersionDroit de la pc accordee
     *
     * @return instance de SimpleVersionDroit, version du droit de la pcaccordee
     */
    public SimpleVersionDroit getSimpleVersionDroit() {
        return pcAccordee.getSimpleVersionDroit();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(pcAccordee.getSimplePCAccordee().getSpy());
    }

    public String getStatusPcOctroi() {
        return PC_STATUS_OCTROI;
    }

    public String getStatusPcPartiel() {
        return PC_STATUS_PARTIEL;
    }

    public String getStatusPcRefus() {
        return PC_STATUS_REFUS;
    }

    /**
     * Retourne le Tiers
     *
     * @return
     */
    public TiersSimpleModel getTiers() {
        return pcAccordee.getPersonneEtendue().getTiers();
    }

    public TiersSimpleModel getTiers(String membre) {
        // requerant d'apres dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return pcAccordee.getPersonneEtendue().getTiers();
        }
        // Personne beneficiare d'apres decision
        else {
            return pcAccordee.getPersonneEtendueConjoint().getTiers();
        }

    }

    private String getTiersInfosAsString(String membre) {

        PersonneEtendueComplexModel personneEtendue = getPersonneEtendue(membre);

        String NSS = personneEtendue.getPersonneEtendue().getNumAvsActuel();
        String NomPrenom = personneEtendue.getTiers().getDesignation1() + " "
                + personneEtendue.getTiers().getDesignation2();
        String dateNaissance = personneEtendue.getPersonne().getDateNaissance();
        String sexe = getSession().getCodeLibelle(personneEtendue.getPersonne().getSexe());
        String nationalite = PCUserHelper.getLibellePays(personneEtendue);// this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    public boolean getUseAllocationNoel() throws PropertiesException {
        return EPCProperties.ALLOCATION_NOEL.getBooleanValue();
    }

    public String getValiditeInfos() {
        String debut = getPcAccordee().getSimplePCAccordee().getDateDebut();
        String fin = getPcAccordee().getSimplePCAccordee().getDateFin();
        String retour = "";
        if (JadeStringUtil.isEmpty(fin)) {
            // Chargement texte
            String chaine = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PCACCORDE_D_VALABLE_DESLE");

            retour = chaine + " " + debut;
        } else {
            // Chargement texte
            String chaine = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PCACCORDE_D_VALABLE_DU_AU");
            chaine = PRStringUtils.replaceString(chaine, "{datedebut}", debut);
            retour = chaine + " " + fin;
        }
        return retour;
    }

    public String getViewBeanErrorsToDisplayPopUp() {
        String errorMsg = globaz.framework.util.FWTextFormatter.slash(
                globaz.framework.util.FWTextFormatter.newLineToBr(getMessage()), '\"');
        setMessage("");
        setMsgType(globaz.framework.bean.FWViewBeanInterface.OK);
        return errorMsg;
    }

    public String getWebAppColor() {
        return Jade.getInstance().getWebappBackgroundColor();
    }

    /**
     * Tri des plan de calculs passé en paramètres Pour le splans en refus et octroi partiel, c'est lexcdent de revenus
     * qui définit l'ordre
     *
     * @param listePlanCalculToOrder
     */
    private void orderListePlanCalcul(List<SimplePlanDeCalcul> listePlanCalculToOrder) {

        Collections.sort(listePlanCalculToOrder, new Comparator<SimplePlanDeCalcul>() {

            @Override
            public int compare(SimplePlanDeCalcul o1, SimplePlanDeCalcul o2) {
                int excedentO1 = Integer.parseInt(o1.getExcedentPCAnnuel());
                int excedentO2 = Integer.parseInt(o2.getExcedentPCAnnuel());

                // Si l'excedent est plus petit
                if (excedentO1 < excedentO2) {
                    return 1;
                } else if (excedentO1 > excedentO2) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

    }

    private void readPca() throws PCAccordeeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {
        pcAccordee = PegasusServiceLocator.getPCAccordeeService().readDetail(pcAccordee.getId());
    }

    @Override
    public void retrieve() throws Exception {

        readPca();

        searchPlanCalculWithMembreFamille();

        if (planDeCalculSearch == null) {
            throw new PCAccordeeException("The pcaccordee is coming from reprise and not contain data");
        }
        fillListeMembreFamille();

        fillListePlanCalculs();

        searchSimpleAllocationNoel();

    }

    /**
     * Lance la recherche des plans de de calcul et retourne le modele de rechecrche
     *
     * @return
     * @throws JadePersistenceException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws PersonneDansPlanCalculException
     */
    private void searchPlanCalculWithMembreFamille() throws PersonneDansPlanCalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
        search.setDefinedSearchSize(JadeAbstractSearchModel.SIZE_NOLIMIT);

        String idPcaToSearch = "";
        // Gestion de l'id PCAccordee, idParent ou standard, si idPcaParent, on recheche la pca parente
        if (!JadeStringUtil.isBlankOrZero(pcAccordee.getSimplePCAccordee().getIdPcaParent())) {
            idPcaToSearch = pcAccordee.getSimplePCAccordee().getIdPcaParent();
        } else {
            idPcaToSearch = pcAccordee.getId();
        }
        search.setForIdPCAccordee(idPcaToSearch);
        // return (PlanDeCalculWitMembreFamilleSearch)PegasusServiceLocator.getPCAccordeeService().search(search);
        planDeCalculSearch = PegasusServiceLocator.getPCAccordeeService().search(search);

    }

    private void searchSimpleAllocationNoel() throws PropertiesException, JadePersistenceException,
            PCAccordeeException, AllocationDeNoelException, JadeApplicationServiceNotAvailableException {
        if (getUseAllocationNoel()) {
            SimpleAllocationNoel allocationNoel = PegasusServiceLocator.getSimpleAllocationDeNoelService()
                    .readAllocationNoelByIdPca(pcAccordee.getId());
            if ((allocationNoel != null) && !allocationNoel.isNew()) {
                simpleAllocationNoel = allocationNoel;
            }
        }
    }

    public void setDateLiberation(String dateLiberation) {
        this.dateLiberation = dateLiberation;
    }

    public void setEntete(REEnteteBlocage entete) {
        this.entete = entete;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        pcAccordee.getSimplePCAccordee().setId(newId);

    }

    public void setIdPlanCalcule(String idPlanCalcule) {
        this.idPlanCalcule = idPlanCalcule;
    }

    /**
     * @param pcAccordee the pcAccordee to set
     */
    public void setPcAccordee(PCAccordee pcAccordee) {
        this.pcAccordee = pcAccordee;
    }

    /**
     * Recupére la part cantonale
     *
     * @return
     * @throws JadeApplicationException, JadePersistenceException
     */
    public String getPartCantonale(SimplePlanDeCalcul plancalcul) throws JadeApplicationException,
            JadePersistenceException {
        return CorvusServiceLocator.getSimpleVentilationService().getPartCantonaleTotal(plancalcul.getIdPCAccordee());
    }


    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {
        pcAccordee = PegasusServiceLocator.getPCAccordeeService().update(pcAccordee);
        // si le plan de calcul retenu a changé...
        if (!JadeStringUtil.isEmpty(idPlanCalcule)) {
            SimplePlanDeCalcul planDeCalulRetenu = PegasusServiceLocator.getPCAccordeeService().updatePlanDecalculeRetenu(idPlanCalcule);

            // Mise à jour de la ventilation des parts cantonales.
            PeriodePCAccordee.TypeSeparationCC separationCC = calculTypeSeparationCC(planDeCalulRetenu);
            PegasusImplServiceLocator.getCalculPersistanceService().updateVentilationPartCantonalePC(separationCC, pcAccordee.getSimplePCAccordee().getIdPrestationAccordee(), false, planDeCalulRetenu.getMontantPartCantonale());
            if (PeriodePCAccordee.TypeSeparationCC.CALCUL_SEPARE_MALADIE.equals(separationCC)) {
                PegasusImplServiceLocator.getCalculPersistanceService().updateVentilationPartCantonalePC(separationCC, pcAccordee.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee(), true, planDeCalulRetenu.getMontantPartCantonale());
            } else if (PeriodePCAccordee.TypeSeparationCC.CALCUL_DOM2_PRINCIPALE.equals(separationCC)) {
                PegasusImplServiceLocator.getCalculPersistanceService().updateVentilationPartCantonalePC(separationCC, pcAccordee.getSimplePrestationsAccordeesConjoint().getIdPrestationAccordee(), true, planDeCalulRetenu.getMontantPartCantonale());
            }

            CalculComparatifServiceImpl.updateJoursAppoint(pcAccordee);

            if (EPCProperties.ALLOCATION_NOEL.getBooleanValue()) {
                SimpleAllocationNoel simpleAllocationNoel = PegasusServiceLocator.getSimpleAllocationDeNoelService()
                        .readAllocationNoelByIdPca(pcAccordee.getId());

                if ((simpleAllocationNoel != null) && !simpleAllocationNoel.isNew()) {
                    int nbreMembres = PegasusServiceLocator.getPCAccordeeService().countMembreFamilleForPlanRetenu(
                            idPlanCalcule);

                    BigDecimal montantParPersonne = new BigDecimal(this.simpleAllocationNoel.getMontantAllocation())
                            .divide(new BigDecimal(this.simpleAllocationNoel.getNbrePersonnes()));
                    simpleAllocationNoel.setNbrePersonnes(String.valueOf(nbreMembres));
                    simpleAllocationNoel.setMontantAllocation(montantParPersonne.multiply(new BigDecimal(nbreMembres))
                            .toString());
                    PegasusServiceLocator.getSimpleAllocationDeNoelService().update(simpleAllocationNoel);
                }
            }
        }
    }

    private PeriodePCAccordee.TypeSeparationCC calculTypeSeparationCC(SimplePlanDeCalcul planDeCalulRetenu) throws JadeApplicationServiceNotAvailableException {

        PeriodePCAccordee.TypeSeparationCC typeSeparationCC;
        String byteToArrayString = new String(planDeCalulRetenu.getResultatCalcul());
        TupleDonneeRapport tupleRoot = PegasusImplServiceLocator.getCalculPersistanceService().deserialiseDonneesCcXML(byteToArrayString);
        typeSeparationCC = PegasusCalculUtil.getTypeSeparation(tupleRoot);
        return typeSeparationCC;
    }


}
