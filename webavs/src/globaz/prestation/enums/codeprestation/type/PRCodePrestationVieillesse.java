package globaz.prestation.enums.codeprestation.type;

import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;

/**
 * Description des codes prestations liés aux prestations de type invalidité.
 * 
 * @see IRECodePrestationEnum
 * @see PRTypeCodePrestation
 * @see PRDomainDePrestation
 * @author lga
 */
public enum PRCodePrestationVieillesse implements IPRCodePrestationEnum {
    _10(10, "10", true, true, false),
    _12(12, "12", true, true, false),
    _20(20, "20", true, false, false),
    _22(22, "22", true, false, false),
    _33(33, "33", false, true, false),
    _34(34, "34", false, true, true),
    _35(35, "35", false, true, true),
    _36(36, "36", false, true, true),
    _43(43, "43", false, false, false),
    _44(44, "44", false, false, true),
    _45(45, "45", false, false, true),
    _46(46, "46", false, false, true);

    /**
     * Test si le code prestation est un code prestation vieillesse.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation vieillesse
     */
    public static final boolean isCodePrestationVieillesse(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation vieillesse.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation vieillesse
     */
    public static final boolean isCodePrestationVieillesse(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation vieillesse ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation vieillesse ET que ce soit un code de
     *         prestation principale.
     */
    public static boolean isPrestationPrincipale(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation vieillesse ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à tester sous forme de <code>String</code>
     * @return <code>true</code> Si le code prestation est un code prestation vieillesse ET que ce soit un code de
     *         prestation principale.
     */
    public static boolean isPrestationPrincipale(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationVieillesse.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Retourne le <code>PRCodePrestationVieillesse</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation vieillesse
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationVieillesse</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation vieillesse
     */
    public static final IPRCodePrestationEnum getCodePrestation(String codePrestation) {
        for (PRCodePrestationVieillesse code : PRCodePrestationVieillesse.values()) {
            if (code.getCodePrestationAsString().equals(codePrestation)) {
                return code;
            }
        }
        return null;
    }

    /**
     * Retourne le <code>PRCodePrestationVieillesse</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation vieillesse
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationVieillesse</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation vieillesse
     */
    public static final IPRCodePrestationEnum getCodePrestation(int codePrestation) {
        for (PRCodePrestationVieillesse code : PRCodePrestationVieillesse.values()) {
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

    private PRCodePrestationVieillesse(int codePrestation, String codePrestationAsString,
            boolean isPrestationPrincipale, boolean isPrestationOrdinaire, boolean isPrestationPourEnfant) {
        this.codePrestation = codePrestation;
        this.codePrestationAsString = codePrestationAsString;
        this.isPrestationPrincipale = isPrestationPrincipale;
        this.isPrestationOrdinaire = isPrestationOrdinaire;
        this.isPrestationPourEnfant = isPrestationPourEnfant;
    }

    /**
     * @return le code prestation sous forme de valeur entière
     */
    @Override
    public final int getCodePrestation() {
        return codePrestation;
    }

    /**
     * @return le code prestation sous forme de String
     */
    @Override
    public final String getCodePrestationAsString() {
        return codePrestationAsString;
    }

    /**
     * Retourne le type de prestation selon les définition prévue dans le type énuméré PRTypeCodePrestation
     * 
     * @return Retourne le domaine de prestation
     */
    @Override
    public PRTypeCodePrestation getTypeDePrestation() {
        return PRTypeCodePrestation.VIEILLESSE;
    }

    /**
     * Renseigne si le code prestation est un code de prestation principale
     * 
     * @return True si le code prestation est un code de prestation principale
     */
    @Override
    public boolean isPrestationPrincipale() {
        return isPrestationPrincipale;
    }

    /**
     * Renseigne si le code prestation est un code de prestation ordinaire
     * 
     * @return True si le code prestation est un code de prestation ordinaire
     */
    @Override
    public boolean isPrestationOrdinaire() {
        return isPrestationOrdinaire;
    }

    /**
     * Renseigne si le code prestation est un code de prestation pour enfant
     * 
     * @return True si le code prestation est un code de pour enfant
     */
    @Override
    public boolean isPrestationPourEnfant() {
        return isPrestationPourEnfant;
    }

    @Override
    public PRDomainDePrestation getDomainDePrestation() {
        return PRDomainDePrestation.AVS;
    }

}
