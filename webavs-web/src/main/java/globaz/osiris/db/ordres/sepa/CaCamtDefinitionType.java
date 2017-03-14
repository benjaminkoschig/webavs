package globaz.osiris.db.ordres.sepa;

public enum CaCamtDefinitionType {
    DOMAINE_EMPTY(""),
    DOMAINE_PAIEMENT("PMNT"),
    FAMILY_EMPTY(""),
    FAMILY_CREDIT("RCDT"),
    SUBFAMILY_EMPTY(""),
    SUBFAMILY_VCOM("VCOM"),
    SUBFAMILY_CAJT("CAJT");

    final String code;

    private CaCamtDefinitionType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
