/*
 * Créé le 29 decembre 06
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.corvus.application;

import globaz.corvus.servlet.IREActions;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.prestation.application.PRAbstractApplication;

/**
 * <H1>Description</H1>
 * <p>
 * Classe application pour la partie Corvus (rentes) des prestations.
 * </p>
 * 
 * @author bsc
 */
public class REApplication extends PRAbstractApplication {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Le répertoire racine de l'application. */
    public static final String APPLICATION_CORVUS_REP = "corvusRoot";

    /** Le préfixe de l'application. */
    public static final String APPLICATION_PREFIX = "RE";

    public static final String CS_DOMAINE_ADRESSE_CORVUS = "519006";

    /** Le nom de l'application. */
    public static final String DEFAULT_APPLICATION_CORVUS = "CORVUS";

    /** La clé utilisée pour les post-it's dans le domaine rentes **/
    public static final String KEY_POSTIT_RENTES = "globaz.corvus";

    public static final String PROPERTY_GROUPE_COMMUNICATION_OAI_MODIFICATION_ADRESSE_TIERS = "groupeCommunicationOAI";
    public static final String PROPERTY_ACOR_FACTORY = "acor.re.factory.class";
    public static final String PROPERTY_ACTIVER_ANAKIN_VALIDATOR = "activer.anakin.validator";
    public static final String PROPERTY_CLONE_DEFINITION = "clone.corvus.definition";
    public static final String PROPERTY_CLONE_DEM_API_COPIE = "clone.corvus.demande.api.copie";
    public static final String PROPERTY_CLONE_DEM_API_CORRECTION = "clone.corvus.demande.api.correction";
    public static final String PROPERTY_CLONE_DEM_INVALIDITE_COPIE = "clone.corvus.demande.invalidite.copie";
    public static final String PROPERTY_CLONE_DEM_INVALIDITE_CORRECTION = "clone.corvus.demande.invalidite.correction";
    public static final String PROPERTY_CLONE_DEM_SURVIVANT_COPIE = "clone.corvus.demande.survivant.copie";
    public static final String PROPERTY_CLONE_DEM_SURVIVANT_CORRECTION = "clone.corvus.demande.survivant.correction";

    public static final String PROPERTY_CLONE_DEM_VIEILLESSE_COPIE = "clone.corvus.demande.vieillesse.copie";

    public static final String PROPERTY_CLONE_DEM_VIEILLESSE_CORRECTION = "clone.corvus.demande.vieillesse.correction";

    /** email par defaut pour les process d'importation des CI */
    public final static String PROPERTY_DEFAULT_EMAIL = "corvus.user.email";

    /**
     * Détermine si le libelle confidentiel doit être ajouté dans l'adresse du destinataire dans l'envoi des documents
     */
    public static final String PROPERTY_DOC_CONFIDENTIEL = "documents.is.confidentiel";

    /**
	 */
    public static final String PROPERTY_GROUPE_CORVUS_GESTIONNAIRE = "groupe.corvus.gestionnaire";

    public static final String PROPERTY_ID_ORGANE_EXECUTION = "id.orgrane.execution.pmt.rentes";

    /** le role pour recevoir les mails des process d'importation des CI */
    public final static String RESPONSABLE_CI = "corvus.role.resp.ci";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe REApplication.
     * 
     * @throws Exception
     */
    public REApplication() throws Exception {
        super(REApplication.DEFAULT_APPLICATION_CORVUS);
    }

    /**
     * Crée une nouvelle instance de la classe REApplication.
     * 
     * @param id
     * @throws Exception
     */
    public REApplication(String id) throws Exception {
        super(id);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
        // Raccord de méthode auto-généré
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("CORVUSMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {

        FWAction.registerActionCustom(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE
                + ".actionCopierDemande", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE
                + ".actionCopierDemandePourPrestTrans", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE
                + ".imprimerListeDemandeRente", FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficherCopie", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".doBuildDemande", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionAjouterPeriodeAPI",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionAjouterPeriodeINV",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionSupprimerPeriodeAPI",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionSupprimerPeriodeINV",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionAfficherPeriodeAPI",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionAfficherPeriodeINV",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionListerPeriodesINV",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".actionListerPeriodesAPI",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".supprimerSaisieDemandeRente",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".arreterSaisieDemandeRente",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".arreterMajPartielleDemande",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".majPartielleDemande",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".calculerCopie", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".ajouterSaisieDemandeRente",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".ajouterInformationsComplementaires",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".arreterInformationsComplementaires",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficherInformationsComplementaires",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".terminerDemandeVieillesse",
                FWSecureConstants.ADD);

        /**
         * Action création d'une prestation transitoire
         */
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE + ".afficherPrestationTransitoire",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE
                + ".actionAjouterPeriodeINVPrestationTransitoire", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE
                + ".ajouterSaisieDemandePrestationTransitoire", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_DEMANDE_RENTE
                + ".arreterSaisieDemandePrestationTransitoire", FWSecureConstants.UPDATE);

        /**
         * Actions liées au calcul des rentes
         */
        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_DEMANDE_RENTE + ".afficher", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_DEMANDE_RENTE + ".actionExporterScriptACOR",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_DEMANDE_RENTE + ".actionCallACORWeb",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_DEMANDE_RENTE + ".actionCheckACORWeb",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_DEMANDE_RENTE + ".actionImporterScriptACOR",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_DEMANDE_RENTE + ".actionCalculerAPI",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_RECAPITULATIF_DEMANDE_RENTE + ".validerHistorique",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("corvus.annonces.annonce.afficher", FWSecureConstants.READ);

        // FWAction.registerActionCustom(IREActions.ACTION_PERIODE_API +".xxx",
        // FWSecureConstants.ADD);

        // FWAction.registerActionCustom(IREActions.ACTION_PERIODE_INVALIDITE
        // +".xxx", FWSecureConstants.ADD);

        // FWAction.registerActionCustom(IREActions.ACTION_BASES_DE_CALCUL
        // +".xxx", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_MANUELLE_RABCPD + ".ajouterRABCPD",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_MANUELLE_RABCPD + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + ".actionBloquerRA",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
                + ".actionDebloquerMontantRA", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + ".actionExecuterDeblocage",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + ".actionDesactiverBlocage",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE + ".supprimer",
                FWSecureConstants.REMOVE);
        FWAction.registerActionCustom(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
                + ".imprimerListeRenteAccordee", FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_RENTE_LIEE_JOINT_RENTE_ACCORDEE + ".imprimerListeRenteLiee",
                FWSecureConstants.READ);
        // FWAction.registerActionCustom(IREActions.ACTION_RENTE_LIEE_JOINT_RENTE_ACCORDEE
        // +".xxx", FWSecureConstants.ADD);

        // FWAction.registerActionCustom(IREActions.ACTION_PRESTATIONS_DUES_JOINT_DEMANDE_RENTE
        // +".xxx", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT
                + ".actionEnvoyerDemandeRassemblement", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DEMANDES_DE_RASSEMBLEMENT
                + ".actionNouvelleDemandeRassemblement", FWSecureConstants.ADD);

        // FWAction.registerActionCustom(IREActions.ACTION_RASSEMBLEMENT_CI
        // +".xxx", FWSecureConstants.ADD);

        // FWAction.registerActionCustom(IREActions.ACTION_INSCRIPTION_CI
        // +".xxx", FWSecureConstants.ADD);

        // FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES_CALCUL_ACOR
        // +".xxx", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_MANUELLE_INSCRIPTION_CI + ".ajouterInscriptionCI",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_MANUELLE_INSCRIPTION_CI + ".arreter",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_TAUX, FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_PARAMETRAGE_API, FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_GENERER_LISTES_AB + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_INTERETS_MORATOIRES + ".afficher",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_TERMINER_DEMANDERENTEPREVBIL + ".afficher",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_SAISIR_ECHEANCE + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_SAISIE_MANUELLE_INSCRIPTION_CI + ".afficher",
                FWSecureConstants.UPDATE);

        // JJE
        FWAction.registerActionCustom(IREActions.ACTION_CREANCIER + ".actionRepartirLesCreances", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_CREANCIER + ".miseAJourCreancesAccordees",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".afficherPreparation",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".genererDecision", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".actionPrevalider", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".actionPrevalider", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".afficherModifier", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".annuler", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".supprimerDecision", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".enregistrerModifications",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".actionReAfficher", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".reAfficher", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".actionDecisionPrecedente",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_DECISION + ".actionDecisionSuivante",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".afficherPreparation",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".genererDecision", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".actionPrevalider",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".actionPrevalider",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".afficherModifier",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".annuler", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".supprimerDecision",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".enregistrerModifications",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".actionReAfficher",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".reAfficher", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".actionDecisionPrecedente",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".actionDecisionSuivante",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_ORDRES_VERSEMENTS + ".actionAfficherPageCID",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_ORDRES_VERSEMENTS + ".actionAjouterCID", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_PROCESS_VALIDER_DECISIONS + ".executerTTT",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_PROCESS_VALIDER_DECISIONS + ".actionValiderDirect",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_PREPARER_INTERETS_MORATOIRES + ".actionAfficher",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_INTERET_MORATOIRE + ".calculerInteretMoratoire",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_CALCUL_INTERET_MORATOIRE
                + ".genererDecisionSansInteretMoratoire", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_INTERET_MORATOIRE + ".calculerInteretMoratoire",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES + ".actionAjouterAnnonce", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES + ".actionModifierAnnonce", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES + ".actionSupprimerAnnonce", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES + ".afficher", FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_AUGMENTATION_9 + ".actionAjouterAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_AUGMENTATION_9 + ".actionModifierAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_AUGMENTATION_9 + ".actionSupprimerAnnonce",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_AUGMENTATION_10 + ".actionAjouterAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_AUGMENTATION_10 + ".actionModifierAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_AUGMENTATION_10 + ".actionSupprimerAnnonce",
                FWSecureConstants.UPDATE);

        // Fin JJE

        // PCA
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_DIMINUTION_9 + ".actionSupprimerAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_DIMINUTION_9 + ".actionAjouterAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_DIMINUTION_9 + ".actionModifierAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_DIMINUTION_10 + ".actionSupprimerAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_DIMINUTION_10 + ".actionAjouterAnnonce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_DIMINUTION_10 + ".actionModifierAnnonce",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".actionAfficherDecision", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".afficherPreparation", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".genererDecision", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".actionPrevalider", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".afficherModifier", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".annuler", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".supprimerDecision", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".enregistrerModifications", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".actionReAfficher", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".reAfficher", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".actionDecisionPrecedente", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".actionDecisionSuivante", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".activerValidationDecision",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_DECISIONS + ".desactiverValidationDecision",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_GENERER_LISTE_ORDRES_VERSEMENTS + ".actionAfficherPageCID",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_GENERER_LISTE_ORDRES_VERSEMENTS + ".actionAjouterCID",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_GENERER_LISTE_ORDRES_VERSEMENTS + ".reAfficher",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_DEBLOQUER_MONTANT_RENTE_ACCORDEE + ".deleguerActionChercher",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_RECAP_DETAIL + ".afficherAi", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_RECAP_DETAIL + ".afficherAvs", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_RECAP_DETAIL + ".modifierRecapAvs", FWSecureConstants.REMOVE);
        FWAction.registerActionCustom(IREActions.ACTION_RECAP_DETAIL + ".modifierRecapAi", FWSecureConstants.REMOVE);
        // Fin PCA

        // SCR
        // Tester ou passe le helper
        FWAction.registerActionCustom(IREActions.ACTION_RECAP_CHARGER + ".charger", FWSecureConstants.READ);
        // Tester ou passe le helper
        FWAction.registerActionCustom(IREActions.ACTION_RECAP_CHARGER + ".afficherChargement", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_RECAP_CHARGER + ".execute", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_RECAP_VISU + ".afficherChargement", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_RECAP_VISU + ".charger", FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_RECAP_DETAIL + ".chargerAvs", FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_VALIDER_LOT + ".actionAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_DECOMPTE + ".actionAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_GENERER_RECAPITULATION_RENTES + ".actionAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_GENERER_RECAPITULATION_RENTES_ARC8D + ".actionAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_GENERER_RECAPITULATION_RENTES_ARC8D + ".actionReAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_GENERER_RECAPITULATION_RENTES_ARC8D + ".reAfficher",
                FWSecureConstants.READ);

        // Non référencée !!!
        // FWAction.registerActionCustom(IREActions.ACTION_GENERER_LISTE_DEMANDE_ATTENTE
        // +".xxx", FWSecureConstants.ADD);
        // Non référencée !!!
        // FWAction.registerActionCustom(IREActions.ACTION_GENERER_LISTE_DECISIONS_VALIDEES
        // +".xxx", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_SOLDE_POUR_RESTITUTION + ".actionAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SOLDE_POUR_RESTITUTION + ".actionChercher",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_SITUATION_FAMILIALE + ".actionModifier",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_SITUATION_FAMILIALE + ".modifier", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_SITUATION_FAMILIALE + ".actionAfficherFamille",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_FACTURE_A_RESTITUER + ".actionChangeEtat",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ENVOYER_ANNONCE + ".actionExecuter", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_AFFICHER_LOT_DECISION + ".afficherLotDepuisDecision",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES + ".actionAjouter", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES + ".actionLister", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES + ".actionAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES + ".actionModifier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES + ".actionChercher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES + ".actionActiverEnvoiAcor",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_HISTORIQUE_RENTES + ".actionDesactiverEnvoiAcor",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_AVANCES + ".actionChercher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_AVANCES + ".actionAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_AVANCES + ".actionModifier", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("corvus.process.executerAvances.afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_PONCTUELLES + ".actionAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_PONCTUELLES + ".actionAjouter", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_PONCTUELLES + ".ajouterAnnoncePonctuelle",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_PONCTUELLES + ".actionModifier",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_PREVALIDER_DECISION + ".actionAfficherDossierGed",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_DEMANDE_RENTES_JOINT_PRESTATION_ACCORDEE
                + ".actionAfficherDossierGed", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_RENTE_ACCORDEE_JOINT_DEMANDE_RENTE
                + ".actionAfficherDossierGed", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_ANNONCES_PONCTUELLES + ".ajouterAnnoncePonctuelleAncienNss",
                FWSecureConstants.ADD);

        // Non référencée
        // FWAction.registerActionCustom(IREActions.ACTION_GENERER_LISTES_AB
        // +".xxx", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_EXECUTER_PAIEMENT_MENSUEL + ".afficher",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_CONCORDANCE_CENTRALE + ".afficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_CONCORDANCE_CENTRALE + ".executer", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_REPRISE_DU_DROIT + ".afficher", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_ANAKIN_VALIDATOR + ".afficher", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_ANAKIN_VALIDATOR + ".executer", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IREActions.ACTION_GLOBAZ_ADMIN + ".majDateFinDemandeRente",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_COPIES_DEFAUT + ".afficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_COPIES_DEFAUT + ".chercher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_COPIES_DEFAUT + ".ajouter", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IREActions.ACTION_COPIES_DEFAUT + ".supprimer", FWSecureConstants.REMOVE);

        FWAction.registerActionCustom(IREActions.ACTION_ANNULER_DIMINUTION_RENTE_ACCORDEE + ".afficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_ANNULER_DIMINUTION_RENTE_ACCORDEE + ".executer",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom("corvus.recap.detailRecapMensuelleAvs.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.recap.detailRecapMensuelleAi.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.imprimerDecision.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererListeAnnonces.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererListeCiAdditionnels.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererListeRestitutionsSoldes.reAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererListeRestitutionsMouvements.reAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.recap.visuRecapMensuelle.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererListeImpotSource.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererListeDecisionsValidees.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererListeDemandesAttente.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererStatOFAS.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.genererAttestationFiscaleUnique.reAfficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.comparaisonCentrale.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("corvus.process.validerLot.executer", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom("lyra.echeances.echeance.afficher", FWSecureConstants.UPDATE);

        // Tous les points du menu adaptations sont en update saut rentes adaptées
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_CIRCULAIRE + ".afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_PMT_FICTIF + ".afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_IMPORT_51_53 + ".afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_LISTE_ERREURS + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_MISE_A_JOUR_PREST + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_LISTE_PRST_AUGMENTEES + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_RENTES_ADAPTEES + ".afficher",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_MAJ_RECAP_ADAPTATION + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_LISTE_RECAP_ADAPTATION + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_LISTE_ANNONCES_SUB + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_ENVOI_ANNONCES_SUB + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_LISTE_REMARQUES_CENTRALE + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ADAPTATION_2EME_ENVOI_CENTRALE + ".afficher",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_RENTE_VERSEE_A_TORT_AJAX + ".creerNouvelleEntiteeAJAX",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ORDRES_VERSEMENTS_AJAX + ".creerCIDAJAX",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IREActions.ACTION_ORDRES_VERSEMENTS_AJAX + ".gererRestitutionAJAX",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IREActions.ACTION_BASES_DE_CALCUL_EDITER_NUMERO_DECISION,
                FWSecureConstants.UPDATE);
    }
}
