/*
 * Créé le 17 août 05
 * 
 * Pour changer le modèle de ce fichier généré, allez à : Fenêtre&gt;Préférences&gt;Java&gt;Génération de code&gt;Code
 * et commentaires
 */
package globaz.ij.application;

import globaz.framework.controller.FWAction;
import globaz.framework.menu.FWMenuCache;
import globaz.framework.secure.FWSecureConstants;
import globaz.ij.servlet.IIJActions;
import globaz.prestation.application.PRAbstractApplication;
import java.util.Properties;

/**
 * <H1>Description</H1>
 * <p>
 * Classe application pour la partie IJ des prestations.
 * </p>
 * 
 * @author vre
 */
public class IJApplication extends PRAbstractApplication {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** Le répertoire racine de l'application. */
    public static final String APPLICATION_IJ_REP = "ijRoot";

    /** Le préfixe de l'application. */
    public static final String APPLICATION_PREFIX = "IJ";

    public static final String CS_DOMAINE_ADRESSE_IJAI = "519009";

    /** Le nom de l'application. */
    public static final String DEFAULT_APPLICATION_IJ = "IJ";

    /** Détermine si la caisse possède une entête spécifique */
    public static final String HAS_SPECIFIC_HEADER = "hasSpecificHeader";

    /** nom de la propriete qui definit le role administrateur */
    private static final String ID_ROLE_PRESTATION_GLOBAZ = "role.prestation.globaz";

    /** Détermine si la caisse est privée */
    public static final String IS_CAISSE_CANTONALE = "isCaisseCantonale";

    /** la cle utilisee pour les postit sur les prestations */
    public static final String KEY_POSTIT_BASES_INDEMNISATION = "globaz.ij.basesindemnisation";

    /** la cle utilisee pour les postit sur les prestations */
    public static final String KEY_POSTIT_PRESTATIONS = "globaz.ij.prestations";
    /** la cle utilisee pour les postit sur les prononce */
    public static final String KEY_POSTIT_PRONONCES = "globaz.ij.prononces";

    /** Le no de la caisse CCJU */
    public static final String NO_CAISSE_CCJU = "150";
    /** Le no de la caisse CICICAM */
    public static final String NO_CAISSE_CICICAM = "059";
    /** Le no de la caisse CVCI */
    public static final String NO_CAISSE_CVCI = "109";

    public static final String PROPERTY_ACOR_FACTORY = "acor.ij.factory.class";
    public static final String PROPERTY_CLONE_BASE_COR = "clone.ij.baseindemnisation.correction";
    public static final String PROPERTY_CLONE_DEFINITION = "clone.ij.definition";
    public static final String PROPERTY_CLONE_GRANDEIJ_COPIE = "clone.ij.grande.copie";
    public static final String PROPERTY_CLONE_GRANDEIJ_COR = "clone.ij.grande.correction";
    public static final String PROPERTY_CLONE_IJ_AA_COPIE = "clone.ij.aa.copie";
    public static final String PROPERTY_CLONE_IJ_AA_COR = "clone.ij.aa.correction";

    public static final String PROPERTY_CLONE_IJ_AIT_COPIE = "clone.ij.ait.copie";

    public static final String PROPERTY_CLONE_IJ_AIT_COR = "clone.ij.ait.correction";

    public static final String PROPERTY_CLONE_PETITEIJ_COPIE = "clone.ij.petite.copie";

    public static final String PROPERTY_CLONE_PETITEIJ_COR = "clone.ij.petite.correction";

    public static final String PROPERTY_CLONE_PREST_COP = "clone.ij.prestations.copie";

    /** 
     */
    public static final String PROPERTY_DATE_DEBUT_4EME_REVISION = "dateDebut4emeRevision";

    public static final String PROPERTY_DISPLAY_NIP = "document.display.nip";

    /**
     * Détermine si le libelle confidentiel doit être ajouté dans l'adresse du destinataire dans l'envoi des documents
     */
    public static final String PROPERTY_DOC_CONFIDENTIEL = "documents.is.confidentiel";

    /**
     * détermine si on enrichi le header avec la donnée Nom du collaborateur (mappé sur le template tag
     * P_HEADER_NOM_COLLABORATEUR)
     */
    public static final String PROPERTY_DOC_NOMCOLABO = "documents.use.header.nom.collaborateur";

    /**
     * Le nom de la propriété qui donne le code de genre de prestation pour Acor (importation des rentes en cours)
     */
    public static final String PROPERTY_GENRE_PRESTATION_ACOR = "genrePrestationRenteEnCoursAcor";

    /** 
     */
    public static final String PROPERTY_GROUPE_IJ_GESTIONNAIRE = "groupe.ij.gestionnaire";

    /**
     * Le nom de la propriété qui permet d'afficher un récapitulatif dans le décompte APG
     */
    public static final String PROPERTY_IS_RECAPITULATIF_DECOMPTE = "isRecapitulatifDecompte";

    /** Détermine la valeur minimum garanti d'une indemnité (4eme rev.) */
    public static final String PROPERTY_MONTANT_INDEMNITE_MINIMUM_GARANTI_REV_4 = "montant.indemnite.minimum.garanti.rev.4";

    /** Détermine la valeur minimum garanti d'une indemnité (4eme rev.) dès 2008 */
    public static final String PROPERTY_MONTANT_INDEMNITE_MINIMUM_GARANTI_REV_4_DES2008 = "montant.indemnite.minimum.garanti.rev.4.des2008";

    /** Détermine la valeur plafonnée d'une indemnité (4eme rev.) */
    public static final String PROPERTY_MONTANT_INDEMNITE_PLAFONNEE_REV_4 = "montant.indemnite.plafonnee.rev.4";

    /** Détermine la valeur plafonnée d'une indemnité (4eme rev.) dès 2008 */
    public static final String PROPERTY_MONTANT_INDEMNITE_PLAFONNEE_REV_4_DES2008 = "montant.indemnite.plafonnee.rev.4.des2008";

    /** Valeur maximum garanti pour une petite IJ */
    public static final String PROPERTY_MONTANT_MAX_PETITE_IJ = "montant.petite.ij.maximum";

    /** Valeur maximum garanti pour une petite IJ */
    public static final String PROPERTY_MONTANT_MAX_PETITE_IJ_REV_4_DES2008 = "montant.petite.ij.maximum.rev.4.des2008";

    /** Valeur maximum garanti pour une petite IJ */
    public static final String PROPERTY_MONTANT_MAX_PETITE_IJ_REV_5_DES2008 = "montant.petite.ij.maximum.rev.5.des2008";

    /** Détermine la valeur maximum du revenu déterminant (4eme rev.) */
    public static final String PROPERTY_MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_4 = "montant.revenu.determinant.maximum.rev.4";
    /** Détermine la valeur maximum du revenu déterminant (4eme rev.) dès 2008 */
    public static final String PROPERTY_MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_4_DES2008 = "montant.revenu.determinant.maximum.rev.4.des2008";

    /** Valeur maximum du revenu déterminant (5eme rev.) */
    public static final String PROPERTY_MONTANT_REVENU_DETERMINANT_MAXIMUM_REV_5 = "montant.revenu.determinant.maximum.rev.5";

    /** Détermine la valeur minimum du revenu déterminant (4eme rev.) */
    public static final String PROPERTY_MONTANT_REVENU_DETERMINANT_MINIMUM_REV_4 = "montant.revenu.determinant.minimum.rev.4";

    /** Détermine la valeur minimum du revenu déterminant (4eme rev.) dès 2008 */
    public static final String PROPERTY_MONTANT_REVENU_DETERMINANT_MINIMUM_REV_4_DES2008 = "montant.revenu.determinant.minimum.rev.4.des2008";

    /** Le no de l'office AI CCJU */
    public static final String PROPERTY_NO_OFFICEAI = "noOfficeAI";

    /**
	 * 
	 */
    public static final String PROPERTY_PERIODE_IJ_FORMAT_AAAMM = "format.periode.aaaamm";

    /** 
     */
    public static final String PROPERTY_PRONONCE_AVEC_DECISION = "prononce.avec.decision";

    /** 
     */
    public static final String PROPERTY_SALAIRE_ALLOCATION_MAX = "salaire.allocationmax";

    // ~ Constructors
    // ---------------------------------------------------------------------------------------------------

    /**
     * Crée une nouvelle instance de la classe IJApplication.
     * 
     * @throws Exception
     */
    public IJApplication() throws Exception {
        super(IJApplication.DEFAULT_APPLICATION_IJ);
    }

    /**
     * Crée une nouvelle instance de la classe IJApplication.
     * 
     * @param id
     * @throws Exception
     */
    public IJApplication(String id) throws Exception {
        super(id);
    }

    // ~ Methods
    // --------------------------------------------------------------------------------------------------------

    /**
     * @see globaz.globall.db.BApplication#_declareAPI()
     */
    @Override
    protected void _declareAPI() {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * @throws Exception
     *             DOCUMENT ME!
     * 
     * @see globaz.globall.db.BApplication#_initializeApplication()
     */
    @Override
    protected void _initializeApplication() throws Exception {
        FWMenuCache cache = FWMenuCache.getInstance();
        cache.addFile("IJMenu.xml");
    }

    @Override
    protected void _initializeCustomActions() {

        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_LOT + ".executer", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_COMPENSATIONS + ".executer", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_DECOMPTES + ".executer", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_ENVOYER_ANNONCES + ".executer", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_INSCRIRE_CI + ".executer", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_FORMULAIRES + ".executer", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_FORMULAIRE + ".executer", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IIJActions.ACTION_PETITE_IJ_JOINT_REVENU + ".arreterEtape5",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IIJActions.ACTION_SITUATION_PROFESSIONNELLE + ".recupererInfosPrononc",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_SITUATION_PROFESSIONNELLE + ".saveNewViewBean",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_SITUATION_PROFESSIONNELLE + ".rechercherAffilie",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + ".actionCreerCorrection",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + ".actionAnnuler",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + ".actionCreerCopie",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE_JOINT_DEMANDE + ".simulerPaiementDroit",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".afficherDateFin", FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".terminerPrononce", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".terminerPrononceFinal", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".actionAfficherDossierGed",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".saisirEcheance", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".modiferEcheance", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".saisirNoDecision", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_PRONONCE + ".modiferNoDecision", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IIJActions.ACTION_REQUERANT + ".actionFindIdTiersByNoAvs", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_REQUERANT + ".arreterEtape1", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IIJActions.ACTION_BASE_INDEMNISATION + ".creerCorrection", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_BASE_INDEMNISATION + ".displayCalendar", FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_BASE_INDEMNISATION + ".displayFullMonthCalendar",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA + ".creerCorrection",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA + ".displayCalendar",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_BASE_INDEMNISATION_AIT_AA + ".displayFullMonthCalendar",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_PRESTATION_JOINT_LOT_PRONONCE + ".actionAjouterDansLot",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IIJActions.ACTION_SAISIE_PRONONCE + ".arreterEtape3", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIE_PRONONCE + ".vaChercherCsTypeIJ", FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_SAISIE_PRONONCE_AIT + ".arreterEtape3", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIE_PRONONCE_AIT + ".vaChercherCsTypeIJ",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST + ".arreterEtape3",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST + ".vaChercherCsTypeIJ",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIE_PRONONCE_ALLOC_ASSIST + ".calculer",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".actionExporterScriptACOR",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".actionExporterScriptACOR2",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".actionIJCalculeePrecedante",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".actionIJCalculeeSuivante",
                FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".actionImporterDecompte",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".actionCalculerPrestation",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".actionCallACORWeb",
                FWSecureConstants.ADD);

        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE_AIT_AA + ".calculerAit", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE_AIT_AA + ".calculerAa", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_IJ + ".actionExporterScriptACOR", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_IJ + ".actionExporterScriptACOR2", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_IJ + ".actionImporterIJ", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_IJ + ".calculerAit", FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_IJ + ".actionCallACORWeb", FWSecureConstants.ADD);

        FWAction.registerActionCustom(IIJActions.ACTION_FACTURES_LOT + ".actionAfficherEcranDE", FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_FACTURES_LOT + ".saveNewViewBean", FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_DECISION + ".arreterGenererDecision",
                FWSecureConstants.ADD);
        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_DECISION + ".allerVersChoisirParagraphes",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_REPARTITION_PAIEMENTS + ".actionNouvelleVentilation",
                FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_INFO_COMPL + ".actionAfficherInfoCompl", FWSecureConstants.READ);

        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".afficherDateFin", FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".terminerPrononce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".terminerPrononceFinal",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".actionAfficherDossierGed",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".saisirEcheance", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".modiferEcheance",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".saisirNoDecision",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_TERMINER_PRONONCE + ".modiferNoDecision",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".afficherDateFin", FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".terminerPrononce", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".terminerPrononceFinal",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".actionAfficherDossierGed",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".saisirEcheance", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".modiferEcheance", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".saisirNoDecision", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_ECHEANCE + ".modiferNoDecision",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".afficherDateFin", FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".terminerPrononce",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".terminerPrononceFinal",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".actionAfficherDossierGed",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".saisirEcheance",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".modiferEcheance",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".saisirNoDecision",
                FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_NO_DECISION + ".modiferNoDecision",
                FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_IJ + ".afficher", FWSecureConstants.REMOVE);

        FWAction.registerActionCustom(IIJActions.ACTION_CALCUL_DECOMPTE + ".afficher", FWSecureConstants.REMOVE);

        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_COMPENSATIONS + ".afficher", FWSecureConstants.REMOVE);

        FWAction.registerActionCustom(IIJActions.ACTION_ENVOYER_ANNONCES + ".afficher", FWSecureConstants.REMOVE);

        FWAction.registerActionCustom(IIJActions.ACTION_ENVOYER_CI + ".afficher", FWSecureConstants.REMOVE);

        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_DECOMPTES + ".afficher", FWSecureConstants.REMOVE);

        FWAction.registerActionCustom(IIJActions.ACTION_GENERER_DECISION + ".afficher", FWSecureConstants.REMOVE);
        FWAction.registerActionCustom(IIJActions.ACTION_VALIDER_DECISION + ".afficher", FWSecureConstants.READ);
        FWAction.registerActionCustom(IIJActions.ACTION_VALIDER_DECISION + ".validerTout", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_VALIDER_DECISION + ".validerSedex", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_VALIDER_DECISION + ".validerGED", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_VALIDER_DECISION + ".validerDecision", FWSecureConstants.UPDATE);

        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_TAUX_IS + ".modiferTauxIS", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom(IIJActions.ACTION_SAISIR_TAUX_IS + ".saisirTauxIS", FWSecureConstants.UPDATE);
        FWAction.registerActionCustom("ij.prononces.requerant.actionEcranSuivant", FWSecureConstants.READ);
        FWAction.registerActionCustom("ij.prononces.saisiePrononce.actionEcranSuivant", FWSecureConstants.READ);
        FWAction.registerActionCustom("ij.acor.calculACORIJ.afficher", FWSecureConstants.READ);
        FWAction.registerActionCustom("ij.prononces.saisiePrononceAit.ecranSuivant", FWSecureConstants.READ);

    }

    /**
     * @param properties
     *            DOCUMENT ME!
     * @see globaz.globall.db.BApplication#_readProperties(java.util.Properties)
     */
    protected void _readProperties(Properties properties) {
        // TODO Raccord de méthode auto-généré
    }

    /**
     * retourne l'identifiant du rôle représentant un administrateur du système
     * 
     * @return la valeur courante de l'attribut id role administrateur
     */
    public String getIdRoleAdministrateur() {
        return this.getProperty(IJApplication.ID_ROLE_PRESTATION_GLOBAZ);
    }

}
