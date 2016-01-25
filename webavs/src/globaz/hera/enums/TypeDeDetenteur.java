package globaz.hera.enums;

/**
 * Repr�sente les valeurs possible du champs typrDeDetenteur de l'�cran GSF0009
 * 
 * @author lga
 * 
 */
public enum TypeDeDetenteur {

    /**
     * Dans le cas de la famille il n'existe pa de cl� ACOR. Si le type de d�tenteur est FAMILLE, la valeur
     * correspondante SFPERIOD.WHTYDE
     */
    FAMILLE(36006001, "TYPE_DETENTEUR_FAMILLE", ""),
    TIERS(36006002, "TYPE_DETENTEUR_TIERS", "tiers"),
    TUTEUR_LEGAL(36006003, "TYPE_DETENTEUR_TUTEUR_LEGAL", "1");

    private int codeSystem;
    private String labelKey;
    private String acorKey;

    private TypeDeDetenteur(int codeSystem, String labelKey, String acorKey) {
        this.codeSystem = codeSystem;
        this.labelKey = labelKey;
        this.acorKey = acorKey;

    }

    public final int getCodeSystem() {
        return codeSystem;
    }

    public final String getCodeSystemAsString() {
        return String.valueOf(codeSystem);
    }

    public final String getLabelKey() {
        return labelKey;
    }

    public final String getAcorKey() {
        return acorKey;
    }

    /**
     * Retourne le type �num�r� {@link TypeDeDetenteur} correspondant au code syst�me pass� en param�tre
     * 
     * @param codeSystem Le code syst�me � tester
     * @return le type �num�r� {@link TypeDeDetenteur} correspondant au code syst�me pass� en param�tre ou null si pas
     *         trouv�
     */
    public static TypeDeDetenteur fromCodeSytem(String codeSystem) {
        for (TypeDeDetenteur type : TypeDeDetenteur.values()) {
            if (type.getCodeSystemAsString().equals(codeSystem)) {
                return type;
            }
        }
        return null;
    }

}
