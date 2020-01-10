package ch.globaz.al.business.constantes;

/**
 * Constantes liées aux déclarations de versement
 * 
 * @author PTA
 * 
 */
public interface ALConstDeclarationVersement {

    /**
     * type de document détaillé
     */
    String DECLA_TYPE_DOC_DET = "7";

    /**
     * type de document global
     */
    String DECLA_TYPE_DOC_GLOB = "8";

    /**
     * traitement des déclarations de versement pour dossiers directs destinées à la déclaration d'impôts
     */

    String DECLA_VERSE_ATTEST_IMPOT = "1";

    /**
     * traitement des déclarations de versement pour frontalier
     */

    String DECLA_VERSE_FRONTALIER = "3";
    /**
     * traitement des déclarations de versement pour dossiers directs destinées à l'imposition à la source
     */
    String DECLA_VERSE_IMP_SOURCE = "2";
    /**
     * traitement des déclarations de versement pour les non-actifs
     */
    String DECLA_VERSE_NON_ACTIF = "4";

    /**
     * traitement de déclarations de versement sur demande
     */

    String DECLA_VERSE_PONCTUELLE = "9";
    /**
     * traitement des déclarations de versement sur demande ponctuelle pour un affilié
     */
    String DECLA_VERSE_PONCTUELLE_AFF = "5";
    /**
     * traitement des déclarations de versement sur demande ponctuelle pour un dossier
     */
    String DECLA_VERSE_PONCTUELLE_DOS = "6";

    /** Constante permettant de récupérer le total */
    String TOTAL = "total";
    /** Constante permettant de récupérer le total annuel */
     String TOTAL_ANNEE = "totalAnnee";
    /** Constante permettant de récupérer le total rétroactif */
    String TOTAL_RETROACTIF = "totalRetroActif";
    /**
     * Constante permettant de récupérer le total d'impôt à la source.
     */
    String TOTAL_IS ="totalImpotSource" ;
}
