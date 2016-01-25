package ch.globaz.perseus.business.constantes;

public enum CSSexePersonne {
    FEMELLE("516002", "2"),
    MALE("516001", "1");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSSexePersonne getEnumFromCodeSystem(String codeSystem) {
        CSSexePersonne[] allValues = CSSexePersonne.values();
        for (int i = 0; i < allValues.length; i++) {
            CSSexePersonne vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String value;

    private CSSexePersonne(String codeSystem, String value) {
        this.codeSystem = codeSystem;
        this.value = value;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
