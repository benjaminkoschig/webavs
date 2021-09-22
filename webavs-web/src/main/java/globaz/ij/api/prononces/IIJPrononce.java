package globaz.ij.api.prononces;

/**
 * @author vre
 */
public interface IIJPrononce {

    // ~ Static fields/initializers
    // -------------------------------------------------------------------------------------

    /** lors d'un clonage, un fils du prononcé cloné sera créé. */
    public static final int CLONE_FILS = 11110002;

    /** lors d'un clonage, un tout nouveau prononcé sera créé. */
    public static final int CLONE_NORMAL = 11110001;
    public static final String CS_AGRICULTEUR_INDEPENDANT_LFA = "52403004";
    public final static String CS_ALLOC_ASSIST = "52402004";
    public final static String CS_ALLOC_INIT_TRAVAIL = "52402003";
    public final static String CS_FPI = "52402005";

    public static final String CS_ANNULE = "52404004";

    public static final String CS_ATTENTE = "52404001";
    public static final String CS_ATTENTE_D_EMPLOIE = "52410007";
    public static final String CS_ATTENTE_READAPTATION = "52410002";
    public static final String CS_COMMUNIQUE = "52404003";

    public final static String CS_DECIDE = "52404006";

    public static final String CS_DELAI_ATTENTE = "52401004";
    public static final String CS_ECOLE_SPECIALE = "52401003";
    public final static String CS_EXTERNE = "52427003";
    public static final String CS_FORMATION_PROFESSIONNELLE_INITIALE = "52410005";

    public static final String CS_GRANDE_IJ = "52402001";

    //
    public static final String CS_GROUPE_ETAT_PRONONCE = "IJETATPRO";
    //
    public static final String CS_GROUPE_GENRE_READAPTATION = "IJGENRREAD";
    //
    public static final String CS_GROUPE_MOTIF_IJ = "IJMOTIF";
    //
    public static final String CS_GROUPE_STATUT_PROFESSIONNEL = "IJSTATPRO";
    // Type_d_hebergement
    public static final String CS_GROUPE_TYPE_HEBERGEMENT = "IJTYPHEBER";
    //
    public static final String CS_GROUPE_TYPE_IJ = "IJTYPE";

    public static final String CS_INDEPENDANT = "52403002";

    public final static String CS_INTERNE = "52427002";
    public final static String CS_INTERNE_EXTERNE = "52427001";
    public final static String CS_MESURE_REINSERTION = "52410009";
    public static final String CS_MESURES_MEDICALES = "52401001";
    public static final String CS_MESURES_PROFESSIONNELLES = "52401002";
    public static final String CS_MESURES_SCOLAIRES_SPECIALES = "52410004";
    public static final String CS_MISE_AU_COURANT = "52410008";
    public static final String CS_NON_ACTIF = "52403003";
    public static final String CS_OBSERVATION_INSTRUCTION = "52410001";

    public final static String CS_PERCEPTION_COTISATIONS_AVS = "52424003";

    public static final String CS_PETITE_IJ = "52402002";

    public static final String CS_READAPTATION_MEDICALE = "52410003";

    public static final String CS_RECLASSEMENT = "52410006";
    public static final String CS_SALARIE = "52403001";
    public final static String CS_TANSFERE = "52404005";

    /** Informations_complementaires */
    public final static String CS_TRANSFER_DOSSIER = "52423001";

    public static final String CS_VALIDE = "52404002";
    public final static String CS_VERSEMENT_RENTE_AI = "52424002";
    /** Motif_transfer_dossier */
    public final static String CS_VERSEMENT_RENTE_AVS = "52424001";
}
