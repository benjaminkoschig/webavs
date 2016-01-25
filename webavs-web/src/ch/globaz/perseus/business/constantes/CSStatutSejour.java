package ch.globaz.perseus.business.constantes;

public enum CSStatutSejour {
    AUTRE("54037009", "07"),
    PAS_AUTORISATION_SEJOUR("54037008", "06"),
    PERMIS_ANNUEL_b_SANS_REFUGIES_RECONNUS_B("54037001", "01"),
    PERMIS_COURTE_DUREE("54037003", "03"),
    PERMIS_ETABLISSEMENT_C_INCLUS_REGUGIES_RECONNUS_C("54037002", "02"),
    PERSONNES_ADMISES_PROVISOIREMENT_7_PLUS("54037007", "16"),
    REFUGIES_ADMIS_PROVISOIREMENT_7_MOINS("54037005", "15"),
    REFUGIES_ADMIS_PROVISOIREMENT_7_PLUS("54037006", "17"),
    REFUGIES_RECONNUS_B("54037004", "14");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSStatutSejour getEnumFromCodeSystem(String codeSystem) {
        CSStatutSejour[] allValues = CSStatutSejour.values();
        for (int i = 0; i < allValues.length; i++) {
            CSStatutSejour vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String value;

    private CSStatutSejour(String codeSystem, String value) {
        this.codeSystem = codeSystem;
        this.value = value;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}