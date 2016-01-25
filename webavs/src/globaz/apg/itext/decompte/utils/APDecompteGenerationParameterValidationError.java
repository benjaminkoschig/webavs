package globaz.apg.itext.decompte.utils;

/**
 * Cet enum liste toutes les erreurs de validations possible du process de génération des décomptes
 * 
 * @author lga
 */
public enum APDecompteGenerationParameterValidationError {
    ID_LOT_VIDE("GENERATION_DECOMPTE_ERREUR_ID_LOT_VIDE"),
    ID_LOT_INVALIDE("GENERATION_DECOMPTE_ERREUR_ID_LOT_INVALIDE"),
    DATE_COMPTABLE_VIDE("GENERATION_DECOMPTE_ERREUR_DATE_COMPTABLE_VIDE"),
    DATE_DOCUMENT_VIDE("GENERATION_DECOMPTE_ERREUR_DATE_DOCUMENT_VIDE"),

    EMAIL_INVALID("GENERATION_DECOMPTE_ERREUR_EMAIL_INVALID"),
    TYPE_DE_PRESTATION_ACM_NON_RENSEIGNE("GENERATION_DECOMPTE_ERREUR_TYPE_DE_PRESTATION_ACM_NON_RENSEIGNE");

    private String labelKey;

    private APDecompteGenerationParameterValidationError(final String labelKey) {
        this.labelKey = labelKey;
    }

    /**
     * Retourne la clé du label associé à cette erreur de validation
     * 
     * @return
     */
    public final String getLabelKey() {
        return labelKey;
    }

}
