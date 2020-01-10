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
    String DECLA_TYPE_DOC_DET = "7";

    /**
     * type de document global
     */
    String DECLA_TYPE_DOC_GLOB = "8";

    /**
     * traitement des d�clarations de versement pour dossiers directs destin�es � la d�claration d'imp�ts
     */

    String DECLA_VERSE_ATTEST_IMPOT = "1";

    /**
     * traitement des d�clarations de versement pour frontalier
     */

    String DECLA_VERSE_FRONTALIER = "3";
    /**
     * traitement des d�clarations de versement pour dossiers directs destin�es � l'imposition � la source
     */
    String DECLA_VERSE_IMP_SOURCE = "2";
    /**
     * traitement des d�clarations de versement pour les non-actifs
     */
    String DECLA_VERSE_NON_ACTIF = "4";

    /**
     * traitement de d�clarations de versement sur demande
     */

    String DECLA_VERSE_PONCTUELLE = "9";
    /**
     * traitement des d�clarations de versement sur demande ponctuelle pour un affili�
     */
    String DECLA_VERSE_PONCTUELLE_AFF = "5";
    /**
     * traitement des d�clarations de versement sur demande ponctuelle pour un dossier
     */
    String DECLA_VERSE_PONCTUELLE_DOS = "6";

    /** Constante permettant de r�cup�rer le total */
    String TOTAL = "total";
    /** Constante permettant de r�cup�rer le total annuel */
     String TOTAL_ANNEE = "totalAnnee";
    /** Constante permettant de r�cup�rer le total r�troactif */
    String TOTAL_RETROACTIF = "totalRetroActif";
    /**
     * Constante permettant de r�cup�rer le total d'imp�t � la source.
     */
    String TOTAL_IS ="totalImpotSource" ;
}
