package globaz.apg.enums;

public enum APTypeDePrestation implements Comparable<APTypeDePrestation> {

    ACM_ALFA("ACM_ALFA", 52015002, true, true),
    ACM2_ALFA("ACM2_ALFA", 52015005, false, true),
    ACM_NE("ACM_NE", 52015004, true, false),
    LAMAT("LAMAT", 52015003, true, true),
    STANDARD("Standard", 52015001, true, true),
    COMPCIAB("COMPCIAB", 52015006, true, false),

    //ESVE MATERNITE NOUVEAU GENRE
    MATCIAB("MATCIAB", 52015014, true, true),

    MATCIAB1PA("MATCIAB1PA", 52015009, true, true),
    MATCIAB2PA("MATCIAB2PA", 52015011, true, true),

    MATCIAB1PE("MATCIAB1PE", 52015008, true, true),
    MATCIAB2PE("MATCIAB2PE", 52015010, true, true),

    JOUR_ISOLE("ISOLES", 52015007, true, false),
    PANDEMIE("PANDEMIE", 52015012, true, false);

    public static APTypeDePrestation resoudreTypeDePrestationParCodeSystem(final int codeSystem) {
        for (final APTypeDePrestation type : APTypeDePrestation.values()) {
            if (type.getCodesystem() == codeSystem) {
                return type;
            }
        }
        return null;
    }

    private int codesystem;
    private boolean isPrestationAPG;
    private boolean isPrestationMaternite;

    private String nomTypePrestation;

    private APTypeDePrestation(final String nomTypePrestation, final int codesystem, final boolean isPrestationAPG,
            final boolean isPrestationMaternite) {
        this.nomTypePrestation = nomTypePrestation;
        this.codesystem = codesystem;
        this.isPrestationAPG = isPrestationAPG;

        this.isPrestationMaternite = isPrestationMaternite;
    }

    /**
     * @return the codesystem
     */
    public final int getCodesystem() {
        return codesystem;
    }

    /**
     * @return the codesystem
     */
    public final String getCodesystemString() {
        return String.valueOf(codesystem);
    }

    /**
     * @return the isPrestationAPG
     */
    public final boolean getIsPrestationAPG() {
        return isPrestationAPG;
    }

    /**
     * @return the isPrestationMaternite
     */
    public final boolean getIsPrestationMaternite() {
        return isPrestationMaternite;
    }

    /**
     * @return the nomTypePrestation
     */
    public final String getNomTypePrestation() {
        return nomTypePrestation;
    }

    public boolean isCodeSystemEqual(final String codeSystem) {
        return String.valueOf(getCodesystem()).equalsIgnoreCase(codeSystem);
    }

}
