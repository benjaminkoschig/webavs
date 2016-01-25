package globaz.prestation.enums.codeprestation.type;

import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;

/**
 * Renseigne sur les code prestations existants liés aux prestations d'invalidités Pour plus d'info @see
 * IRECodePrestationEnum
 * 
 * @author lga
 */
public enum PRCodePrestationSurvivant implements IPRCodePrestationEnum {
    _13(13, "13", true, true, false),
    _14(14, "14", false, true, true),
    _15(15, "15", false, true, true),
    _16(16, "16", false, true, true),
    _23(23, "23", true, false, false),
    _24(24, "24", false, false, true),
    _25(25, "25", false, false, true),
    _26(26, "26", false, false, true);

    /**
     * Test si le code prestation est un code prestation survivant.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation survivant
     */
    public static final boolean isCodePrestationSurvivant(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation survivant.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation survivant
     */
    public static final boolean isCodePrestationSurvivant(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation survivant ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à tester sous forme de <code>String</code>
     * @return <code>true</code> Si le code prestation est un code prestation survivant ET que ce soit un code de
     *         prestation principale.
     */
    public static boolean isPrestationPrincipale(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation survivant ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à tester sous forme de <code>String</code>
     * @return <code>true</code> Si le code prestation est un code prestation survivant ET que ce soit un code de
     *         prestation principale.
     */
    public static boolean isPrestationPrincipale(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationSurvivant.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Retourne le <code>PRCodePrestationSurvivant</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation survivant
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationSurvivant</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation survivant
     */
    public static final IPRCodePrestationEnum getCodePrestation(String codePrestation) {
        for (PRCodePrestationSurvivant code : PRCodePrestationSurvivant.values()) {
            if (code.getCodePrestationAsString().equals(codePrestation)) {
                return code;
            }
        }
        return null;
    }

    /**
     * OK Retourne le <code>PRCodePrestationSurvivant</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation survivant
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationSurvivant</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation survivant
     */
    public static final IPRCodePrestationEnum getCodePrestation(int codePrestation) {
        for (PRCodePrestationSurvivant code : PRCodePrestationSurvivant.values()) {
            if (code.getCodePrestation() == codePrestation) {
                return code;
            }
        }
        return null;
    }

    private int codePrestation;
    private String codePrestationAsString;
    private boolean isPrestationPrincipale;
    private boolean isPrestationOrdinaire;
    private boolean isPrestationPourEnfant;

    private PRCodePrestationSurvivant(int codePrestation, String codePrestationAsString,
            boolean isPrestationPrincipale, boolean isPrestationOrdinaire, boolean isPrestationPourEnfant) {
        this.codePrestation = codePrestation;
        this.codePrestationAsString = codePrestationAsString;
        this.isPrestationPrincipale = isPrestationPrincipale;
        this.isPrestationOrdinaire = isPrestationOrdinaire;
        this.isPrestationPourEnfant = isPrestationPourEnfant;
    }

    @Override
    public final int getCodePrestation() {
        return codePrestation;
    }

    @Override
    public final String getCodePrestationAsString() {
        return codePrestationAsString;
    }

    @Override
    public PRDomainDePrestation getDomainDePrestation() {
        return PRDomainDePrestation.AVS;
    }

    @Override
    public PRTypeCodePrestation getTypeDePrestation() {
        return PRTypeCodePrestation.SURVIVANT;
    }

    @Override
    public boolean isPrestationPrincipale() {
        return isPrestationPrincipale;
    }

    @Override
    public boolean isPrestationOrdinaire() {
        return isPrestationOrdinaire;
    }

    @Override
    public boolean isPrestationPourEnfant() {
        return isPrestationPourEnfant;
    }

}
