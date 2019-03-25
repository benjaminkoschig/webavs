/*
 * Créé le 20 avr. 05
 *
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.apg.application;

import java.util.Properties;
import globaz.apg.servlet.IAPActions;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.prestation.application.PRAbstractApplication;

/**
 * <H1>Description</H1>
 *
 * @author scr
 *         <p>
 *         Application APG
 *         </p>
 */
public class APApplication extends PRAbstractApplication {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** Le répertoire racine de l'application */
    public static final String APPLICATION_APG_REP = "apgRoot";

    /** Le préfixe de l'application */
    public static final String APPLICATION_PREFIX = "AP";

    /**
     */
    public static final String CS_DOMAINE_ADRESSE_APG = "519002";

    /** Le nom de l'application */
    // @deprecated
    public static final String DEFAULT_APPLICATION_APG = "APG";

    /** nom de la propriete qui definit le role administrateur */
    private static final String ID_ROLE_PRESTATION_GLOBAZ = "role.prestation.globaz";

    /** la cle utilisee pour les postit sur les droits APG et Mat */
    public static final String KEY_POSTIT_DROIT_APG_MAT = "globaz.apg.droits";

    /** la cle utilisee pour les postit sur les prestations APG et Mat */
    public static final String KEY_POSTIT_PRESTATIONS_APG_MAT = "globaz.apg.prestations";

    /** Le no de la caisse CCJU */
    public static final String NO_CAISSE_CCJU = "150";

    /** Le no de la caisse CICICAM */
    public static final String NO_CAISSE_CICICAM = "059";

    /** Le no de la caisse CVCI */
    public static final String NO_CAISSE_CVCI = "109";

    /** Le no de la caisse CCVD */
    public static final String NO_CAISSE_CCVD = "022";

    /** Le no de la caisse AGRIVIT */
    public static final String NO_CAISSE_AGRIVIT = "116";

    /**
     * le nom de la propriete qui permet d'obtenir le nom de la classe de factory des adapteurs acor
     */
    public static final String PROPERTY_ACOR_FACTORY = "acor.apg.factory.class";

    /**
     */
    public static final String PROPERTY_CLONE_COPIER_DROIT_APG = "clone.copie.droit.apg.id";

    /**
     */
    public static final String PROPERTY_CLONE_COPIER_DROIT_MATERNITE = "clone.copie.droit.maternite.id";

    /**
     */
    public static final String PROPERTY_CLONE_CORRIGER_DROIT_APG = "clone.correction.droit.apg.id";

    /** Le nom de la propriété du nom de fichier de clonage */
    public static final String PROPERTY_CLONE_DEFINITION_FILENAME = "clone.structure.definition.filename";

    /** Le nom de la propriété département emabled */
    public static final String PROPERTY_DEPARTEMENT_ENABLED = "droits.situation.professionnelle.departement.enabled";

    public static final String PROPERTY_DISPLAY_NIP = "document.display.nip";

    /**
     * Détermine si le libelle confidentiel doit être ajouté dans l'adresse du destinataire dans l'envoi des documents
     */
    public static final String PROPERTY_DOC_CONFIDENTIEL = "documents.is.confidentiel";

    /**
     * @deprecated Utiliser plutôt APProperties
     */
    @Deprecated
    public static final String PROPERTY_DROIT_ACM_MAT_DUREE_JOURS = "droits.acm.maternite.dureejours";

    /**
     * Le nom de la propriété de la durée de jour d'un droit maternité cantonale
     */
    public static final String PROPERTY_DROIT_MAT_CANTONALE_DUREE_JOURS = "droits.maternite.cantonale.dureejours";

    /** Le nom de la propriété de la durée de jour d'un droit maternité */
    public static final String PROPERTY_DROIT_MAT_DUREE_JOURS = "droits.maternite.dureejours";

    /** Le groupe gestionnaire APG */
    public static final String PROPERTY_GROUPE_APG_GESTIONNAIRE = "groupe.apg.gestionnaire";

    /** Le groupe gestionnaire maternité */
    public static final String PROPERTY_GROUPE_MATERNITE_GESTIONNAIRE = "groupe.maternite.gestionnaire";

    /**
     * Le nom de la propriété identifiant s'il s'agit d'une caisse traitant du droit maternité cantonale
     */
    public static final String PROPERTY_IS_DROIT_MAT_CANTONALE = "isDroitMaterniteCantonale";

    /**
     * Le nom de la propriété qui permet d'afficher un récapitulatif dans le décompte APG
     */
    public static final String PROPERTY_IS_RECAPITULATIF_DECOMPTE = "isRecapitulatifDecompte";

    /** Les décomptes générer doivent-ils être envoyé à la GED */
    /** @deprecated **/
    @Deprecated
    public static final String PROPERTY_IS_SENT_TO_GED = "isSentToGED";
    /**
     * Le nom de la propriété pour le montant minimum de la prestation maternité
     */
    public static final String PROPERTY_MONTANT_MIN_MATERNITE = "montant.minimum.prestation.maternite";

    /**
     * le montant journalier minimum à payer à l'assuré si le montant à verser est plus grand que le montant versé par
     * l'employeur
     */
    public static final String PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE = "montant.minimum.paye.assure";

    /** le montant minimum payé à l'employeur (champ 'Min. payé' de ACOR) */
    public static final String PROPERTY_MONTANT_MINIMUM_PAYE_EMPLOYEUR_APG = "montant.minimum.paye.employeur.apg";

    /** le montant minimum payé à l'employeur (champ 'Min. payé' de ACOR) */
    public static final String PROPERTY_MONTANT_MINIMUM_PAYE_EMPLOYEUR_MAT = "montant.minimum.paye.employeur.mat";

    /** Service de la ged a utiliser */
    public static final String PROPERTY_SERVICE_GED = "service.ged";
    /** ID Assurance pour les compléments CIAB */
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID = "apg.assurance.complementCIAB.ju.paritaire.id";
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID = "apg.assurance.complementCIAB.ju.personnel.id";
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID = "apg.assurance.complementCIAB.be.paritaire.id";
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID = "apg.assurance.complementCIAB.be.personnel.id";

    /** Service de la ged a utiliser */
    public static final String PROPERTY_IS_FERCIAB = "apg.isFERCIAB";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe APApplication.
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APApplication() throws Exception {
        super(APApplication.DEFAULT_APPLICATION_APG);
    }

    /**
     * Crée une nouvelle instance de la classe APApplication.
     *
     * @param id
     *            DOCUMENT ME!
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APApplication(String id) throws Exception {
        super(id);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * (non-Javadoc)
     *
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * (non-Javadoc)
     *
     * @throws Exception
     *             DOCUMENT ME!
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("APGMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {

        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_COMPENSATIONS + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".actionAfficherLAPG", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".actionRecapitulatif", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".copierDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".corrigerDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".restituerDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".calculerDroitMaterniteCantonale",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".calculerACM", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".actionAfficherDossierGed",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".simulerPaiementDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("apg.droits.droitAPGP" + ".finaliserCreationDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("apg.droits.droitMatP" + ".finaliserCreationDroit", FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_GENERER_COMM_DEC_AMAT,
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_SAISIE_CARTE_APG + ".actionAjouterPeriode",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_SAISIE_CARTE_APG + ".actionSupprimerPeriode",
                FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_SAISIE_CARTE_AMAT,
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_PERE_MAT + ".actionAfficherPere", FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_ENFANT_MAT,
        // FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_PERIODE,
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_SITUATION_PROFESSIONNELLE + ".ecranSuivant",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_SITUATION_PROFESSIONNELLE + ".rechercherAffilie",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_SITUATION_PROFESSIONNELLE + ".modifier",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_ENFANT_APG + ".actionAfficherEnfantDeListe",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_ENFANT_APG + ".actionCalculerPrestation",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_ENFANT_APG + ".actionCalculerToutesLesPrestation",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_ENFANT_APG + ".actionEtape3", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_ENFANT_APG + ".actionFindIdTiersByNoAvs",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_ENFANT_APG + ".arreterEtape3", FWSecureConstants.READ);

        // FWAction.registerActionCustom(IAPActions.ACTION_RECAPITUALATIF_DROIT_APG+".",
        // FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_RECAPITUALATIF_DROIT_MAT+".",
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATIONS + ".actionCalculerPrestations",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                IAPActions.ACTION_PRESTATIONS + "." + IAPActions.ACTION_CONTROLE_PRESTATION_CALCULEES,
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATIONS + ".actionCalculerToutesLesPrestations",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATIONS + ".actionImporterPrestationsDepuisACOR",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_REPARTITION_PAIEMENTS + ".actionNouvelleVentilation",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IAPActions.ACTION_REPARTITION_PAIEMENTS + ".actionPrestationPrecedante",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_REPARTITION_PAIEMENTS + ".actionPrestationSuivante",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_REPARTITION_PAIEMENTS + ".actionRepartirLesMontants",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_REPARTITION_PAIEMENTS + ".miseAJourRepartitionsCotisations",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_REPARTITION_PAIEMENTS + ".actionPreparerChercher",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_CONTROLE_PRESTATIONS_APG + ".afficher",
                FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_LOTS,
        // FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_COMPENSATIONS_LOT,
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_FACTURES_LOT + ".actionAfficherEcranDE",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_LOT + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + ".actionAjouterDansLot",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + ".actionMettreDansLot",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(
                IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + ".actionRepartitionPaiements",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATION_JOINT_LOT_TIERS_DROIT + ".imprimerListePrestations",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_ANNONCEAPG + ".choisirType", FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_ANNONCEREVIVION1999 + ".choisirType", FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_ANNONCEREVISION2005 + ".choisirType", FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_ENVOYER_ANNONCE + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_STATS_OFAS + ".afficher", FWSecureConstants.READ);

        // FWAction.registerActionCustom(IAPActions.ACTION_COTISATION_JOINT_REPARTITION,
        // FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_COTISATIONS_ASSURANCES,
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_DECOMPTES + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_ENVOYER_CI + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_INSCRIRE_CI + ".afficher", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_ATTESTATIONS_FISCALES + ".afficher", FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_LISTE_RECAPITULATION_ANNONCE + ".afficher",
                FWSecureConstants.READ);

        // FWAction.registerActionCustom(IAPActions.ACTION_CATALOGUES,
        // FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_TEXTE_JOINT_CATALOGUE,
        // FWSecureConstants.UPDATE);

        // FWAction.registerActionCustom(IAPActions.ACTION_TEXTE_JOINT_CATALOGUE_SAISIE,
        // FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".actionCalculerACOR", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".actionTelechargerFichier2",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_INFO_COMPL + ".actionAfficherInfoCompl",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_ATTESTATIONS_APG + ".afficher", FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_LISTE_CONTROLE + ".afficher", FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_DECISION_REFUS + ".afficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_DECISION_REFUS + ".afficherDepuisChoixAnnexesCopies",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_DECISION_REFUS + ".allerVersChoixAnnexesEtCopies",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IAPActions.ACTION_ANNONCESEDEX + ".reenvoyer", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_ANNONCESEDEX + ".modifier", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("apg.process.genererAttestations.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("apg.process.recapitulationAnnonce.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("apg.process.genererStatsOFAS.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("apg.process.listeControle.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("apg.process.genererDecisionAMAT.reAfficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_LISTE_PRESTATION_VERSEE + "." + FWAction.ACTION_REAFFICHER,
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_LISTE_TAXATIONS + "." + FWAction.ACTION_REAFFICHER,
                FWSecureConstants.READ);

    }

    /**
     * (non-Javadoc)!
     *
     * @param arg0
     *            DOCUMENT ME!
     * @see globaz.globall.db.BApplication#_readProperties(java.util.Properties)
     */
    protected void _readProperties(Properties arg0) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * retourne l'identifiant du rôle représentant un administrateur du système
     *
     * @return la valeur courante de l'attribut id role administrateur
     */
    public String getIdRoleAdministrateur() {
        return this.getProperty(APApplication.ID_ROLE_PRESTATION_GLOBAZ);
    }

}
