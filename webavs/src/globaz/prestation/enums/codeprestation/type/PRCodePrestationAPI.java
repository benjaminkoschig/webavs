package globaz.prestation.enums.codeprestation.type;

import globaz.prestation.enums.codeprestation.IPRCodePrestationEnum;
import globaz.prestation.enums.codeprestation.PRDomainDePrestation;
import globaz.prestation.enums.codeprestation.PRTypeCodePrestation;

/**
 * Renseigne sur les code prestations existants liés aux prestations d'invalidités Pour plus d'info @see
 * IPRCodePrestationEnum
 * 
 * @author lga
 */
public enum PRCodePrestationAPI implements IPRCodePrestationEnum {
    _81(81, "81", PRDomainDePrestation.AI, true),
    _82(82, "82", PRDomainDePrestation.AI, true),
    _83(83, "83", PRDomainDePrestation.AI, true),
    _84(84, "84", PRDomainDePrestation.AI, true),
    _85(85, "85", PRDomainDePrestation.AVS, true),
    _86(86, "86", PRDomainDePrestation.AVS, true),
    _87(87, "87", PRDomainDePrestation.AVS, true),
    _88(88, "88", PRDomainDePrestation.AI, true),
    _89(89, "89", PRDomainDePrestation.AVS, true),
    _91(91, "91", PRDomainDePrestation.AI, false),
    _92(92, "92", PRDomainDePrestation.AI, false),
    _93(93, "93", PRDomainDePrestation.AI, false),
    _94(94, "94", PRDomainDePrestation.AVS, false),
    _95(95, "95", PRDomainDePrestation.AVS, false),
    _96(96, "96", PRDomainDePrestation.AVS, false),
    _97(97, "97", PRDomainDePrestation.AVS, false);

    /**
     * Test si le code prestation est un code prestation API (allocation pour impotent).
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation API (allocation pour impotent)
     */
    public static final boolean isCodePrestationAPI(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation API (allocation pour impotent).
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation API (allocation pour impotent)
     */
    public static final boolean isCodePrestationAPI(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation API (allocation pour impotent) ET que ce soit un code de
     * prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation API (allocation pour impotent) ET que ce
     *         soit un code de prestation principale.
     */
    public static boolean isPrestationPrincipale(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation API (allocation pour impotent) ET que ce soit un code de
     * prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à tester sous forme de <code>String</code>
     * @return <code>true</code> Si le code prestation est un code prestation API (allocation pour impotent) ET que ce
     *         soit un code de prestation principale.
     */
    public static boolean isPrestationPrincipale(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationAPI.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Retourne le <code>PRCodePrestationAPI</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation API (allocation pour impotent)
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationAPI</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation API (allocation pour impotent)
     */
    public static final IPRCodePrestationEnum getCodePrestation(String codePrestation) {
        for (PRCodePrestationAPI code : PRCodePrestationAPI.values()) {
            if (code.getCodePrestationAsString().equals(codePrestation)) {
                return code;
            }
        }
        return null;
    }

    /**
     * Retourne le <code>PRCodePrestationAPI</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation vieillesse
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationAPI</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation vieillesse
     */
    public static final IPRCodePrestationEnum getCodePrestation(int codePrestation) {
        for (PRCodePrestationAPI code : PRCodePrestationAPI.values()) {
            if (code.getCodePrestation() == codePrestation) {
                return code;
            }
        }
        return null;
    }

    private int codePrestation;
    private String codePrestationAsString;
    private boolean isAPIADomicile;
    private PRDomainDePrestation typeDePrestation;

    private PRCodePrestationAPI(int codePrestation, String codePrestationAsString,
            PRDomainDePrestation typeDePrestation, boolean isAPIADomicile) {
        this.codePrestation = codePrestation;
        this.codePrestationAsString = codePrestationAsString;
        this.typeDePrestation = typeDePrestation;
        this.isAPIADomicile = isAPIADomicile;
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
        return typeDePrestation;
    }

    @Override
    public PRTypeCodePrestation getTypeDePrestation() {
        return PRTypeCodePrestation.API;
    }

    public final boolean isAPIADomicile() {
        return isAPIADomicile;
    }

    @Override
    public boolean isPrestationPrincipale() {
        return true;
    }

    @Override
    public boolean isPrestationOrdinaire() {
        return true;
    }

    @Override
    public boolean isPrestationPourEnfant() {
        return false;
    }

}
