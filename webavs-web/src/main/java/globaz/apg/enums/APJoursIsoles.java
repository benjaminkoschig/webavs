package globaz.apg.enums;

public enum APJoursIsoles implements Comparable<APJoursIsoles> {

    DEMENAGEMENT ("501", "52001050"),
    NAISSANCE("502", "52001051"),
    MARIAGE_LPART("503", "52001052"),
    DECES("504", "52001053 "),
    JOURNEES_DIVERSES("505", "52001054"),
    CONGE_JEUNESSE("506", "52001055"),
    SERVICE_ETRANGER("507", "52001056");

    public static APJoursIsoles resoudreGenreParCodeSystem(final String codeSystem) {
        for (final APJoursIsoles genre : APJoursIsoles.values()) {
            if (genre.getCodesystem().equals(codeSystem)) {
                return genre;
            }
        }
        return null;
    }

    private String codesystem;
    private String code;

    private APJoursIsoles(final String code, final String codesystem) {
        this.code = code;
        this.codesystem = codesystem;
    }
    
    public String getCode() {
        return code;
    }

    /**
     * @return the codesystem
     */
    public final String getCodesystem() {
        return codesystem;
    }

    public boolean isCodeSystemEqual(final String codeSystem) {
        return String.valueOf(getCodesystem()).equalsIgnoreCase(codeSystem);
    }

}
