package ch.globaz.perseus.business.constantes;

public enum CSTypeVersement {
    ASSURANCE_SOCIALE("55025002"),
    AUTRE_TIERS("55025003"),
    IMPOT_A_LA_SOURCE("55025004"),
    REQUERANT("55025001");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeVersement getEnumFromCodeSystem(String codeSystem) {
        CSTypeVersement[] allValues = CSTypeVersement.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeVersement vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeVersement(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
