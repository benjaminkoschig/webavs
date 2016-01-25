package ch.globaz.perseus.business.constantes;

public enum CSRentes {
    AI("55020005"),
    ASSURANCES_MILITAIRE("55020004"),
    ASSURANCES_PRIVES("55020003"),
    AVS("55020006"),
    LAA("55020001"),
    LPP("55020007"),
    RENTE_ETRANGERE("55020008"),
    TROISIEME_PILIER("55020002"),
    VEUF_VEUVE("55020009");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSRentes getEnumFromCodeSystem(String codeSystem) {
        CSRentes[] allValues = CSRentes.values();
        for (int i = 0; i < allValues.length; i++) {
            CSRentes vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSRentes(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }
}
