package globaz.corvus.utils.codeprestation.enums;

public enum RECodePrestationNiveau2 {
    _81(81, "81"),
    _82(82, "82"),
    _83(83, "83"),
    _84(84, "84"),
    _85(85, "85"),
    _86(86, "86"),
    _87(87, "87"),
    _88(88, "88"),
    _89(89, "89"),
    _91(91, "91"),
    _92(92, "92"),
    _93(93, "93"),
    _94(94, "94"),
    _95(95, "95"),
    _96(96, "96"),
    _97(97, "97");

    /**
     * Test si le code prestation fournis en paramètres fait partie de ce niveau
     * 
     * @param codePrestation
     *            Le code prestation à testé sous forme de valeur entière
     * @return <code>true</code> si le code prestation fait partie de ce niveau
     */
    public static final boolean isCodePrestationACeNiveau(int codePrestation) {
        for (RECodePrestationNiveau2 code : RECodePrestationNiveau2.values()) {
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
                return RECodePrestationNiveau2.isCodePrestationACeNiveau(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int codePrestation;

    private String codePrestationAsString;

    private RECodePrestationNiveau2(int codePrestation, String codePrestationAsString) {
        this.codePrestation = codePrestation;
        this.codePrestationAsString = codePrestationAsString;
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
