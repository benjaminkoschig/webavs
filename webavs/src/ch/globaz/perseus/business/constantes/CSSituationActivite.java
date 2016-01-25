package ch.globaz.perseus.business.constantes;

public enum CSSituationActivite {
    ACTIF("54036001", "23"),
    AUTRE("54036004", "19"),
    NON_ACTIF("54036003", "25"),
    SANS_EMPLOI("54036002", "24");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSSituationActivite getEnumFromCodeSystem(String codeSystem) {
        CSSituationActivite[] allValues = CSSituationActivite.values();
        for (int i = 0; i < allValues.length; i++) {
            CSSituationActivite vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String value;

    private CSSituationActivite(String codeSystem, String value) {
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
