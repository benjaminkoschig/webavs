package globaz.corvus.utils.codeprestation.enums;

public enum RECodePrestationNiveau4 {
    _33(33, "33"),
    _53(53, "53"),
    _73(73, "73");

    /**
     * Test si le code prestation fournis en param�tres fait partie de ce niveau
     * 
     * @param codePrestation
     *            Le code prestation � test� sous forme de valeur enti�re
     * @return <code>true</code> si le code prestation fait partie de ce niveau
     */
    public static final boolean isCodePrestationInGroup(int codePrestation) {
        for (RECodePrestationNiveau4 code : RECodePrestationNiveau4.values()) {
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
    public static final boolean isCodePrestationInGroup(String codePrestation) {
        Integer value = null;
        try {
            value = Integer.valueOf(codePrestation);
            if (value != null) {
                return RECodePrestationNiveau4.isCodePrestationInGroup(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int codePrestation;

    private String codePrestationAsString;

    private RECodePrestationNiveau4(int codePrestation, String codePrestationAsString) {
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
