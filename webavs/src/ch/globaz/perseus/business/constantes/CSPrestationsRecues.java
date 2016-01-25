package ch.globaz.perseus.business.constantes;

public enum CSPrestationsRecues {
    PRESTATIONS_COMPLEMENTAIRES("55018002"),
    PRESTATIONS_EVAM("55018004"),
    PRESTATIONS_RI("55018001"),
    SUBSIDES_LAMAL("55018003");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSPrestationsRecues getEnumFromCodeSystem(String codeSystem) {
        CSPrestationsRecues[] allValues = CSPrestationsRecues.values();
        for (int i = 0; i < allValues.length; i++) {
            CSPrestationsRecues vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSPrestationsRecues(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }
}
