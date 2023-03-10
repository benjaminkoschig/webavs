package globaz.apg.enums;

/**
 * Cet enum d?crit les cause d'erreurs de validation suite ? l'?dition ou ? la cr?ation d'un droit LAPG
 * 
 * @author lga
 */
public enum APValidationDroitError {
    AUCUNE_PERIODE_DEFINIE("VALIDATION_STEP1_ERROR_AUCUNE_PERIODE_DEFINIE"),
    CHEVAUCHEMENT_PERIODES_DETECTE("VALIDATION_STEP1_ERROR_CHEVAUCHEMENT_PERIODES_DETECTE"),
    FORMAT_DATE_INVALID("VALIDATION_STEP1_ERROR_FORMAT_DATE_INVALID"),
    GENRE_SERVICE_INVALID("VALIDATION_STEP1_ERROR_GENRE_SERVICE"),
    JOURS_SOLDE_INVALID("VALIDATION_STEP1_ERROR_JOURS_SOLDES_INVALID"),
    JOURS_SOLDE_INSUFFISANT("VALIDATION_STEP1_ERROR_JOURS_SOLDES_INSUFFISANT"),
    NPA_VIDE("VALIDATION_STEP1_ERROR_NPA_INVALID"),
    NSS_INVALID("VALIDATION_STEP1_ERROR_NSS_INVALID"),
    NUMERO_CONTROLE_INVALID("VALIDATION_STEP1_ERROR_NUMERO_CONTROLE_INVALID"),
    NUMERO_REFERENCE_NON_RENSEIGNE("VALIDATION_STEP1_ERROR_COMPTE_NON_RENSEIGNE"),
    NUMERO_REFERENCE_TROP_COURT("VALIDATION_STEP1_ERROR_COMPTE_TROP_COURT"),
    PERIODE_INCOHERENTE("VALIDATION_STEP1_ERROR_PERIODE_INCOHERENTE"),
    PERIODE_DANS_FUTUR("VALIDATION_STEP1_ERROR_PERIODE_DANS_FUTUR"),
    REMARQUE_TROP_LONGUE("VALIDATION_STEP1_ERROR_TROP_RETOUR_LIGNE_REMARQUE"), // cr?er le texte
    DATE_DEPOT_VIDE("VALIDATION_STEP1_DATE_DEPOT_VIDE"),
    DATE_RECEPTION_VIDE("VALIDATION_STEP1_DATE_RECEPTION_VIDE"),
    DROITS_ACQUIS_PAS_ARRONDI("VALIDATION_STEP1_DROITS_ACQUIS_PAS_ARRONDI"),
    PAYS_INVALID("VALIDATION_STEP1_PAYS_INVALID"),
    PAYS_DOIT_ETRE_SUISSE("VALIDATION_STEP1_PAYS_DOIT_ETRE_SUISSE"),
    CANTON_INTROUVABLE("VALIDATION_STEP1_CANTON_INTROUVABLE"),
    NO_COMPTE_CCIIF_NB_POS("VALIDATION_STEP1_NO_COMPTE_CCIIF_NB_POS"),
    NO_COMPTE_DOIT_FINIR_PAR_1_2_POUR_GENRE_20_21("VALIDATION_STEP1_NO_COMPTE_DOIT_FINIR_PAR_1_2_POUR_GENRE_20_21"),
    NO_COMPTE_CCIIF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21(
            "VALIDATION_STEP1_NO_COMPTE_CCIIF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21"),
    NO_COMPTE_CCIIF_II_INCORECTE_A("VALIDATION_STEP1_NO_COMPTE_CCIIF_II_INCORECTE_A"),
    NO_COMPTE_CCIIF_II_INCORECTE_B("VALIDATION_STEP1_NO_COMPTE_CCIIF_II_INCORECTE_B"),
    NO_COMPTE_CCIIF_II_INCORECTE_C("VALIDATION_STEP1_NO_COMPTE_CCIIF_II_INCORECTE_C"),
    NO_COMPTE_CCIIF_II_INCORECTE_D("VALIDATION_STEP1_NO_COMPTE_CCIIF_II_INCORECTE_D"),
    NO_COMPTE_CCIIF_II_INCORECTE_E("VALIDATION_STEP1_NO_COMPTE_CCIIF_II_INCORECTE_E"),
    NO_COMPTE_CCNNNNF_NB_POS("VALIDATION_STEP1_NO_COMPTE_CCNNNNF_NB_POS"),
    NO_COMPTE_CCNNNNF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21(
            "VALIDATION_STEP1_NO_COMPTE_CCNNNNF_DOIT_COMMENCER_PAR_POUR_GENRE_20_21"),
    PERIODE_CHEVAUCHANTE_AVEC_AUTRE_DROIT(
            "VALIDATION_PERIODE_CHEVAUCHANTE_AVEC_AUTRE_DROIT");

    private String errorLabel;

    private APValidationDroitError(String errorLabel) {
        this.errorLabel = errorLabel;
    }

    public final String getErrorLabel() {
        return errorLabel;
    }

}
