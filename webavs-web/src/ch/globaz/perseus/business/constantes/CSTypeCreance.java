package ch.globaz.perseus.business.constantes;

public enum CSTypeCreance {
    TYPE_CREANCE_ASSURANCER_SOCIALE("55023002"),
    TYPE_CREANCE_IMPOT_SOURCE("55023003"),
    TYPE_CREANCE_TIERS("55023001");

    /**
     * Permet de retrouver un �l�ment d'�numeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code syst�me
     * @return Le type d'enum correspondant
     */
    public static CSTypeCreance getEnumFromCodeSystem(String codeSystem) {
        CSTypeCreance[] allValues = CSTypeCreance.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeCreance vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeCreance(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
