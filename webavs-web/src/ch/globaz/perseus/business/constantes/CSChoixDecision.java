package ch.globaz.perseus.business.constantes;

public enum CSChoixDecision {
    NEGATIVE_REPONSE("55026001"),
    POSITIVE_REPONSE("55026002"),
    SANS_REPONSE("55026003");

    /**
     * Permet de retrouver un �l�ment d'�numeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code syst�me
     * @return Le type d'enum correspondant
     */
    public static CSChoixDecision getEnumFromCodeSystem(String codeSystem) {
        CSChoixDecision[] allValues = CSChoixDecision.values();
        for (int i = 0; i < allValues.length; i++) {
            CSChoixDecision vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSChoixDecision(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }
}
