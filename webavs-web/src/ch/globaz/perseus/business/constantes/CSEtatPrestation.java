/**
 * 
 */
package ch.globaz.perseus.business.constantes;

/**
 * @author DDE
 * 
 */
public enum CSEtatPrestation {

    COMPTABILISE("55024002"),
    DEFINITIF("55024001"),
    ERREUR("55024003");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatPrestation getEnumFromCodeSystem(String codeSystem) {
        CSEtatPrestation[] allValues = CSEtatPrestation.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatPrestation vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSEtatPrestation(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
