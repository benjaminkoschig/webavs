package ch.globaz.perseus.business.constantes;

public enum CSTypeGarde {
    GARDE_EXCLUSIVE("55006002"),
    GARDE_PARTAGEE("55006001");

    /**
     * Permet de retrouver un �l�ment d'�numeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code syst�me
     * @return Le type d'enum correspondant
     */
    public static CSTypeGarde getEnumFromCodeSystem(String codeSystem) {
        CSTypeGarde[] allValues = CSTypeGarde.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeGarde vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeGarde(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
