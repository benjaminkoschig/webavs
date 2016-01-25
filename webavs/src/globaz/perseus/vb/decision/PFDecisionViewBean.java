package globaz.perseus.vb.decision;

import globaz.framework.bean.FWViewBeanInterface;
import globaz.globall.db.BProcessLauncher;
import globaz.globall.db.BSession;
import globaz.globall.db.BSessionUtil;
import globaz.globall.db.BSpy;
import globaz.globall.util.JACalendar;
import globaz.globall.vb.BJadePersistentObjectViewBean;
import globaz.jade.client.util.JadeDateUtil;
import globaz.jade.client.util.JadeStringUtil;
import globaz.jade.context.JadeThread;
import globaz.jade.exception.JadeApplicationException;
import globaz.jade.exception.JadePersistenceException;
import globaz.jade.log.JadeLogger;
import globaz.jade.log.business.JadeBusinessMessageLevels;
import globaz.jade.persistence.JadePersistenceManager;
import globaz.jade.persistence.model.JadeAbstractModel;
import globaz.jade.service.provider.application.util.JadeApplicationServiceNotAvailableException;
import globaz.perseus.helpers.decision.PFDecisionHelper;
import globaz.perseus.process.decision.PFDecisionProcess;
import globaz.perseus.utils.PFUserHelper;
import globaz.prestation.tools.PRStringUtils;
import globaz.prestation.utils.ged.PRGedUtils;
import globaz.pyxis.constantes.IConstantes;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import ch.globaz.perseus.business.constantes.CSCaisse;
import ch.globaz.perseus.business.constantes.CSEtatDecision;
import ch.globaz.perseus.business.constantes.CSEtatDemande;
import ch.globaz.perseus.business.constantes.CSTypeDecision;
import ch.globaz.perseus.business.constantes.CSTypeDemande;
import ch.globaz.perseus.business.constantes.CSTypeRetenue;
import ch.globaz.perseus.business.constantes.IPFCatalogueTextes;
import ch.globaz.perseus.business.constantes.IPFConstantes;
import ch.globaz.perseus.business.exceptions.models.demande.DemandeException;
import ch.globaz.perseus.business.exceptions.models.pcfaccordee.PCFAccordeeException;
import ch.globaz.perseus.business.exceptions.models.retenue.RetenueException;
import ch.globaz.perseus.business.models.decision.AnnexeDecision;
import ch.globaz.perseus.business.models.decision.CopieDecision;
import ch.globaz.perseus.business.models.decision.Decision;
import ch.globaz.perseus.business.models.decision.DecisionSearchModel;
import ch.globaz.perseus.business.models.demande.Demande;
import ch.globaz.perseus.business.models.lot.Lot;
import ch.globaz.perseus.business.models.pcfaccordee.PCFAccordee;
import ch.globaz.perseus.business.models.retenue.SimpleRetenueSearchModel;
import ch.globaz.perseus.business.models.situationfamille.Enfant;
import ch.globaz.perseus.business.services.PerseusServiceLocator;
import ch.globaz.perseus.businessimpl.checkers.decision.DecisionChecker;
import ch.globaz.perseus.businessimpl.checkers.decision.SimpleDecisionChecker;
import ch.globaz.perseus.businessimpl.services.PerseusImplServiceLocator;
import ch.globaz.perseus.businessimpl.services.document.BabelContainer;
import ch.globaz.pyxis.business.model.AdresseTiersDetail;
import ch.globaz.pyxis.business.model.PersonneEtendueComplexModel;
import ch.globaz.pyxis.business.service.TIBusinessServiceLocator;

/**
 * ViewBean derrière l'écran de détail sur les décisions.
 * 
 * @author JSI
 * 
 */

public class PFDecisionViewBean extends BJadePersistentObjectViewBean {

    public enum GenreRetenue {
        LA_RETENU_N_A_PU_ETRE_TROUVE,
        PAS_DE_RETENU,
        RETENU_EXISTANTE;
    }

    private static final String CDT_DATE_DECISION = "{dateDecision}";
    private static final String CDT_MONTANT_MENSUEL = "{montantMensuel}";
    private String adresseAssure = null;
    private String adressePaiementAssure = null;
    private String annexesIsChanged = "0";
    private BabelContainer babelContainer = new BabelContainer();
    private String boolAideAuLogement = null;
    private String boolAideAuxEtudes = null;
    private String boolPensionAlimentaire = null;
    private boolean champAjoutAideCategorielle = false;
    private boolean champRemarqueVisible = false;
    // Liste contenant les chaines de caracères envoyés pour les copies
    private ArrayList<String> copies = new ArrayList<String>();
    // Etat de la zone copie du formulaire
    private String copiesIsChanged = "0";
    private String dateSurDecision = null;
    private Decision decision;
    private String detailAssure = null;
    private String eMailAddress;
    private String etatValidation = null;
    private String gestionnaire = null;
    private Boolean hasCreancier = null;
    private String idDemandePrecedante;
    private boolean isNewProject = false;
    private boolean isNewSuppressionVolontaire = false;
    private String isSendToGed = null;
    private String listeAnnexesString = null;
    private String paragraphePrecedent = null;
    private PCFAccordee pcfaccordee;

    private String viderAnnoncesChangement = null;

    public PFDecisionViewBean() {
        super();
        decision = new Decision();
    }

    public PFDecisionViewBean(Decision decision) {
        super();
        this.decision = decision;
    }

    @Override
    public void add() throws Exception {

        boolean isControleAdPmt;

        if (!hasAdressePaiement()
                && !CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(
                        getDecision().getSimpleDecision().getCsTypeDecision())
                && !CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(
                        getDecision().getSimpleDecision().getCsTypeDecision())) {
            if (CSEtatDemande.ENREGISTRE.getCodeSystem().equals(
                    getDecision().getDemande().getSimpleDemande().getCsEtatDemande())) {
                isControleAdPmt = false;
            } else {
                isControleAdPmt = true;
            }
        } else {
            isControleAdPmt = false;
        }
        // Pas de controle des adresses de paiement pour les décisions de suppression
        if (CSTypeDecision.SUPPRESSION.getCodeSystem().equals(getDecision().getSimpleDecision().getCsTypeDecision())) {
            isControleAdPmt = false;
        }

        if (isControleAdPmt) {
            JadeThread.logError(DecisionChecker.class.getName(),
                    "perseus.decision.simpledecision.adressepaiement.mandatory");
        } else {
            if (etatValidation.equals("prevalidee")) {
                getDecision().getSimpleDecision().setCsEtat(CSEtatDecision.PRE_VALIDE.getCodeSystem());
            } else {
                getDecision().getSimpleDecision().setCsEtat(CSEtatDecision.ENREGISTRE.getCodeSystem());
            }
            getDecision().getSimpleDecision().setNumeroDecision(
                    PFDecisionHelper.getNumeroDemandeCalculee(new Integer(Calendar.getInstance().get(Calendar.YEAR))
                            .toString()));
            getDecision().getSimpleDecision().setCsTypeDecision(
                    PerseusServiceLocator.getDecisionService().definitTypeDecision(getDecision(), isNewProject,
                            isNewSuppressionVolontaire));
            getDecision().getSimpleDecision().setDatePreparation(
                    JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now())));
            getDecision().getSimpleDecision().setAideAuLogement("on".equals(boolAideAuLogement));
            getDecision().getSimpleDecision().setAideAuxEtudes("on".equals(boolAideAuxEtudes));
            getDecision().getSimpleDecision().setPensionAlimentaire("on".equals(boolPensionAlimentaire));
            PerseusServiceLocator.getDecisionService().create(decision);
        }
    }

    public boolean champAjoutAideCategorielle() {
        // Si la décision provient d'une demande d'ajout d'aides catégorielles et que le type de décision est en octroi,
        // affichages des options liées au type de
        // demande
        if (CSTypeDemande.AIDES_CATEGORIELLES.getCodeSystem().equals(
                decision.getDemande().getSimpleDemande().getTypeDemande())
                && !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean champRemarqueVisible() throws Exception {

        if (!CSTypeDecision.PROJET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())
                && !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            // Si la décision contient déjà du texte, celle-ci s'affiche
            if (!JadeStringUtil.isEmpty(getDecision().getSimpleDecision().getRemarqueUtilisateur())) {
                return true;
            } else
            // Si la décision ne contient pas de texte, mais qu'elle est en refus forcé
            if (decision.getDemande().getSimpleDemande().getRefusForce() == true) {
                return true;
            } else
            // Si la demande est de type annonce de changement ou
            if (CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(
                    decision.getDemande().getSimpleDemande().getTypeDemande())
                    && (!CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(
                            decision.getSimpleDecision().getCsTypeDecision())
                            && !CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(
                                    decision.getSimpleDecision().getCsTypeDecision()) && (!CSTypeDecision.OCTROI_PARTIEL
                            .getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())))) {
                return true;
            } else if (decision.getDemande().getSimpleDemande().getNonEntreeEnMatiere() == true) {
                return false;
            }

            // Si la décision est en partiel et qu'il existe une demande précédente
            if (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
                // Récupération de la demande précédente
                Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(
                        decision.getDemande());
                // Si la décision à une demande précédente
                if (null != demandePrecedante) {
                    DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
                    decisionSearchModel.setForIdDemande(demandePrecedante.getSimpleDemande().getId());
                    decisionSearchModel.setForCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
                    decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
                    boolean hasDemandePartiel = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
                    decisionSearchModel.setForCsTypeDecision(CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem());
                    boolean hasDemandeRefus = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
                    decisionSearchModel.setForCsTypeDecision(CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem());
                    boolean hasNonEntreeEnMatiere = PerseusServiceLocator.getDecisionService().count(
                            decisionSearchModel) > 0;
                    if (!hasDemandePartiel && !hasDemandeRefus && !hasNonEntreeEnMatiere) {
                        return true;
                    } else {
                        if (hasDemandePartiel) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                } else {
                    return true;
                }
            } else {
                return false;
            }
        }
        return false;
    }

    // TODO Methode permettant de ressortir le paragraphe précédent d'une décision
    public String defineParagraphePrecedent() throws Exception {

        String texte = "";
        // Récupération de la demande

        // Récupération de la demande précédente
        Demande demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(
                decision.getDemande());
        // Récupération de la demande précédente
        PCFAccordee pcfAncienne = null;
        if (demandePrecedante != null) {
            pcfAncienne = PerseusServiceLocator.getPCFAccordeeService().readForDemande(demandePrecedante.getId());
        }

        // Récupération de la personne étendue
        PersonneEtendueComplexModel personne = TIBusinessServiceLocator.getPersonneEtendueService().read(
                decision.getSimpleDecision().getIdTiersAdresseCourrier());

        // Modèle de document: 7
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())
                && (CSTypeDemande.REVISION_EXTRAORDINAIRE.getCodeSystem().equals(decision.getDemande()
                        .getSimpleDemande().getTypeDemande()))) {
            // Chargement du catalogue de texte et de Babel
            getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_DECISION_COMMUNE);
            getBabelContainer().setCodeIsoLangue(getSession().getCode(personne.getTiers().getLangue()));
            getBabelContainer().load();
            // Récupération du texte dans le catalogue
            texte = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_COMMUNE, 8, 1);
        }

        // Modèle de document: 9 et 10
        if (CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(decision.getSimpleDecision().getCsTypeDecision())) {
            if (null == pcfAncienne) {
                // Modèle de document: 9
                // Chargement du catalogue de texte et de Babel
                getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_DECISION_REFUS);
                getBabelContainer().setCodeIsoLangue(getSession().getCode(personne.getTiers().getLangue()));
                getBabelContainer().load();
                // Récupération du texte dans le catalogue
                texte = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_REFUS, 3, 2);
            } else {
                // Modèle de document: 10
                // Chargement du catalogue de texte et de Babel
                getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE);
                getBabelContainer().setCodeIsoLangue(getSession().getCode(personne.getTiers().getLangue()));
                getBabelContainer().load();
                // Récupération du texte dans le catalogue
                texte = getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_PERSONNELLE, 3, 2)
                        .substring(144);
            }
        }

        // Modèle de document : 8
        if ((null != demandePrecedante)
                && (CSTypeDecision.OCTROI_PARTIEL.getCodeSystem().equals(decision.getSimpleDecision()
                        .getCsTypeDecision()))) {

            DecisionSearchModel decisionSearchModel = new DecisionSearchModel();
            decisionSearchModel.setForIdDemande(demandePrecedante.getSimpleDemande().getId());
            decisionSearchModel.setForCsTypeDecision(CSTypeDecision.OCTROI_PARTIEL.getCodeSystem());
            decisionSearchModel.setForCsEtat(CSEtatDecision.VALIDE.getCodeSystem());
            boolean hasDemandePartiel = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
            decisionSearchModel.setForCsTypeDecision(CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem());
            boolean hasDemandeRefus = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
            decisionSearchModel.setForCsTypeDecision(CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem());
            boolean hasNonEntreeEnMatiere = PerseusServiceLocator.getDecisionService().count(decisionSearchModel) > 0;
            if (!hasDemandePartiel && !hasDemandeRefus && !hasNonEntreeEnMatiere) {
                // Chargement du catalogue de texte et de Babel
                getBabelContainer().RegisterCtx(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE);
                getBabelContainer().setCodeIsoLangue(getSession().getCode(personne.getTiers().getLangue()));
                getBabelContainer().load();

                // Récupération de l'ancienne décision
                DecisionSearchModel dsm = new DecisionSearchModel();
                dsm.setForIdDemande(demandePrecedante.getSimpleDemande().getIdDemande());
                dsm = PerseusServiceLocator.getDecisionService().search(dsm);
                Decision ancienneDecision = null;
                for (JadeAbstractModel model : dsm.getSearchResults()) {
                    Decision ad = (Decision) model;
                    if (!CSTypeDecision.PROJET.getCodeSystem().equals(ad.getSimpleDecision().getCsTypeDecision())
                            || !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(
                                    ad.getSimpleDecision().getCsTypeDecision())) {
                        ancienneDecision = ad;
                        break;
                    }
                }

                // Récupération de la date et du montant de l'ancienne décision
                String montantAncienneDecision = pcfAncienne.getSimplePCFAccordee().getMontant();

                // Récupération du texte dans le catalogue
                String dateDecision = PRStringUtils.replaceString(
                        getBabelContainer().getTexte(IPFCatalogueTextes.CS_DECISION_SUPPRESSION_ECONOMIQUE, 3, 1),
                        PFDecisionViewBean.CDT_DATE_DECISION, ancienneDecision.getSimpleDecision().getDateDocument());

                texte = PRStringUtils.replaceString(dateDecision, PFDecisionViewBean.CDT_MONTANT_MENSUEL,
                        montantAncienneDecision);
            }

        }

        return texte;
    }

    @Override
    public void delete() throws Exception {
        PerseusServiceLocator.getDecisionService().delete(decision);
    }

    /**
     * Utiliser getAdresseCourrier() à la place.
     * 
     * @deprecated
     * @return
     */
    @Deprecated
    public String getAdresseAssure() {
        return adresseAssure;
    }

    /**
     * Récupère l'adresse de courrier du bénéficiaire de la décision.
     * 
     * @return adresse de courrier formatée
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdresseCourrier() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(decision.getSimpleDecision().getIdTiersAdresseCourrier())) {
            detailTiers = PFUserHelper.getAdresseAssure(decision.getSimpleDecision().getIdTiersAdresseCourrier(),
                    IConstantes.CS_AVOIR_ADRESSE_COURRIER, decision.getSimpleDecision()
                            .getIdDomaineApplicatifAdresseCourrier(), JACalendar.todayJJsMMsAAAA());
        }

        return detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    /**
     * Récupère l'adresse de paiement du bénéficiaire de la décision.
     * 
     * @return adresse de paiement formatée
     * @throws JadeApplicationServiceNotAvailableException
     * @throws JadePersistenceException
     * @throws JadeApplicationException
     */
    public String getAdressePaiement() throws JadeApplicationServiceNotAvailableException, JadePersistenceException,
            JadeApplicationException {
        AdresseTiersDetail detailTiers = null;
        if (!JadeStringUtil.isEmpty(decision.getSimpleDecision().getIdTiersAdressePaiement())) {
            detailTiers = PFUserHelper.getAdressePaiementAssure(decision.getSimpleDecision()
                    .getIdTiersAdressePaiement(), decision.getSimpleDecision().getIdDomaineApplicatifAdressePaiement(),
                    JACalendar.todayJJsMMsAAAA());
        }
        return detailTiers != null ? detailTiers.getAdresseFormate() : "";
    }

    /**
     * Utiliser getAdressePaiement()
     * 
     * @deprecated
     * @return
     */
    @Deprecated
    public String getAdressePaiementAssure() {
        return adressePaiementAssure;
    }

    public String getAnnexesIsChanged() {
        return annexesIsChanged;
    }

    // TODO Methode qui appel Babel container
    public BabelContainer getBabelContainer() {
        return babelContainer;
    }

    public String getBoolAideAuLogement() {
        return boolAideAuLogement;
    }

    public String getBoolAideAuxEtudes() {
        return boolAideAuxEtudes;
    }

    public String getBoolPensionAlimentaire() {
        return boolPensionAlimentaire;
    }

    public ArrayList<String> getCopies() {
        return copies;
    }

    public String getCopiesIsChanged() {
        return copiesIsChanged;
    }

    // Methode qui permet en fonction du type de décision, d'afficher la date du jeudi qui suit la date du jour. Sinon
    // elle retourne la date du jour
    public String getDateDocumentDefault() throws Exception {
        String dateDocument = "";

        // si la décision généré est de l'un des type ci dessous, la date sur le document doit être celle du jour
        if (CSTypeDecision.OCTROI_COMPLET.getCodeSystem().equals(getTypeDecisionCS())) {
            Calendar c = Calendar.getInstance();
            if (Calendar.THURSDAY == c.get(Calendar.DAY_OF_WEEK)) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            while (Calendar.THURSDAY != c.get(Calendar.DAY_OF_WEEK)) {
                c.add(Calendar.DAY_OF_MONTH, 1);
            }
            dateDocument = JadeDateUtil.getGlobazFormattedDate(c.getTime());

        } else {
            dateDocument = JadeDateUtil.getGlobazFormattedDate(new Date(JadeDateUtil.now()));
        }

        return dateDocument;
    }

    public String getDateSurDecision() {
        if (JadeStringUtil.isEmpty(dateSurDecision)) {
            dateSurDecision = "";
        }
        return dateSurDecision;
    }

    public Decision getDecision() {
        return decision;
    }

    public String getDecisionAction() throws JadePersistenceException {
        String type = decision.getSimpleDecision().getCsTypeDecision();
        // Decision sans calcul
        if ((CSTypeDecision.REFUS_SANS_CALCUL.getCodeSystem().equals(type))
                || (CSTypeDecision.NON_ENTREE_MATIERE.getCodeSystem().equals(type))) {
            return "perseus.decision.decisionSansCalcul.afficher";
            // Decision avec calcul
        } else {
            return "perseus.decision.decisionAvecCalcul.afficher";
        }
    }

    public String getDetailAssure() {
        return detailAssure;
    }

    public String getDetailAssure(BSession session, PersonneEtendueComplexModel personne) {
        return PFUserHelper.getDetailAssure(session, personne);
    }

    public String geteMailAddress() {
        return eMailAddress;
    }

    public String getEtatComptabilisation() throws Exception {
        String texte = "";
        // Récupérer le lot contenant la décision et retourner son état, sa date si comptabilisé et le numéro du
        Lot lot = PerseusServiceLocator.getLotService().getLotForDemande(decision.getDemande().getId());
        if (lot != null) {
            // Récupère l'état du lot
            texte += getISession().getCodeLibelle(lot.getSimpleLot().getEtatCs());
            texte += " (" + lot.getSimpleLot().getDescription() + ")";
        } else {
            texte += "-";
        }
        return texte;
    }

    public String getEtatValidation() {
        return etatValidation;
    }

    public String getGestionnaire() {
        return gestionnaire;
    }

    /**
     * @return the hasCreancier
     */
    public Boolean getHasCreancier() {
        try {
            hasCreancier = PerseusServiceLocator.getDemandeService().hasCreanciers(decision.getDemande().getId());
        } catch (Exception e) {
            JadeThread.logError(
                    PFDecisionHelper.class.getName(),
                    "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFDecisionViewBean.init) : "
                            + e.getMessage());
        }
        return hasCreancier;
    }

    @Override
    public String getId() {
        return decision.getId();

    }

    public String getIsSendToGed() {
        return isSendToGed;
    }

    public String getListeAnnexesString() {
        return listeAnnexesString;
    }

    public String getListWebPersonnesDansCalcul() throws Exception {
        String htmlCode = "";
        String htmlCodeConjoint = "";
        htmlCode += getDecision().getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                .getPersonneEtendue().getTiers().getDesignation1()
                + " ";
        htmlCode += getDecision().getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                .getPersonneEtendue().getTiers().getDesignation2();

        htmlCodeConjoint += getDecision().getDemande().getSituationFamiliale().getConjoint().getMembreFamille()
                .getPersonneEtendue().getTiers().getDesignation1()
                + " ";
        htmlCodeConjoint += getDecision().getDemande().getSituationFamiliale().getConjoint().getMembreFamille()
                .getPersonneEtendue().getTiers().getDesignation2();
        if (!JadeStringUtil.isBlank(htmlCodeConjoint)) {
            htmlCode += htmlCodeConjoint;
        }
        ArrayList<Enfant> enfants = (ArrayList<Enfant>) PerseusServiceLocator.getDemandeService().getListEnfants(
                getDecision().getDemande());
        for (Enfant enfant : enfants) {
            htmlCode += ", " + enfant.getMembreFamille().getPersonneEtendue().getTiers().getDesignation1();
            htmlCode += " " + enfant.getMembreFamille().getPersonneEtendue().getTiers().getDesignation2();
        }
        return htmlCode;
    }

    /***
     * 
     * @return isRetenuePourDemandePrecedante
     *         Si la demande précédente a une retenue alors on retourne <b><code>true</code></b>
     *         Sinon on retourne <b><code>false</code></b>
     */
    public GenreRetenue getMessageRetenuePourDemandePrecedante() {

        try {
            Demande demandePrecedante = new Demande();
            PCFAccordee pcfAncienne = null;

            // Récupération de la demande précédente
            if (JadeStringUtil.isEmpty(idDemandePrecedante)) {
                demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(
                        decision.getDemande());
                // Si la décision a une demande précédente
                if (demandePrecedante != null) {
                    setIdDemandePrecedante(demandePrecedante.getSimpleDemande().getId());
                } else {
                    throw new DemandeException();
                }
            }

            pcfAncienne = PerseusServiceLocator.getPCFAccordeeService().readForDemande(idDemandePrecedante);

            // On fait une requete pour voir si oui ou non on a une retenue
            SimpleRetenueSearchModel simpleRetenueSearchModel = new SimpleRetenueSearchModel();
            SimpleRetenueSearchModel retenuSearchMod = new SimpleRetenueSearchModel();
            if (pcfAncienne != null) {
                simpleRetenueSearchModel.setForIdPcfAccordee(pcfAncienne.getId());
                simpleRetenueSearchModel.setForNotCsTypeRetenue(CSTypeRetenue.IMPOT_SOURCE.getCodeSystem());
                retenuSearchMod = PerseusImplServiceLocator.getSimpleRetenueService().search(simpleRetenueSearchModel);
            } else {
                throw new PCFAccordeeException();
            }

            if (retenuSearchMod.getSize() != 0) {
                return GenreRetenue.RETENU_EXISTANTE;
            } else {
                return GenreRetenue.PAS_DE_RETENU;
            }

        } catch (DemandeException e) {
            JadeLogger.error(this, e.toString());
            return GenreRetenue.PAS_DE_RETENU;
        } catch (PCFAccordeeException e) {
            JadeLogger.error(this, e.toString());
            return GenreRetenue.PAS_DE_RETENU;
        } catch (JadeApplicationServiceNotAvailableException e) {
            JadeLogger.error(this, e.toString());
            return GenreRetenue.LA_RETENU_N_A_PU_ETRE_TROUVE;
        } catch (JadePersistenceException e) {
            JadeLogger.error(this, e.toString());
            return GenreRetenue.LA_RETENU_N_A_PU_ETRE_TROUVE;
        } catch (RetenueException e) {
            JadeLogger.error(this, e.toString());
            return GenreRetenue.LA_RETENU_N_A_PU_ETRE_TROUVE;
        }
    }

    public String getParagraphePrecedent() throws Exception {
        return paragraphePrecedent;
    }

    public PCFAccordee getPcfaccordee() {
        return pcfaccordee;
    }

    // TODO Récupération de la session user
    protected BSession getSession() {
        return BSessionUtil.getSessionFromThreadContext();
    }

    @Override
    public BSpy getSpy() {
        return new BSpy(decision.getSpy());

    }

    public String getTypeDecision() throws Exception {
        return getISession().getCodeLibelle(getTypeDecisionCS());
    }

    public String getTypeDecisionCS() throws Exception {
        if (JadeStringUtil.isEmpty(decision.getSimpleDecision().getCsTypeDecision())) {
            decision.getSimpleDecision().setCsTypeDecision(
                    PerseusServiceLocator.getDecisionService().definitTypeDecision(getDecision(), isNewProject,
                            isNewSuppressionVolontaire));
        }

        return decision.getSimpleDecision().getCsTypeDecision();
    }

    /**
     * @return the viderAnnoncesChangement
     */
    public String getViderAnnoncesChangement() {
        return viderAnnoncesChangement;
    }

    private boolean hasAdressePaiement() {
        try {
            if (getDecision().getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                    .getPersonneEtendue().getTiers().getIdTiers()
                    .equals(decision.getSimpleDecision().getIdTiersAdressePaiement())
                    && JadeStringUtil.isBlank(getAdressePaiement())) {
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            JadeThread.logError(DecisionChecker.class.getName(),
                    "perseus.decision.simpledecision.adressepaiement.mandatory");
            e.printStackTrace();
            return false;
        }
    }

    public boolean hasGed(String csCaisse) {
        try {
            boolean ged = false;
            if (PRGedUtils.isGedActive()) {
                if (!JadeStringUtil.isEmpty(csCaisse)) {
                    if (CSCaisse.AGENCE_LAUSANNE.getCodeSystem().equals(csCaisse)) {
                        if (!Boolean.parseBoolean(getSession().getApplication().getProperty(
                                "perseus.ged.caisse.secondaire"))) {
                            ged = false;
                        } else {
                            ged = true;
                        }
                    } else if (CSCaisse.CCVD.getCodeSystem().equals(csCaisse)) {
                        if (!Boolean.parseBoolean(getSession().getApplication().getProperty(
                                "perseus.ged.caisse.principale"))) {
                            ged = false;
                        } else {
                            ged = true;
                        }
                    } else {
                        return false;
                    }
                }
            } else {
                ged = false;
            }
            return ged;
        } catch (Exception ex) {
            JadeLogger.error(PRGedUtils.class, ex.toString());
            return false;
        }
    }

    private void imprimerDecision(Decision dec, String isSendToGed) {
        PFDecisionProcess process = new PFDecisionProcess();
        process.setSession((BSession) getISession());
        process.setDecisionId(dec.getId());
        /**
         * La variable de l'adresse email est automatiquement setter à NULL si elle est nommée (eMailAddress) et doit
         * donc être renommée différement (mailAd) pour fonctionner correctement.
         */
        process.seteMailAddress(geteMailAddress());
        process.setMailAd(geteMailAddress());
        process.setDateDocument(dec.getSimpleDecision().getDateDocument());
        process.setIsSendToGed(isSendToGed);

        try {
            BProcessLauncher.startJob(process);
        } catch (Exception e) {
            e.printStackTrace();
            setMessage("Unable to start........");
            setMsgType(FWViewBeanInterface.ERROR);
        }
    }

    /**
     * Effectue toutes les récupérations de données d'une décision qui va être affichée à l'écran, en fonction des
     * divers paramètres qui lui sont liés comme son état, son type, etc.
     */
    public void init() {
        try {
            // Affichage de la saisie de texte dans la JSP
            // Lecture de la demande
            Demande demande = new Demande();
            demande.getSimpleDemande().setId(getDecision().getDemande().getId());
            demande = (Demande) JadePersistenceManager.read(demande);

            setGestionnaire(demande.getDossier().getDossier().getGestionnaire());

            decision.setDemande(demande);

            this.setPcfaccordee(getDecision().getDemande().getId());
            setDetailAssure(PFUserHelper.getDetailAssure((BSession) getISession(), getDecision().getDemande()
                    .getSituationFamiliale().getRequerant().getMembreFamille().getPersonneEtendue()));
            // Si on ne vient pas du RC List et qu'on veut retrouver la décision
            if (JadeStringUtil.isBlank(decision.getId())) {
                DecisionSearchModel dsm = new DecisionSearchModel();
                dsm.setForIdDemande(getDecision().getDemande().getId());
                // On regarde si on vient du menu option pour créer un projet de décision
                if (isNewProject) {
                    dsm.setForCsTypeDecision(CSTypeDecision.PROJET.getCodeSystem());
                }
                // On regarde si on vient du menu option pour créer une décision de suppression
                if (isNewSuppressionVolontaire) {
                    dsm.setForCsTypeDecision(CSTypeDecision.SUPPRESSION.getCodeSystem());
                }
                dsm = PerseusServiceLocator.getDecisionService().search(dsm);
                // On traite les résultats
                if (dsm.getSize() > 0) {
                    // Tout d'abord si on a un projet ou une suppression volontaire
                    if (isNewProject || isNewSuppressionVolontaire) {
                        if (dsm.getSize() > 1) {
                            throw new Exception("Multiple projects or suppressions volontaires for a PCF accordée.");
                        } else {
                            setDecision((Decision) dsm.getSearchResults()[0]);
                        }
                        // Et si a une décision autre
                    } else {
                        for (JadeAbstractModel abstractModel : dsm.getSearchResults()) {
                            Decision decisionRetrieved = (Decision) abstractModel;
                            if (!CSTypeDecision.PROJET.getCodeSystem().equals(
                                    decisionRetrieved.getSimpleDecision().getCsTypeDecision())
                                    && !CSTypeDecision.SUPPRESSION.getCodeSystem().equals(
                                            decisionRetrieved.getSimpleDecision().getCsTypeDecision())) {
                                setDecision(decisionRetrieved);
                            }
                        }
                    }
                    // this.getDecision().getSimpleDecision().setCsEtat(CSEtatDecision.ENREGISTRE.getCodeSystem());
                }
                // On définit un petit texte pour décrire brievement le requérant
                // On regarde si on a déjà une adresse de courrier définie
                if (JadeStringUtil.isIntegerEmpty(getDecision().getSimpleDecision().getIdTiersAdresseCourrier())) {
                    // Si on en a pas, on récupère celle du requérant
                    decision.getSimpleDecision().setIdTiersAdresseCourrier(
                            getDecision().getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                                    .getPersonneEtendue().getTiers().getIdTiers());
                    decision.getSimpleDecision()
                            .setIdDomaineApplicatifAdresseCourrier(IPFConstantes.CS_DOMAINE_ADRESSE);
                }
                // On regarde si on a déjà une adresse de paiement définie
                if (JadeStringUtil.isIntegerEmpty(getDecision().getSimpleDecision().getIdTiersAdressePaiement())) {
                    // Si on en a pas, on récupère celle du requérant
                    decision.getSimpleDecision().setIdTiersAdressePaiement(
                            getDecision().getDemande().getSituationFamiliale().getRequerant().getMembreFamille()
                                    .getPersonneEtendue().getTiers().getIdTiers());
                    decision.getSimpleDecision()
                            .setIdDomaineApplicatifAdressePaiement(IPFConstantes.CS_DOMAINE_ADRESSE);
                }
                if (JadeStringUtil.isIntegerEmpty(getDecision().getSimpleDecision().getDateDocument())) {
                    getDecision().getSimpleDecision().setDateDocument(getDateDocumentDefault());
                }
            }
            getTypeDecision();
            paragraphePrecedent = defineParagraphePrecedent();
        } catch (Exception e) {
            JadeThread.logError(
                    PFDecisionHelper.class.getName(),
                    "Erreur technique, merci d'envoyer un PrintScreen à Globaz (PFDecisionViewBean.init) : "
                            + e.getMessage());
        }
    }

    public Boolean isDecisionReadyForValidation() {
        // Si elle est dans l'etat prévalidé
        if (CSEtatDecision.PRE_VALIDE.getCodeSystem().equals(decision.getSimpleDecision().getCsEtat())) {
            return true;
        }
        return false;
    }

    public boolean isNewProject() {
        return isNewProject;
    }

    public boolean isNewSuppressionVolontaire() {
        return isNewSuppressionVolontaire;
    }

    @Override
    public void retrieve() throws Exception {
        decision = PerseusServiceLocator.getDecisionService().read(decision.getId());
        init();

        if (JadeStringUtil.isEmpty(idDemandePrecedante)) {
            // Récupération de la demande précédente
            Demande demandePrecedante;
            demandePrecedante = PerseusServiceLocator.getDemandeService().getDemandePrecedante(decision.getDemande());
            // Si la décision à une demande précédente
            if (null != demandePrecedante) {
                setIdDemandePrecedante(demandePrecedante.getSimpleDemande().getId());
            }
        }

        if (decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                .getNumAvsActuel() != null) {
            getISession().setAttribute(
                    "likeNss",
                    decision.getDemande().getDossier().getDemandePrestation().getPersonneEtendue().getPersonneEtendue()
                            .getNumAvsActuel());
        }
    }

    public void setAdresseAssure(String adresseAssure) {
        this.adresseAssure = adresseAssure;
    }

    public void setAdressePaiementAssure(String adressePaiementAssure) {
        this.adressePaiementAssure = adressePaiementAssure;
    }

    /**
     * Methode de traitement des annexes
     */
    private void setAnnexes() {
        // si changement dans les annexes
        if ("1".equals(annexesIsChanged)) {
            // Remise à zéro de la liste des annexes
            getDecision().setListAnnexes(new ArrayList<AnnexeDecision>());

            System.out.println(listeAnnexesString);

            String[] listeAnnexes = null;
            listeAnnexes = listeAnnexesString.split("\\r\\n");

            // si la premiere n'est pas vide
            if (!"".equals(listeAnnexes[0])) {
                // Parcours des objets
                for (String annexe : listeAnnexes) {

                    AnnexeDecision annexeDecision = new AnnexeDecision();

                    annexeDecision.getSimpleAnnexeDecision().setDescriptionAnnexe(annexe);
                    getDecision().addToListAnnexes(annexeDecision);
                }
            }
        }
    }

    public void setAnnexesIsChanged(String annexesIsChanged) {
        this.annexesIsChanged = annexesIsChanged;
    }

    public void setBoolAideAuLogement(String boolAideAuLogement) {
        this.boolAideAuLogement = boolAideAuLogement;
    }

    public void setBoolAideAuxEtudes(String boolAideAuxEtudes) {
        this.boolAideAuxEtudes = boolAideAuxEtudes;
    }

    public void setBoolPensionAlimentaire(String boolPensionAlimentaire) {
        this.boolPensionAlimentaire = boolPensionAlimentaire;
    }

    public void setChampRemarqueVisible(boolean champRemarqueVisible) {
        this.champRemarqueVisible = champRemarqueVisible;
    }

    /**
     * Methode de traitement des copies
     */
    private void setCopies() {
        if ("1".equals(copiesIsChanged)) {
            // Remise à zéro de la liste des copies
            getDecision().setListCopies(new ArrayList<CopieDecision>());

            // parcours des copies dans la liste
            for (String idTiers : copies) {
                CopieDecision copieDecision = new CopieDecision();
                copieDecision.getSimpleCopieDecision().setIdTiers(idTiers);

                getDecision().addToListCopies(copieDecision);
            }
        }
    }

    public void setCopies(ArrayList<String> copies) {
        this.copies = copies;
    }

    public void setCopiesIsChanged(String copiesIsChanged) {
        this.copiesIsChanged = copiesIsChanged;
    }

    public void setDateSurDecision(String dateSurDecision) {
        this.dateSurDecision = dateSurDecision;
    }

    public void setDecision(Decision decision) {
        this.decision = decision;
    }

    public void setDetailAssure(String detailAssure) {
        this.detailAssure = detailAssure;
    }

    public void seteMailAddress(String eMailAddress) {
        this.eMailAddress = eMailAddress;
    }

    public void setEtatValidation(String etat) {
        etatValidation = etat;
    }

    public void setGestionnaire(String gestionnaire) {
        this.gestionnaire = gestionnaire;
    }

    /**
     * @param hasCreancier
     *            the hasCreancier to set
     */
    public void setHasCreancier(Boolean hasCreancier) {
        this.hasCreancier = hasCreancier;
    }

    @Override
    public void setId(String newId) {
        decision.setId(newId);

    }

    public void setIdDemandePrecedante(String idDemandePrecedante) {
        this.idDemandePrecedante = idDemandePrecedante;
    }

    public void setIsSendToGed(String isSendToGed) {
        this.isSendToGed = isSendToGed;
    }

    public void setListeAnnexesString(String listeAnnexesString) {
        this.listeAnnexesString = listeAnnexesString;
    }

    public void setNewProject(boolean isNewProject) {
        if (!this.isNewProject) {
            this.isNewProject = isNewProject;
        }
    }

    public void setNewSuppressionVolontaire(boolean isNewSuppressionVolontaire) {
        if (!this.isNewSuppressionVolontaire) {
            this.isNewSuppressionVolontaire = isNewSuppressionVolontaire;
        }
    }

    public void setPcfaccordee(PCFAccordee pcfaccordee) {
        this.pcfaccordee = pcfaccordee;
    }

    public void setPcfaccordee(String idDemande) throws Exception {
        pcfaccordee = PerseusServiceLocator.getPCFAccordeeService().readForDemande(idDemande);
    }

    /**
     * @param viderAnnoncesChangement
     *            the viderAnnoncesChangement to set
     */
    public void setViderAnnoncesChangement(String viderAnnoncesChangement) {
        this.viderAnnoncesChangement = viderAnnoncesChangement;
    }

    @Override
    public void update() throws Exception {
        getDecision().getSimpleDecision().setAideAuLogement("on".equals(boolAideAuLogement));
        getDecision().getSimpleDecision().setAideAuxEtudes("on".equals(boolAideAuxEtudes));
        getDecision().getSimpleDecision().setPensionAlimentaire("on".equals(boolPensionAlimentaire));
        Boolean emptyAnnoncesChangements = "true".equals(viderAnnoncesChangement);

        String etatTemp = getDecision().getSimpleDecision().getCsEtat();
        setAnnexes();
        this.setCopies();
        if (decision.isNew()) {
            add();
        } else {
            // Si l'utilisateur a cliqué sur prévalider
            if (etatValidation.equals("prevalidee")) {
                getDecision().getSimpleDecision().setCsEtat(CSEtatDecision.PRE_VALIDE.getCodeSystem());
            }
            // Si l'utilisateur a cliqué sur valider
            else if (etatValidation.equals("validee")) {
                decision = PerseusServiceLocator.getDecisionService().valider(decision, getISession().getUserId(),
                        emptyAnnoncesChangements);
                // A la validation, si la demande est faite par la CCVD, j'indique une mise en GED
                if (hasGed(decision.getDemande().getSimpleDemande().getCsCaisse())) {
                    isSendToGed = "on";
                }
                // Si l'utilisateur a cliqué sur la validation du choix du requérant (seulement pour un projet)
                // } else if (this.prevalid.equals("validatedAndChosen")) {
                // if (CSChoixDecision.POSITIVE_REPONSE.equals(this.getDecision().getSimpleDecision().getCsChoix())) {
                // this.decision = PerseusServiceLocator.getDecisionService().valider(this.decision);
                // }
            } else if (etatValidation.equals("valideechoisie")
                    && CSTypeDecision.PROJET.getCodeSystem().equals(
                            getDecision().getSimpleDecision().getCsTypeDecision())) {
                if (JadeStringUtil.isBlankOrZero(decision.getSimpleDecision().getCsChoix())) {
                    JadeThread.logError(SimpleDecisionChecker.class.getName(),
                            "perseus.decision.simpledecision.cschoix.mandatory");
                }
                if (JadeStringUtil.isIntegerEmpty(decision.getSimpleDecision().getDateChoix())) {
                    JadeThread.logError(SimpleDecisionChecker.class.getName(),
                            "perseus.decision.simpledecision.datechoix.mandatory");
                }
                if (JadeStringUtil.isBlankOrZero(dateSurDecision)) {
                    JadeThread.logError(SimpleDecisionChecker.class.getName(),
                            "perseus.decision.simpledecision.datesurdecisionsuiteauprojet.mandatory");
                }
                // Action à faire une fois que l'utilisateur valide le retour du projet de décision
                // this.decision = PerseusServiceLocator.getDecisionService().createNewDecisionAfterProjetValidation(
                // this.decision);
            } else {
                getDecision().getSimpleDecision().setCsEtat(CSEtatDecision.ENREGISTRE.getCodeSystem());
            }
            if (!etatValidation.equals("validee")) {
                decision = PerseusServiceLocator.getDecisionService().update(decision);
            }
        }
        if (!JadeThread.logHasMessagesFromLevel(JadeBusinessMessageLevels.ERROR)) {
            if (etatValidation.equals("valideechoisie")
                    && CSTypeDecision.PROJET.getCodeSystem().equals(
                            getDecision().getSimpleDecision().getCsTypeDecision())) {
                decision.getSimpleDecision().setDateDocument(dateSurDecision);
                decision = PerseusServiceLocator.getDecisionService().createNewDecisionAfterProjetValidation(decision);
                // A la validation du choix du requérant, si la demande est faite par la CCVD, j'indique une mise en GED
                if (hasGed(decision.getDemande().getSimpleDemande().getCsCaisse())) {
                    isSendToGed = "on";
                }
            }
            if (CSEtatDecision.VALIDE.getCodeSystem().equals(decision.getSimpleDecision().getCsEtat())
                    || CSEtatDecision.PRE_VALIDE.getCodeSystem().equals(decision.getSimpleDecision().getCsEtat())) {
                // Imprimer directement la décision dès que la décision est pré-validée ou validée
                if (CSEtatDecision.VALIDE.getCodeSystem().equals(decision.getSimpleDecision().getCsEtat())) {
                    imprimerDecision(decision, isSendToGed);
                } else {
                    imprimerDecision(decision, "");
                }
            }
        }
        // On remet l'ancien état juste au cas où on a eu une erreur et pour qu'on continue à afficher la décision de
        // manière correcte
        getDecision().getSimpleDecision().setCsEtat(etatTemp);
        getDecision().getSimpleDecision().setCsChoix(null);
    }
}
