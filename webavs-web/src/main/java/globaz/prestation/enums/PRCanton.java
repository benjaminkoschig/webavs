package globaz.prestation.enums;

public enum PRCanton {
    AG(19, "ARGOVIE"),
    AI(16, "APPENZELL_RODES_INTERIEURES"),
    AR(15, "APPENZELL_RODES_EXTERIEURES"),
    BE(2, "BERNE"),
    BL(13, "BALE_CAMPAGNE"),
    BS(12, "BALE_VILLE"),
    FR(10, "FRIBOURG"),
    GE(25, "GENEVE"),
    GL(8, "GLARIS"),
    GR(18, "GRISONS"),
    JU(50, "JURA"),
    LU(3, "LUCERNE"),
    NE(24, "NEUCHATEL"),
    NW(7, "NIDWALD"),
    OW(6, "OBWALD"),
    SG(17, "SAINT_GALL"),
    SH(14, "SCHAFFHOUSE"),
    SO(11, "SOLEURE"),
    SZ(5, "SCHWYZ"),
    TG(20, "THURGOVIE"),
    TI(21, "TESSIN"),
    UR(4, "URI"),
    VD(22, "VAUD"),
    VS(23, "VALAIS"),
    ZG(9, "ZOUG"),
    ZH(1, "ZURICH");

    private int codeCanton;
    private String labelKey;

    private PRCanton(int codeCanton, String labelKey) {
        this.codeCanton = codeCanton;
        this.labelKey = labelKey;
    }

    public String getLabelKey() {
        return labelKey;
    }

    public int getCodeCanton() {
        return codeCanton;
    }

    public String getCodeAsString() {
        return String.valueOf(codeCanton);
    }

    public static PRCanton getCanton(int codeCanton) {
        try {
            return PRCanton.getCanton(String.valueOf(codeCanton));
        } catch (Exception exception) {
            return null;
        }
    }

    public static PRCanton getCanton(String codeCanton) {
        for (PRCanton canton : PRCanton.values()) {
            if (canton.getCodeAsString().equals(codeCanton)) {
                return canton;
            }
        }
        return null;
    }
}
