package ch.globaz.perseus.business.constantes;

public enum CSSourceEnfant {
    DU_CONJOINT("55005004", "04"),
    HORS_MARIAGE("55005003", "03"),
    MARIAGE("55005001", "03"),
    MARIAGE_PRECEDENT("55005002", "03"),
    RECUEILLI_ADOPTE("55005005", "03");
    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSSourceEnfant getEnumFromCodeSystem(String codeSystem) {
        CSSourceEnfant[] allValues = CSSourceEnfant.values();
        for (int i = 0; i < allValues.length; i++) {
            CSSourceEnfant vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String value;

    private CSSourceEnfant(String codeSystem, String value) {
        this.codeSystem = codeSystem;
        setValue(value);
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
