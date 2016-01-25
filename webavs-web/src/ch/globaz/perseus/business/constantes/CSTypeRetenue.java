package ch.globaz.perseus.business.constantes;

public enum CSTypeRetenue {
    ADRESSE_PAIEMENT("54033001"),
    FACTURE_EXISTANTE("54033003"),
    IMPOT_SOURCE("54033002");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeRetenue getEnumFromCodeSystem(String codeSystem) {
        CSTypeRetenue[] allValues = CSTypeRetenue.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeRetenue vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeRetenue(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
