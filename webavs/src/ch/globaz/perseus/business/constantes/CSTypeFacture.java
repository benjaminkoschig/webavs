package ch.globaz.perseus.business.constantes;

public enum CSTypeFacture {
    FRAIS_DE_GARDE("55030001"),
    // TODO old --> "55022018"

    FRAIS_DE_MALADIE("55030002");
    // TODO old --> "55022019"

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeFacture getEnumFromCodeSystem(String codeSystem) {
        CSTypeFacture[] allValues = CSTypeFacture.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeFacture vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeFacture(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
