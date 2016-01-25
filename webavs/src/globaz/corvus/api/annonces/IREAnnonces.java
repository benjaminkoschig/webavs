/*
 * Créé le 6 août 07
 */
package globaz.corvus.api.annonces;

/**
 * @author HPE
 */
public interface IREAnnonces {

    public static final String ANNONCE_10EME_REVISION = "annonce10EmeRevision";
    public static final String ANNONCE_9EME_REVISION = "annonce9EmeRevision";
    public static final String CODE_MUTATION_AUTRE_EVENEMENT = "08";

    public static final String CODE_MUTATION_CHGMT_ETAT_CIVIL = "02";
    public static final String CODE_MUTATION_CONVERSION_PRST_AI_EN_AVS = "05";
    public static final String CODE_MUTATION_CONVERSION_RENTE_ENTIERE = "04";
    public static final String CODE_MUTATION_DECES = "01";

    public static final String CODE_MUTATION_EVENEMENT_PROCHE_FAM = "07";
    public static final String CODE_MUTATION_LIMIT_AGE = "03";
    public static final String CODE_MUTATION_PRESTATION_TRANSITOIRE = "09";
    public static final String CODE_MUTATION_SUPP_DEGRE = "06";
    public final static String CS_CODE_EN_COURS = "52839003";
    public static final String CS_CODE_MUTATION_EVENEMENT_PROCHE_FAM = "52823007";
    public final static String CS_CODE_NON_TRAITE = "52839001";
    public final static String CS_CODE_TRAITE = "52839002";
    public final static String CS_ETAT_ENVOYE = "52838002";
    public final static String CS_ETAT_OUVERT = "52838001";

    public static final String CS_GROUPE_CODE_MUTATION = "RECODMUT";

    // RECTRANN : Code traitement de l'annonce
    public static final String CS_GROUPE_CODE_TRAITEMENT_ANNONCE = "RECTRANN";
    // REETAANN : Etat de l'annonce
    public static final String CS_GROUPE_ETAT_ANNONCE = "REETAANN";

}
