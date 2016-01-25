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
public enum PRCodePrestationInvalidite implements IPRCodePrestationEnum {
    _50(50, "50", true, true, false),
    @Deprecated
    _52(52, "52", true, true, false),
    @Deprecated
    _53(53, "53", false, true, false),
    _54(54, "54", false, true, true),
    _55(55, "55", false, true, true),
    _56(56, "56", false, true, true),

    _70(70, "70", true, false, false),
    @Deprecated
    _72(72, "72", true, false, false),
    @Deprecated
    _73(73, "73", false, false, false),
    _74(74, "74", false, false, true),
    _75(75, "75", false, false, true),
    _76(76, "76", false, false, true);

    /**
     * Test si le code prestation est un code prestation invalidité.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation invalidité
     */
    public static final boolean isCodePrestationInvalidite(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation invalidité.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation invalidité
     */
    public static final boolean isCodePrestationInvalidite(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation invalidité ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation invalidité ET que ce soit un code de
     *         prestation principale.
     */
    public static boolean isPrestationPrincipale(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation invalidité ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à tester sous forme de <code>String</code>
     * @return <code>true</code> Si le code prestation est un code prestation invalidité ET que ce soit un code de
     *         prestation principale.
     */
    public static boolean isPrestationPrincipale(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationInvalidite.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Retourne le <code>PRCodePrestationInvalidite</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation invalidité
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationInvalidite</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation invalidité
     */
    public static final IPRCodePrestationEnum getCodePrestation(String codePrestation) {
        for (PRCodePrestationInvalidite code : PRCodePrestationInvalidite.values()) {
            if (code.getCodePrestationAsString().equals(codePrestation)) {
                return code;
            }
        }
        return null;
    }

    /**
     * Retourne le <code>PRCodePrestationInvalidite</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation invalidité
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationInvalidite</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation invalidité
     */
    public static final IPRCodePrestationEnum getCodePrestation(int codePrestation) {
        for (PRCodePrestationInvalidite code : PRCodePrestationInvalidite.values()) {
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

    private PRCodePrestationInvalidite(int codePrestation, String codePrestationAsString,
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
        return PRDomainDePrestation.AI;
    }

    @Override
    public PRTypeCodePrestation getTypeDePrestation() {
        return PRTypeCodePrestation.INVALIDITE;
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
