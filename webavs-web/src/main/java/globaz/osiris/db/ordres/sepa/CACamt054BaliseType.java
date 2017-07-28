package globaz.osiris.db.ordres.sepa;

public enum CACamt054BaliseType {
    DOM_EMPTY(""),
    DOM_PMNT("PMNT"),

    FA_EMPTY(""),
    FA_RCDT("RCDT"),
    FA_IDDT("IDDT"),
    FA_RDDT("RDDT"),

    SUBFA_EMPTY(""),
    SUBFA_VCOM("VCOM"),
    SUBFA_CAJT("CAJT"),
    SUBFA_DAJT("DAJT");

    final String code;

    private CACamt054BaliseType(final String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
