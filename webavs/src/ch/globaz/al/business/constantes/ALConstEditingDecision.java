package ch.globaz.al.business.constantes;

/**
 * Constantes liées à l'édition de décisions par editing
 * 
 * @author pta
 * 
 */

public interface ALConstEditingDecision {

    /**
     * Constantes pour les activités des allocataires
     */
    public static final String ACTIVITE_AGRICULTEUR = "AGRICULTEUR";
    public static final String ACTIVITE_COLLABO_AGRI = "COLLABORATEUR_AGRICOLE";
    public static final String ACTIVITE_EXP_ALPAGE = "EXPLOITANT_ALPAGE";
    public static final String ACTIVITE_INDEP = "INDEPENDANT";
    public static final String ACTIVITE_NON_ACTIF = "NON_ACTIF";
    public static final String ACTIVITE_PECHEUR = "PECHEUR";
    public static final String ACTIVITE_SALARIE = "SALARIE";
    public static final String ACTIVITE_TRAV_AGRICOLE = "TRAVAILLEUR_AGRICOLE";
    public static final String ACTIVITE_TRAV_SANS_EMPL = "TRAVAILLEUR_SANS_EMPLOYEUR";
    public static final String ACTIVITE_VIGNERON = "VIGNERON";

    /**
     * constantes pour les genres d'employeur cs Affiliation TODO voir pour non avs
     */
    public static final String CS_GENRE_AFFILIATION_PROVISOIRE = "804007";
    public static final String CS_GENRE_ANOBAG = "804008";
    public static final String CS_GENRE_EMPL_D_F = "804012";
    public static final String CS_GENRE_EMPLOYEUR = "804002";
    public static final String CS_GENRE_INDEPENDANT = "804001";
    public static final String CS_GENRE_INDEPENDANT_ET_EMPLOYEUR = "804005";
    public static final String CS_GENRE_NA_A_L_ETRANGER_SELON_ART1_AL4 = "805006";
    public static final String CS_GENRE_NON_ACTIF = "804004";
    // public static final String CS_GENRE_NON_AVS= "";
    public static final String CS_GENRE_NON_SOUMIS = "804008";
    public static final String CS_GENRE_TSE = "804010";
    public static final String CS_GENRE_TSE_VOLONTAIRE = "804011";
    /**
     * Constantes pour l'état du dossier
     */
    public static final String ETAT_DOSSIER_ACTIF = "ACTIF";
    public static final String ETAT_DOSSIER_RAD = "RADIE";

    /**
     * constantes pour les genres d'employeur affiliation TODO voir pour non avs
     */
    public static final String GENRE_AFFILIATION_PROVISOIRE = "AFFILIATION_PROVISOIRE";
    public static final String GENRE_ANOBAG = "ANOBAG";
    public static final String GENRE_EMPL_D_F = "EMPL_D_F";
    public static final String GENRE_EMPLOYEUR = "EMPLOYEUR";
    public static final String GENRE_INDEPENDANT = "INDEPENDANT";
    public static final String GENRE_INDEPENDANT_ET_EMPLOYEUR = "INDEPENDANT_ET_EMPLOYEUR";
    public static final String GENRE_NA_A_L_ETRANGER_SELON_ART1_AL4 = "NA_A_L_ETRANGER_SELON_ART1_AL4";
    public static final String GENRE_NON_ACTIF = "NON_ACTIF";
    // public static final String GENRE_NON_AVS = "NON_AVS";
    public static final String GENRE_NON_SOUMIS = "NON_SOUMIS";
    public static final String GENRE_TSE = "TSE";
    public static final String GENRE_TSE_VOLONTAIRE = "TSE_VOLONTAIRE";

    /**
     * constantes pour la loi fédéral agricole
     */
    public static final String LOI_FED_AGRICOLE = "LFA";
    /**
     * cosntantes pour la monnaie CH
     */
    public static final String MONNAIE_CH = "CH";
    /**
     * Constantes pour les motifs de fin d'un droit
     */
    public static final String MOTIF_FIN_DROIT_CHGMT = "CTAR";
    public static final String MOTIF_FIN_DROIT_ECHU = "ECH";
    public static final String MOTIF_FIN_DROIT_RADIE = "RAD";
    /**
     * Constante pour les codes systèmes cs tiers pour
     */
    public static final String PERS_CS_GENRE_MADAME = "502002";
    public static final String PERS_CS_GENRE_MADEMOISELLE = "19150007";

    public static final String PERS_CS_GENRE_MONSIEUR = "502001";
    /**
     * Constantes pour le sexe des personnes reprenant les cs tiers
     */
    public static final String PERS_CS_SEXE_FEMME = "516002";

    public static final String PERS_CS_SEXE_HOMME = "516001";
    /**
     * constantes pour les genres personne (madame, monsieur, mademoiselle)
     */
    public static final String PERS_GENRE_MADAME = "1";
    public static final String PERS_GENRE_MADEMOISELLE = "3";

    public static final String PERS_GENRE_MONSIEUR = "2";
    /**
     * Constante pour les valeurs editing pour le sexe
     */
    public static final String PERS_SEXE_FEMME = "2";
    public static final String PERS_SEXE_HOMME = "1";
    /**
     * Constantes pour les statuts de dossier
     */
    public static final String STATUT_CANTONAL_PRIORITAIRE = "CANTONAL_PRIORITAIRE";
    public static final String STATUT_CANTONAL_SUPPLETIF = "CANTONAL_SUPPLETIF";
    public static final String STATUT_INTERNATIONAL_PRIORITAIRE = "INTERNATIONAL_PRIORITAIRE";
    public static final String STATUT_INTERNATIONAL_SUPPL = "INTERNATIONAL_SUPPLETIF";
    public static final String STATUT_NORMAL = "NORMAL";
    public static final String STATUT_PRIORITAIRE = "PRIORITAIRE";
    /**
     * constantes pour les type de droit (enfant, formation, ménage)
     */
    public static final String TYPE_DROIT_ENF = "ENF";
    public static final String TYPE_DROIT_FORM = "FORM";
    public static final String TYPE_DROIT_MEN = "MEN";
    /**
     * Constantes pour les type de bonifications
     */
    public static final String TYPE_PAIEMENT_DIR_ALLOC = "DIRECT_ALLOC";

    public static final String TYPE_PAIEMENT_DIR_TIERS = "DIRECT_TIERS";
    public static final String TYPE_PAIEMENT_IND = "INDIRECT";
    /**
     * constantes pour les divers PRESTATIONS (autres type de droit)
     */
    public static final String TYPE_PRESTA_ACCUEIL = "ACCUEIL";
    public static final String TYPE_PRESTA_NAISS = "NAISSANCE";

}
