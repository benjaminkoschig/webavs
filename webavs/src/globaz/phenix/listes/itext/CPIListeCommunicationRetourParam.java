package globaz.phenix.listes.itext;

import globaz.framework.printing.itext.fill.FWIImportParametre;

public class CPIListeCommunicationRetourParam extends FWIImportParametre {

    public static final String LABEL_AFFILIE = "L_AFFILIE";
    public static final String LABEL_AUTRE_REV = "L_AUTRE_REV";
    public static final String LABEL_CAPITAL = "L_CAPITAL";
    public static final String LABEL_CAPITAL_ENTREPRISE = "L_CAPITAL_ENTREPRISE";
    public static final String LABEL_CODE_SEXE = "L_CODE_SEXE";
    public static final String LABEL_COMMUNICATION = "L_COMMUNICATION";
    // Labels génériques
    public static final String LABEL_COMPANYNAME = "L_COMPANYNAME";
    public static final String LABEL_CONJOINT = "L_CONJOINT";
    public static final String LABEL_CONTRIBUABLE = "L_CONTRIBUABLE";
    public static final String LABEL_D = "L_D";
    public static final String LABEL_ENCOURS = "L_ENCOURS";
    public static final String LABEL_ETAT_CIVIL = "L_ETAT_CIVIL";
    public static final String LABEL_FORTUNE_PRIVEE = "L_FORTUNE_PRIVEE";
    // labels des codes
    public static final String LABEL_GENRE_TAXATION = "L_GENRE_TAXATION";
    public static final String LABEL_GTAXATION1 = "L_GTAXATION1";
    public static final String LABEL_GTAXATION2 = "L_GTAXATION2";
    public static final String LABEL_GTAXATION3 = "L_GTAXATION3";
    public static final String LABEL_GTAXATION4 = "L_GTAXATION4";
    public static final String LABEL_IFD = "L_IFD";
    public static final String LABEL_INFO_GENERAL = "L_INFO_GENERAL";
    public static final String LABEL_INFO_SUPP = "L_INFO_SUPP";
    public static final String LABEL_INFORMATION = "L_INFORMATION";
    public static final String LABEL_MONTANT_DETERMINANT = "L_MONTANT_DETERMINANT";
    public static final String LABEL_NUM_AVS = "L_NUM_AVS";
    public static final String LABEL_PERIODE = "L_PERIODE";
    public static final String LABEL_PERIODE_ENCOURS = "L_PERIODE_ENCOURS";
    // Labels du tableau
    public static final String LABEL_PROVISOIRE = "L_PROVISOIRE";
    public static final String LABEL_RA = "L_RA";
    public static final String LABEL_RACHAT_LPP = "L_RACHAT_LPP";
    public static final String LABEL_REMARQUE = "L_REMARQUE";
    public static final String LABEL_REV_A = "L_REV_A";
    public static final String LABEL_REV_NA = "L_REV_NA";
    public static final String LABEL_REV_R = "L_REV_R";
    public static final String LABEL_COTISATION = "L_COTISATION";
    public static final String LABEL_RNA = "L_RNA";
    public static final String LABEL_RR = "L_RR";
    public static final String LABEL_SALAIRE = "L_SALAIRE";
    // Label détail fisc
    public static final String LABEL_TITRE_IMPOT = "L_TITRE_IMPOT";
    public static final String PARAM_ADRESSE = "P_ADRESSE";
    public static final String PARAM_REMARQUE = "P_REMARQUE";
    public static final String PARAM_ADRESSE_AFF = "P_ADRESSE_AFF";
    public static final String PARAM_ADRESSE_CON = "P_ADRESSE_CON";
    public static final String PARAM_ANNEE = "P_ANNEE";
    public static final String PARAM_ANNEE_NAISSANCE = "P_ANNEE_NAISSANCE";
    public static final String PARAM_AUTRE_REV = "P_AUTRE_REV";
    public static final String PARAM_AUTRE_REV_CON = "P_AUTRE_REV_CON";
    public static final String PARAM_CANTON = "P_CANTON";
    public static final String PARAM_CAPITAL = "P_CAPITAL";
    public static final String PARAM_CAPITAL_ENCOURS = "P_CAPITAL_ENCOURS";
    public static final String PARAM_CAPITAL_ENTREPRISE = "P_CAPITAL_ENTREPRISE";
    public static final String PARAM_CAPITAL_ENTREPRISE_CON = "P_CAPITAL_ENTREPRISE_CON";
    public static final String PARAM_CODE_SEXE = "P_CODE_SEXE";
    public static final String PARAM_CODE_SEXE_AFF = "P_CODE_SEXE_AFF";
    public static final String PARAM_CODE_SEXE_CON = "P_CODE_SEXE_CON";
    public static final String PARAM_DATE = "P_DATE";
    public static final String PARAM_DATE_N_AFF = "P_DATE_N_AFF";
    public static final String PARAM_DATE_N_CON = "P_DATE_N_CON";
    public static final String PARAM_DEBUT_AFF = "P_DEBUT_AFF";
    // Communication fiscale retour
    public static final String PARAM_DESCRIPTION = "P_DESCRIPTION";
    public static final String PARAM_ETAT_CIVIL = "P_ETAT_CIVIL";
    public static final String PARAM_ETAT_CIVIL_AFF = "P_ETAT_CIVIL_AFF";
    public static final String PARAM_ETAT_CIVIL_CON = "P_ETAT_CIVIL_CON";
    public static final String PARAM_FORTUNE = "P_FORTUNE";
    public static final String PARAM_FORTUNE_ENCOURS = "P_FORTUNE_ENCOURS";
    // Revenus communs
    public static final String PARAM_FORTUNE_PRIVEE = "P_FORTUNE_PRIVEE";
    public static final String PARAM_GENRE_AFFILIE = "P_GENRE_AFFILIE";
    public static final String PARAM_GROUPE_EXTRACTION = "P_GROUPE_EXTRACTION";
    public static final String PARAM_GROUPE_TAXATION = "P_GROUPE_TAXATION";
    public static final String PARAM_LOGS = "P_LOGS";
    public static final String PARAM_MONTANT_DETERMINANT = "P_MONTANT";
    public static final String PARAM_MONTANT_DETERMINANT_ENCOURS = "P_MONTANT_DETERMINANT_ENCOURS";
    // Affilié
    public static final String PARAM_NUM_AFF = "P_NUM_AFF";
    public static final String PARAM_NUM_AFF_CON = "P_NUM_AFF_CON";
    public static final String PARAM_NUM_AVS = "P_NUM_AVS";
    public static final String PARAM_NUM_AVS_AFF = "P_NUM_AVS_AFF";
    public static final String PARAM_NUM_AVS_CON = "P_NUM_AVS_CON";
    public static final String PARAM_NUM_CONTRIBUABLE = "P_NUM_CONTRIBUABLE";
    public static final String PARAM_PERIODE = "P_PERIODE";
    public static final String PARAM_PERIODE_CON = "P_PERIODE_CON";
    public static final String PARAM_PERIODE_ENCOURS = "P_PERIODE_ENCOURS";
    public static final String PARAM_RA = "P_RA";
    public static final String PARAM_RA_ENCOURS = "P_RA_ENCOURS";
    public static final String PARAM_RACHAT_LPP = "P_RACHAT_LPP";
    public static final String PARAM_RACHAT_LPP_CJT = "P_RACHAT_LPP_CJT";
    public static final String PARAM_REV_A = "P_REV_A";
    public static final String PARAM_REV_A_CON = "P_REV_A_CON";
    public static final String PARAM_COTISATION = "P_COTISATION";
    public static final String PARAM_COTISATION_ENCOURS = "P_COTISATION_ENCOURS";
    // Revenus
    public static final String PARAM_REV_NA = "P_REV_NA";
    // Revenu conjoint
    public static final String PARAM_REV_NA_CON = "P_REV_NA_CON";
    public static final String PARAM_REV_R = "P_REV_R";
    // Tableau
    // Provisoire (selon communication fiscale)
    public static final String PARAM_RNA = "P_RNA";
    // Décision en cours
    public static final String PARAM_RNA_ENCOURS = "P_RNA_ENCOURS";

    public static final String PARAM_RR = "P_RR";
    public static final String PARAM_RR_ENCOURS = "P_RR_ENCOURS";
    public static final String PARAM_SALAIRE = "P_SALAIRE";
    public static final String PARAM_SALAIRE_CON = "P_SALAIRE_CON";
    public static final String PARAM_SOURCE_SUB1 = "SOURCE_SUB1";
    public static final String PARAM_SOURCE_SUB2 = "SOURCE_SUB2";
    public static final String PARAM_SOURCE_SUB3 = "SOURCE_SUB3";
}
