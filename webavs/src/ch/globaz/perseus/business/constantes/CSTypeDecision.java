package ch.globaz.perseus.business.constantes;

public enum CSTypeDecision {
    OCTROI_COMPLET("55014002"),
    OCTROI_PARTIEL("55014001"),
    PROJET("55014003"),
    REFUS_SANS_CALCUL("55014004"),
    RENONCIATION("55014006"),
    SUPPRESSION("55014005"),
    NON_ENTREE_MATIERE("55014007");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSTypeDecision getEnumFromCodeSystem(String codeSystem) {
        CSTypeDecision[] allValues = CSTypeDecision.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeDecision vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeDecision(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}