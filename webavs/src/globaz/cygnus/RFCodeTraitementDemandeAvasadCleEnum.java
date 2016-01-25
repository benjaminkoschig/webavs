package globaz.cygnus;

public enum RFCodeTraitementDemandeAvasadCleEnum {

    DONNEES_INCOHERENTES("8"),
    NSS_PAS_TROUVE("9"),
    PAIEMENT_PARTIEL("1"),
    PAIEMENT_TOTAL("6"),
    QD_DANS_HOME("3"),
    REFUS_ASV("2"),
    REFUS_PC("5");

    private String code = "";

    private RFCodeTraitementDemandeAvasadCleEnum(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
