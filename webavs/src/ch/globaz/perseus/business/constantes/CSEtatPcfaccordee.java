/**
 * 
 */
package ch.globaz.perseus.business.constantes;

/**
 * @author DDE
 * 
 */
public enum CSEtatPcfaccordee {

    CALCULE("55010001"),
    VALIDE("55010002");

    /**
     * Permet de retrouver un élément d'énumeration sur la base de son code system
     * 
     * @param codeSystem
     *            Le code système
     * @return Le type d'enum correspondant
     */
    public static CSEtatPcfaccordee getEnumFromCodeSystem(String codeSystem) {
        CSEtatPcfaccordee[] allValues = CSEtatPcfaccordee.values();
        for (int i = 0; i < allValues.length; i++) {
            CSEtatPcfaccordee vm = allValues[i];
            if (vm.codeSystem.equals(codeSystem)) {
                return vm;
            }
        }
        return null;
    }

    private String codeSystem;

    private CSEtatPcfaccordee(String codeSystem) {
        this.codeSystem = codeSystem;
    }

    /**
     * @return the codeSystem
     */
    public String getCodeSystem() {
        return codeSystem;
    }

}
