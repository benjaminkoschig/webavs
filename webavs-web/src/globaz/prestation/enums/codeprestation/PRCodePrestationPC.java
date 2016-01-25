package globaz.prestation.enums.codeprestation;

import globaz.externe.IPRConstantesExternes;
import java.util.ArrayList;
import java.util.List;

public enum PRCodePrestationPC implements IPRCodePrestationEnum {
    /**
     * PC AVS : type survivant
     */
    _110(110, "110", PRDomainDePrestation.AVS, "PC_AVS"),
    /**
     * PC AVS : type PC
     */
    _113(113, "113", PRDomainDePrestation.AVS, "PC_AVS"),
    /**
     * PC AVS : allocation de No�l, adresse du tiers r�cup�r�e dans le domaine des rentes
     * 
     * @see {@link IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE}
     */
    _118(118, "118", PRDomainDePrestation.AVS, "PC_AVS_ALLOCATION_NOEL_TIERS_DOMAINE_RENTES"),
    /**
     * PC AVS : allocation de No�l, adresse du tiers r�cup�r�e dans le domaine allocation de No�l
     * 
     * @see {@link IPRConstantesExternes.TIERS_CS_DOMAINE_ALLOCATION_DE_NOEL}
     */
    _119(119, "119", PRDomainDePrestation.AVS, "PC_AVS_ALLOCATION_NOEL_TIERS_DOMAINE_ALLOC_NOEL"),
    /**
     * PC AI
     */
    _150(150, "150", PRDomainDePrestation.AI, "PC_AI"),
    /**
     * PC AI : allocation de No�l, adresse du tiers r�cup�r�e dans le domaine des rentes
     * 
     * @see {@link IPRConstantesExternes.TIERS_CS_DOMAINE_APPLICATION_RENTE}
     */
    _158(158, "158", PRDomainDePrestation.AI, "PC_AI_ALLOCATION_NOEL_TIERS_DOMAINE_RENTES"),
    /**
     * PC AI : allocation de No�l, adresse du tiers r�cup�r�e dans le domaine allocation de No�l
     * 
     * @see {@link IPRConstantesExternes.TIERS_CS_DOMAINE_ALLOCATION_DE_NOEL}
     */
    _159(159, "159", PRDomainDePrestation.AI, "PC_AI_ALLOCATION_NOEL_TIERS_DOMAINE_ALLOC_NOEL");

    /**
     * Test si le code prestation est un code prestation PC.
     * 
     * @param codePrestation
     *            Le code prestation � �valuer
     * @return <code>true</code> Si le code prestation est un code prestation PC
     */
    public static final boolean isCodePrestationPC(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation PC.
     * 
     * @param codePrestation
     *            Le code prestation � �valuer
     * @return <code>true</code> Si le code prestation est un code prestation PC
     */
    public static final boolean isCodePrestationPC(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation PC ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation � �valuer
     * @return <code>true</code> Si le code prestation est un code prestation PC ET que ce soit un code de prestation
     *         principale.
     */
    public static boolean isPrestationPrincipale(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation PC ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation � tester sous forme de <code>String</code>
     * @return <code>true</code> Si le code prestation est un code prestation PC ET que ce soit un code de prestation
     *         principale.
     */
    public static boolean isPrestationPrincipale(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Retourne le <code>PRCodePrestationPC</code> associ� au code prestation fournis en param�tre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation PC
     * 
     * @param codePrestation
     *            Le code prestation � �valuer
     * @return le <code>PRCodePrestationPC</code> associ� au code prestation fournis en param�tre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation PC
     */
    public static final IPRCodePrestationEnum getCodePrestation(String codePrestation) {
        for (PRCodePrestationPC code : PRCodePrestationPC.values()) {
            if (code.getCodePrestationAsString().equals(codePrestation)) {
                return code;
            }
        }
        return null;
    }

    /**
     * Retourne le <code>PRCodePrestationPC</code> associ� au code prestation fournis en param�tre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation PC
     * 
     * @param codePrestation
     *            Le code prestation � �valuer
     * @return le <code>PRCodePrestationPC</code> associ� au code prestation fournis en param�tre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation PC
     */
    public static final IPRCodePrestationEnum getCodePrestation(int codePrestation) {
        for (PRCodePrestationPC code : PRCodePrestationPC.values()) {
            if (code.getCodePrestation() == codePrestation) {
                return code;
            }
        }
        return null;
    }

    /**
     * Retourne l'ensemble des codes prestation li�s aux allocation de No�l
     * 
     * @return l'ensemble des codes prestation li�s aux allocation de No�l
     */
    public static final List<PRCodePrestationPC> getCodePrestationAllocationNoel() {
        List<PRCodePrestationPC> list = new ArrayList<PRCodePrestationPC>();
        for (PRCodePrestationPC code : PRCodePrestationPC.values()) {
            if (PRCodePrestationPC.isCodePrestationAllocationNoel(code.getCodePrestationAsString())) {
                list.add(code);
            }
        }
        return list;
    }

    private static final PRDomainDePrestation getDomainDePrestation(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationPC.getCodePrestation(codePrestation);
        if (code != null) {
            return code.getDomainDePrestation();
        }
        return null;
    }

    /**
     * Retourne vrai si le codePrestation correspond � un type de prestation PC <strong> ET </strong> que ce
     * codePrestation correspond � une prestation de type AI
     * 
     * @param codePrestation
     *            Le code du genre de rente
     * @return vrai si le codePrestation correspond � un type de prestation PC <strong> ET </strong> que ce
     *         codePrestation correspond � une prestation de type AI
     */
    public static final boolean isCodePrestationAI(String codePrestation) {
        PRDomainDePrestation typeDePrestation = PRCodePrestationPC.getDomainDePrestation(codePrestation);
        if (typeDePrestation != null) {
            return typeDePrestation.equals(PRDomainDePrestation.AI);
        }
        return false;
    }

    /**
     * Retourne <code>true</code> si le code prestation repr�sente une PC (AVS ou AI) allocation de No�l
     * 
     * @param codePrestation
     *            Le code prestation � tester
     * @return <code>true</code> si le code prestation repr�sente une allocation de No�l AVS ou AI
     */
    public static final boolean isCodePrestationAllocationNoel(String codePrestation) {
        return PRCodePrestationPC.isCodePrestationAllocationNoelAVS(codePrestation)
                || PRCodePrestationPC.isCodePrestationAllocationNoelAI(codePrestation);
    }

    /**
     * Retourne <code>true</code> si le code prestation repr�sente une allocation de No�l AI
     * 
     * @param codePrestation
     *            Le code prestation � tester
     * @return <code>true</code> si le code prestation repr�sente une allocation de No�l AI
     */
    public static final boolean isCodePrestationAllocationNoelAI(String codePrestation) {
        if (_158.getCodePrestationAsString().equals(codePrestation)
                || _159.getCodePrestationAsString().equals(codePrestation)) {
            return true;
        }
        return false;
    }

    /**
     * Retourne <code>true</code> si le code prestation repr�sente une allocation de No�l AVS
     * 
     * @param codePrestation
     *            Le code prestation � tester
     * @return <code>true</code> si le code prestation repr�sente une allocation de No�l AVS
     */
    public static final boolean isCodePrestationAllocationNoelAVS(String codePrestation) {
        if (_118.getCodePrestationAsString().equals(codePrestation)
                || _119.getCodePrestationAsString().equals(codePrestation)) {
            return true;
        }
        return false;
    }

    /**
     * Retourne vrai si le codePrestation correspond � un type de prestation PC <strong> ET </strong> que ce
     * codePrestation correspond � une prestation de type AVS
     * 
     * @param codePrestation
     *            Le code du genre de rente
     * @return vrai si le codePrestation correspond � un type de prestation PC <strong> ET </strong> que ce
     *         codePrestation correspond � une prestation de type AVS
     */
    public static final boolean isCodePrestationAVS(String codePrestation) {
        PRDomainDePrestation typeDePrestation = PRCodePrestationPC.getDomainDePrestation(codePrestation);
        if (typeDePrestation != null) {
            return typeDePrestation.equals(PRDomainDePrestation.AVS);
        }
        return false;
    }

    private int codePrestation;
    private String codePrestationAsString;
    private PRDomainDePrestation typeDePrestation;
    private String description;

    private PRCodePrestationPC(int codePrestation, String codePrestationAsString,
            PRDomainDePrestation typeDePrestation, String description) {
        this.codePrestation = codePrestation;
        this.codePrestationAsString = codePrestationAsString;
        this.typeDePrestation = typeDePrestation;
        this.description = description;
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

    @Override
    public PRTypeCodePrestation getTypeDePrestation() {
        return PRTypeCodePrestation.PC;
    }

    @Override
    public final PRDomainDePrestation getDomainDePrestation() {
        return typeDePrestation;
    }

    public String getDescription() {
        return description;
    }
}
