package ch.globaz.al.business.constantes;

/**
 * Codes système liés aux allocataires
 * 
 * <ul>
 * <li>Permis de travail</li>
 * <li>Etat civil</li>
 * <li>Sexe</li>
 * <li>Titre</li>
 * 
 * @author jts
 * 
 */
public interface ALCSAllocataire {
    /*
     * etat civil
     */
    /**
     * CS : état civil célibataire
     */
    public static final String ETAT_CIVIL_CELIBATAIRE = "515001";
    /**
     * CS : état civil divorcé
     */
    public static final String ETAT_CIVIL_DIVORCE = "515003";
    /**
     * CS : état civil marié
     */
    public static final String ETAT_CIVIL_MARIE = "515002";
    /**
     * CS : état civil séparé
     */
    public static final String ETAT_CIVIL_SEPARE = "515005";
    /**
     * CS : état civil séparé de fait
     */
    public static final String ETAT_CIVIL_SEPARE_DE_FAIT = "515006";
    /**
     * CS : état civil veuf
     */
    public static final String ETAT_CIVIL_VEUF = "515004";

    /*
     * ========================================================================= groupes de codes système
     * =========================================================================
     */
    /**
     * CS : Groupe "Permis"
     * 
     * @see ALCSAllocataire#PERMIS_B
     * @see ALCSAllocataire#PERMIS_C
     * @see ALCSAllocataire#PERMIS_F
     * @see ALCSAllocataire#PERMIS_G
     * @see ALCSAllocataire#PERMIS_L
     * @see ALCSAllocataire#PERMIS_N
     * @see ALCSAllocataire#PERMIS_S
     */
    public static final String GROUP_ALLOCATAIRE_PERMIS = "60010000";
    /**
     * CS : Groupe "Etat civil"
     * 
     * @see ALCSAllocataire#ETAT_CIVIL_CELIBATAIRE
     * @see ALCSAllocataire#ETAT_CIVIL_DIVORCE
     * @see ALCSAllocataire#ETAT_CIVIL_MARIE
     * @see ALCSAllocataire#ETAT_CIVIL_SEPARE
     * @see ALCSAllocataire#ETAT_CIVIL_SEPARE_DE_FAIT
     * @see ALCSAllocataire#ETAT_CIVIL_VEUF
     */
    public static final String GROUP_ETAT_CIVIL = "10500015";
    /**
     * CS : Groupe "Sexe"
     * 
     * @see ALCSAllocataire#SEXE_F
     * @see ALCSAllocataire#SEXE_M
     */
    public static final String GROUP_SEXE = "10500016";
    /**
     * CS : Groupe "Titre"
     * 
     * @see ALCSAllocataire#TITRE_MADAME
     * @see ALCSAllocataire#TITRE_MADEMOISELLE
     * @see ALCSAllocataire#TITRE_MONSIEUR
     */
    public static final String GROUP_TITRE = "10500002";
    /*
     * =========================================================================
     * =========================================================================
     */

    /**
     * CS : Permis B - Résidence longue durée
     */
    public static final String PERMIS_B = "61010002";
    /**
     * CS : Permis C - A l'année, avec domicile fiscal
     */
    public static final String PERMIS_C = "61010003";
    /**
     * CS : Permis F - Demandeur d'asile
     */
    public static final String PERMIS_F = "61010005";
    /**
     * CS : Permis G - Permis pour frontalier
     */
    public static final String PERMIS_G = "61010004";

    /**
     * CS : Permis L - Résidence courte durée
     */
    public static final String PERMIS_L = "61010001";

    /**
     * CS : Permis N - Requérants d'asile
     */
    public static final String PERMIS_N = "61010006";
    /**
     * CS : ZZ - code reprise
     * 
     * @deprecated de doit pas être utilisé dans d'autre cas qu'une reprise de données
     */
    public static final String PERMIS_REPRISE = "61010008";
    /**
     * CS : Permis S - Personnes qui ont besoin de protection
     */
    public static final String PERMIS_S = "61010007";

    /*
     * sexe
     */
    /**
     * CS : sexe féminin
     */
    public static final String SEXE_F = "516002";
    /**
     * CS : sexe masculin
     */
    public static final String SEXE_M = "516001";

    /*
     * titre
     */
    /**
     * CS : titre Madame
     */
    public static final String TITRE_MADAME = "502002";
    /**
     * CS : titre Mademoiselle
     */
    public static final String TITRE_MADEMOISELLE = "19130005";
    /**
     * CS : titre Monsieur
     */
    public static final String TITRE_MONSIEUR = "502001";

}
