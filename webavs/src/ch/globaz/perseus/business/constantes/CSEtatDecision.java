/**
 * 
 */
package ch.globaz.perseus.business.constantes;

/**
 * @author DDE
 * 
 */
public enum CSEtatDecision {

    ENREGISTRE("55009001"),
    PRE_VALIDE("55009002"),
    VALIDE("55009003");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatDecision getEnumFromCodeSystem(String codeSystem) {
        CSEtatDecision[] allValues = CSEtatDecision.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatDecision vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSEtatDecision(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
