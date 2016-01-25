package globaz.corvus.utils.codeprestation.enums;

public enum RECodePrestationNiveau5 {
    _14(14, "14"),
    _15(15, "15"),
    _16(16, "16"),
    _24(24, "24"),
    _25(25, "25"),
    _26(26, "26"),
    _34(34, "34"),
    _35(35, "35"),
    _36(36, "36"),
    _45(45, "45"),
    _54(54, "54"),
    _55(55, "55"),
    _56(56, "56"),
    _74(74, "74"),
    _75(75, "75"),
    _76(76, "76");

    /**
     * Test si le code prestation fournis en paramètres fait partie de ce niveau
     * 
     * @param codePrestation
     *            Le code prestation à testé sous forme de valeur entière
     * @return <code>true</code> si le code prestation fait partie de ce niveau
     */
    public static final boolean isCodePrestationACeNiveau(int codePrestation) {
        for (RECodePrestationNiveau5 code : RECodePrestationNiveau5.values()) {
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
    public static final boolean isCodePrestationInGroup(String codePrestation) {
        Integer value = null;
        try {
            value = Integer.valueOf(codePrestation);
            if (value != null) {
                return RECodePrestationNiveau5.isCodePrestationACeNiveau(value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private int codePrestation;

    private String codePrestationAsString;

    private RECodePrestationNiveau5(int codePrestation, String codePrestationAsString) {
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
