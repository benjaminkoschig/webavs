package ch.globaz.perseus.business.constantes;

public enum CSFormation {
    APPRENTISSAGE("55017002", "04"),
    AUTRE("55017006", "08"),
    ECOLE_SUPP("55017004", "06"),
    FORMATION_ELEMENTAIRE("55017007", "03"),
    MATURITE("55017003", "05"),
    SCOLARITE("55017001", "02"),
    SCOLARITE_MOINS_7_ANS("55017008", "01"),
    UNI_HES("55017005", "07");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSFormation getEnumFromCodeSystem(String codeSystem) {
        CSFormation[] allValues = CSFormation.values();
        for (int i = 0; i < allValues.length; i++) {
            CSFormation vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String value;

    private CSFormation(String codeSystem, String value) {
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
