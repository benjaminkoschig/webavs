package ch.globaz.perseus.business.constantes;

public enum CSEtatCivil {
    CELIBATAIRE("54035001", "01"),
    DIVORCE("54035005", "05"),
    MARIE("54035002", "02"),
    PARTENARIAT_ENREGISTRE("54035006", "06"),
    SEPARE("54035003", "03"),
    VEUF("54035004", "04");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatCivil getEnumFromCodeSystem(String codeSystem) {
        CSEtatCivil[] allValues = CSEtatCivil.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatCivil vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String value;

    private CSEtatCivil(String codeSystem, String value) {
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
