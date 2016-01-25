package globaz.prestation.enums.codeprestation;

public enum PRCodePrestationRFM implements IPRCodePrestationEnum {
    _210(210, "210", PRDomainDePrestation.AVS),
    _213(213, "213", PRDomainDePrestation.AVS),
    _250(250, "250", PRDomainDePrestation.AI);

    /**
     * Test si le code prestation est un code prestation RFM.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation RFM
     */
    public static final boolean isCodePrestationRFM(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation RFM.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation RFM
     */
    public static final boolean isCodePrestationRFM(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(codePrestation);
        if (code != null) {
            return true;
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation RFM ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return <code>true</code> Si le code prestation est un code prestation RFM ET que ce soit un code de prestation
     *         principale.
     */
    public static boolean isPrestationPrincipale(int codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Test si le code prestation est un code prestation RFM ET que ce soit un code de prestation principale.
     * 
     * @param codePrestation
     *            Le code prestation à tester sous forme de <code>String</code>
     * @return <code>true</code> Si le code prestation est un code prestation RFM ET que ce soit un code de prestation
     *         principale.
     */
    public static boolean isPrestationPrincipale(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(codePrestation);
        if (code != null) {
            return code.isPrestationPrincipale();
        }
        return false;
    }

    /**
     * Retourne vrai si le codePrestation correspond à un type de prestation RFM <strong> ET </strong> que ce
     * codePrestation correspond à une prestation du domaine AI
     * 
     * @param codePrestation
     *            Le code du genre de rente
     * @return vrai si le codePrestation correspond à un type de prestation RFM <strong> ET </strong> que ce
     *         codePrestation correspond à une prestation du domaine AI
     */
    public static final boolean isCodePrestationAI(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(codePrestation);
        if (code != null) {
            return code.getDomainDePrestation().equals(PRDomainDePrestation.AI);
        }
        return false;
    }

    /**
     * Retourne vrai si le codePrestation correspond à un type de prestation RFM <strong> ET </strong> que ce
     * codePrestation correspond à une prestation de type AVS
     * 
     * @param codePrestation
     *            Le code du genre de rente
     * @return vrai si le codePrestation correspond à un type de prestation RFM <strong> ET </strong> que ce
     *         codePrestation correspond à une prestation de type AVS
     */
    public static final boolean isCodePrestationAVS(String codePrestation) {
        IPRCodePrestationEnum code = PRCodePrestationRFM.getCodePrestation(codePrestation);
        if (code != null) {
            return code.getDomainDePrestation().equals(PRDomainDePrestation.AVS);
        }
        return false;
    }

    /**
     * Retourne le <code>PRCodePrestationRFM</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation RFM
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationRFM</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation RFM
     */
    public static final IPRCodePrestationEnum getCodePrestation(String codePrestation) {
        for (PRCodePrestationRFM code : PRCodePrestationRFM.values()) {
            if (code.getCodePrestationAsString().equals(codePrestation)) {
                return code;
            }
        }
        return null;
    }

    /**
     * Retourne le <code>PRCodePrestationRFM</code> associé au code prestation fournis en paramètre ou
     * <strong>null</strong> si le code prestation n'est pas un code prestation RFM
     * 
     * @param codePrestation
     *            Le code prestation à évaluer
     * @return le <code>PRCodePrestationRFM</code> associé au code prestation fournis en paramètre ou
     *         <strong>null</strong> si le code prestation n'est pas un code prestation RFM
     */
    public static final IPRCodePrestationEnum getCodePrestation(int codePrestation) {
        for (PRCodePrestationRFM code : PRCodePrestationRFM.values()) {
            if (code.getCodePrestation() == codePrestation) {
                return code;
            }
        }
        return null;
    }

    private int codePrestation;
    private String codePrestationAsString;
    private PRDomainDePrestation typeDePrestation;

    private PRCodePrestationRFM(int codePrestation, String codePrestationAsString, PRDomainDePrestation typeDePrestation) {
        this.codePrestation = codePrestation;
        this.codePrestationAsString = codePrestationAsString;
        this.typeDePrestation = typeDePrestation;
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
     * @return the typeDePrestation (AVS, AI, ...)
     */
    @Override
    public final PRDomainDePrestation getDomainDePrestation() {
        return typeDePrestation;
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
        return PRTypeCodePrestation.RFM;
    }
}
