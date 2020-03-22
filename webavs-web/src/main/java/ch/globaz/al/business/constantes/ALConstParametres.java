package ch.globaz.al.business.constantes;

/**
 * Constantes liées au paramètres de l'application (table ALPARAM). La valeur de chaque constante correspond au nom du
 * paramètre dans la table et non à sa valeur réelle
 * 
 * @author jts
 * 
 */
public interface ALConstParametres {

    /**
     * Indique si les prestations suisses sont générées automatiquement ou non dans le calcul ADI
     */
    public static final String ADI_AUTO_PRESTATIONS_CH = "ADI_AUTO_PRESTATIONS_CH";
    /**
     * parmètre indiquant s'il faut afficher ou non le champ attestation à l'allocataire
     */

    public static final String AFFICHAGE_ATTESTATION_ALLOC = "declarationVersementAlloc";
    /**
     * Nom de l'application
     */
    public static final String APPNAME = "WEBAF";
    /**
     * paramètre indiquant la valeur par défaut à mettre dans le champ attestation à l'allocataire
     */

    public static final String ATTEST_ALLOC_VAL = "declarationVersementAllocValeur";

    /**
     * Catégorie de tarif de la caisse (code système)
     */
    public static final String CAT_TARIF_CAISSE = "CATARCAIS";

    /**
     * Nom du paramètre indiquant s'il faut vérifier que l'allocataire PSA touche des PC
     */
    public static final String CHECK_PC_FAMILLE = "CHECK_PC_FAMILLE";
    /**
     * Nom du paramètre indiquant le / les états dossier ne bloquant pas la génération globale
     */
    public static final String DOSSIER_ETAT_GEN_GLOBALE = "DOSSIER_ETAT_GEN_GLOBALE";
    /**
     * Nom du paramètre indiquant le / les états dossier engendrant une journalisation lors du changement d'état
     */
    public static final String DOSSIER_ETAT_JOURNALISE = "DOSSIER_ETAT_JOURNALISE";
    /**
     * Nom du paramètre indiquant le / les états dossier engendrant la gestion des annonces RAFAM lors de mutation au
     * sein d'un dossier
     */
    public static final String DOSSIER_ETAT_RAFAM = "DOSSIER_ETAT_RAFAM";
    /**
     * Nom du paramètre indiquant si la vue v2 de la liste des droits est activée
     */
    public static final String DROITS_VIEW_EXTENDED = "DROITS_VIEW_EXTENDED";

    /**
     * Nom du paramètre indiquant si le supplément FNB est activé
     */
    public static final String FNB_IS_ENABLED = "FNB_IS_ENABLED";
    /**
     * Indique si les documents impliquant les types d'activités d'allocataires doivent être imprimés séparément ou
     * groupés
     */
    public static final String IMPRIMER_ACTIVITES_SEP = "IMPRIMER_ACTIVITES_SEP";
    /**
     * Incrément pour la génération de numéros de facture
     */
    public static final String INCREMENT_FACTURE = "INCREMENT_FACTURE";
    public static final String LOCK_DOSSIER_AFFILIE = "LOCK_DOSSIER_AFFILIE";
    /**
     * Mode de calcul à utiliser
     */
    public static final String MODE_CALCUL = "CALCMODE";
    /**
     * Mode caisse AF (utilisation du tarif de la caisse dans tous les cas)
     */
    public static final String MODE_CALCUL_CAISSEAF = "CAISSEAF";

    /**
     * Mode Canton de l'employeur (utilisation du tarif de la caisse dans tous les cas)
     */
    public static final String MODE_CALCUL_CANTEMPL = "CANTEMPL";
    /**
     * Mode comparaison de droit (comparaison des tarif)
     */
    public static final String MODE_CALCUL_COMPDROIT = "COMPDROIT";
    /**
     * Mode paiement direct par défaut
     */
    public static final String MODE_PAIEMENT_DIRECT = "PMTDIRECT";
    /**
     * Nombre d'heure dans un mois. Utilisé pour la génération de prestation unitaire à l'heure
     */
    public static final String NBR_HEURES_PAR_MOIS = "NBHEURMOIS";
    /**
     * Nombre de jours dans un mois. Utilisé pour la génération de prestation unitaire au jour
     */
    public static final String NBR_JOURS_PAR_MOIS = "NBJOURMOIS";
    /**
     * Nom de la caisse
     */
    public static final String NOM_CAISSE = "NOMCAISSE";
    /**
     * Numéro OFAS de la caisse
     */
    public static final String NUMERO_CAISSE = "caisse.no";

    /**
     * Numéro de succursale de la caisse
     */
    public static final String NUMERO_SUCCURSALE_CAISSE = "caisse.no.succursale";
    /**
     * Précision à utilisé pour le calcul d'un dossier à l'heure
     */
    public static final String PRECISION_UNITE_HEURE = "PRECISION_UNITE_HEURE";
    /**
     * Précision à utilisé pour le calcul d'un dossier au jour
     */
    public static final String PRECISION_UNITE_JOUR = "PRECISION_UNITE_JOUR";
    /**
     * Précision à utilisé pour le calcul d'un dossier au mois
     */
    public static final String PRECISION_UNITE_MOIS = "PRECISION_UNITE_MOIS";
    /**
     * protocole liste affilié en CSV pour la compensation
     */
    public static final String PROTOCOLE_CSV_COMPENSATION_LISTE_AFFILIE = "protocoleCSVCompensationListeAffilie";
    /**
     * Paramètre indiquant s'il faut générer un protocole CSV lors de la simulation des paiement direct pour les
     * prestation dépassant un montant sup à
     */
    public static final String PROTOCOLE_CSV_SIMU_PAIEMENT_DIRECT = "protocoleCSVSimuPaiementDirect";
    /**
     * paramètre si générer protocole csv listeAllocataire pour généer la simulation des paiements direct
     */
    public static final String PROTOCOLE_CSV_SIMU_PAIEMENT_DIRECT_DETAIL_ALLOC = "protocoleCSVSimuPaiementDirectListeAlloc";
    /**
     * Mode d'édition des annonces RAFAM
     */
    public static final String RAFAM_ADMIN_EDITION = "RAFAM_ADMIN_EDITION";
    /**
     * Nom du paramètre indiquant si le NSS doit être reformatté sur le recap CSV
     */
    public static final String RECAP_FORMAT_NSS = "al.recapFormatNss";
    /**
     * paramètre pour affichages de toutes les annonces rafam d'an droit ou seulement
     */
    public static final String RAFAM_AFFICHAGE_ANNONCE = "AF_ANNONCES_RAFAM_LIST";
    /**
     * Mode d'envoi des annonces RAFAM
     */
    public static final String RAFAM_ENVOI_AUTO = "RAFAM_ENVOI_AUTO";
    /**
     * Taux de change euros pour le calcul ADI
     */
    public static final String TAUX_EURO = "TAUX_EURO";
    /**
     * Nom du paramètre pour Méthode de récupération du taux selon la date
     */
    public static final String TAUX_METHOD_DATE = "TAUX_METHOD_DATE";
    /**
     * Valeur pour le paramètre TAUX_METHOD_DATE
     */
    public static final String TAUX_METHOD_JOUR = "JOUR";

    /**
     * Valeur pour le paramètre TAUX_METHOD_DATE
     */
    public static final String TAUX_METHOD_PERIODEAF = "PERIODAF";

    /**
     * Nom du paramètre pour le template de configuration des processus AF
     */
    public static final String TEMPLATE_PROCESSUS = "TMPPROC";

    /**
     * Nom du paramètre indiquant si Tucana est Actif
     */
    public static final String TUCANA_IS_ENABLED = "TUCANA_IS_ENABLED";

    /** Paramètre définissant le type de copie pour les non actifs */
    public static final String TYPE_COPIE_DECISION_NONACTIF = "typeCopieDecisionNonActif";

    public static final String WARN_RETRO_BEFORE = "WARN_RETRO_BEFORE";

    /**
     * Age et date du début de droit de formation
     */
    public static final String DEBUT_DROIT_FORMATION = "DEBUT_DROIT_FORMATION";
}
