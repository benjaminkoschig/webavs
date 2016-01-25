package ch.globaz.perseus.business.constantes;

public enum CSTypeBareme {
    BAREME_B("54038001"),

    BAREME_H("54038002");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeBareme getEnumFromCodeSystem(String codeSystem) {
        CSTypeBareme[] allValues = CSTypeBareme.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeBareme vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeBareme(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
