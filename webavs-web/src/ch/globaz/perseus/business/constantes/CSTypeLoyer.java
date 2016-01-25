package ch.globaz.perseus.business.constantes;

public enum CSTypeLoyer {
    CINQ_PERSONNES_ET_PLUS("55004005"),
    DEUX_PERSONNES("55004002"),
    QUATRE_PERSONNES("55004004"),
    TROIS_PERSONNES("55004003"),
    UNE_PERSONNE("55004001");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeLoyer getEnumFromCodeSystem(String codeSystem) {
        CSTypeLoyer[] allValues = CSTypeLoyer.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeLoyer vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeLoyer(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
