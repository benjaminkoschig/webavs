package globaz.apg.properties;

/**
 * Type �num�r� utilis� pour d�finir les param�tres FWPARP li�s au module APG et MATERNITE
 * 
 * @author lga
 *
 */
public enum APParameter {

    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_0_ENFANT("MAXTXJO_0"),
    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_1_ENFANT("MAXTXJO_1"),
    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_2_ENFANT("MAXTXJO_2"),
    TAUX_JOURNALIER_MAX_DROIT_ACQUIS_PLUS_DE_2_ENFANT("MAXTXJO_3");

    private String parameterName;

    private APParameter(String parameterName) {
        this.parameterName = parameterName;
    }

    /**
     * Retourne le nom du param�tre
     * 
     * @return le nom du param�tre
     */
    public String getParameterName() {
        return parameterName;
    }

}
