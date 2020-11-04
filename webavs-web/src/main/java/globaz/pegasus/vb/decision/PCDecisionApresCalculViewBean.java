/**
 *
 */
package globaz.pegasus.vb.decision;

import ch.globaz.pegasus.business.exceptions.models.crancier.CreancierException;
import ch.globaz.pegasus.business.models.creancier.CreanceAccordeeSearch;
import ch.globaz.pegasus.business.models.creancier.CreancierSearch;
import ch.globaz.pegasus.businessimpl.services.models.decision.DecisionApresCalculServiceImpl;
import globaz.babel.api.ICTDocument;
import globaz.externe.IPRConstantesExternes;
import globaz.framework.bean.FWViewBeanInterface;
import globaz.framework.controller.FWAction;
import globaz.framework.util.FWCurrency;
import globaz.globall.db.*;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.pegasus.utils.PCUserHelper;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.tools.nnss.PRNSSUtil;

import java.math.BigDecimal;
import java.rmi.RemoteException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ch.globaz.babel.business.exception.CatalogueTexteException;
import ch.globaz.babel.business.services.BabelServiceLocator;
import ch.globaz.common.business.language.LanguageResolver;
import ch.globaz.common.business.models.CTDocumentImpl;
import ch.globaz.common.properties.PropertiesException;
import ch.globaz.jade.business.models.Langues;
import ch.globaz.pegasus.business.constantes.EPCCodeAmal;
import ch.globaz.pegasus.business.constantes.EPCProperties;
import ch.globaz.pegasus.business.constantes.IPCCatalogueTextes;
import ch.globaz.pegasus.business.constantes.IPCDecision;
import ch.globaz.pegasus.business.constantes.IPCDemandes;
import ch.globaz.pegasus.business.constantes.IPCDroits;
import ch.globaz.pegasus.business.constantes.IPCValeursPlanCalcul;
import ch.globaz.pegasus.business.constantes.decision.EtatDecision;
import ch.globaz.pegasus.business.exceptions.models.decision.DecisionException;
import ch.globaz.pegasus.business.exceptions.models.demande.DemandeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.AllocationDeNoelException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PCAccordeeException;
import ch.globaz.pegasus.business.exceptions.models.pcaccordee.PersonneDansPlanCalculException;
import ch.globaz.pegasus.business.exceptions.models.variablemetier.VariableMetierException;
import ch.globaz.pegasus.business.models.decision.CopiesDecision;
import ch.globaz.pegasus.business.models.decision.CopiesDecisionSearch;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalcul;
import ch.globaz.pegasus.business.models.decision.DecisionApresCalculSearch;
import ch.globaz.pegasus.business.models.decision.ListDecisions;
import ch.globaz.pegasus.business.models.decision.ListDecisionsSearch;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecision;
import ch.globaz.pegasus.business.models.decision.SimpleAnnexesDecisionSearch;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamille;
import ch.globaz.pegasus.business.models.pcaccordee.PlanDeCalculWitMembreFamilleSearch;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleAllocationNoel;
import ch.globaz.pegasus.business.models.pcaccordee.SimpleJoursAppoint;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalcul;
import ch.globaz.pegasus.business.models.pcaccordee.SimplePlanDeCalculSearch;
import ch.globaz.pegasus.business.services.PegasusServiceLocator;
import ch.globaz.pegasus.businessimpl.services.PegasusImplServiceLocator;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.model.PersonneEtendueSimpleModel;
import ch.globaz.pyxis.business.model.PersonneSimpleModel;
import ch.globaz.pyxis.business.model.TiersSimpleModel;
import ch.globaz.pyxis.business.service.AdresseService;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * @author SCE 27 juil. 2010
 */
public class PCDecisionApresCalculViewBean extends BJadePersistentObjectViewBean implements IPCDecisionViewBean {

    private static final String DATE_DECISION_AMAL_REPLACE = "{date_decision_amal}";
    // Etat de la zone annexe du formualaire
    private String annexesIsChanged = "0";
    Map<Langues, CTDocumentImpl> documentsBabel;
    private ICTDocument babelDoc = null;
    private String blocReductionAmal = null;
    // Liste personnes comprises
    private String conjointCompris = null;
    // Liste contenant les chaines de caracères envoyés pour les copies
    private ArrayList<String> copies = new ArrayList<String>();
    // Etat de la zone copie du formulaire
    private String copiesIsChanged = "0";
    private String csGenreDecision = null;
    // private String dateDecRecente = null;
    // instance de la classe métier
    private DecisionApresCalcul decisionApresCalcul = null;
    private Boolean displayBlocReductionAmal = true;
    private String enfantsCompris = null;
    private String ERROR_ADRESS_MESSAGE = null;
    // Id decision header passé en param
    private String idDecision = null;
    // Id Droit et version Droit
    // private String idDroit = null;
    private String idTiersCopies = null; // id tiers renvoyé
    private String idVersionDroit = null;
    private boolean isLotOuvert = false;
    private String listeAnnexesString = null;
    // Chaine contenant les id des décision pour le meme lot
    private String lotDecision = null;

    // Monnaie devise
    private String monnaie = "CHF";

    // nbre de personnes comprises sans le requérant
    private int nbresPersonnesComprises = 0;
    private DecisionApresCalculSearch search = null;
    private SimpleAllocationNoel simpleAllocationNoel = new SimpleAllocationNoel();
    private Boolean decisionProvisoire = null;

    private String textRemarqueNormal = null;
    private String textRemarqueProvisoire = null;

    // gestion des utilisateurs-gestionnaire, à voir
    // private JadeUser[] users = null;
    // Etat de la validation, si validation¨
    // private String validAction = null;

    /**
     * Constructeur simple
     *
     * @throws JadePersistenceException
     */
    public PCDecisionApresCalculViewBean() throws JadePersistenceException, RemoteException {
        super();
        decisionApresCalcul = new DecisionApresCalcul();

        ERROR_ADRESS_MESSAGE = "<div class='noAdresse ui-state-error ui-corner-all'>"
                + "<span class='ui-icon ui-icon-alert' style='float: left; margin-right: .3em;'></span>" + "<span>"
                + ((BSession) getISession()).getLabel("JSP_PC_ADRESSE_INTROUVABLE") + "</span>" + "</div>";

    }

    protected void _update(FWViewBeanInterface viewBean, FWAction action, globaz.globall.api.BISession session)
            throws Exception {
        ((BIPersistentObject) viewBean).update();
    }

    public boolean getIsDecisionPasEnRefus() throws DecisionException {
        return getEtatDecision().equals(EtatDecision.OCTROI) || getEtatDecision().equals(EtatDecision.PARTIEL);
    }

    /**
     * Retourne l'état de la décision refus, octroi ou partiel
     *
     * @return, contante enum de EtatDecision
     * @throws DecisionException
     */
    private EtatDecision getEtatDecision() throws DecisionException {

        if (decisionApresCalcul.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {
            return EtatDecision.OCTROI;
        } else if (decisionApresCalcul.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
            return EtatDecision.PARTIEL;
        } else {
            return EtatDecision.REFUS;
        }

    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#add()
     */
    @Override
    public void add() throws Exception {
    }

    /**
     * Retourne une chaine de caractère comprenant les membres de familles compris dans le calcul
     *
     * @return chaine de caractère
     * @throws PersonneDansPlanCalculException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     */
    private void dealPersonnesComprises() throws PersonneDansPlanCalculException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {

        try {
            // recherche des membres familles compris dans le calcul PC, avec id pcaccordées
            PlanDeCalculWitMembreFamilleSearch search = new PlanDeCalculWitMembreFamilleSearch();
            search.setForIdPcal(decisionApresCalcul.getPcAccordee().getPlanRetenu().getIdPlanDeCalcul());
            search.setForComprisPcal(Boolean.TRUE);
            search.setOrderKey("orderByNaissance");
            // search.setForCsRoleFamille(IPCDroits.CS_ROLE_FAMILLE_ENFANT);

            // String membreCompris = "";
            StringBuilder enfantCompris = new StringBuilder();
            for (JadeAbstractModel model : PegasusServiceLocator.getPCAccordeeService().search(search)
                    .getSearchResults()) {
                String csRole = IPCDroits.CS_ROLE_FAMILLE_CONJOINT;
                if (isForConjointDom2R()
                        || decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getCsRoleBeneficiaire()
                        .equals(IPCDroits.CS_ROLE_FAMILLE_CONJOINT)) {
                    csRole = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;
                }
                // Conjoint
                if (((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getSimpleDroitMembreFamille()
                        .getCsRoleFamillePC().equals(csRole)) {
                    StringBuilder conjoint = new StringBuilder();
                    conjoint.append(
                            ((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille().getNom())
                            .append(" ")
                            .append(((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                    .getPrenom());
                    conjointCompris = conjoint.toString();
                    nbresPersonnesComprises++;
                }

                // Enfants
                if (((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getSimpleDroitMembreFamille()
                        .getCsRoleFamillePC().equals(IPCDroits.CS_ROLE_FAMILLE_ENFANT)) {
                    enfantCompris
                            .append(((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                    .getNom())
                            .append(" ")
                            .append(((PlanDeCalculWitMembreFamille) model).getDroitMembreFamille().getMembreFamille()
                                    .getPrenom()).append(", ");
                    nbresPersonnesComprises++;
                }

            }
            if (!"".equals(enfantCompris.toString())) {
                // on enleve la dernier virgule
                enfantsCompris = enfantCompris.substring(0, enfantCompris.length() - 2);
            } else {
                enfantsCompris = "";
            }
            if (conjointCompris == null) {
                conjointCompris = "";
            }

        } catch (PCAccordeeException e) {
            throw new PersonneDansPlanCalculException("Error in getPlanRetenu", e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#delete()
     */
    @Override
    public void delete() throws Exception {
    }

    /**
     * Check si décision ok, pas de la reprise
     *
     * @throws PCAccordeeException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws DecisionException
     * @throws JadePersistenceException
     * @throws Exception
     */
    public void detail() throws PCAccordeeException, JadeApplicationServiceNotAvailableException, DecisionException,
            JadePersistenceException, Exception {
        // / create objet recherche
        DecisionApresCalculSearch search = new DecisionApresCalculSearch();
        search.setForIdDecisionHeader(getIdDecision());
        int nbre = PegasusServiceLocator.getDecisionApresCalculService().count(search);

        // Si pas de résultat, décision de reprise, pas de détails
        if (nbre == 0) {
            throw new DecisionException("pegasus.decisions.reprise.nodetail");
        }
    }

    /**
     * Retourne l'adresse de courier du tiers
     *
     * @return String, adresse de courrier
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdresseCourrier() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {

        AdresseTiersDetail detailTiers = TIBusinessServiceLocator.getAdresseService().getAdresseTiers(
                decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getIdTiersCourrier(), Boolean.TRUE,
                JACalendar.todayJJsMMsAAAA(), IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE,
                AdresseService.CS_TYPE_COURRIER, null);

        return detailTiers.getAdresseFormate() != null ? detailTiers.getAdresseFormate() : ERROR_ADRESS_MESSAGE;
    }

    /**
     * Retourne l'adresse de paiement du tiers
     *
     * @return String, adresse de paiement
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiement() throws JadePersistenceException, JadeApplicationException {

        String idTiers = "";

        if (isForConjointDom2R()) {
            idTiers = decisionApresCalcul.getPcAccordee().getSimpleInformationsComptabiliteConjoint()
                    .getIdTiersAdressePmt();
        } else {
            idTiers = decisionApresCalcul.getPcAccordee().getSimpleInformationsComptabilite().getIdTiersAdressePmt();
        }

        if (JadeStringUtil.isEmpty(idTiers)) {
            return ERROR_ADRESS_MESSAGE;
        }

        AdresseTiersDetail detailTiers = TIBusinessServiceLocator.getAdresseService().getAdressePaiementTiers(idTiers,
                Boolean.TRUE, IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE, JACalendar.todayJJsMMsAAAA(),
                null);

        return detailTiers.getAdresseFormate() != null ? detailTiers.getAdresseFormate() : ERROR_ADRESS_MESSAGE;
    }

    /**
     * @return the annexesIsChanged
     */
    public String getAnnexesIsChanged() {
        return annexesIsChanged;
    }

    public String getBlocReductionAmal() {
        if (blocReductionAmal != null) {
            return blocReductionAmal;
        } else {
            return "-";
        }
    }

    public String getConjointCompris() {
        return conjointCompris;
    }

    /**
     * @return the copies
     */
    public ArrayList<String> getCopies() {
        return copies;
    }

    /**
     * @return the copiesIsChanged
     */
    public String getCopiesIsChanged() {
        return copiesIsChanged;
    }

    /**
     * Retourne le code système du genre de la decision
     *
     * @return
     */
    public String getCsGenreDecision() {
        return csGenreDecision;
    }

    /**
     * @return the decisionApresCalcul
     */
    public DecisionApresCalcul getDecisionApresCalcul() {
        return decisionApresCalcul;
    }

    /**
     * Retourne le tyoe de la decision
     *
     * @return
     */
    public String getDecisionType() {
        return "DECISION_APRES_CALCUL";
    }

    public Boolean getDisplayBlocReductionAmal() {
        return displayBlocReductionAmal;
    }

    public String getEnfantsCompris() {
        return enfantsCompris;
    }

    public String getGedLabel() {
        return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_GED_LINK_LABEL");
    }

    /**
     * Retourne le gestionnaire
     *
     * @return String, nom du gestionnaire
     */
    public String getGestionnaire() {
        return decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getPreparationPar();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#getId()
     */
    @Override
    public String getId() {
        return decisionApresCalcul.getId();
    }

    public String getIddac() {
        return decisionApresCalcul.getSimpleDecisionApresCalcul().getIdDecisionApresCalcul();
    }

    /**
     * @return the idDecision
     */
    @Override
    public String getIdDecision() {
        return idDecision;
    }

    /**
     * Reroune l'id de la decision lié, le cas échéant, 0 dans le cas contraire
     *
     * @return
     */
    public String getIdDecisionConjoint() {
        if (!JadeStringUtil.isBlank(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionConjoint())) {
            return decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionConjoint();

        } else {
            return "0";
        }
    }

    /**
     * Retourn l'id du header de la decision
     *
     * @return
     */
    public String getIdDecisionHeader() {
        return decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getIdDecisionHeader();
    }

    /**
     * Retourne l'id de la demande PC
     *
     * @return
     */
    public String getIdDemandePc() {
        return decisionApresCalcul.getVersionDroit().getSimpleDroit().getIdDemandePC();
    }

    /**
     * @return the idDroit
     */
    public String getIdDroit() {
        return decisionApresCalcul.getVersionDroit().getSimpleDroit().getIdDroit();
    }

    public String getIdTierRequerant() {
        return getPersonneEtendue(IPCDroits.CS_ROLE_FAMILLE_REQUERANT).getIdTiers();
    }

    public String getIdTierBeneficiaire() {
        return decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire();
    }

    public String getIdTiers() {
        return decisionApresCalcul.getDecisionHeader().getPersonneEtendue().getTiers().getIdTiers();
    }

    /**
     * @return the idTiersCopies
     */
    public String getIdTiersCopies() {
        return idTiersCopies;
    }

    /**
     * @return the idVersionDroit
     */
    public String getIdVersionDroit() {
        return idVersionDroit;
    }

    /**
     * Si l'idTier de la demande n'est pas egal a l'idTiers Beneficiaire de la decision, alors conjoint
     *
     * @return
     */
    private Boolean isForConjointDom2R() {

        return !decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getIdTiersBeneficiaire()
                .equals(decisionApresCalcul.getPcAccordee().getSimplePrestationsAccordees().getIdTiersBeneficiaire());
    }

    /**
     * Retourne un chaine correspondant à la description de la somme des jours d'appoint, si il y en a, ou un message
     * aucun JA
     *
     * @return chaine de caractère
     */
    public String getJoursAppoint() {

        String valRetour = null;
        SimpleJoursAppoint joursAppoint = decisionApresCalcul.getPcAccordee().getFirstJoursAppoint();

        if ((joursAppoint != null) && !joursAppoint.isNew()) {
            float montantJour = Float.parseFloat(joursAppoint.getMontantJournalier());
            int nbJours = Integer.parseInt(joursAppoint.getNbrJoursAppoint());
            String libelle = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECALCUL_D_JA");

            float montant = montantJour * nbJours;
            valRetour = nbJours + " " + libelle + " " + new FWCurrency(montantJour).toStringFormat() + " = "
                    + new FWCurrency(montant).toStringFormat();
        } else {
            valRetour = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECALCUL_D_NOJA");
        }
        return valRetour;
    }

    /**
     * Retourne la liste des annexes liés à la décision
     *
     * @return
     */
    public ArrayList<SimpleAnnexesDecision> getListeAnnexes() {
        return decisionApresCalcul.getDecisionHeader().getListeAnnexes();
    }

    /**
     * @return the listeAnnexesString
     */
    public String getListeAnnexesString() {
        return listeAnnexesString;
    }

    /**
     * Retourne la liste des copies
     *
     * @return
     */
    public ArrayList<CopiesDecision> getListeCopies() {
        return decisionApresCalcul.getDecisionHeader().getListeCopies();
    }

    /**
     * Retourne le lot de la décision
     *
     * @return
     */
    public String getLotDecision() {
        return lotDecision;
    }

    /**
     * Retourne la monnaie
     *
     * @return
     */
    public String getMonnaie() {
        return monnaie;
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

    public String getNoAvs() {
        return decisionApresCalcul.getDecisionHeader().getPersonneEtendue().getPersonneEtendue().getNumAvsActuel();
    }

    /**
     * @return the noDroit
     */
    public String getNoVersion() {
        return decisionApresCalcul.getVersionDroit().getSimpleVersionDroit().getNoVersion();
    }

    /**
     * Retourne l'état de la pca en fonction du résultat du plan de calcul PARTIEL, OCTROI, ou montant si octroyé
     *
     * @return
     */
    public String getPCAResultState() {

        // si octroi
        if (decisionApresCalcul.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI)) {
            String amount = null;
            amount = new FWCurrency(decisionApresCalcul.getPlanCalcul().getMontantPCMensuelle()).toStringFormat();
            return amount;
        }
        // refus ou octroi partiel
        else {
            // octroi partiel
            if (decisionApresCalcul.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_OCTROI_PARTIEL)) {
                return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECISION_OCTROI_PARTIEL");
            }
            // refus
            else {
                return BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECISION_REFUS");
            }
        }

    }

    /**
     * Retourne le modele simple de la personne
     *
     * @return personneSimpleModel
     */
    private PersonneSimpleModel getPersonne(String membre) {
        // Requerant d'apres dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionApresCalcul.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue().getPersonne();
        }
        // PErsonne beneficiare d'apres decision
        else {
            return decisionApresCalcul.getDecisionHeader().getPersonneEtendue().getPersonne();

        }

    }

    public PersonneEtendueComplexModel getPersonneBeneficiaire() {
        return decisionApresCalcul.getDecisionHeader().getPersonneEtendue();
    }

    /**
     * Retourne le modele complex de la personne
     *
     * @return personneComplexModel
     */
    private PersonneEtendueSimpleModel getPersonneEtendue(String membre) {
        // Requerant d'apres le dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionApresCalcul.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue().getPersonneEtendue();
        } else {
            // Sinon personne bénéficiaire de la decision
            return decisionApresCalcul.getDecisionHeader().getPersonneEtendue().getPersonneEtendue();
        }

    }

    private PersonneEtendueComplexModel getPersonneEtendueComplex(String membre) {
        // Requerant d'apres le dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionApresCalcul.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue();
        } else {
            // Sinon personne bénéficiaire de la decision
            return decisionApresCalcul.getDecisionHeader().getPersonneEtendue();
        }
    }

    public SimplePlanDeCalcul getPlanDeCalculeRetenu() throws PCAccordeeException,
            JadeApplicationServiceNotAvailableException, JadePersistenceException {
        SimplePlanDeCalcul simplePlanDeCalcul = PegasusServiceLocator.getPCAccordeeService()
                .findSimplePlanCalculeRetenu(
                        decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getIdPCAccordee());

        /*
         * for (SimplePlanDeCalcul pl : this.decisionApresCalcul.getPcAccordee().getPlanCalculs()) { if
         * (pl.getIsPlanRetenu()) { simplePlanDeCalcul = pl; } }
         */
        return simplePlanDeCalcul;
    }

    public DecisionApresCalculSearch getSearch() {
        return search;
    }

    /**
     * Retourne l'objet session
     *
     * @return objet BSession
     */
    public BSession getSession() {
        return (BSession) getISession();
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.vb.BJadePersistentObjectViewBean#getSpy()
     */
    @Override
    public BSpy getSpy() {
        return new BSpy(decisionApresCalcul.getSpy());
    }

    /**
     * Retourne le Tiers
     *
     * @return
     */
    public TiersSimpleModel getTiers(String membre) {
        // requerant d'apres dossier
        if (membre.equals(IPCDroits.CS_ROLE_FAMILLE_REQUERANT)) {
            return decisionApresCalcul.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                    .getPersonneEtendue().getTiers();
        }
        // Personne beneficiare d'apres decision
        else {
            return decisionApresCalcul.getDecisionHeader().getPersonneEtendue().getTiers();
        }

    }

    /**
     * Formatte une chaine de caratere pour afficher les infos du requérant
     *
     * @return
     */
    public String getTiersInfosAsString(String membre) {

        // String membre = IPCDroits.CS_ROLE_FAMILLE_REQUERANT;

        String NSS = getPersonneEtendue(membre).getNumAvsActuel();
        String NomPrenom = getTiers(membre).getDesignation1() + " " + getTiers(membre).getDesignation2();
        String dateNaissance = getPersonne(membre).getDateNaissance();
        String sexe = getSession().getCodeLibelle(getPersonne(membre).getSexe());
        String nationalite = PCUserHelper.getLibellePays(getPersonneEtendueComplex(membre).getTiers());// this.getSession().getCodeLibelle(

        String reqInfos = PRNSSUtil.formatDetailRequerantDetail(NSS, NomPrenom, dateNaissance, sexe, nationalite);

        return reqInfos;
    }

    public boolean getUseAllocationNoel() throws PropertiesException {
        return EPCProperties.ALLOCATION_NOEL.getBooleanValue();
    }

    /**
     * @return the validAction
     */
    // public String getValidAction() {
    // return this.validAction;
    // }
    public String getValiditeInfos() {
        String debut = decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getDateDebutDecision();
        String fin = decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getDateFinDecision();
        String retour = "";
        if (JadeStringUtil.isEmpty(fin)) {
            // Chargement texte
            String chaine = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECALCUL_D_PERIODE_DESLE");

            retour = chaine + " " + debut;
        } else {
            // Chargement texte
            String chaine = BSessionUtil.getSessionFromThreadContext().getLabel("JSP_PC_DECALCUL_D_PERIODE_DE_A");
            chaine = PRStringUtils.replaceString(chaine, "{datedebut}", debut);
            retour = chaine + " " + fin;
        }
        return retour;
    }

    public boolean isPcaEnRefus() {
        return decisionApresCalcul.getPlanCalcul().getEtatPC().equals(IPCValeursPlanCalcul.STATUS_REFUS);
    }

    /**
     * Retourne l'état (true/false) de létat d'insertion de l'att Billag (auto ou manuel)
     *
     * @return
     * @throws Exception
     */
    public boolean hasBillagAuto() throws Exception {
        // if (!isPcaEnRefus()) {
        for (SimpleAnnexesDecision annexe : decisionApresCalcul.getDecisionHeader().getListeAnnexes()) {
            if (IPCDecision.ANNEXE_BILLAG_AUTO.equals(annexe.getCsType())) {
                Langues langueTiers = LanguageResolver.resolveISOCode(decisionApresCalcul.getPcAccordee().getPersonneEtendue().getTiers()
                        .getLangue());
                String prop = getSession().getApplication().getProperty(IPCDecision.DESTINATAIRE_REDEVANCE);
                String message =
                        MessageFormat.format(LanguageResolver.resolveLibelleFromLabel(
                                langueTiers.getCodeIso(), IPCDecision.BILLAG_ANNEXES_STRING, getSession()), prop);
                annexe.setValeur(message);
                return true;
            }
        }
        // }
        return false;
    }

    public Boolean hasConjoint() {
        return !(JadeStringUtil.isBlankOrZero(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionConjoint()));
    }

    public boolean hasDecompte() {
        return !IPCDecision.CS_PREP_COURANT.equals(decisionApresCalcul.getSimpleDecisionApresCalcul()
                .getCsTypePreparation())
                && decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getIsCalculRetro();
    }

    public boolean hasCreancier() throws Exception {
        CreancierSearch creanceAccordeeSearch = new CreancierSearch();
        creanceAccordeeSearch.setForIdDemande(decisionApresCalcul.getPcAccordee().getSimpleDroit().getIdDemandePC());
        if (PegasusServiceLocator.getCreancierService().count(creanceAccordeeSearch) > 0) {
            return true;
        } else {
            return false;
        }

    }

    public Boolean hasPersonnesComprises() {
        return nbresPersonnesComprises > 0;
    }

    /**
     * Retourne true si la decision est prete pour la validation Condition: doit etre une decision apres calcul, et
     * pré-validé
     *
     * @return
     */
    public Boolean isDecisionReadyForValidation() {
        // Si elle est dans l'etat prévalidé
        return (decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getCsEtatDecision()
                .equals(IPCDecision.CS_ETAT_PRE_VALIDE));

    }

    public Boolean isDevalidable() {
        return isValider()
                && isLotOuvert
                && !IPCDemandes.CS_REOUVERT.equals(decisionApresCalcul.getVersionDroit().getDemande()
                .getSimpleDemande().getCsEtatDemande());
    }

    /**
     * Retourne true si la decision est la plus recente du lot
     *
     * @return
     */
    public Boolean isMostRecentDecision() {
        // return this.dateDecRecente.equals(this.decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
        // .getDateDebutDecision());
        return decisionApresCalcul.getSimpleDecisionApresCalcul().getIsMostRecent();
    }

    /**
     * Retourne vrai si etat pre valider
     *
     * @return Boolean etat pre valider
     */
    public Boolean isPreValider() {
        return IPCDecision.CS_ETAT_PRE_VALIDE.equals(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision())
                || IPCDecision.CS_VALIDE.equals(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());
    }

    private Boolean isRequerant() {
        String tiersRequerant = decisionApresCalcul.getVersionDroit().getDemande().getDossier().getDemandePrestation()
                .getDemandePrestation().getIdTiers();

        // Si le tiers requerant est le beneficiare de la decision
        return tiersRequerant.equals(decisionApresCalcul.getDecisionHeader().getPersonneEtendue().getTiers()
                .getIdTiers());
    }

    /**
     * Retourne true si la decision est valider
     *
     * @return
     */
    public Boolean isValider() {
        return IPCDecision.CS_VALIDE.equals(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getCsEtatDecision());
    }

    public Boolean isVersionInitial() throws DemandeException, JadeApplicationServiceNotAvailableException,
            JadePersistenceException {

        return "1".equals(getNoVersion())
                && PegasusImplServiceLocator.getSimpleDemandeService().isDemandeInitial(
                decisionApresCalcul.getVersionDroit().getDemande().getSimpleDemande(),
                decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getDateDebut());
    }

    /**
     * Custom action prévalidation Impression ftp auto des documents si properties Changement de l'état du header
     * -->Prévalidation
     *
     * @throws Exception
     */
    public void prevalider() throws Exception {

        // TRAITEMENT ANNEXES
        processAnnexes();
        // TRAITEMENT COPIES
        processCopies();
        // gestion header
        decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .setCsEtatDecision(IPCDecision.CS_ETAT_PRE_VALIDE);
        // Check adresse refus et octroi partiel
        if (!decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision()
                .equals(IPCDecision.CS_TYPE_REFUS_AC)) {
            PegasusServiceLocator.getValidationDecisionService().checkAdresses(decisionApresCalcul);
        }

        Boolean testPlausiPrevalidation = null;
        try {
            testPlausiPrevalidation = EPCProperties.RPC_TEST_PREVALIDATION.getBooleanValue();
        } catch (PropertiesException e) {
            testPlausiPrevalidation = true;
        }

        if(testPlausiPrevalidation) {
            ListDecisionsSearch listSecisionsSearch = new ListDecisionsSearch();
            listSecisionsSearch.setForVersionDroitApc(decisionApresCalcul.getVersionDroit().getId());
            listSecisionsSearch.setForVersionDroitSup(decisionApresCalcul.getVersionDroit().getId());

            List<String> decisions = new ArrayList<>();
            listSecisionsSearch = PegasusServiceLocator.getDecisionService().searchDecisions(listSecisionsSearch);
            for (JadeAbstractModel model : listSecisionsSearch.getSearchResults()) {
                ListDecisions decision = ((ListDecisions) model);
                decisions.add(decision.getDecisionHeader().getId());
            }

            if (decisions.size() > 1) {
                checkPlausi(decisions.toArray(new String[decisions.size()]));
            } else {
                checkPlausi(decisionApresCalcul.getDecisionHeader().getId());
            }
        }


        // update
        decisionApresCalcul = PegasusServiceLocator.getDecisionApresCalculService().update(decisionApresCalcul);
    }

    /**
     * Methode de test des plausi
     *
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws VariableMetierException
     */
    public void checkPlausi(String... idDecision) throws JadeApplicationServiceNotAvailableException, VariableMetierException, JadePersistenceException {
        PegasusServiceLocator.getRpcService().testPlausiForDecision(idDecision);
    }

    /**
     * Methode de traitement des annexes
     */
    private void processAnnexes() {
        // si changement dans les annexes
        if ("1".equals(annexesIsChanged)) {
            // Remise à zéro de la liste des annexes
            decisionApresCalcul.getDecisionHeader().setListeAnnexes(new ArrayList<SimpleAnnexesDecision>());

            String[] listeAnnexes = null;
            listeAnnexes = listeAnnexesString.split("\\r\\n");

            // si la premiere n'est pas vide
            if (!"".equals(listeAnnexes[0])) {
                // Parcours des objets
                for (String annexe : listeAnnexes) {

                    SimpleAnnexesDecision annexeDecision = new SimpleAnnexesDecision();

                    String annexeTab[] = annexe.split("_");
                    if (IPCDecision.ANNEXE_BILLAG_AUTO.equals(annexeTab[1])) {
                        annexeDecision.setCsType(IPCDecision.ANNEXE_BILLAG_AUTO);
                    } else {
                        annexeDecision.setCsType(IPCDecision.ANNEXE_COPIE_MANUEL);
                    }
                    annexeDecision.setValeur(annexeTab[0]);
                    decisionApresCalcul.getDecisionHeader().addToListeAnnexes(annexeDecision);
                }
            }
        }
    }

    /**
     * Methode de traitement des copies
     */
    private void processCopies() {
        if ("1".equals(copiesIsChanged)) {
            // Remise à zéro de la liste des copies
            decisionApresCalcul.getDecisionHeader().setListeCopies(new ArrayList<CopiesDecision>());

            // parcours des copies dans la liste
            for (String copie : copies) {

                String idTiers = copie.split("-")[1];
                String bValue = copie.split("-")[0];

                String booleanValue = (bValue.split("\\r\\n")[0]);// 9 valeurs

                // pour
                // l'objet

                CopiesDecision copieDecision = new CopiesDecision();
                copieDecision.getSimpleCopiesDecision().setIdTiersCopie(idTiers);

                copieDecision.getSimpleCopiesDecision().setPageDeGarde(booleanValue.charAt(0) == '1');
                copieDecision.getSimpleCopiesDecision().setLettreBase(booleanValue.charAt(1) == '1');
                copieDecision.getSimpleCopiesDecision().setVersementA(booleanValue.charAt(2) == '1');
                copieDecision.getSimpleCopiesDecision().setRemarque(booleanValue.charAt(3) == '1');
                copieDecision.getSimpleCopiesDecision().setMoyensDeDroit(booleanValue.charAt(4) == '1');
                copieDecision.getSimpleCopiesDecision().setSignature(booleanValue.charAt(5) == '1');
                copieDecision.getSimpleCopiesDecision().setRecapitulatif(booleanValue.charAt(6) == '1');
                copieDecision.getSimpleCopiesDecision().setAnnexes(booleanValue.charAt(7) == '1');
                copieDecision.getSimpleCopiesDecision().setCopies(booleanValue.charAt(8) == '1');
                copieDecision.getSimpleCopiesDecision().setPlandeCalcul(booleanValue.charAt(9) == '1');

                decisionApresCalcul.getDecisionHeader().addToListCopies(copieDecision);
            }
        }
    }

    private String replaceDateDecisionAmalInString(String baseString) {
        return PRStringUtils.replaceString(baseString, PCDecisionApresCalculViewBean.DATE_DECISION_AMAL_REPLACE,
                decisionApresCalcul.getSimpleDecisionApresCalcul().getDateDecisionAmal());
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#retrieve()
     */
    @Override
    public void retrieve() throws Exception {
        // / create objet recherche
        DecisionApresCalculSearch search = new DecisionApresCalculSearch();
        search.setForIdDecisionHeader(getIdDecision());
        search = PegasusServiceLocator.getDecisionApresCalculService().search(search);

        decisionApresCalcul = (DecisionApresCalcul) search.getSearchResults()[0];

        // charge les jours d'appoint
        PegasusServiceLocator.getPCAccordeeService().loadJoursAppoint(decisionApresCalcul.getPcAccordee());

        SimplePlanDeCalculSearch planSearch = new SimplePlanDeCalculSearch();
        planSearch.setForIdPCAccordee(decisionApresCalcul.getPcAccordee().getSimplePCAccordee().getId());
        ArrayList<SimplePlanDeCalcul> listePlanCalculs = new ArrayList<SimplePlanDeCalcul>();

        for (SimplePlanDeCalcul plan : PegasusServiceLocator.getPCAccordeeService().searchPlanCalcul(planSearch)) {
            listePlanCalculs.add(plan);
        }
        decisionApresCalcul.getPcAccordee().setPlanCalculs(listePlanCalculs);

        // recherche des annexes et copies
        // annexes
        SimpleAnnexesDecisionSearch annexesDecisionSearch = new SimpleAnnexesDecisionSearch();
        annexesDecisionSearch.setForIdDecisionHeader(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionHeader());
        annexesDecisionSearch = PegasusServiceLocator.getSimpleAnnexesDecisionsService().search(annexesDecisionSearch);
        // Parcours des objets
        for (JadeAbstractModel searchModel : annexesDecisionSearch.getSearchResults()) {
            decisionApresCalcul.getDecisionHeader().addToListeAnnexes((SimpleAnnexesDecision) searchModel);
        }

        // copies
        CopiesDecisionSearch copiesDecisionSearch = new CopiesDecisionSearch();
        copiesDecisionSearch.setForIdDecisionHeader(decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .getIdDecisionHeader());
        copiesDecisionSearch = PegasusServiceLocator.getCopiesDecisionsService().search(copiesDecisionSearch);
        // Parcours des objets
        for (JadeAbstractModel searchModel : copiesDecisionSearch.getSearchResults()) {
            decisionApresCalcul.getDecisionHeader().addToListCopies((CopiesDecision) searchModel);
        }

        // Recherche des pca
        ListDecisionsSearch listDecisionsSearch = new ListDecisionsSearch();
        listDecisionsSearch.setForVersionDroitApc(decisionApresCalcul.getSimpleDecisionApresCalcul()
                .getIdVersionDroit());
        listDecisionsSearch = PegasusServiceLocator.getDecisionService().searchDecisions(listDecisionsSearch);

        StringBuilder returnString = new StringBuilder("");
        String separator = ",";

        for (JadeAbstractModel decision : listDecisionsSearch.getSearchResults()) {
            returnString.append(((ListDecisions) decision).getDecisionHeader().getSimpleDecisionHeader()
                    .getIdDecisionHeader());
            // += ((ListDecisions) decision).getDecisionHeader().getSimpleDecisionHeader()
            // .getIdDecisionHeader();
            returnString.append(separator);// += separator;
        }
        // Suppression derniere virgule
        // returnString = returnString.substring(0, returnString.length() - 1);
        lotDecision = returnString.substring(0, returnString.length() - 1);
        // Personnes comprises
        dealPersonnesComprises();

        // si la décision est déjà validée, chercher si le lot est ouvert afin de savoir si la décision est dévalidable
        isLotOuvert = PegasusServiceLocator.getDecisionService().isDecisionDevalidable(
                decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader(),
                decisionApresCalcul.getVersionDroit().getSimpleVersionDroit());

        setBlocReductionPrimesAssMaladieAsString();
        searchSimpleAllocationNoelAndSetItIUsed();
        setLabelRemarqueForProvisoire(decisionApresCalcul);
    }

    private void searchSimpleAllocationNoelAndSetItIUsed() throws PropertiesException, JadePersistenceException,
            PCAccordeeException, AllocationDeNoelException, JadeApplicationServiceNotAvailableException {
        if (getUseAllocationNoel()) {
            SimpleAllocationNoel allocationNoel = PegasusServiceLocator.getSimpleAllocationDeNoelService()
                    .readAllocationNoelByIdPca(decisionApresCalcul.getPcAccordee().getId());
            if ((allocationNoel != null) && !allocationNoel.isNew()) {
                simpleAllocationNoel = allocationNoel;
            }
        }
    }

    private void setLabelRemarqueForProvisoire(DecisionApresCalcul decision) {
        CTDocumentImpl document = documentsBabel.get(LanguageResolver.resolveISOCode(decisionApresCalcul.getDecisionHeader().getPersonneEtendue().getTiers().getLangue()));

        textRemarqueNormal = PRStringUtils.replaceString(document.getTextes(2).getTexte(10).getDescription(),
                DecisionApresCalculServiceImpl.DEMANDE_DU, decision
                        .getVersionDroit().getSimpleVersionDroit().getDateAnnonce());
        textRemarqueProvisoire = PRStringUtils.replaceString(document.getTextes(2).getTexte(40).getDescription(),
                DecisionApresCalculServiceImpl.DEMANDE_DU, decision
                        .getVersionDroit().getSimpleVersionDroit().getDateAnnonce());
    }

    /**
     * @param annexesIsChanged
     *            the annexesIsChanged to set
     */
    public void setAnnexesIsChanged(String annexesIsChanged) {
        this.annexesIsChanged = annexesIsChanged;
    }

    /**
     * Retourne le texte de description de la reduction de primes dans l'assurance maladie
     *
     * @return
     * @throws CatalogueTexteException
     * @throws JadeApplicationServiceNotAvailableException
     * @throws Exception
     */
    public void setBlocReductionPrimesAssMaladieAsString() throws CatalogueTexteException,
            JadeApplicationServiceNotAvailableException, Exception {
        // TODO : langue ??? On a l'info mais il faut voir sur qui on récupère la langue

        documentsBabel = BabelServiceLocator.getPCCatalogueTexteService()
                .searchForTypeDecision(IPCCatalogueTextes.BABEL_DOC_NAME_APRES_CALCUL);
        babelDoc = documentsBabel.get(Langues.Francais);
        // Etat dec
        String csEtat = decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().getCsTypeDecision();

        String codeAmal = decisionApresCalcul.getSimpleDecisionApresCalcul().getCodeAmal();

        setDisplayBlocReductionAmal(Boolean.TRUE);

        if (codeAmal.equals(EPCCodeAmal.CODE_A.getProperty())) {
            blocReductionAmal = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(23).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_C.getProperty())) {
            blocReductionAmal = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(26).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_F.getProperty())) {
            blocReductionAmal = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(21).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_H.getProperty())) {
            blocReductionAmal = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(24).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_D.getProperty())) {
            blocReductionAmal = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(25).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_J.getProperty())) {
            blocReductionAmal = replaceDateDecisionAmalInString(babelDoc.getTextes(5).getTexte(22).getDescription());
        } else if (codeAmal.equals(EPCCodeAmal.CODE_K.getProperty())
                || codeAmal.equals(EPCCodeAmal.CODE_UNDEFINED.getProperty()) || JadeStringUtil.isBlankOrZero(codeAmal)) {
            setDisplayBlocReductionAmal(Boolean.FALSE);

        }

    }

    /**
     * @param copies
     *            the copies to set
     */
    public void setCopies(ArrayList<String> copies) {
        this.copies = copies;
    }

    /**
     * @param copiesIsChanged
     *            the copiesIsChanged to set
     */
    public void setCopiesIsChanged(String copiesIsChanged) {
        this.copiesIsChanged = copiesIsChanged;
    }

    public void setCsGenreDecision(String csGenreDecision) {
        this.csGenreDecision = csGenreDecision;
        decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().setCsGenreDecision(this.csGenreDecision);
    }

    /**
     * @param decisionApresCalcul
     *            the decisionApresCalcul to set
     */
    public void setDecisionApresCalcul(DecisionApresCalcul decisionApresCalcul) {
        this.decisionApresCalcul = decisionApresCalcul;
    }

    public void setDisplayBlocReductionAmal(Boolean displayBlocReductionAmal) {
        this.displayBlocReductionAmal = displayBlocReductionAmal;
    }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#setId(java.lang.String)
     */
    @Override
    public void setId(String newId) {
        decisionApresCalcul.setId(newId);

    }

    /**
     * @param idDecision
     *            the idDecision to set
     */
    public void setIdDecision(String idDecision) {
        this.idDecision = idDecision;
    }

    /**
     * @param idTiersCopies
     *            the idTiersCopies to set
     */
    public void setIdTiersCopies(String idTiersCopies) {
        this.idTiersCopies = idTiersCopies;
    }

    /**
     * @param idVersionDroit
     *            the idVersionDroit to set
     */
    public void setIdVersionDroit(String idVersionDroit) {
        this.idVersionDroit = idVersionDroit;
    }

    /**
     * @param listeAnnexesString
     *            the listeAnnexesString to set
     */
    public void setListeAnnexesString(String listeAnnexesString) {
        this.listeAnnexesString = listeAnnexesString;
    }

    public void setLotDecision(String lotDecision) {
        this.lotDecision = lotDecision;
    }

    public void setMonnaie(String monnaie) {
        this.monnaie = monnaie;
    }

    public void setSearch(DecisionApresCalculSearch search) {
        this.search = search;
    }

    public Boolean getDecisionProvisoire() {
        return decisionProvisoire;
    }

    public Boolean isDecisoinProvisoire() {
        return decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().isDecisionProvisoire();
    }

    public void setDecisionProvisoire(Boolean decisionProvisoire) {
        this.decisionProvisoire = decisionProvisoire;
        decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader().setDecisionProvisoire(decisionProvisoire);
    }

    public String getTextRemarqueNormal() {
        return textRemarqueNormal;
    }

    public void setTextRemarqueNormal(String textRemarqueNormal) {
        this.textRemarqueNormal = textRemarqueNormal;
    }

    public String getTextRemarqueProvisoire() {
        return textRemarqueProvisoire;
    }

    public void setTextRemarqueProvisoire(String textRemarqueProvisoire) {
        this.textRemarqueProvisoire = textRemarqueProvisoire;
    }

    /**
     * @param validAction
     *            the validAction to set
     */
    // public void setValidAction(String validAction) {
    // this.validAction = validAction;
    // }

    /*
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BIPersistentObject#update()
     */
    @Override
    public void update() throws Exception {

        // TRAITEMENT ANNEXES
        processAnnexes();
        // TRAITEMENT COPIES
        processCopies();
        // gestion header, on repasse à enregistré
        decisionApresCalcul.getDecisionHeader().getSimpleDecisionHeader()
                .setCsEtatDecision(IPCDecision.CS_ETAT_ENREGISTRE);
        // update
        decisionApresCalcul = PegasusServiceLocator.getDecisionApresCalculService().update(decisionApresCalcul);

    }
}
