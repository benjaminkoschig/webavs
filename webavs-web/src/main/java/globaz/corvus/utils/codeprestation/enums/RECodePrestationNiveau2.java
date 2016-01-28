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
     * Test si le code prestation fournis en param�tres fait partie de ce niveau
     * 
     * @param codePrestation
     *            Le code prestation � test� sous forme de valeur enti�re
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
     * Test si le code prestation fait partie du groupe 1. Cette m�thode est 'safe', c'est � dire qu'elle ne va g�n�rer
     * aucune exception dans le cas ou la valeur fournie en param�tres est ''pourrie''. Si c'est le cas, l'exception
     * sera 'catched' et la m�thode renverra <code>false</code>
     * 
     * @param codePrestation
     * @return <code>true</code> Dans le cas ou la cha�ne de caract�re � pu �tre convertie en valeur enti�re ET que
     *         cette valeur enti�re figure dans cette enum
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
     * @return le code prestation sous forme de valeur enti�re
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
