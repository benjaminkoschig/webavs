package globaz.apg.enums;

public enum APCentreRegionauxServiceCivil {

    AARAU(17, "48-00017"),
    THOUNE(16, "48-00016"),
    RUTI(15, "48-00015"),
    RIVIERA(14, "48-00014"),
    LUCERNE(13, "48-00013"),
    LANDQUART(12, "48-00012"),
    LAUSANNE(11, "48-00011"),
    FEDERAL(99, "48-00099");

    private int code;
    private String ancienCode;

    private APCentreRegionauxServiceCivil(int code, String ancienCode) {
        this.code = code;
        this.ancienCode = ancienCode;
    }

    public String getAncienCode() {
        return ancienCode;
    }

    public int getCode() {
        return code;
    }

    public String getCodeAsString() {
        return String.valueOf(code);
    }

    public static APCentreRegionauxServiceCivil getCentreServiceCivil(String code) {
        for (APCentreRegionauxServiceCivil centre : APCentreRegionauxServiceCivil.values()) {
            if (centre.getCodeAsString().equals(code)) {
                return centre;
            }
        }
        return null;
    }

}
