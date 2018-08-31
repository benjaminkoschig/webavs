package ch.globaz.vulpecula.domain.models.communicationsalaires;


public enum CSTypeListeSalaire {
    RETAVAL("68908001"),
    RESOR("68908002");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeListeSalaire getEnumFromCodeSystem(String codeSystem) {
        CSTypeListeSalaire[] allValues = CSTypeListeSalaire.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeListeSalaire vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeListeSalaire(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
