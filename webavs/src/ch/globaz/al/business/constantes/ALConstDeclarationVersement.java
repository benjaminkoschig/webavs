package ch.globaz.al.business.constantes;

/**
 * Constantes li�es aux d�clarations de versement
 * 
 * @author PTA
 * 
 */
public interface ALConstDeclarationVersement {

    /**
     * type de document d�taill�
     */
    public static final String DECLA_TYPE_DOC_DET = "7";

    /**
     * type de document global
     */
    public static final String DECLA_TYPE_DOC_GLOB = "8";

    /**
     * traitement des d�clarations de versement pour dossiers directs destin�es � la d�claration d'imp�ts
     */

    public static final String DECLA_VERSE_ATTEST_IMPOT = "1";

    /**
     * traitement des d�clarations de versement pour frontalier
     */

    public static final String DECLA_VERSE_FRONTALIER = "3";
    /**
     * traitement des d�clarations de versement pour dossiers directs destin�es � l'imposition � la source
     */
    public static final String DECLA_VERSE_IMP_SOURCE = "2";
    /**
     * traitement des d�clarations de versement pour les non-actifs
     */
    public static final String DECLA_VERSE_NON_ACTIF = "4";

    /**
     * traitement de d�clarations de versement sur demande
     */

    public static final String DECLA_VERSE_PONCTUELLE = "9";
    /**
     * traitement des d�clarations de versement sur demande ponctuelle pour un affili�
     */
    public static final String DECLA_VERSE_PONCTUELLE_AFF = "5";
    /**
     * traitement des d�clarations de versement sur demande ponctuelle pour un dossier
     */
    public static final String DECLA_VERSE_PONCTUELLE_DOS = "6";

    /** Constante permettant de r�cup�rer le total */
    public static final String TOTAL = "total";
    /** Constante permettant de r�cup�rer le total annuel */
    public static final String TOTAL_ANNEE = "totalAnnee";
    /** Constante permettant de r�cup�rer le total r�troactif */
    public static final String TOTAL_RETROACTIF = "totalRetroActif";
}
