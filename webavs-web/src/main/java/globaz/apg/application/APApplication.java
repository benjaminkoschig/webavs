/*
 * Cr�� le 20 avr. 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � : Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code
 * et commentaires
 */
package globaz.apg.application;

import globaz.apg.servlet.IAPActions;
import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.fweb.taglib.FWAppColorTag;
import globaz.prestation.application.PRAbstractApplication;

import java.util.Properties;

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

    /** Le r�pertoire racine de l'application */
    public static final String APPLICATION_APG_REP = "apgRoot";

    /** Le pr�fixe de l'application */
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

    /** Le nom de la propri�t� du nom de fichier de clonage */
    public static final String PROPERTY_CLONE_DEFINITION_FILENAME = "clone.structure.definition.filename";

    /** Le nom de la propri�t� d�partement emabled */
    public static final String PROPERTY_DEPARTEMENT_ENABLED = "droits.situation.professionnelle.departement.enabled";

    public static final String PROPERTY_DISPLAY_NIP = "document.display.nip";

    /**
     * D�termine si le libelle confidentiel doit �tre ajout� dans l'adresse du destinataire dans l'envoi des documents
     */
    public static final String PROPERTY_DOC_CONFIDENTIEL = "documents.is.confidentiel";

    /**
     * @deprecated Utiliser plut�t APProperties
     */
    @Deprecated
    public static final String PROPERTY_DROIT_ACM_MAT_DUREE_JOURS = "droits.acm.maternite.dureejours";

    /**
     * Le nom de la propri�t� de la dur�e de jour d'un droit maternit� cantonale
     */
    public static final String PROPERTY_DROIT_MAT_CANTONALE_DUREE_JOURS = "droits.maternite.cantonale.dureejours";

    /** Le nom de la propri�t� de la dur�e de jour d'un droit maternit� */
    public static final String PROPERTY_DROIT_MAT_DUREE_JOURS = "droits.maternite.dureejours";

    /** Le nom de la propri�t� de la dur�e de jour d'un droit maternit� */
    public static final String PROPERTY_DROIT_PAT_DUREE_JOURS = "droits.paternite.dureejours";

    /** Le groupe gestionnaire APG */
    public static final String PROPERTY_GROUPE_APG_GESTIONNAIRE = "groupe.apg.gestionnaire";

    /** Le groupe gestionnaire maternit� */
    public static final String PROPERTY_GROUPE_MATERNITE_GESTIONNAIRE = "groupe.maternite.gestionnaire";

    /** Le groupe gestionnaire pand�mie */
    public static final String PROPERTY_GROUPE_PANDEMIE_GESTIONNAIRE = "groupe.pandemie.gestionnaire";

    /**
     * Le nom de la propri�t� identifiant s'il s'agit d'une caisse traitant du droit maternit� cantonale
     */
    public static final String PROPERTY_IS_DROIT_MAT_CANTONALE = "isDroitMaterniteCantonale";

    /**
     * Le nom de la propri�t� qui permet d'afficher un r�capitulatif dans le d�compte APG
     */
    public static final String PROPERTY_IS_RECAPITULATIF_DECOMPTE = "isRecapitulatifDecompte";

    /** Les d�comptes g�n�rer doivent-ils �tre envoy� � la GED */
    /** @deprecated **/
    @Deprecated
    public static final String PROPERTY_IS_SENT_TO_GED = "isSentToGED";
    /**
     * Le nom de la propri�t� pour le montant minimum de la prestation maternit�
     */
    public static final String PROPERTY_MONTANT_MIN_MATERNITE = "montant.minimum.prestation.maternite";

    /**
     * le montant journalier minimum � payer � l'assur� si le montant � verser est plus grand que le montant vers� par
     * l'employeur
     */
    public static final String PROPERTY_MONTANT_MINIMUM_PAYE_ASSURE = "montant.minimum.paye.assure";

    /** le montant minimum pay� � l'employeur (champ 'Min. pay�' de ACOR) */
    public static final String PROPERTY_MONTANT_MINIMUM_PAYE_EMPLOYEUR_APG = "montant.minimum.paye.employeur.apg";

    /** le montant minimum pay� � l'employeur (champ 'Min. pay�' de ACOR) */
    public static final String PROPERTY_MONTANT_MINIMUM_PAYE_EMPLOYEUR_MAT = "montant.minimum.paye.employeur.mat";

    /** Service de la ged a utiliser */
    public static final String PROPERTY_SERVICE_GED = "service.ged";
    /** ID Assurance pour les compl�ments CIAB */
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_JU_ID = "apg.assurance.complementCIAB.ju.paritaire.id";
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_JU_ID = "apg.assurance.complementCIAB.ju.personnel.id";
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PARITAIRE_BE_ID = "apg.assurance.complementCIAB.be.paritaire.id";
    public static final String PROPERTY_ASSURANCE_COMPLEMENT_PERSONNEL_BE_ID = "apg.assurance.complementCIAB.be.personnel.id";

    /** Service de la ged a utiliser */
    public static final String PROPERTY_IS_FERCIAB = "apg.isFERCIAB";


    /** Propri�t� d'activation MATCIAB1 et MATCIAB2 */
    public static final String PROPERTY_APG_FERCIAB_MATERNITE = "apg.FERCIAB.maternite";

    /** Le nom de la propri�t� sur la v�rification du nombre de jour saisi */
    public static final String PROPERTY_PAT_WARN_VERIF_JOUR = "droits.paternite.verifier.jour";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Cr�e une nouvelle instance de la classe APApplication.
     *
     * @throws Exception
     *             DOCUMENT ME!
     */
    public APApplication() throws Exception {
        super(APApplication.DEFAULT_APPLICATION_APG);
    }

    /**
     * Cr�e une nouvelle instance de la classe APApplication.
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
        // TODO Raccord de m�thode auto-g�n�r�
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
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".actionEnvoyerMail", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".actionAttenteReponse", FWSecureConstants.READ);
        FWAction.registerActionCustom(IAPActions.ACTION_DROIT_LAPG + ".actionRefuser", FWSecureConstants.READ);
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
        FWAction.registerActionCustom("apg.droits.droitPatP" + ".finaliserCreationDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("apg.droits.droitPan" + ".finaliserCreationDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_SAISIE_CARTE_PAI + ".finaliserCreationDroit", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_SAISIE_CARTE_PAN+ ".passerDroitErreur", FWSecureConstants.UPDATE);

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
        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATIONS + ".actionCalculerToutesLesPrestationsMATCIAB2",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATIONS + ".actionImporterPrestationsDepuisACOR",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IAPActions.ACTION_PRESTATIONS + ".actionValiderPrestationsDepuisACORWeb",
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

        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".afficher",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".actionExporterScriptACOR",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".actionCallACORWeb",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".actionImporterApg",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".actionCalculerACOR", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_CALCUL_ACOR + ".actionTelechargerFichier",
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
        FWAction.registerActionCustom(IAPActions.ACTION_GENERER_DROIT_PAN_MENSUEL + ".afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_PANDEMIE_FIN_DU_DROIT + ".afficher", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IAPActions.ACTION_LISTE_PANDEMIE_CONTROLE+".afficher",FWSecureConstants.UPDATE);
    }

    /**
     * (non-Javadoc)!
     *
     * @param arg0
     *            DOCUMENT ME!
     */
    protected void _readProperties(Properties arg0) {
        // TODO Raccord de m�thode auto-g�n�r�
    }

    /**
     * retourne l'identifiant du r�le repr�sentant un administrateur du syst�me
     *
     * @return la valeur courante de l'attribut id role administrateur
     */
    public String getIdRoleAdministrateur() {
        return this.getProperty(APApplication.ID_ROLE_PRESTATION_GLOBAZ);
    }

}
