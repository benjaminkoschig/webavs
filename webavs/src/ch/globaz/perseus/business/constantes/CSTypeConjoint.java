package ch.globaz.perseus.business.constantes;

public enum CSTypeConjoint {
    CONCUBIN("55016003", "17"),
    CONJOINT("55016001", "01"),
    PARTENAIRE_ENREGISTRE("55016002", "18");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeConjoint getEnumFromCodeSystem(String codeSystem) {
        CSTypeConjoint[] allValues = CSTypeConjoint.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeConjoint vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;
    private String value;

    private CSTypeConjoint(String codeSystem, String value) {
        this.codeSystem = codeSystem;
        setValue(value);
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
