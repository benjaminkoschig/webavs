package ch.globaz.perseus.business.constantes;

public enum CSFormationEnfant {
    APPRENTI("55003003"),
    ECOLIER("55003001"),
    ETUDIANT("55003002");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSFormationEnfant getEnumFromCodeSystem(String codeSystem) {
        CSFormationEnfant[] allValues = CSFormationEnfant.values();
        for (int i = 0; i < allValues.length; i++) {
            CSFormationEnfant vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSFormationEnfant(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }
}
