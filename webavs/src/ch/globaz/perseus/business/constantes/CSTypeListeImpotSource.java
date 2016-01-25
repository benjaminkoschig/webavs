package ch.globaz.perseus.business.constantes;

public enum CSTypeListeImpotSource {
    LISTE_CORRECTIVE("54034002"),
    LISTE_RECAPITULATIVE("54034001");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeListeImpotSource getEnumFromCodeSystem(String codeSystem) {
        CSTypeListeImpotSource[] allValues = CSTypeListeImpotSource.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeListeImpotSource vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeListeImpotSource(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
