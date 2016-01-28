package ch.globaz.perseus.business.constantes;

public enum CSEtatLot {
    LOT_ERREUR("55012004"),
    LOT_OUVERT("55012002"),
    LOT_PARTIEL("55012003"),
    LOT_VALIDE("55012001");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatLot getEnumFromCodeSystem(String codeSystem) {
        CSEtatLot[] allValues = CSEtatLot.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatLot vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSEtatLot(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
