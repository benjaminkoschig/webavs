package globaz.corvus.utils.codeprestation.enums;

/**
 * Représente les code prestation de niveau 1. C'est à dire les code prestation liés aux rentes principales
 * 
 * @author lga
 */
public enum RECodePrestationNiveau1 {
    _10(10, "10"),
    _13(13, "13"),
    _20(20, "20"),
    _23(23, "23"),
    _50(50, "50"),
    _70(70, "70"),
    _72(72, "72");

    private int codePrestation;
    private String codePrestationAsString;

    private RECodePrestationNiveau1(int codePrestation, String codePrestationAsString) {
        this.codePrestation = codePrestation;
        this.codePrestationAsString = codePrestationAsString;
    }

    /**
     * Test si le code prestation fournis en paramètres fait partie de ce niveau
     * 
     * @param codePrestation
     *            Le code prestation à testé sous forme de valeur entière
     * @return <code>true</code> si le code prestation fait partie de ce niveau
     */
    public static final boolean isCodePrestationACeNiveau(int codePrestation) {
        for (RECodePrestationNiveau1 code : RECodePrestationNiveau1.values()) {
            if (code.getCodePrestation() == codePrestation) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test si le code prestation fait partie du groupe 1. Cette méthode est 'safe', c'est à dire qu'elle ne va générer
     * aucune exception dans le cas ou la valeur fournie en paramètres est ''pourrie''. Si c'est le cas, l'exception
     * sera 'catched' et la méthode renverra <code>false</code>
     * 
     * @param codePrestation
     * @return <code>true</code> Dans le cas ou la chaîne de caractère à pu être convertie en valeur entière ET que
     *         cette valeur entière figure dans cette enum
     */
    public static final boolean isCodePrestationACeNiveau(String codePrestation) {
        Integer value = null;
        try {
            value = Integer.valueOf(codePrestation);
            if (value != null) {
                return RECodePrestationNiveau1.isCodePrestationACeNiveau(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return le code prestation sous forme de valeur entière
     */
    public final int getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return le code prestation sous forme de String
     */
    public final String getCodePrestationAsString() {
        return codePrestationAsString;
    }

}
