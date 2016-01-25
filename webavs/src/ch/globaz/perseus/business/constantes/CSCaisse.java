package ch.globaz.perseus.business.constantes;

public enum CSCaisse {
    AGENCE_LAUSANNE("55027002"),
    CCVD("55027001");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSCaisse getEnumFromCodeSystem(String codeSystem) {
        CSCaisse[] allValues = CSCaisse.values();
        for (int i = 0; i < allValues.length; i++) {
            CSCaisse vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSCaisse(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
