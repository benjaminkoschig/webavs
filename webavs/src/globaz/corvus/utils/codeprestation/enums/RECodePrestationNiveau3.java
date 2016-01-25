package globaz.corvus.utils.codeprestation.enums;

public enum RECodePrestationNiveau3 {
    _110(110, "110"),
    _130(130, "130"),
    _150(150, "150");

    /**
     * Test si le code prestation fournis en param�tres fait partie de ce niveau
     * 
     * @param codePrestation
     *            Le code prestation � test� sous forme de valeur enti�re
     * @return <code>true</code> si le code prestation fait partie de ce niveau
     */
    public static final boolean isCodePrestationACeNiveau(int codePrestation) {
        for (RECodePrestationNiveau3 code : RECodePrestationNiveau3.values()) {
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
                return RECodePrestationNiveau3.isCodePrestationACeNiveau(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int codePrestation;

    private String codePrestationAsString;

    private RECodePrestationNiveau3(int codePrestation, String codePrestationAsString) {
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
