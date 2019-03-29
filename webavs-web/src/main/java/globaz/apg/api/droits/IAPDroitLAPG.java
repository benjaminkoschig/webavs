package globaz.apg.api.droits;

/**
 * Descpription
 *
 * @author scr Date de création 23 mai 05
 */
public interface IAPDroitLAPG {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    // Code système des états des droits APG

    /** DOCUMENT ME! */
    public static final String CS_ALLOCATION_DE_MATERNITE = "52001012";

    /** DOCUMENT ME! */
    public static final String CS_ARMEE_SERVICE_NORMAL = "52001001";

    public final static String CS_CINQ_MOIS_ACTIVITE = "52022003";

    /** DOCUMENT ME! */
    public static final String CS_COURS_MONITEURS_JEUNES_TIREURS = "52001011";

    /** le droit est en attente */
    public static final String CS_ETAT_DROIT_ATTENTE = "52003001";

    /** le droit est définitif */
    public static final String CS_ETAT_DROIT_DEFINITIF = "52003002";

    // Codes système des genre de service

    /** le droit est partiel */
    public static final String CS_ETAT_DROIT_PARTIEL = "52003003";
    public static final String CS_ETAT_DROIT_TRANSFERE = "52003004";
    public static final String CS_ETAT_DROIT_REFUSE = "52003005";

    /** DOCUMENT ME! */
    public static final String CS_FORMATION_DE_BASE = "52001007";

    /** DOCUMENT ME! */
    public static final String CS_FORMATION_DE_CADRE_JEUNESSE_SPORTS = "52001008";

    /** Nom du groupe des codes système des états des droit APG */
    public static final String CS_GROUPE_ETAT_DROIT_APG = "APETADROIT";

    /** Nom du groupe des codes système des genres de service APG */
    public static final String CS_GROUPE_GENRE_SERVICE_APG = "APGENSERVI";

    // Motif_refus_dossier
    /** Nom du groupe des codes systèmes des motifs de refus d'un dossier */
    public static final String CS_GROUPE_MOTIFS_REFUS = "APMOREDO";

    // Motif_transfer_dossier
    /** Nom du groupe des codes système des motifs de transfer de dossiers */
    public static final String CS_GROUPE_MOTIFS_TRANSFER = "APMOTRDO";

    // Informations_complementaires
    /** Nom du groupe des codes système des types d'informations complémentaires */
    public static final String CS_GROUPE_TYPE_INFO_COMPL = "APINFCMP";

    public final static String CS_NEUF_MOIS_ASSUJETTISSEMENT = "52022002";

    public final static String CS_PERCEPTION_COTISATIONS_AVS = "52017003";

    public static final String CS_PROTECTION_CIVILE_CADRE_SPECIALISTE = "52001013";

    public static final String CS_PROTECTION_CIVILE_COMMANDANT = "52001014";

    /** DOCUMENT ME! */
    public static final String CS_PROTECTION_CIVILE_SERVICE_NORMAL = "52001006";

    /** DOCUMENT ME! */
    public static final String CS_RECRUTEMENT = "52001004";

    public final static String CS_REFUS_DOSSIER = "52016002";

    /** le droit est refusé */
    public final static String CS_REFUSE = "52003005";

    public final static String CS_SANS_ACTIVITE_ACCOUCHEMENT = "52022004";

    public final static String CS_SANS_ACTIVITE_LUCRATIVE = "52022001";

    /** DOCUMENT ME! */
    public static final String CS_SERVICE_AVANCEMENT = "52001003";

    /** DOCUMENT ME! */
    public static final String CS_SERVICE_CIVIL_AVEC_TAUX_RECRUES = "52001010";

    /** DOCUMENT ME! */
    public static final String CS_SERVICE_INTERRUPTION_AVANT_ECOLE_SOUS_OFF = "52001015";

    /** DOCUMENT ME! */
    public static final String CS_SERVICE_INTERRUPTION_PENDANT_SERVICE_AVANCEMENT = "52001016";

    /** DOCUMENT ME! */
    public static final String CS_SERVICE_CIVIL_SERVICE_NORMAL = "52001009";

    /** DOCUMENT ME! */
    public static final String CS_SERVICE_EN_QUALITE_DE_RECRUE = "52001002";

    /** DOCUMENT ME! */
    public static final String CS_SOF_EN_SERVICE_LONG = "52001005";

    /** le droit est transféré */
    public final static String CS_TANSFERE = "52003004";

    public final static String CS_TRANSFER_DOSSIER = "52016001";

    public final static String CS_VERSEMENT_RENTE_AI = "52017002";

    public final static String CS_VERSEMENT_RENTE_AVS = "52017001";
    /**
     * Constante pour FERCIAB
     */
    public final static String CS_DEMENAGEMENT_CIAB = "52001050";
    public final static String CS_NAISSANCE_CIAB = "52001051";
    public final static String CS_MARIAGE_LPART_CIAB = "52001052";
    public final static String CS_DECES_CIAB = "52001053";
    public final static String CS_JOURNEES_DIVERSES_CIAB = "52001054";
    public final static String CS_CONGE_JEUNESSE_CIAB = "52001055";
    public final static String CS_SERVICE_ETRANGER_CIAB = "52001056";
    public final static String CS_DECES_DEMI_JOUR_CIAB = "52001057";

}
