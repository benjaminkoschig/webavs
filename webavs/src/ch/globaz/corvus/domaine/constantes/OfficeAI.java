package ch.globaz.corvus.domaine.constantes;


public enum OfficeAI {
    // @formatter:off
	APPENZELL_RHODES_EXTERIEURES(315),
	APPENZELL_RHODES_INTERIEURES(316),
	ARGOVIE(319),
	ASSURES_A_L_ETRANGER(327),
	BALE_CAMPAGNE(313),
	BALE_VILLE(312),
	BERNE(302),
	FRIBOURG(310),
	GENEVE(325),
	GLARIS(308),
	GRISON(318),
	JURA(350),
	LUCERNE(303),
	NEUCHATEL(324),
	NIDWALD(307),
	OBWALD(306),
	SCHAFFHOUSE(314),
	SCHWYZ(305),
	SOLEURE(311),
	ST_GALL(317),
	TESSIN(321),
	THURGOVIE(320),
	URI(304),
	VALAIS(323),
	VAUD(322),
	ZOUG(309),
	ZURICH(301);
	// @formatter:on

    /**
     * @param code
     *            un code d'office AI
     * @return l'office AI correspondant au code passé en paramètre
     * @throws IllegalArgumentException
     *             si le code ne correpond à aucun office connu
     */
    public static final OfficeAI getOfficeAIPourCode(final int code) {
        OfficeAI officeAI = null;
        for (OfficeAI unOfficeAI : OfficeAI.values()) {
            if (unOfficeAI.getCodeOfficeAI() == code) {
                officeAI = unOfficeAI;
            }
        }

        if (officeAI == null) {
            throw new IllegalArgumentException("The value [" + code + "] is not valid for the systemCode of type ["
                    + OfficeAI.class.getName() + "]");
        }

        return officeAI;
    }

    private int codeOfficeAI;

    private OfficeAI(final int codeOfficeAI) {
        this.codeOfficeAI = codeOfficeAI;
    }

    public int getCodeOfficeAI() {
        return codeOfficeAI;
    }
}
