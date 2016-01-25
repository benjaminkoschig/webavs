package ch.globaz.perseus.business.constantes;

public enum CSEtatFacture {
    ENREGISTRE("55028001"),
    RESTITUE("55028003"),
    VALIDE("55028002");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatFacture getEnumFromCodeSystem(String codeSystem) {
        CSEtatFacture[] allValues = CSEtatFacture.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatFacture vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSEtatFacture(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
