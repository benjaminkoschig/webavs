package ch.globaz.perseus.business.constantes;

public enum CSTypeDemande {
    AIDES_CATEGORIELLES("55002005"),
    FOND_CANTONAL("55002004"),
    PC_AVS_AI("55002003"),
    REVISION_EXTRAORDINAIRE("55002002"),
    REVISION_PERIODIQUE("55002001");

    /**
     * Permet de retrouver un �l�ment d'�numeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code syst�me
     * @return Le type d'enum correspondant
     */
    public static CSTypeDemande getEnumFromCodeSystem(String codeSystem) {
        CSTypeDemande[] allValues = CSTypeDemande.values();
        for (int i = 0; i < allValues.length; i++) {
            CSTypeDemande vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSTypeDemande(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }
}
