package ch.globaz.perseus.business.constantes;

public enum CSDemandesEnCours {
    AIDE_LOGEMENT("55019003"),
    AIDES_FORMATION("55019002"),
    PENSION_ALIMENTAIRES("55019001");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSDemandesEnCours getEnumFromCodeSystem(String codeSystem) {
        CSDemandesEnCours[] allValues = CSDemandesEnCours.values();
        for (int i = 0; i < allValues.length; i++) {
            CSDemandesEnCours vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSDemandesEnCours(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }
}
